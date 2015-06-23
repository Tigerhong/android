package com.example.gestruredetectottest.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;




import java.util.LinkedList;
import java.util.List;

import com.example.gestruredetectottest.R;

/**
 * Created by moon.zhong on 2015/3/13.
 */
public class SlidingView extends ViewGroup {

    /*无效的点*/
    private static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600; // ms
    private static final int MIN_DISTANCE_FOR_FLING = 25; // dips
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    /*内容View*/
    private View mContent;

    private boolean mIsBeingDragged;

    private boolean mIsUnableToDrag;

    private Scroller mScroller;

    private boolean mScrolling;
    /*共外面调用的监听事件*/
    private OnPageChangeListener mListener;
    /*内部监听事件*/
    private OnPageChangeListener mInternalPageChangeListener;

    private float mLastMotionX;
    private float mLastMotionY;

    private float mInitialMotionX;

    /*当前活动的点Id,有效的点的Id*/
    protected int mActivePointerId = INVALID_POINTER;

    protected VelocityTracker mVelocityTracker;
    private int mMinMunVelocity;
    private int mMaxMunVelocity;
    private int mFlingDistance;
    private int mTouchSlop;

    private boolean isEnable = true;
    /*没有滑动，正常显�?*/
    private int mCurItem = 0;

    private float mRatio;

    private int mPix;

    private Paint mShadowPaint;

    private Drawable mLeftShadow;

    private final int SHADOW_WIDTH = 15;

    private int mShadowWidth;
    private boolean mShouldDraw = true;
    private List<ViewPager> mViewPagers;

    public SlidingView(Context context) {
        this(context, null);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView(context);
    }

