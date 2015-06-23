package com.example.fragmenttabhost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentContainer extends Fragment {
	FragmentTabHost mTabHost;
	String TAG_TAB_1 ="Tab1";
	String TAG_TAB_2 ="Tab2";
	String TAG_TAB_3 ="Tab3";
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	//为该Fragment加载布局文件
	View view = inflater.inflate(R.layout.frg_container, container, false);
	//找到FragmentTabHost
	mTabHost = (FragmentTabHost) view.findViewById(R.id.frg_tabhost);
	
	mTabHost.setup(getActivity(), getChildFragmentManager(),R.id.real_tabcontent);
	mTabHost.addTab(mTabHost.newTabSpec(TAG_TAB_1).setIndicator(TAG_TAB_1),FragmentTab1.class,null);
	mTabHost.addTab(mTabHost.newTabSpec(TAG_TAB_2).setIndicator(TAG_TAB_2),FragmentTab2.class,null);
	mTabHost.addTab(mTabHost.newTabSpec(TAG_TAB_3).setIndicator(TAG_TAB_3),FragmentTab3.class,null);
	return view;
}
@Override
public void onDestroyView() {
	// TODO Auto-generated method stub
	super.onDestroyView();
	mTabHost = null;
}
}
