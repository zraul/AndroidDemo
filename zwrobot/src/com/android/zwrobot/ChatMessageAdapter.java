package com.android.zwrobot;

import java.text.SimpleDateFormat;
import java.util.List;

import com.android.zwrobot.bean.ChatMessage;
import com.android.zwrobot.bean.ChatMessage.Type;

import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ChatMessage> mDatas;
	
	public ChatMessageAdapter(Context context, List<ChatMessage> data) {
		mInflater = LayoutInflater.from(context);
		mDatas = data;
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
		ChatMessage chatMessage = mDatas.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			//通过ItemType设置不同的布局
			if (getItemViewType(position) == 0) {
				convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mDate = (TextView) convertView.findViewById(R.id.id_from_msg_data);
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
			} else {
				convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mDate = (TextView) convertView.findViewById(R.id.id_to_msg_data);
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//设置数据
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		viewHolder.mDate.setText(df.format(chatMessage.getDate()));
		viewHolder.mMsg.setText(chatMessage.getMsg());
		
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		ChatMessage chatMessage = mDatas.get(position);
		if (chatMessage.getType() == Type.INCOMING) {
			return 0;
		}
		
		return 1;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	private final class ViewHolder {
		TextView mDate;
		TextView mMsg;
		
		
	}
}
