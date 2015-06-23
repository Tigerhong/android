package com.zhengong.wechat.view;

import java.util.ArrayList;
import java.util.List;

import com.zhengong.wechat.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class TabView extends LinearLayout implements View.OnClickListener {

	private int mTextSize;
	private int mTextColorNormal;
	private int mTextColorSelect;
	private int mPadding;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdaper;
	private int mChildSize;
	private List<TabItem> mTabItems;
	protected 	OnPageChangeListener mOnPagerChangeListener;
	private OnItemIconTextSelectListener mListener;

	public TabView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public TabView(Context context) {
		this(context,null);
	}

	@SuppressLint("NewApi")
	public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(attrs);
	}

	private void initView(AttributeSet attrs) {
		//获取自定义的属性集
		TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.TabView);
		int N = typedArray.getIndexCount();
		for (int i = 0; i < N; i++) {
			switch (typedArray.getIndex(i)) {
			case R.styleable.TabView_text_size:
				mTextSize = (int)typedArray.getDimension(i, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
						mTextSize, getResources().getDisplayMetrics()));
				break;
			case R.styleable.TabView_text_normal_color:
				mTextColorNormal = typedArray.getColor(i, mTextColorNormal);
				break;
			case R.styleable.TabView_text_select_color:
				mTextColorSelect = typedArray.getColor(i, mTextColorSelect);
				break;
			case R.styleable.TabView_text_padding:
				mPadding = (int) typedArray.getDimension(i, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mPadding, 
						getResources().getDisplayMetrics()));
				break;
			}
		}
		typedArray.recycle();
		mTabItems = new ArrayList<>();
	}

	public void setViewPager(final ViewPager mViewPager){
		if(mViewPager ==null){
			return;
		}
		this.mViewPager = mViewPager;
		this.mPagerAdaper = mViewPager.getAdapter();
		if(mPagerAdaper == null){
			throw new RuntimeException("亲，在您设置Tabview的ViewPager时，请先设置ViewPager的PagerAdapter");
		}
		this.mChildSize = this.mPagerAdaper.getCount();
		this.mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(mOnPagerChangeListener != null){
					mOnPagerChangeListener.onPageScrollStateChanged(position);
				}
			}
			
			@SuppressLint("NewApi")
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				View leftView;
				View rightView;
				if(positionOffset >0){
					leftView = mViewPager.getChildAt(position);
					rightView = mViewPager.getChildAt(position+1);
					leftView.setAlpha(1-positionOffset);
					rightView.setAlpha(positionOffset);
					
					mTabItems.get(position).setTabAlpha(1-positionOffset);
					mTabItems.get(position+1).setTabAlpha(positionOffset);
				}else{
					mViewPager.getChildAt(position).setAlpha(1);
					mTabItems.get(position).setTabAlpha(1-positionOffset);
				}
				if(mOnPagerChangeListener != null){
					mOnPagerChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				if(mOnPagerChangeListener != null){
					mOnPagerChangeListener.onPageScrollStateChanged(state);
				}
			}
		});
		if(mPagerAdaper instanceof OnItemIconTextSelectListener){
			mListener = (OnItemIconTextSelectListener) mPagerAdaper;
		}else{
			throw new RuntimeException("请让你的pagerAdapter实现OnItemIconTextSelectListener接口");
		}
		initItem();
	}
	
	private void initItem() {
			for (int i = 0; i < mChildSize; i++) {
				TabItem tabItem = new TabItem(getContext());
				LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
				tabItem.setPadding(mPadding, mPadding, mPadding, mPadding);
				tabItem.setIconText(mListener.onIconSelect(i), mListener.onTextSelect(i));
				tabItem.setTextSize(mTextSize);
				tabItem.setTextColotNormal(mTextColorNormal);
				tabItem.setTextColotSelect(mTextColorSelect);
				tabItem.setLayoutParams(layoutParams);
				tabItem.setTag(i);
				tabItem.setOnClickListener(this);
				mTabItems.add(tabItem);
				addView(tabItem);
			}
	}
//当点击tab按钮的时候，直接跳到当期页面
	@Override
	public void onClick(View v) {
		int position = (int) v.getTag();
		if(mViewPager.getCurrentItem() == position){
			return;
		}
		for (TabItem tabItem : mTabItems) {
			tabItem.setTabAlpha(0);
		}
		mTabItems.get(position).setTabAlpha(1);
		mViewPager.setCurrentItem(position,false);
	}
	public interface OnItemIconTextSelectListener{
		int[] onIconSelect(int position);
		String onTextSelect(int position);
	}
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
}
}
