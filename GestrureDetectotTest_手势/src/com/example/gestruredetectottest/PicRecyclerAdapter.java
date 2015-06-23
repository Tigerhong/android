package com.example.gestruredetectottest;

import java.util.List;
import java.util.Random;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PicRecyclerAdapter extends BaseRecyclerAdapter<PicRecyclerAdapter.ViewHolder, String>{
	int id[] = {
			R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,} ;
	private OnItemClickListener onItemClickListener ;
	public PicRecyclerAdapter(List<String> mList) {
		super(mList);
	}

	@Override
	public ViewHolder createViewHolder(LayoutInflater inflater, int viewType) {
		View view = inflater.inflate(R.layout.img_item, null);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position, String data) {
		int resId = id[new Random().nextInt(3)] ;
		holder.imageView.setImageResource(resId);
//		Log.i("resId", resId+" pppp");
		holder.imageView.setTag(resId);
		holder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 if (onItemClickListener != null){
	                    onItemClickListener.onItemClick(v);
	                }
			}
		});
	}
	public static class ViewHolder extends BaseRecyclerAdapter.BaseViewHolder{
		public ImageView imageView;
		public ViewHolder(View itemView) {
			super(itemView);
			imageView = findView(R.id.id_img) ;
		}
	}
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	public interface OnItemClickListener{
		void onItemClick(View view) ;
	}
}
