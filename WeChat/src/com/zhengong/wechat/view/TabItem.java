package com.zhengong.wechat.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class TabItem extends View{

	private Rect mBoundText;
	private Paint mTextPaintNormal;
	private int mTextSize = 12;
	private int mTextColorNormal = 0xff45c01a;
	private Paint mTextPaintSelect;
	private int mTextColorSelect = 0xff777777;
	private Paint mIconPaintSelect;
	private Paint mIconPaintNormal;
	private String mTextValue;
	private Bitmap mIconNormal;
	private Bitmap mIconSelect;
	private int mViewWidth;
	private int mViewHeigh;
	public TabItem(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public TabItem(Context context) {
		this(context,null);
	}
	public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
		initText();
	}
	private void initText() {
		mTextPaintNormal = new Paint();
		mTextPaintNormal.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics()));
		mTextPaintNormal.setColor(mTextColorNormal);
		mTextPaintNormal.setAntiAlias(true);
		mTextPaintNormal.setAlpha(0xff);

		mTextPaintSelect = new Paint();
		mTextPaintSelect.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics()));
		mTextPaintSelect.setColor(mTextColorSelect);
		mTextPaintSelect.setAntiAlias(true);
		mTextPaintSelect.setAlpha(0);

		mIconPaintSelect = new Paint(Paint.ANTI_ALIAS_FLAG);
		mIconPaintSelect.setAlpha(0);

		mIconPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
		mIconPaintNormal.setAlpha(0xff);
	}
	private void initView() {
		mBoundText = new Rect();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heighMode = MeasureSpec.getMode(heightMeasureSpec);
		int heighSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = 0;
		int heigh = 0;

		measureText();
		int contentWidth = Math.max(mBoundText.width(), mIconNormal.getWidth());
		int desiredWidth = getPaddingLeft()+getPaddingRight()+contentWidth;
		switch (widthMode) {
		case MeasureSpec.AT_MOST:
			width = Math.min(widthSize, desiredWidth);
			break;
		case MeasureSpec.EXACTLY:
			width = widthSize;
			break;
		case MeasureSpec.UNSPECIFIED:
			width = desiredWidth;
			break;
		}
		int contentHeigh = Math.max(mBoundText.height(), mIconNormal.getHeight());
		 int desiredHeigh= getPaddingTop()+getPaddingBottom()+contentHeigh;
		 switch (heighMode) {
		case MeasureSpec.AT_MOST:
			heigh = Math.min(heighSize, desiredHeigh);
			break;
		case MeasureSpec.EXACTLY:
			heigh = heighSize;
			break;
		case MeasureSpec.UNSPECIFIED:
			heigh = desiredHeigh;
			break;
		}
		setMeasuredDimension(width, heigh);
		mViewWidth = getMeasuredWidth();
		mViewHeigh = getMeasuredHeight();
	}
	private void measureText() {
		mTextPaintNormal.getTextBounds(mTextValue, 0, mTextValue.length(), mBoundText);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawText(canvas);
	drawBitmap(canvas);		
	}
	private void drawText(Canvas canvas) {
	float x = (mViewWidth - mBoundText.width())/2.0f;
	float y = (mViewHeigh +mBoundText.height()+mIconNormal.getHeight())/2.0f;
	canvas.drawText(mTextValue, x, y, mTextPaintNormal);		
	canvas.drawText(mTextValue, x, y, mTextPaintSelect);		
	}
	private void drawBitmap(Canvas canvas) {
	int left = (mViewWidth - mIconNormal.getWidth())/2;		
	int top = (mViewHeigh - mIconNormal.getHeight()-mBoundText.height())/2;
	canvas.drawBitmap(mIconNormal, left, top,mIconPaintNormal);
	canvas.drawBitmap(mIconSelect, left, top,mIconPaintSelect);
	}
	//设置字体的大小
	public void setTextSize(int textSize){
	this.mTextSize = textSize;
	mTextPaintNormal.setTextSize(textSize);
	mTextPaintSelect.setTextSize(textSize);
	}
	
	public void setTextColotSelect(int mTextColotSelect){
	this.mTextColorSelect = mTextColotSelect;
	mTextPaintSelect.setColor(mTextColotSelect);
	mTextPaintSelect.setAlpha(0);
	}
	public void setTextColotNormal(int mTextColotNormal){
	this.mTextColorNormal = mTextColotNormal;
	mTextPaintNormal.setColor(mTextColotNormal);
	mTextPaintNormal.setAlpha(0xff);
	}
	public void setTextValue(String TextValue){
	this.mTextValue = TextValue;
	}
	public void setIconText(int[] iconSelId,String TextValue){
	this.mIconSelect = BitmapFactory.decodeResource(getResources(), iconSelId[0]);
	this.mIconNormal = BitmapFactory.decodeResource(getResources(), iconSelId[1]);
	this.mTextValue = TextValue;
	}
	public void setTabAlpha(float alpha){
	int paintAlpha = (int)(alpha*255);
	mIconPaintSelect.setAlpha(paintAlpha);
	mIconPaintNormal.setAlpha(255-paintAlpha);
	mTextPaintSelect.setAlpha(paintAlpha);
	mTextPaintNormal.setAlpha(255-paintAlpha);
	invalidate();
	}
}
