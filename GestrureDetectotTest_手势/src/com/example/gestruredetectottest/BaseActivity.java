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
		//��ʼ�� ����ʶ����
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//e1:��һ�ΰ��µ�λ�� e2: �����뿪��Ļʱ��λ�� velocityX ��x����ٶ� velocityY��y����ٶ�

				//�ж���ֱ�����ƶ��Ĵ�С
				if(Math.abs(e1.getRawY() - e2.getRawY()) >100){
					Toast.makeText(getApplicationContext(), "�������Ϸ�", 1).show();
					return true;
				}
				if(Math.abs(velocityY) < 150){
					Toast.makeText(getApplicationContext(), "�ƶ�̫��", 1).show();
					return true;
				}

				if((e1.getRawX() - e2.getRawX()) > 200){//��ʾ���һ��� ��ʾ��һҳ
					//					Toast.makeText(getApplicationContext(), "�ƶ�̫��", 1).show();
					next(null);
					return true;
				}
				if((e2.getRawX() - e1.getRawX()) > 200){//��ʾ���󻬶� ��ʾ��һҳ
					//					Toast.makeText(getApplicationContext(), "�ƶ�̫��", 1).show();
					pre(null);
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}});

	}
	/**
	 * ��setContentView����ִ��ǰִ��һЩ���ݲ���
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
	 * ��ʼ��view
	 */
	public abstract void initView() ;
/**
 * ��ʼ������
 */
	public abstract void initData() ;
	/**
	 * ���ͨ��intent�����ﰡ������
	 */
	public void getIntentData() {
		// TODO Auto-generated method stub

	}
	//��һҳ
	public abstract void pre(View view);
	//��һҳ
	public abstract void next(View view) ;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
