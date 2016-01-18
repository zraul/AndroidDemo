package com.example.database;

import java.util.List;

import android.R.integer;

import com.example.entities.ThreadInfo;

/**
 * @author zhengraul
 * ���ݷ��ʽӿ�
 */
public interface ThreadDAO {
	public void insertThread(ThreadInfo threadInfo);//�����߳���Ϣ
	public void deleteThread(String url, int thread_id);//ɾ���߳���Ϣ
	public void updateThread(String url,int thread_id,int finished);//�����߳����ؽ���
	public List<ThreadInfo> getThreads(String url);//��ѯ�ļ����߳���Ϣ
	public boolean isExists(String url, int thread_id);//�߳���Ϣ�Ƿ��Ѿ�����
}
