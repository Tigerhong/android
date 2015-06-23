package com.zhengong.slidingmenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity {

	private SlidingMenu mSlidingMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSlidingMenu = new SlidingMenu(this, LayoutInflater.from(this).inflate(R.layout.fragment1, null), 
				LayoutInflater.from(this).inflate(R.layout.fragment2, null));
		setContentView(mSlidingMenu);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					mSlidingMenu.open();
			}
		});
	findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					mSlidingMenu.close();
			}
		});
	}
}
