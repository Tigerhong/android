package com.zhengong.wechat;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhengong.wechat.adapter.RecycleAdapter;
import com.zhengong.wechat.entity.RecycleBean;
import com.zhengong.wechat.view.Divider;

@SuppressLint("NewApi")
public class WeChatFragment extends Fragment {
private View mView;
private List<RecycleBean> mRecycleBean;
private RecycleAdapter mRecycleAdapter;
private RecyclerView mRecycleView;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	if (mView != null) {
		return mView;
	}
	if (mView !=null) {
		return mView;
	}
	mRecycleBean = new ArrayList<>();
	for (int i = 0; i < 20; i++) {
		RecycleBean recycleBean = new RecycleBean();
		mRecycleBean.add(recycleBean);
	}
	mRecycleAdapter = new RecycleAdapter(mRecycleBean);
	mView = inflater.inflate(R.layout.view_we_chat, null);
	 mRecycleView = (RecyclerView)mView.findViewById(R.id.id_recycle_view);
	 LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
	 layoutManager.setOrientation(LinearLayout.VERTICAL);
	 layoutManager.setReverseLayout(true);
	 mRecycleView.setLayoutManager(layoutManager);
	 mRecycleView.addItemDecoration(new Divider());
	 mRecycleView.setAdapter(mRecycleAdapter);
	return mView;
}
}
