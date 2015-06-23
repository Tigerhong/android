package com.zhengong.wechat;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class WeGameFragment extends BaseFragment {
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.base4, null);
	return view;
}

@Override
public LayoutManager getLayoutManager(Context activity) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void initConfig(RecyclerView recyclerView) {
	// TODO Auto-generated method stub
	
}
}
