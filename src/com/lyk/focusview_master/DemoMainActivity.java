package com.lyk.focusview_master;

import java.util.ArrayList;
import java.util.List;

import modle.FocusItemModle;
import modle.DemoModle;

import com.lyk.focusview_master.FocusView.OnItemClickListener;
import com.lyk.focusview_master.FocusView.OrientationType;

import android.R.anim;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;




public class DemoMainActivity extends Activity implements  OnItemClickListener<FocusItemModle<DemoModle>> {

	
	
	@SuppressWarnings("rawtypes")
	private List<DemoModle> mTvLists =new ArrayList<DemoModle>();

	
	int i = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mian_tv_ui);  
		FocusView view = (FocusView) findViewById(R.id.focus_ui);
		view.setOnItemClickListener(this);
		view.setBackgroundColor(Color.BLACK);
		view.setGap(5);
		view.setVisibleItems(6, 5);
		view.setOrientation(OrientationType.Horizontal);
		view.setAnimation(R.anim.scale_small, R.anim.scale_big);
		getData();
		FocusAdapter adapter = new FocusAdapter(mTvLists, getBaseContext());
		view.setAdapter(adapter);
		

	}
	
	
	/**
	 * initData
	 */
	private void getData() {
		
		for (int i = 0; i < 30; i++) {
			mTvLists.add(new DemoModle(R.drawable.ic_launcher, "text" + i ));
		}
		
	}

	@Override
	public void onItemClick(FocusView mFocusView, View focusView,
			FocusItemModle<DemoModle> focusItem, int Postion, int row, int col,
			long id) {
		Toast.makeText(this," row: "+ row + ", col: " + col,Toast.LENGTH_SHORT).show();
		
	}

}
