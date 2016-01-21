package com.zraul.baseadapter;

import java.util.List;

import com.zraul.baseadapter.bean.Bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private List<Bean> mDatas;
	private LayoutInflater mInflater;
	
	public MyAdapter(Context context, List<Bean> datas){
		this.mInflater = LayoutInflater.from(context);
		mDatas = datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item, parent, false);
			holder = new ViewHolder();
			holder.mTitle = (TextView) convertView.findViewById(R.id.id_title);
			holder.mDesc = (TextView) convertView.findViewById(R.id.id_desc);
			holder.mTime = (TextView) convertView.findViewById(R.id.id_time);
			holder.mPhone = (TextView) convertView.findViewById(R.id.id_phone);	
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Bean bean = mDatas.get(position);
		holder.mTitle.setText(bean.getTitle());
		holder.mDesc.setText(bean.getDesc());
		holder.mTime.setText(bean.getTime());
		holder.mPhone.setText(bean.getPhone());
		
		return convertView;
	}

	private class ViewHolder{
		public TextView mTitle;
		public TextView mDesc;
		public TextView mTime;
		public TextView mPhone;
	}
}
