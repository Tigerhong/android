package com.zhengong.wechat.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class Divider extends RecyclerView.ItemDecoration{

	private Paint mPaint;
	private int mColor = Color.YELLOW;

	private int mHeight = 1;
	private Rect mRect = new Rect();
	public Divider(){
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setStyle(Style.FILL);
	}
	
	@Override
	public void onDraw(Canvas c, RecyclerView parent) {
		int left = parent.getLeft();
		int right = parent.getRight();
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = parent.getChildAt(i);
			RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
			int top = view.getBottom() + layoutParams.bottomMargin + Math.round(ViewCompat.getTranslationX(view));
			int bottom = top + mHeight;
			mRect.set(left, top, right, bottom);
			c.drawRect(mRect, mPaint);
		}
	}
	
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
			State state) {
		outRect.set(0, 0, 0, mHeight);
	}
}
