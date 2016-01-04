package com.zw.qqslidingmenu;

import com.nineoldandroids.view.ViewHelper;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mMenuWidth;

	private int mScreenWidth;
	private int mMenuRightPadding = 50;

	private boolean once = false;
	private boolean isOpen = false;

	/**
	 * 当使用了自定义属性时，会调用此构造方法
	 * **/
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		// 获取自定义属性,TypedArray必须recycle
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = array.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = array.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = array.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		array.recycle();

		WindowManager wManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;

		// 把dp转化为px
		// mMenuRightPadding = (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
		// .getDisplayMetrics());
	}

	// 未使用自定义属性时调用
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context) {
		this(context, null);
	}

	@Override
	/**
	 * 设置子View的宽和高， 设置自己的宽和高
	 * **/
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);

			mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 设置偏移量隐藏Menu
	 * **/

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			// 隐藏的宽度
			int scrollX = getScrollX();
			if (scrollX >= mMenuWidth / 2) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	public void openMen() {
		if (isOpen) {
			return;
		} else {
			this.smoothScrollTo(0, 0);
			isOpen = true;
		}
	}

	public void closeMenu() {
		if (!isOpen) {
			return;
		} else {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}

	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMen();
		}
	}

	/**
	 * 滚动发生时
	 * **/
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		// 调用属性动画，设置TranslationX
		float scale = l * 1.0f / mMenuWidth;

		float rightScale = 0.7f + 0.3f * scale;
		float leftScale = 1.0f - scale * 0.3f;
		float leftAlpha = 0.6f + 0.4f * (1 - scale);
		// 调用动画
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

		// 设置菜单动画
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);

		// 缩放内容区域,设置缩放中心点
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

	}
}
