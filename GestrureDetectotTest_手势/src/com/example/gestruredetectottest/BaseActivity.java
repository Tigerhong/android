package com.example.gestruredetectottest;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBase() ;
		//初始化 手势识别器
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//e1:第一次按下的位置 e2: 当手离开屏幕时的位置 velocityX 沿x轴的速度 velocityY沿y轴的速度

				//判断竖直方向移动的大小
				if(Math.abs(e1.getRawY() - e2.getRawY()) >100){
					Toast.makeText(getApplicationContext(), "动作不合法", 1).show();
					return true;
				}
				if(Math.abs(velocityY) < 150){
					Toast.makeText(getApplicationContext(), "移动太慢", 1).show();
					return true;
				}

				if((e1.getRawX() - e2.getRawX()) > 200){//表示向右滑动 表示下一页
					//					Toast.makeText(getApplicationContext(), "移动太慢", 1).show();
					next(null);
					return true;
				}
				if((e2.getRawX() - e1.getRawX()) > 200){//表示向左滑动 表示下一页
					//					Toast.makeText(getApplicationContext(), "移动太慢", 1).show();
					pre(null);
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}});

	}
	/**
	 * 在setContentView方法执行前执行一些数据操作
	 */
	private void initBase() {
		// TODO Auto-generated method stub

	}
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		getIntentData() ;
		initView() ;
		initData() ;
	}
	/**
	 * 初始化view
	 */
	public abstract void initView() ;
/**
 * 初始化数据
 */
	public abstract void initData() ;
	/**
	 * 获得通过intent传过里啊的数据
	 */
	public void getIntentData() {
		// TODO Auto-generated method stub

	}
	//上一页
	public abstract void pre(View view);
	//下一页
	public abstract void next(View view) ;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
