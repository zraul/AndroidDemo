package com.zraul.baseadapter.utils;

import java.util.List;

import com.zraul.baseadapter.R;
import com.zraul.baseadapter.bean.Bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;

	public CommonAdapter(Context context, List<T> datas) {
		this.mContext = context;
		this.mDatas = datas;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
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
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item, position);

		convert(holder, getItem(position));
		
		return holder.getConvertView();
	}
	
	
	public abstract void convert(ViewHolder holder, T t);
}
