package com.example.servicedownload;

import java.util.ArrayList;
import java.util.List;

import com.example.entities.FileInfo;
import com.example.services.DownloadService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	private ListView mLvFile = null;
	private List<FileInfo> mFileList = null;
	private FileListAdapter mAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mLvFile = (ListView) findViewById(R.id.lvFile);
		// 创建文件集合
		mFileList = new ArrayList<FileInfo>();

		// 创建文件信息对象
		FileInfo fileInfo = new FileInfo(
				0,
				"http://dlsw.baidu.com/sw-search-sp/soft/2b/12883/QQPinyin_Setup_4.7.2065.400.1420767664.exe",
				"QQPinyin_Setup_4.7.2065.400.1420767664.exe", 0, 0);
		FileInfo fileInfo1 = new FileInfo(
				1,
				"http://dlsw.baidu.com/sw-search-sp/soft/2b/12883/QQPinyin_Setup_4.7.2065.400.1420767664.exe",
				"QQPinyin_Setup_4.7.2065.400.1420767664.exe", 0, 0);
		FileInfo fileInfo2 = new FileInfo(
				2,
				"http://dlsw.baidu.com/sw-search-sp/soft/2b/12883/QQPinyin_Setup_4.7.2065.400.1420767664.exe",
				"QQPinyin_Setup_4.7.2065.400.1420767664.exe", 0, 0);
		FileInfo fileInfo3 = new FileInfo(
				3,
				"http://dlsw.baidu.com/sw-search-sp/soft/2b/12883/QQPinyin_Setup_4.7.2065.400.1420767664.exe",
				"QQPinyin_Setup_4.7.2065.400.1420767664.exe", 0, 0);

		mFileList.add(fileInfo);
		mFileList.add(fileInfo1);
		mFileList.add(fileInfo2);
		mFileList.add(fileInfo3);

		// 创建适配器
		mAdapter = new FileListAdapter(this, mFileList);
		// 设置适配器
		mLvFile.setAdapter(mAdapter);

		// 注册广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadService.ACTION_UPDATE);
		filter.addAction(DownloadService.ACTION_FINISH);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	/**
	 * 接收更新进度条的广播
	 */
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(android.content.Context context, Intent intent) {
			if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
				int finished = intent.getIntExtra("finished", 0);
				int id = intent.getIntExtra("id", 0);
				mAdapter.updateProgress(id, finished);
			} else if (DownloadService.ACTION_FINISH.equals(intent.getAction())) {
				// 更新进度条为0
				FileInfo fileInfo = (FileInfo) intent
						.getSerializableExtra("fileInfo");
				mAdapter.updateProgress(fileInfo.getId(), 0);
				Toast.makeText(MainActivity.this,
						mFileList.get(fileInfo.getId()) + "下载完成",
						Toast.LENGTH_LONG).show();
			}
		};
	};
}
