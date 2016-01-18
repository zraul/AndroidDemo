package com.example.servicedownload;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity implements OnClickListener {

	private TextView mTextView;
	private ProgressBar mProgressBar;
	private Button mBtStart;
	private Button mBtStop;
	private FileInfo mFileInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTextView = (TextView) findViewById(R.id.textView1);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mBtStart = (Button) findViewById(R.id.button1);
		mBtStop = (Button) findViewById(R.id.button2);
		mProgressBar.setMax(100);
		
		//创建文件信息对象
		mFileInfo  = new FileInfo(0,
				"http://dlsw.baidu.com/sw-search-sp/soft/2b/12883/QQPinyin_Setup_4.7.2065.400.1420767664.exe", 
				"QQPinyin_Setup_4.7.2065.400.1420767664.exe", 0, 0);
		
		mBtStart.setOnClickListener(this);
		mBtStop.setOnClickListener(this);
		//注册广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadService.ACTION_UPDATE);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			startDownload();
			break;
		case R.id.button2:
			stopDownload();
			break;
		default:
			break;
		}
	}

	public void startDownload(){
		//通过Intent传递参数
		Intent intent = new Intent(MainActivity.this, DownloadService.class);
		intent.setAction(DownloadService.ACTION_START);
		intent.putExtra("fileInfo", mFileInfo);
		startService(intent);
	}
	
	public void stopDownload(){
		//通过Intent传递参数
		Intent intent = new Intent(MainActivity.this, DownloadService.class);
		intent.setAction(DownloadService.ACTION_STOP);
		intent.putExtra("fileInfo", mFileInfo);
		startService(intent);
	}
	
	/**
	 * 接收更新进度条的广播
	 */
	BroadcastReceiver mReceiver = new BroadcastReceiver(){
		public void onReceive(android.content.Context context, Intent intent) {
			if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
				int finished = intent.getIntExtra("finished", 0);
				mProgressBar.setProgress(finished);
			}
		};
	};
}
