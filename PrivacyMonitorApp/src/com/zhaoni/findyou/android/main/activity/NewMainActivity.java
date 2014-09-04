package com.zhaoni.findyou.android.main.activity;

import org.ixming.inject4android.annotation.ViewInject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;

import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.activity.MyBaseFragmentActivity;
import com.zhaoni.findyou.android.main.adapter.IndexPagerAdapter;

public class NewMainActivity extends MyBaseFragmentActivity {

	@ViewInject(id = R.id.main_new_vp)
	private ViewPager mIndex_VP;
	@ViewInject(id = R.id.main_new_pts)
	private PagerTabStrip mIndex_PTS;
	
	private IndexPagerAdapter mAdapter;
	
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_main_new;
	}

	@Override
	public void initView(View view) {
		//设置pagerTabStrip  
		int color = getResources().getColor(R.color.deep_sky_blue);
		mIndex_PTS.setTabIndicatorColor(color);  
		mIndex_PTS.setTextColor(color);
		mIndex_PTS.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.main_title_textsize));
		mIndex_PTS.setClickable(false);  
		mIndex_PTS.setTextSpacing(40);  
		mIndex_PTS.setBackgroundColor(Color.TRANSPARENT);  
		mIndex_PTS.setDrawFullUnderline(true);
		
		mIndex_VP.setAdapter(mAdapter = new IndexPagerAdapter(this, getSupportFragmentManager()));
		mIndex_VP.setOffscreenPageLimit(mAdapter.getCount() - 1);
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
	}

	@Override
	public void initListener() {
		mIndex_VP.setOnPageChangeListener(mAdapter);
	}

	@Override
	public Handler provideActivityHandler() {

		return null;
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		PAApplication.killProcess();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mIndex_VP.getCurrentItem() > 0) {
				mIndex_VP.setCurrentItem(0, true);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
