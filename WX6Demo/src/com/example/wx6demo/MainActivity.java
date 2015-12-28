package com.example.wx6demo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Build;

public class MainActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener {

	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private String[] mTitles = new String[] { "First Fragment",
			"Second Fragment", "Third Fragment", "Four Fragment" };
	private FragmentPagerAdapter mAdapter;
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getActionBar().setDisplayShowHomeEnabled(false);
		setOverflowButtonAlways();

		initView();
		initDatas();
		initEvent();
	}

	//初始化所有事件
	private void initEvent() {
		// TODO Auto-generated method stub
		mViewPager.setOnPageChangeListener(this);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		for (String title : mTitles) {
			TabFragment tabFragment = new TabFragment();
			Bundle bundle = new Bundle();
			bundle.putString(TabFragment.TITLE, title);
			tabFragment.setArguments(bundle);
			mTabs.add(tabFragment);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mTabs.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_search) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method method = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					method.setAccessible(true);
					method.invoke(menu, true);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowButtonAlways() {
		ViewConfiguration configuration = ViewConfiguration.get(this);
		try {
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(configuration, false);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		clickTab(v);
		
	}

	/**
	 * 点击Tab
	**/
	private void clickTab(View v) {
		resetOtherTabs();
		switch (v.getId()) {
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		default:
			break;
		}
	}

	// 重置其他的TabIndicator的颜色
	private void resetOtherTabs() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		//Log.e("111", position+"  "+positionOffset+"   "+positionOffsetPixels);
		// TODO Auto-generated method stub
		if (positionOffset>0) {
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position+1);
			
			left.setIconAlpha(1-positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
