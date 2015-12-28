package com.example.wx6demo;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbAccessory;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconWithText extends View {

	private int mColor = 0xFF45C01A;
	private Bitmap mIconBitmap;
	private String mText = "΢��";
	private int mTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;
	private float mAlpha;// ͸����
	private Rect mIconRect;
	private Rect mTextBound;
	private Paint mTextPaint;

	public ChangeColorIconWithText(Context context) {
		this(context, null);
	}

	public ChangeColorIconWithText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ChangeColorIconWithText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		TypedArray aTypedValue = context.obtainStyledAttributes(attrs,
				R.styleable.ChangeColorIconWithText);

		for (int i = 0; i < aTypedValue.getIndexCount(); i++) {
			int attr = aTypedValue.getIndex(i);
			switch (attr) {
			case R.styleable.ChangeColorIconWithText_icon:
				BitmapDrawable drawable = (BitmapDrawable) aTypedValue
						.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
				break;
			case R.styleable.ChangeColorIconWithText_color:
				mColor = aTypedValue.getColor(attr, 0xFF45C01A);
				break;
			case R.styleable.ChangeColorIconWithText_text:
				mText = aTypedValue.getString(attr);
				break;
			case R.styleable.ChangeColorIconWithText_text_size:
				mColor = (int) aTypedValue.getDimension(attr, TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
								getResources().getDisplayMetrics()));
				break;
			default:
				break;
			}
		}

		aTypedValue.recycle();

		mTextBound = new Rect();
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0xff555555);
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
				- getPaddingRight(), getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom() - mTextBound.height());
		int left = getMeasuredWidth() / 2 - iconWidth / 2;
		int top = (getMeasuredHeight() - mTextBound.height()) / 2 - iconWidth
				/ 2;

		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		int alpha = (int) Math.ceil(255 * mAlpha);
		//���ڴ��б���Bitmap���Ȼ��ƴ�ɫ���ٻ���ͼ��
		setupTargetBitmap(alpha);
		
		//1������ԭ�ı� 2�����Ʊ�ɫ�ı�
		drawSourceText(canvas,alpha);
		drawTargetText(canvas,alpha);
		//���ڴ��е�Bitmap���Ƶ������
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	private void drawTargetText(Canvas canvas, int alpha) {
		// TODO Auto-generated method stub
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		int x = getMeasuredWidth()/2-mTextBound.width()/2;
		int y = mIconRect.bottom+mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);
	}

	private void drawSourceText(Canvas canvas, int alpha) {
		// TODO Auto-generated method stub
		mTextPaint.setColor(0xff333333);
		mTextPaint.setAlpha(255-alpha);
		int x = getMeasuredWidth()/2-mTextBound.width()/2;
		int y = mIconRect.bottom+mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);
	}

	/**
	 * ���ڴ��л��ƿɱ�ɫ��Icon
	 * */
	private void setupTargetBitmap(int alpha) {
		// TODO Auto-generated method stub
		//���ƴ�ɫ
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		mCanvas.drawRect(mIconRect, mPaint);
		//����DST_IN
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		//����ͼ��
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}
	
	public void setIconAlpha(float alpha){
		this.mAlpha = alpha;
		invalidateView();
	}

	/**
	 * �ػ�
	 * **/
	private void invalidateView() {
		// TODO Auto-generated method stub
		//ui�߳��ж�
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		}else {
			postInvalidate();
		}
	}
	
	private static final String INSTANCE_STATUS = "instance_status";
	private static final String STATUS_ALPHA = "status_alpha";
	
	@Override
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		//�ȱ��游�����Ľ��
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		//������Լ������Ľ��
		bundle.putFloat(STATUS_ALPHA, mAlpha);
		
		return bundle;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			mAlpha = bundle.getFloat(STATUS_ALPHA);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
			return;
		}
		super.onRestoreInstanceState(state);
	}
}
