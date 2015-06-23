package com.example.gestruredetectottest.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ShadowLayout extends RelativeLayout {

	private float mDepth = 0.5f;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Bitmap mShadowBitmap;
	private int BLUR_WIDTH = 5;
	private final Rect mOriginRect = new Rect(0, 0, 150 + 2*BLUR_WIDTH, 150 + 2*BLUR_WIDTH);
	private RectF mDesRecF = new RectF(0, 0, 150, 150);
	private int mRadius = 6;
	private Paint mAlphaPaint;
	
	public ShadowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public ShadowLayout(Context context) {
		super(context);
		initView(context);
	}
	private void initView(Context context) {
		setWillNotDraw(false);
		//���û��ʵ� style
		mPaint.setStyle(Style.FILL);
		//���û��ʵ�ģ��Ч��
		mPaint.setMaskFilter(new BlurMaskFilter(BLUR_WIDTH, Blur.NORMAL));
		//���û��ʵ���ɫ
		mPaint.setColor(Color.BLACK);
		//���� bitmapͼƬ
		mShadowBitmap = Bitmap.createBitmap(mOriginRect.width(), mOriginRect.height(), Config.ARGB_8888);
		//�󶨵���������
		Canvas canvas = new Canvas(mShadowBitmap);
		//�û���ƽ��
		canvas.translate(BLUR_WIDTH, BLUR_WIDTH);
		//������ӰЧ��
		canvas.drawRoundRect(mDesRecF, mRadius, mRadius, mPaint);
		mAlphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	public void setDepth(float depth){
		mDepth = depth;
		invalidate();
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void dispatchDraw(Canvas canvas) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			if(view.getVisibility() == GONE || view.getVisibility() == INVISIBLE || view.getAlpha() == 0){
				continue;
			}
			int left = view.getLeft();
			int top = view.getTop();
			
			//���滭����λ��
			canvas.save();
			//ƽ�ƻ���
			canvas.translate(left+(1-mDepth)*80, top+(1-mDepth)*80);
			//������Ӱ�Ļ��ƿ��
			mAlphaPaint.setAlpha((int)(125+100*(mDepth)));
			//��ȡ��Ӱ�Ļ��ƿ��
			mDesRecF.right = view.getWidth();
			//��ȡ��Ӱ�Ļ��Ƹ߶�
			mDesRecF.bottom = view.getHeight();
			//������Ӱ
			canvas.drawBitmap(mShadowBitmap, mOriginRect, mDesRecF,mAlphaPaint);
			canvas.restore();
		}
		super.dispatchDraw(canvas);
	}
}
