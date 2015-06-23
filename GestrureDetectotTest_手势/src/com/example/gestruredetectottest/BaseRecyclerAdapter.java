package com.example.gestruredetectottest;


import java.util.List;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseRecyclerAdapter<T extends BaseRecyclerAdapter.BaseViewHolder,D> extends RecyclerView.Adapter<T>{

	private List<D> mList;
	
	public BaseRecyclerAdapter(List<D> List) {
		this.mList = List;
	}

	public static class BaseViewHolder extends RecyclerView.ViewHolder{

		public BaseViewHolder(View itemView) {
			super(itemView);
		}
		  public <T extends View> T findView(int id){
	            return (T) itemView.findViewById(id);
	        }
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public void onBindViewHolder(T hodler, int position) {
		onBindViewHolder(hodler, position,mList.get(position));		
	}

	@Override
	public T onCreateViewHolder(ViewGroup parents, int viewType) {
		return createViewHolder(LayoutInflater.from(parents.getContext()),viewType);
	}
	  public abstract T createViewHolder(LayoutInflater inflater,int viewType);

	    public abstract void onBindViewHolder(T holder, int position,D data);
}
