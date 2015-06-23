package com.example.gestruredetectottest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends BaseActivity {

	private RecyclerView mRecyclerView;
	private List mList;
	private PicRecyclerAdapter mAdapter;
	private MainActivity mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.fragment_case);
		init();
	}
	
	private void init() {
		mRecyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
		mList = new ArrayList<>();
		for (int i = 0; i <100; i++) {
			mList.add(i +"");
		}
		mAdapter = new PicRecyclerAdapter(mList);
		//设置RecyclerView显示一行显示多少张图片
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		//设置adapter
		mRecyclerView.
		setAdapter(
				mAdapter);
		//设置StaggeredGirdLayoutManager
		 mRecyclerView.setLayoutManager(layoutManager);
		mAdapter.setOnItemClickListener(new PicRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view) {
				int location[] = new int[2];
				view.getLocationOnScreen(location);
				int resId = (int) view.getTag();
				Bundle bundle = new Bundle();
				bundle.putInt("locationX", location[0]);
				bundle.putInt("locationY", location[1]);
				bundle.putInt("width", view.getWidth());
				bundle.putInt("hight", view.getHeight());
				Log.i("resId", resId+" mmmm");
				bundle.putInt("resId", resId);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(mContext, FirstActivity.class);
				mContext.startActivity(intent);
				mContext.overridePendingTransition(0, 0);
			}
		});
	}
	
	
	@Override
	public void pre(View view) {
	}
	@Override
	public void next(View view) {
		startActivity(new Intent(this,FirstActivity.class));
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

}
