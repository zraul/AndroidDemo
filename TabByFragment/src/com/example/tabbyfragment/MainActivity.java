package com.example.tabbyfragment;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.os.Build;
import android.view.View.*;

public class MainActivity extends FragmentActivity implements
		android.view.View.OnClickListener {

	private LinearLayout mTabWeixin;
	private LinearLayout mTabFriend;
	private LinearLayout mTabAddress;
	private LinearLayout mTabSettings;

	private ImageButton mImgWeixin;
	private ImageButton mImgFriend;
	private ImageButton mImgAddress;
	private ImageButton mImgSettings;
	
	private Fragment mTab1;
	private Fragment mTab2;
	private Fragment mTab3;
	private Fragment mTab4;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		initEvent();
		setSelect(0);
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		mTabWeixin.setOnClickListener(this);
		mTabFriend.setOnClickListener(this);
		mTabAddress.setOnClickListener(this);
		mTabSettings.setOnClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTabWeixin = (LinearLayout) findViewById(R.id.id_tab_weixin);
		mTabFriend = (LinearLayout) findViewById(R.id.id_tab_frd);
		mTabAddress = (LinearLayout) findViewById(R.id.id_tab_address);
		mTabSettings = (LinearLayout) findViewById(R.id.id_tab_settings);

		mImgWeixin = (ImageButton) findViewById(R.id.id_tab_weixin_img);
		mImgFriend = (ImageButton) findViewById(R.id.id_tab_frd_img);
		mImgAddress = (ImageButton) findViewById(R.id.id_tab_address_img);
		mImgSettings = (ImageButton) findViewById(R.id.id_tab_settings_img);
	}

	private void setSelect(int i) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();//事务
		hideFragment(transaction);
		
		// 修改图片为亮的
		// 设置内容区域
		switch (i) {
		case 0:
			if (mTab1 == null) {
				mTab1 = new WeixinFragment();
				transaction.add(R.id.id_content, mTab1);
			} else {
				transaction.show(mTab1);
			}
			mImgWeixin.setImageResource(R.drawable.tab_weixin_pressed);
			break;
		case 1:
			if (mTab2 == null) {
				mTab2 = new FriendFragment();
				transaction.add(R.id.id_content, mTab2);
			} else {
				transaction.show(mTab2);
			}
			mImgFriend.setImageResource(R.drawable.tab_find_frd_pressed);
			break;
		case 2:
			if (mTab3 == null) {
				mTab3 = new AddressFragment();
				transaction.add(R.id.id_content, mTab3);
			} else {
				transaction.show(mTab3);
			}
			mImgAddress.setImageResource(R.drawable.tab_address_pressed);
			break;
		case 3:
			if (mTab4 == null) {
				mTab4 = new SettingFragment();
				transaction.add(R.id.id_content, mTab4);
			} else {
				transaction.show(mTab4);
			}
			mImgSettings.setImageResource(R.drawable.tab_settings_pressed);
			break;
		default:
			break;
		}
		
		transaction.commit();
	}

	private void hideFragment(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (mTab1 != null) {
			transaction.hide(mTab1);
		}
		
		if (mTab2 != null) {
			transaction.hide(mTab2);
		}
		
		if (mTab3 != null) {
			transaction.hide(mTab3);
		}
		
		if (mTab4 != null) {
			transaction.hide(mTab4);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		resetImgs();
		switch (v.getId()) {
		case R.id.id_tab_weixin:
			setSelect(0);
			break;
		case R.id.id_tab_frd:
			setSelect(1);
			break;
		case R.id.id_tab_address:
			setSelect(2);
			break;
		case R.id.id_tab_settings:
			setSelect(3);
			break;
		default:
			break;
		}
	}

	private void resetImgs() {
		// TODO Auto-generated method stub
		mImgWeixin.setImageResource(R.drawable.tab_weixin_normal);
		mImgFriend.setImageResource(R.drawable.tab_find_frd_normal);
		mImgAddress.setImageResource(R.drawable.tab_address_normal);
		mImgSettings.setImageResource(R.drawable.tab_settings_normal);
	}

}
