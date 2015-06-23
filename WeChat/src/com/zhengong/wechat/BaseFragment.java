package com.zhengong.wechat;

import java.util.ArrayList;
import java.util.List;

import com.zhengong.wechat.adapter.RecycleAdapter;
import com.zhengong.wechat.entity.RecycleBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public abstract class BaseFragment<K extends RecyclerView.LayoutManager> extends Fragment {
private View mView;
private List<RecycleBean> mRecycleBean;
private RecycleAdapter mRecycleAdapter;
private RecyclerView mRecycleView;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	if (mView !=null) {
		return mView;
	}
	mRecycleBean = new ArrayList<>();
	for (int i = 0; i < 20; i++) {
		RecycleBean recycleBean = new RecycleBean();
		mRecycleBean.add(recycleBean);
	}
	mRecycleAdapter = new RecycleAdapter(mRecycleBean);
	mView = inflater.inflate(R.layout.base, null);
	 mRecycleView = (RecyclerView)mView.findViewById(R.id.id_recycle_view);
	 mRecycleView.setLayoutManager(getLayoutManager(getActivity()));
	 mRecycleView.setAdapter(mRecycleAdapter);
	 initConfig(mRecycleView);
	return mView;
}

public abstract  K getLayoutManager(Context activity) ;
public abstract void initConfig(RecyclerView recyclerView);
}
