package com.lyk.focusview_master;

import java.util.List;
import java.util.zip.Inflater;
import modle.DemoModle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FocusAdapter extends BaseAdapter {
	
	private List<DemoModle> mLists;
	
	private Context mContext;
	
	
	

	public FocusAdapter(List<DemoModle> mLists, Context mContext) {
		super();
		this.mLists = mLists;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLists.size();
	}

	@Override
	public DemoModle getItem(int position) {
		// TODO Auto-generated method stub
		return mLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return -1;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		/*LayoutInflater mInflater = LayoutInflater.from(mContext);
		View view  = mInflater.inflate(
				R.layout.layout_focus_item,parent);
		ImageView mImageView = (ImageView) view.findViewById(R.id.photo);
		TextView mTextView = (TextView) view.findViewById(R.id.title);
		mImageView.setBackgroundResource(mLists.get(position).getImage());
		mTextView.setText(mLists.get(position).getName());*/
		return null;
	}

}
