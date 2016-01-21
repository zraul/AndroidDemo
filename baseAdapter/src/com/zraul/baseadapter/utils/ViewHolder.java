package com.zraul.baseadapter.utils;

import android.R.integer;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parentGroup, int layoutId,
			int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId,
				parentGroup, false);

		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parentGroup, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parentGroup, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	public View getConvertView() {
		return this.mConvertView;
	}

	/**
	 * ͨ��ViewID��ȡView
	 * 
	 * @param viewId
	 * @return
	 */

	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	
	public ViewHolder setText(int viewId, String text){
		TextView tView = getView(viewId);
		tView.setText(text);
		return this;
	}
}
