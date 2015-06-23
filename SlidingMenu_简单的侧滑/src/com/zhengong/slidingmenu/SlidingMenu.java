package com.zhengong.slidingmenu;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlidingMenu extends ViewGroup {

	private enum SCROLL_STATE{
		Scroll_to_close,Scroll_to_open;
	}
	private View menuView;
	private View mainView;
	private Scroller mScorller;
	private SCROLL_STATE state;

	private OnSlidingMenuListener mSlidingMenuListener;
	private int mMostRecentX;
	private int downX;
	private boolean isOpen = false;

	public SlidingMenu(Context context,View main,View menu) {
		super(context);
		setMainView(main);
		setMenu(menu);
		init(context);
	}

	private void init(Context context) {
		mScorller = new Scroller(context);
	}

	private void setMenu(View menu2) {
		menuView = menu2;
		addView(menu2);
	}

	private void setMainView(View main2) {
		mainView = main2;
		addView(mainView);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//��¼�µ�һ�δ���
			mMostRecentX = (int)event.getX();
			downX = (int)event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int)event.getX();
			int deltaX = mMostRecentX - moveX;
			//����ڲ˵���ʱ���һ������˵��ر�ʱ���󻬶����ᴥ��Scroll�¼�
			if(( !isOpen && (downX -moveX) < 0 )|| (isOpen&&(downX -moveX)>0)){
				scrollBy(deltaX/2, 0);
			}
			mMostRecentX = moveX;
			break;
		case MotionEvent.ACTION_UP:
			int upX = (int)event.getX();//��¼��ָ̧���λ��
			int dx = upX - downX;//��ָ̧���λ�� - �ֻ���һ�ΰ��µ�λ��
			if(!isOpen){//�˵��ر�ʱ
				if(dx > menuView.getMeasuredWidth()/3){//�ж�λ���Ƿ����menuView��ʵ�ʿ�ȵ� 1/3��
					state = SCROLL_STATE.Scroll_to_open;
				}else{
					state = SCROLL_STATE.Scroll_to_close;
				}
			}else{//�˵���ʱ
				if(downX < menuView.getMeasuredWidth()){//�жϰ��µĴ����Ƿ���menu�У�
					if(dx < -menuView.getMeasuredWidth()/3){
						//������ʱ�Ĵ�����menu����ʱ��ֻ�����󻬶�����menuһ�룬�Ż�ر�
						state = SCROLL_STATE.Scroll_to_close;
					}else{
						state = SCROLL_STATE.Scroll_to_open;
					}	
				}else{//������ʱ�Ĵ�����main����ʱ���������ر�
					state = SCROLL_STATE.Scroll_to_close;
				}	
			}
			smoothScrollto();
			break;
		}
		return true;
	}

	private void smoothScrollto() {
		int scrollX = getScrollX();
		switch (state) {
		case Scroll_to_close:
			mScorller.startScroll(scrollX, 0, -scrollX, 0, 500);
			if(mSlidingMenuListener != null){
				mSlidingMenuListener.close();
			}
			isOpen = false;
			break;
		case Scroll_to_open:
			mScorller.startScroll(scrollX, 0, -scrollX - menuView.getMeasuredWidth(), 0, 500);
			if(mSlidingMenuListener != null){
				mSlidingMenuListener.open();
			}
			isOpen = true;
			break;
		}
	}

	public void open(){
		state = SCROLL_STATE.Scroll_to_open;
		smoothScrollto();
	}
	
	public void close(){
		state = SCROLL_STATE.Scroll_to_close;
		smoothScrollto();
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	@Override
	public void computeScroll() {
		if(mScorller.computeScrollOffset()){
			scrollTo(mScorller.getCurrX(), 0);
		}
		invalidate();
		
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mainView.layout(l, t, r, b);
		menuView.layout(-menuView.getMeasuredWidth(), t, 0, b);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mainView.measure(widthMeasureSpec, heightMeasureSpec);
		menuView.measure(widthMeasureSpec-150, heightMeasureSpec);
	}

	public OnSlidingMenuListener getmSlidingMenuListener() {
		return mSlidingMenuListener;
	}

	public void setmSlidingMenuListener(OnSlidingMenuListener mSlidingMenuListener) {
		this.mSlidingMenuListener = mSlidingMenuListener;
	}

	public interface OnSlidingMenuListener{
		public void open();
		public void close();
	}
}
