package com.example.servicedownload;

import java.util.List;

import com.example.entities.FileInfo;
import com.example.services.DownloadService;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.text.StaticLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * @author zhengraul
 * 文件列表适配器
 */

public class FileListAdapter extends BaseAdapter {

	private Context mContext = null;
	private List<FileInfo> mFileList = null;

	public FileListAdapter(Context mContext, List<FileInfo> mFileList) {
		this.mContext = mContext;
		this.mFileList = mFileList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mFileList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		final FileInfo fInfo = mFileList.get(position);
		if (convertView == null) {
			//加载视图
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem, null);
			//获得布局中的控件
			holder = new ViewHolder();
			holder.tvFile = (TextView) convertView.findViewById(R.id.textView1);
			holder.btStart = (Button) convertView.findViewById(R.id.button1);
			holder.btStop = (Button) convertView.findViewById(R.id.button2);
			holder.pbFile = (ProgressBar) convertView.findViewById(R.id.progressBar1);
			holder.tvFile.setText(fInfo.getFileName());
			holder.pbFile.setMax(100);
			holder.btStart.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext, DownloadService.class);
					intent.setAction(DownloadService.ACTION_START);
					intent.putExtra("fileInfo", fInfo);
					mContext.startService(intent);
				}
			});
			holder.btStop.setOnClickListener(new OnClickListener() {		
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext, DownloadService.class);
					intent.setAction(DownloadService.ACTION_STOP);
					intent.putExtra("fileInfo", fInfo);
					mContext.startService(intent);
				}
			});
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.pbFile.setProgress(fInfo.getFinished());	
		return convertView;
	}

	/**
	 * 
	 * @param id
	 * @param progress
	 */
	public void updateProgress(int id, int progress){
		FileInfo fileInfo = mFileList.get(id);
		fileInfo.setFinished(progress);
		notifyDataSetChanged();
	}
	
	/**
	 * 视图的缓存
	 * @author zhengraul
	 * 
	 */
	static class ViewHolder{
		public TextView tvFile;
		public Button btStart;
		public Button btStop;
		public ProgressBar pbFile;
	}
}