    private void initCustomView(Context context) {
        setWillNotDraw(false);
        mScroller = new Scroller(context, sInterpolator);
        mInternalPageChangeListener = new InternalPageChangeListener();
        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLeftShadow = getResources().getDrawable(R.drawable.left_shadow);

        mShadowPaint.setColor(0xff000000);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mMinMunVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaxMunVelocity = configuration.getScaledMaximumFlingVelocity();
        final float density = context.getResources().getDisplayMetrics().density;
        mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
        mShadowWidth = (int) (SHADOW_WIDTH * density);
        mViewPagers = new LinkedList<>();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mContent.layout(0, 0, width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnable) {
            return false;
        }
        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            endToDrag();
            return false;
        }
        if (action != MotionEvent.ACTION_DOWN && mIsUnableToDrag){
            return false ;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                mLastMotionX = mInitialMotionX = MotionEventCompat.getX(ev, index);
                mLastMotionY = MotionEventCompat.getY(ev, index);
                if (thisTouchAllowed(ev)) {
                    mIsBeingDragged = false;
                    mIsUnableToDrag = false;
                } else {
                    mIsUnableToDrag = true;
                }
                if (!mScroller.isFinished()) {
                    startDrag();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                determineDrag(ev);
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

        }
        if (!mIsBeingDragged) {
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }
            mVelocityTracker.addMovement(ev);
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable) {
            return false;
        }
        if (!mIsBeingDragged && !thisTouchAllowed(event))
            return false;
        final int action = event.getAction();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                completeScroll();
                int index = MotionEventCompat.getActionIndex(event);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                mLastMotionX = mInitialMotionX = event.getX();
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int indexx = MotionEventCompat.getActionIndex(event);
                mLastMotionX = MotionEventCompat.getX(event, indexx);
                mActivePointerId = MotionEventCompat.getPointerId(event, indexx);
                break;

            }
            case MotionEvent.ACTION_MOVE:
                if (!mIsBeingDragged) {
                    determineDrag(event);
                    if (mIsUnableToDrag)
                        return false;
                }
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    final int activePointerIndex = getPointerIndex(event, mActivePointerId);
                    if (mActivePointerId == INVALID_POINTER)
                        break;
                    final float x = MotionEventCompat.getX(event, activePointerIndex);
                    final float deltaX = mLastMotionX - x;
                    mLastMotionX = x;
                    float oldScrollX = getScrollX();
                    float scrollX = oldScrollX + deltaX;
                    final float leftBound = getLeftBound();
                    final float rightBound = getRightBound();
                    if (scrollX < leftBound) {
                        scrollX = leftBound;
                    } else if (scrollX > rightBound) {
                        scrollX = rightBound;
                    }
                    // Don't lose the rounded component
                    mLastMotionX += scrollX - (int) scrollX;
                    scrollTo((int) scrollX, getScrollY());

                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaxMunVelocity);
                    int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
                            velocityTracker, mActivePointerId);
                    final int scrollX = getScrollX();
                    final float pageOffset = (float) (-scrollX) / getContentWidth();
                    final int activePointerIndex = getPointerIndex(event, mActivePointerId);
                    if (mActivePointerId != INVALID_POINTER) {
                        final float x = MotionEventCompat.getX(event, activePointerIndex);
                        final int totalDelta = (int) (x - mInitialMotionX);
                        int nextPage = determineTargetPage(pageOffset, initialVelocity, totalDelta);
                        setCurrentItemInternal(nextPage, true, initialVelocity);
                    } else {
                        setCurrentItemInternal(mCurItem, true, initialVelocity);
                    }
                    mActivePointerId = INVALID_POINTER;
                    endToDrag();
                } else {
//                    setCurrentItemInternal(0, true, 0);
                    scrollTo(getScrollX(), getScrollY());
                    endToDrag();
                }
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                int pointerIndex = getPointerIndex(event, mActivePointerId);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                mLastMotionX = MotionEventCompat.getX(event, pointerIndex);
                break;
        }

        return true;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    private float getLeftBound() {
        return -mContent.getWidth();
    }

    private float getRightBound() {
        return 0;
    }

    private int getContentWidth() {
        return mContent.getWidth();
    }

    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1)
            mActivePointerId = INVALID_POINTER;
        return activePointerIndex;
    }

    private void endToDrag() {
        mIsBeingDragged = false;
        mIsUnableToDrag = false;
        mActivePointerId = INVALID_POINTER;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void startDrag() {
        mIsBeingDragged = true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private void determineDrag(MotionEvent event) {
        int pointIndex = MotionEventCompat.getActionIndex(event);
        int pointId = MotionEventCompat.getPointerId(event, pointIndex);
        if (pointId == INVALID_POINTER) {
            return;
        }
        final float x = MotionEventCompat.getX(event, pointIndex);
        final float y = MotionEventCompat.getY(event, pointIndex);
        final float dx = x - mLastMotionX;
        final float dy = y - mLastMotionY;
        final float xDiff = Math.abs(dx);
        final float yDiff = Math.abs(dy);
//        Log.v("zgy","=======mTouchSlop======"+(xDiff > mTouchSlop)+",1="+(xDiff > 3*yDiff)+",2="+(thisSlideAllowed(dx))+",3="+mIsUnableToDrag);
        if (xDiff > mTouchSlop && xDiff > 3*yDiff && thisSlideAllowed(dx)&&!mIsUnableToDrag) {
            startDrag();
            mLastMotionX = x;
            mLastMotionY = y;
        } else if (xDiff > mTouchSlop) {
            mIsUnableToDrag = true;
        }
    }

    private boolean thisTouchAllowed(MotionEvent ev) {
        boolean isAllowed;
        ViewPager viewPager = getIgnoredView(ev);
        if (viewPager != null && viewPager.getCurrentItem() != 0)
            isAllowed = false;
        else isAllowed = true;
        Log.v("zgy","======isAllowed========="+isAllowed) ;
        return isAllowed;
    }

    private ViewPager getIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (ViewPager v : mViewPagers) {
            v.getHitRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY())) return v;
        }
        return null;
    }

    private void addViewPage(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof ViewPager) {
                mViewPagers.add((ViewPager) childView);
            } else if (childView instanceof ViewGroup) {
                addViewPage((ViewGroup) childView);
            }
        }
    }

    private boolean thisSlideAllowed(float dx) {
        return dx > 0;
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished()) {
            if (mScroller.computeScrollOffset()) {
                int oldX = getScrollX();
                int oldY = getScrollY();
                int x = mScroller.getCurrX();
                int y = mScroller.getCurrY();

                if (oldX != x || oldY != y) {
                    scrollTo(x, y);
//                    pageScrolled(x);
                }

                // Keep on drawing until the animation has finished.
                invalidate();
                return;
            }
        }

        // Done with scroll, clean up state.
        completeScroll();
    }

    private void pageScrolled(int xpos) {
        final int widthWithMargin = getWidth();
        int position = Math.abs(xpos) / widthWithMargin;
        final int offsetPixels = Math.abs(xpos) % widthWithMargin;
        final float offset = (float) offsetPixels / widthWithMargin;
        position = mIsBeingDragged ? 0 : position;
        onPageScrolled(position, offset, offsetPixels);
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, offset, offsetPixels);
        }
        mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, int velocity) {
        mCurItem = item;
        final int destX = getDestScrollX(item);

        if (mListener != null) {
            mListener.onPageSelected(mCurItem);
        }
        mInternalPageChangeListener.onPageSelected(mCurItem);
        if (smoothScroll) {
            smoothScrollTo(destX, 0, velocity);
        } else {
            completeScroll();
            scrollTo(destX, 0);
        }
    }

    public int getDestScrollX(int page) {
        switch (page) {
            case 0:
                return mContent.getLeft();
            case 1:
                return -mContent.getRight();
        }
        return 0;
    }

    private int determineTargetPage(float pageOffset, int velocity, int deltaX) {
        int targetPage = 0;
        if (Math.abs(deltaX) > mFlingDistance && Math.abs(velocity) > mMinMunVelocity) {
            if (velocity > 0 && deltaX > 0) {
                targetPage = 1;
            } else if (velocity < 0 && deltaX < 0) {
                targetPage = 0;
            }
        } else {
            targetPage = (int) Math.round(targetPage + pageOffset);
        }
        return targetPage;
    }

    private void completeScroll() {
        boolean needPopulate = mScrolling;
        if (needPopulate) {
            mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (oldX != x || oldY != y) {
//                scrollTo(x, y);
            }
        }

        mScrolling = false;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        pageScrolled((int) x);
    }

    private void smoothScrollTo(int x, int y, int velocity) {
        if (getChildCount() == 0) {
            // Nothing to do.
            return;
        }
        int sx = getScrollX();
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll();
            /*这里为了解决�?个bug，当用手指触摸滑到看不见的时候再用力滑动，如果不做此操作，那么不会回调position = 1*/
            scrollTo(sx, sy);
            return;
        }

        mScrolling = true;

        final int width = getContentWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
        final float distance = halfWidth + halfWidth *
                distanceInfluenceForSnapDuration(distanceRatio);

        int duration = 0;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float pageDelta = (float) Math.abs(dx) / width;
            duration = (int) ((pageDelta + 1) * 100);
            duration = MAX_SETTLE_DURATION;
        }
        duration = Math.min(duration, MAX_SETTLE_DURATION);

        mScroller.startScroll(sx, sy, dx, dy, duration);
        invalidate();
    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) FloatMath.sin(f);
    }

    public void setContent(View v) {
        if (mContent != null)
            this.removeView(mContent);
        mContent = v;
        if (mContent instanceof ViewGroup)
            addViewPage((ViewGroup) mContent);
        addView(mContent);
    }

    public View getContentView() {
        return mContent;
    }

    public void setShouldDraw(boolean shouldDraw) {
        mShouldDraw = shouldDraw;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);

        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        mContent.measure(contentWidth, contentHeight);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShouldDraw) {
            drawBackground(canvas);
            drawShadow(canvas);
        }
    }

    /**
     * 绘制背景颜色，随�?距离的改变�?�改�?
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        mShadowPaint.setAlpha((int) ((1 - mRatio) * 180));
        canvas.drawRect(-mPix, 0, 0, getHeight(), mShadowPaint);
    }

    /**
     * 绘制shadow阴影
     *
     * @param canvas
     */
    private void drawShadow(Canvas canvas) {
        canvas.save();
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        canvas.translate(-mShadowWidth, 0);
        mLeftShadow.draw(canvas);
        canvas.restore();
    }

    private class InternalPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if (mShouldDraw) {
                mRatio = positionOffset;
                mPix = positionOffsetPixels;
                invalidate();
            }
        }

        @Override
        public void onPageSelected(int position) {

        }
    }

    public interface OnPageChangeListener {

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);
    }
}
