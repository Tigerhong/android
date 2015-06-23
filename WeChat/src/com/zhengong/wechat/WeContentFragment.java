package com.zhengong.wechat;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhengong.wechat.adapter.RecycleAdapter;
import com.zhengong.wechat.entity.RecycleBean;
import com.zhengong.wechat.view.Divider;

@SuppressLint("NewApi")
public class WeContentFragment extends Fragment {
private View mView;
private RecyclerView mRecycleView;
private RecycleAdapter mRecycleAdapter;
private List<RecycleBean> mRecycleBean;

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
	mView = inflater.inflate(R.layout.view_we_contact, null);
	 mRecycleView = (RecyclerView)mView.findViewById(R.id.id_contact_view);
	 
//	 LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//	 layoutManager.setOrientation(LinearLayout.VERTICAL);
//	 layoutManager.setReverseLayout(true);
	 GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
	 gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
		
		@Override
		public int getSpanSize(int position) {
			if(position % 3 == 0){
				return 2;
			}
			return 1;
		}
	});
	 mRecycleView.setLayoutManager(gridLayoutManager);
	 mRecycleView.addItemDecoration(new Divider());
	 mRecycleView.setAdapter(mRecycleAdapter);
	return mView;
}

}
