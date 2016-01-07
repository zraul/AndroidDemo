package com.android.zwrobot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.zwrobot.bean.ChatMessage;
import com.android.zwrobot.bean.ChatMessage.Type;
import com.android.zwrobot.utils.HttpUtils;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;
	
	private EditText mInputMsg;
	private Button mSendBtn;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ChatMessage chatMessage = (ChatMessage) msg.obj;
			mDatas.add(chatMessage);
			mAdapter.notifyDataSetChanged();
			mMsgs.smoothScrollToPosition(mDatas.size()-1);//自动滑动到最新的聊天位置
		};
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        initView();
        initDatas();
        initListener();
    }

	private void initListener() {
		// TODO Auto-generated method stub
		mSendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String toMsg = mInputMsg.getText().toString();
				if (TextUtils.isEmpty(toMsg)) {
					Toast.makeText(MainActivity.this, "发送消息不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				
				ChatMessage toMessage = new ChatMessage();
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				mDatas.add(toMessage);
				mAdapter.notifyDataSetChanged();
				mInputMsg.setText("");
				
				new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ChatMessage froMessage = HttpUtils.sendMessage(toMsg);
						Message msg = new Message();
						msg.obj = froMessage;
						mHandler.sendMessage(msg);
					}
				}.start();
			}
		});
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		mDatas = new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("你好，我为你服务", Type.INCOMING, new Date()));
		
		mAdapter = new ChatMessageAdapter(this, mDatas);
		mMsgs.setAdapter(mAdapter);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mMsgs = (ListView) findViewById(R.id.id_listview_chat);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendBtn = (Button) findViewById(R.id.id_send_msg);
	}
    
}
