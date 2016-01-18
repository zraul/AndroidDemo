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
 * @author zhengraul ����������
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
		//��ȡ���ݿ���߳���Ϣ
		List<ThreadInfo> threadInfos = mDaoImpl.getThreads(mFileInfo.getUrl());
		ThreadInfo threadInfo = null;
		if (threadInfos.size() == 0) {
			//��ʼ���߳���Ϣ����
			threadInfo = new ThreadInfo(0,mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
		} else {
			threadInfo = threadInfos.get(0);
		}
		
		//�������߳�
		new DownloadThread(threadInfo).start();
	}
	
	/**
	 * 
	 * @author zhengraul
	 * �����߳�
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
			//�����ݿ�����µ��߳���Ϣ
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
				
				//���ÿ�ʼ���ص�λ��
				int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
				connection.setRequestProperty("Range", "bytes="+start+"-"+mThreadInfo.getEnd());
				//�����ļ�д��λ��
				File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
				Log.e("2222", DownloadService.DOWNLOAD_PATH+mFileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(start);
				Intent intent = new Intent(DownloadService.ACTION_UPDATE);
				mFinished += mThreadInfo.getFinished();
				
				//��ʼ����
				if (connection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
					//��ȡ����
					input = connection.getInputStream();
					byte[] buffer = new byte[1024];
					int len = -1;
					long time = System.currentTimeMillis();
					while ((len=input.read(buffer))!=-1) {
						//д���ļ�
						raf.write(buffer, 0, len);
						//���½���
						mFinished += len;
						if (System.currentTimeMillis() - time > 500) {
							time = System.currentTimeMillis();
							intent.putExtra("finished", mFinished*100/mFileInfo.getLength());
							mContext.sendBroadcast(intent);
						}
						//������ͣ����������
						if (isPause) {
							mDaoImpl.updateThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
							return;
						}
					}
					//ɾ���߳���Ϣ
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
