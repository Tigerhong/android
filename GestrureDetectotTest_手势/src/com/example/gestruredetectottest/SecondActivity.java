package com.example.gestruredetectottest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SecondActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_main);
	}
	@Override
	public void pre(View view) {
		startActivity(new Intent(this, FirstActivity.class));
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

	@Override
	public void next(View view) {
		// TODO Auto-generated method stub
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
