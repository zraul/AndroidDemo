package com.example.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpStatus;

import com.example.database.ThreadDAOImpl;
import com.example.entities.FileInfo;
import com.example.entities.ThreadInfo;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author zhengraul 下载任务类
 */

public class DownloadTask {
	private Context mContext;
	private FileInfo mFileInfo;
	private ThreadDAOImpl mDaoImpl;
	private int mFinished = 0;
	public boolean isPause = false;
	private int mThreadCount = 1;// 线程数量
	private List<DownloadThread> mThreadList = null;
	public static ExecutorService sExecutorService = Executors.newCachedThreadPool();//线程池
	
	public DownloadTask(Context mContext, FileInfo mFileInfo, int mThreadCount) {
		super();
		this.mContext = mContext;
		this.mFileInfo = mFileInfo;
		this.mDaoImpl = new ThreadDAOImpl(mContext);
		this.mThreadCount = mThreadCount;
	}

	public void download() {
		// 读取数据库的线程信息
		List<ThreadInfo> threadInfos = mDaoImpl.getThreads(mFileInfo.getUrl());
		if (threadInfos.size() == 0) {
			// 获得每个线程下载的长度
			int length = mFileInfo.getLength() / mThreadCount;
			for (int i = 0; i < mThreadCount; i++) {
				//创建多个不同的线程信息
				ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(),length * i, (i + 1)
						* length - 1, 0);
				if (i == mThreadCount - 1) {
					threadInfo.setEnd(mFileInfo.getLength());
				}
				//添加到线程信息集合中
				threadInfos.add(threadInfo);
				mDaoImpl.insertThread(threadInfo);
			}
		}
		
		mThreadList = new ArrayList<DownloadThread>();
		
		//启动多个线程进行下载
		for (ThreadInfo info:threadInfos) {
			DownloadThread thread = new DownloadThread(info);
//			thread.start();
			DownloadTask.sExecutorService.execute(thread);//使用线程池启动线程
			mThreadList.add(thread);
		}
	}
	/**
	 * 判断是否所有线程都执行完毕
	 * @return
	 */
	private synchronized void checkAllThreadFinished(){
		boolean allFinished = true;
		//遍历线程集合，判断线程是否都执行完毕
		for (DownloadThread thread:mThreadList) {
			if (!thread.isFinished) {
				allFinished = false;
				break;
			}
		}
		if (allFinished) {
			// 删除线程信息
			mDaoImpl.deleteThread(mFileInfo.getUrl());
			//发送广播通知UI，下载任务结束
			Intent intent = new Intent(DownloadService.ACTION_FINISH);
			intent.putExtra("fileInfo", mFileInfo);
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 
	 * @author zhengraul 下载线程
	 */
	class DownloadThread extends Thread {
		private ThreadInfo mThreadInfo;
		public boolean isFinished = false;//线程是否结束

		public DownloadThread(ThreadInfo mThreadInfo) {
			super();
			this.mThreadInfo = mThreadInfo;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 向数据库插入新的线程信息
//			if (!mDaoImpl.isExists(mThreadInfo.getUrl(), mThreadInfo.getId())) {
//				mDaoImpl.insertThread(mThreadInfo);
//			}

			HttpURLConnection connection = null;
			RandomAccessFile raf = null;
			InputStream input = null;
			try {
				URL url = new URL(mThreadInfo.getUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");

				// 设置开始下载的位置
				int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
				connection.setRequestProperty("Range", "bytes=" + start + "-"
						+ mThreadInfo.getEnd());
				// 设置文件写入位置
				File file = new File(DownloadService.DOWNLOAD_PATH,
						mFileInfo.getFileName());
				Log.e("2222",
						DownloadService.DOWNLOAD_PATH + mFileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(start);
				Intent intent = new Intent(DownloadService.ACTION_UPDATE);
				mFinished += mThreadInfo.getFinished();

				// 开始下载
				if (connection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
					// 读取数据
					input = connection.getInputStream();
					byte[] buffer = new byte[1024];
					int len = -1;
					long time = System.currentTimeMillis();
					while ((len = input.read(buffer)) != -1) {
						// 写入文件
						raf.write(buffer, 0, len);
						// 累加整个文件完成的进度
						mFinished += len;
						//累加每个线程完成的进度
						mThreadInfo.setFinished(mThreadInfo.getFinished()+len);
						//更新进度
						if (System.currentTimeMillis() - time > 1000) {
							time = System.currentTimeMillis();
							intent.putExtra("finished", mFinished * 100
									/ mFileInfo.getLength());
							intent.putExtra("id", mFileInfo.getId());
							mContext.sendBroadcast(intent);
						}
						// 下载暂停，保存数据
						if (isPause) {
							mDaoImpl.updateThread(mThreadInfo.getUrl(),
									mThreadInfo.getId(), mThreadInfo.getFinished());
							return;
						}
					}
					//标识线程执行完毕
					isFinished = true;
					//检查下载任务是否执行完毕
					checkAllThreadFinished();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					raf.close();
					connection.disconnect();
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
