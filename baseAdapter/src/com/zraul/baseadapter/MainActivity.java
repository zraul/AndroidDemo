package com.zraul.baseadapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.zraul.baseadapter.bean.Bean;

public class MainActivity extends Activity {

	private ListView mListView;
	private List<Bean> mDatas;
	private MyAdapter mAdapter;
	private MyAdapterWithCommonViewHolder mAdapterWithCommonViewHolder;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initDatas();
        initView();
    }


	private void initDatas() {
		// TODO Auto-generated method stub
		Bean bean = new Bean("Android", "ﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧ", "2016-01-20", "10086");
		Bean bean1 = new Bean("Android1", "ﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧ", "2016-01-20", "10086");
		Bean bean2 = new Bean("Android2", "ﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧ", "2016-01-20", "10086");
		Bean bean3 = new Bean("Android3", "ﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧ", "2016-01-20", "10086");
		Bean bean4 = new Bean("Android4", "ﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧ", "2016-01-20", "10086");
		Bean bean5 = new Bean("Android5", "ﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧﾍｨﾓﾃﾊﾊﾅ菷ﾍﾊﾇﾕ篥ｴﾗｧ", "2016-01-20", "10086");
		mDatas = new ArrayList<Bean>();
		mDatas.add(bean);
		mDatas.add(bean1);
		mDatas.add(bean2);
		mDatas.add(bean3);
		mDatas.add(bean4);
		mDatas.add(bean5);
		
		mAdapter = new MyAdapter(this, mDatas);
		mAdapterWithCommonViewHolder = new MyAdapterWithCommonViewHolder(this, mDatas);
	}


	private void initView() {
		// TODO Auto-generated method stub
		mListView = (ListView) findViewById(R.id.id_listview);
		//mListView.setAdapter(mAdapter);
		mListView.setAdapter(mAdapterWithCommonViewHolder);
	}
    
}
