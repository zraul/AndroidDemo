package com.zraul.baseadapter;

import java.util.List;

import com.zraul.baseadapter.bean.Bean;
import com.zraul.baseadapter.utils.CommonAdapter;
import com.zraul.baseadapter.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class MyAdapterWithCommonViewHolder extends CommonAdapter<Bean> {

	public MyAdapterWithCommonViewHolder(Context context, List<Bean> datas) {
		super(context, datas);
	}

	@Override
	public void convert(ViewHolder holder, final Bean t) {
		// TODO Auto-generated method stub
		// ((TextView)(holder.getView(R.id.id_title))).setText(t.getTitle());
		// ((TextView)(holder.getView(R.id.id_desc))).setText(t.getDesc());
		// ((TextView)(holder.getView(R.id.id_time))).setText(t.getTime());
		// ((TextView)(holder.getView(R.id.id_phone))).setText(t.getPhone());
		holder.setText(R.id.id_title, t.getTitle())
				.setText(R.id.id_desc, t.getDesc())
				.setText(R.id.id_time, t.getTime())
				.setText(R.id.id_phone, t.getPhone());
		
		final CheckBox cBox = holder.getView(R.id.id_cb);
		cBox.setChecked(t.isChecked());
		
		cBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				t.setChecked(cBox.isChecked());
			}
		});
	}
}
