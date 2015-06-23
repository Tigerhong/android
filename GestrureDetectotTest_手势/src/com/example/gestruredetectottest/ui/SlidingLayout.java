package com.example.gestruredetectottest.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * Created by moon.zhong on 2015/3/13.
 */
public class SlidingLayout extends FrameLayout {

    private SlidingView mSlidingView ;
    private Activity mActivity ;
    private SlidingView.OnPageChangeListener mPageChangeListener ;
    private final int POSITION_FINISH = 1 ;
    private View mContextView  ;

    private OnAnimListener mAnimListener ;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSlidingView = new SlidingView(context) ;
        addView(mSlidingView);
        mPageChangeListener = new SlidingOnPageChangeListener() ;
        mSlidingView.setOnPageChangeListener(mPageChangeListener);
        mActivity = (Activity) context;
        bindActivity(mActivity) ;
    }

    private void bindActivity(Activity activity){
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup child = (ViewGroup) decorView.getChildAt(0);
        decorView.removeView(child);
        setContentView(child) ;
        decorView.addView(this);
    }
    private void setContentView(View view){
        mContextView = view ;
        mSlidingView.setContent(view);
    }
    private class SlidingOnPageChangeListener implements SlidingView.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == POSITION_FINISH){
                mActivity.finish();
            }
            if (mAnimListener != null){
                mAnimListener.onAnimationSet(mContextView,positionOffset,positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }
    }

    public void setOnAnimListener(OnAnimListener listener){
        mAnimListener = listener ;
        mSlidingView.setShouldDraw(false);
    }

    public interface OnAnimListener {

        void onAnimationSet(View view, float offSet, int offSetPix) ;
    }

    public static class SimpleAnimImpl implements OnAnimListener{
        private final int MAX_ANGLE = 20 ;
        @Override
        public void onAnimationSet(View view, float offSet,int offSetPix) {
//            ViewHelper.setPivotX(view,view.getWidth()/2.0F);
//            ViewHelper.setPivotY(view,view.getHeight());
//            ViewHelper.setRotation(view,MAX_ANGLE*offSet);
        }
    }

}
