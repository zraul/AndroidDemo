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
 * @author zhengraul ����������
 */

public class DownloadTask {
	private Context mContext;
	private FileInfo mFileInfo;
	private ThreadDAOImpl mDaoImpl;
	private int mFinished = 0;
	public boolean isPause = false;
	private int mThreadCount = 1;// �߳�����
	private List<DownloadThread> mThreadList = null;
	public static ExecutorService sExecutorService = Executors.newCachedThreadPool();//�̳߳�
	
	public DownloadTask(Context mContext, FileInfo mFileInfo, int mThreadCount) {
		super();
		this.mContext = mContext;
		this.mFileInfo = mFileInfo;
		this.mDaoImpl = new ThreadDAOImpl(mContext);
		this.mThreadCount = mThreadCount;
	}

	public void download() {
		// ��ȡ���ݿ���߳���Ϣ
		List<ThreadInfo> threadInfos = mDaoImpl.getThreads(mFileInfo.getUrl());
		if (threadInfos.size() == 0) {
			// ���ÿ���߳����صĳ���
			int length = mFileInfo.getLength() / mThreadCount;
			for (int i = 0; i < mThreadCount; i++) {
				//���������ͬ���߳���Ϣ
				ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(),length * i, (i + 1)
						* length - 1, 0);
				if (i == mThreadCount - 1) {
					threadInfo.setEnd(mFileInfo.getLength());
				}
				//��ӵ��߳���Ϣ������
				threadInfos.add(threadInfo);
				mDaoImpl.insertThread(threadInfo);
			}
		}
		
		mThreadList = new ArrayList<DownloadThread>();
		
		//��������߳̽�������
		for (ThreadInfo info:threadInfos) {
			DownloadThread thread = new DownloadThread(info);
//			thread.start();
			DownloadTask.sExecutorService.execute(thread);//ʹ���̳߳������߳�
			mThreadList.add(thread);
		}
	}
	/**
	 * �ж��Ƿ������̶߳�ִ�����
	 * @return
	 */
	private synchronized void checkAllThreadFinished(){
		boolean allFinished = true;
		//�����̼߳��ϣ��ж��߳��Ƿ�ִ�����
		for (DownloadThread thread:mThreadList) {
			if (!thread.isFinished) {
				allFinished = false;
				break;
			}
		}
		if (allFinished) {
			// ɾ���߳���Ϣ
			mDaoImpl.deleteThread(mFileInfo.getUrl());
			//���͹㲥֪ͨUI�������������
			Intent intent = new Intent(DownloadService.ACTION_FINISH);
			intent.putExtra("fileInfo", mFileInfo);
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 
	 * @author zhengraul �����߳�
	 */
	class DownloadThread extends Thread {
		private ThreadInfo mThreadInfo;
		public boolean isFinished = false;//�߳��Ƿ����

		public DownloadThread(ThreadInfo mThreadInfo) {
			super();
			this.mThreadInfo = mThreadInfo;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// �����ݿ�����µ��߳���Ϣ
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

				// ���ÿ�ʼ���ص�λ��
				int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
				connection.setRequestProperty("Range", "bytes=" + start + "-"
						+ mThreadInfo.getEnd());
				// �����ļ�д��λ��
				File file = new File(DownloadService.DOWNLOAD_PATH,
						mFileInfo.getFileName());
				Log.e("2222",
						DownloadService.DOWNLOAD_PATH + mFileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(start);
				Intent intent = new Intent(DownloadService.ACTION_UPDATE);
				mFinished += mThreadInfo.getFinished();

				// ��ʼ����
				if (connection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
					// ��ȡ����
					input = connection.getInputStream();
					byte[] buffer = new byte[1024];
					int len = -1;
					long time = System.currentTimeMillis();
					while ((len = input.read(buffer)) != -1) {
						// д���ļ�
						raf.write(buffer, 0, len);
						// �ۼ������ļ���ɵĽ���
						mFinished += len;
						//�ۼ�ÿ���߳���ɵĽ���
						mThreadInfo.setFinished(mThreadInfo.getFinished()+len);
						//���½���
						if (System.currentTimeMillis() - time > 1000) {
							time = System.currentTimeMillis();
							intent.putExtra("finished", mFinished * 100
									/ mFileInfo.getLength());
							intent.putExtra("id", mFileInfo.getId());
							mContext.sendBroadcast(intent);
						}
						// ������ͣ����������
						if (isPause) {
							mDaoImpl.updateThread(mThreadInfo.getUrl(),
									mThreadInfo.getId(), mThreadInfo.getFinished());
							return;
						}
					}
					//��ʶ�߳�ִ�����
					isFinished = true;
					//������������Ƿ�ִ�����
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
