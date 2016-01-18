package com.example.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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

	public DownloadTask(Context mContext, FileInfo mFileInfo) {
		super();
		this.mContext = mContext;
		this.mFileInfo = mFileInfo;
		this.mDaoImpl = new ThreadDAOImpl(mContext);
	}

	public void download(){
		//读取数据库的线程信息
		List<ThreadInfo> threadInfos = mDaoImpl.getThreads(mFileInfo.getUrl());
		ThreadInfo threadInfo = null;
		if (threadInfos.size() == 0) {
			//初始化线程信息对象
			threadInfo = new ThreadInfo(0,mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
		} else {
			threadInfo = threadInfos.get(0);
		}
		
		//创建子线程
		new DownloadThread(threadInfo).start();
	}
	
	/**
	 * 
	 * @author zhengraul
	 * 下载线程
	 */
	class DownloadThread extends Thread{
		private ThreadInfo mThreadInfo;

		public DownloadThread(ThreadInfo mThreadInfo) {
			super();
			this.mThreadInfo = mThreadInfo;
		}
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//向数据库插入新的线程信息
			if (!mDaoImpl.isExists(mThreadInfo.getUrl(), mThreadInfo.getId())) {
				mDaoImpl.insertThread(mThreadInfo);
			}
			
			HttpURLConnection connection = null;
			RandomAccessFile raf = null;
			InputStream input = null;
			try {
				URL url = new URL(mThreadInfo.getUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				
				//设置开始下载的位置
				int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
				connection.setRequestProperty("Range", "bytes="+start+"-"+mThreadInfo.getEnd());
				//设置文件写入位置
				File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
				Log.e("2222", DownloadService.DOWNLOAD_PATH+mFileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(start);
				Intent intent = new Intent(DownloadService.ACTION_UPDATE);
				mFinished += mThreadInfo.getFinished();
				
				//开始下载
				if (connection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
					//读取数据
					input = connection.getInputStream();
					byte[] buffer = new byte[1024];
					int len = -1;
					long time = System.currentTimeMillis();
					while ((len=input.read(buffer))!=-1) {
						//写入文件
						raf.write(buffer, 0, len);
						//更新进度
						mFinished += len;
						if (System.currentTimeMillis() - time > 500) {
							time = System.currentTimeMillis();
							intent.putExtra("finished", mFinished*100/mFileInfo.getLength());
							mContext.sendBroadcast(intent);
						}
						//下载暂停，保存数据
						if (isPause) {
							mDaoImpl.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
							return;
						}
					}
					//删除线程信息
					mDaoImpl.deleteThread(mThreadInfo.getUrl(), mThreadInfo.getId());
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
