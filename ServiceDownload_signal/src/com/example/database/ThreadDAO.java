package com.example.database;

import java.util.List;

import android.R.integer;

import com.example.entities.ThreadInfo;

/**
 * @author zhengraul
 * 数据访问接口
 */
public interface ThreadDAO {
	public void insertThread(ThreadInfo threadInfo);//插入线程信息
	public void deleteThread(String url, int thread_id);//删除线程信息
	public void updateThread(String url,int thread_id,int finished);//更新线程下载进度
	public List<ThreadInfo> getThreads(String url);//查询文件的线程信息
	public boolean isExists(String url, int thread_id);//线程信息是否已经存在
}
