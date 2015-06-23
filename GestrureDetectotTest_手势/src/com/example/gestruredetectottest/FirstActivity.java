package com.example.gestruredetectottest;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class FirstActivity extends BaseActivity {
private FrameLayout mFrameLayout;
private ColorDrawable colorDrawable;
private Bitmap mBitmap;
private ImageView mImageView;
protected int mLeft;
protected int mTop;
protected float mScaleX;
protected float mScaleY;
private Activity mActivity;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	mActivity = this;
	setContentView(R.layout.activity_detail);
}
	@Override
	public void pre(View view) {
		activityOutAnim(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mActivity.finish();
				mActivity.overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
			}
		});
	}
@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		   activityOutAnim(new Runnable() {
	            @Override
	            public void run() {
	                finish();
	                overridePendingTransition(0, 0);
	            }
	        });
	}
	
	@Override
	public void next(View view) {
		startActivity(new Intent(this, SecondActivity.class));
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	@SuppressLint("NewApi")
	@Override
	public void initView() {
		final int left = getIntent().getIntExtra("locationX", 0);
		final int top = getIntent().getIntExtra("locationY", 0);
		final int width = getIntent().getIntExtra("width", 0);
		final int hight = getIntent().getIntExtra("hight", 0);
		int resId = getIntent().getIntExtra("resId", 0);
		mFrameLayout = (FrameLayout)findViewById(R.id.id_layout);
		colorDrawable = new ColorDrawable(Color.RED);
		mFrameLayout.setBackground(colorDrawable);
		mBitmap = BitmapFactory.decodeResource(getResources(),resId);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),mBitmap);
		
		mImageView = (ImageView)findViewById(R.id.id_detail);
		mImageView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
				int location[] = new int[2];
				mImageView.getLocationOnScreen(location);
				mLeft = left - location[0];
				mTop = top - location[1];
				mScaleX = (width*1.0f/mImageView.getWidth());
				mScaleY = (hight*1.0f/mImageView.getHeight());
			     Log.v("zgy", "========resId========" + mImageView.getWidth()) ;
	                Log.v("zgy", "========resId========" + mScaleY) ;
	                activityEnterAnim();
				return true;
			}
		});
		mImageView.setImageDrawable(bitmapDrawable);
	}
	@SuppressLint("NewApi")
	protected void activityEnterAnim() {
		//设置图片的位置，起始位置和缩放位置，和平移的位置
		   mImageView.setPivotX(0);
	        mImageView.setPivotY(0);
	        mImageView.setScaleX(mScaleX);
	        mImageView.setScaleY(mScaleY);
	        mImageView.setTranslationX(mLeft);
	        mImageView.setTranslationY(mTop);
	        mImageView.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).
	                setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
	        //设置背景的透明度
	        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(colorDrawable,"alpha",0,255);
	        //设置属性动画的插值器
	        objectAnimator.setInterpolator(new DecelerateInterpolator());
	        objectAnimator.setDuration(1000);
	        objectAnimator.start();
	}
	
	@SuppressLint("NewApi")
	private void activityOutAnim(Runnable runnable) {
		 mImageView.setPivotX(0);
	        mImageView.setPivotY(0);
	        mImageView.animate().scaleX(mScaleX).scaleY(mScaleY).translationX(mLeft).translationY(mTop).
	                withEndAction(runnable).
	                setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
	        
	        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(colorDrawable,"alpha",255,0);
	        objectAnimator.setInterpolator(new DecelerateInterpolator());
	        objectAnimator.setDuration(1000);
	        objectAnimator.start();
	}
	@Override
	public void initData() {
		
	}
	@Override
		public void getIntentData() {
			super.getIntentData();
		}
}
