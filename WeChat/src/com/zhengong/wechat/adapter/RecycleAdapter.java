package com.zhengong.wechat.adapter;

import java.util.List;
import java.util.Random;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengong.wechat.R;
import com.zhengong.wechat.entity.RecycleBean;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
private List<RecycleBean> mRecycleBeans;
	public RecycleAdapter(List<RecycleBean> mRecycleBeans) {
	super();
	this.mRecycleBeans = mRecycleBeans;
}

	@Override
	public int getItemCount() {
		return mRecycleBeans.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder parent, int viewType) {
		// TODO Auto-generated method stub
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null);
		ViewHodler viewHodler = new ViewHodler(itemView);
		viewHodler.mContent.setHeight(50+new Random().nextInt(100));
		return viewHodler;
	}
class ViewHodler extends RecyclerView.ViewHolder{

	private ImageView mIcon;
	private TextView mName;
	private TextView mContent;
	public ViewHodler(View itemView) {
		super(itemView);
		mIcon =(ImageView) itemView.findViewById(R.id.id_item_icon);
		mName = (TextView) itemView.findViewById(R.id.id_item_title);
		mContent = (TextView) itemView.findViewById(R.id.id_item_content);
	}
}
}
