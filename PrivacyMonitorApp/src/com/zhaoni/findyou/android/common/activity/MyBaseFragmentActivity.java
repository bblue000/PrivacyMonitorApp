package com.zhaoni.findyou.android.common.activity;

import org.ixming.base.common.activity.BaseFragmentActivity;

import com.zhaoni.findyou.android.common.statistics.UMengLog;

public abstract class MyBaseFragmentActivity extends BaseFragmentActivity {
	
	/**
	 * 统计时使用，提供当前Activity的特有统计名称
	 */
	protected String provideUIName() {
		return getClass().getSimpleName();
	}
	
	/**
	 * 是否需要作为UI统计——如果该FragmentActivity仅仅作为一个Activity使用，返回true；如果显示fragment，则返回false，
	 * 在fragment中统计UI信息（默认是false）
	 */
	protected boolean statisticUI() {
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UMengLog.onActivityShow(this);
		
		if (statisticUI())
			UMengLog.onUIStart(provideUIName());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		UMengLog.onActivityHide(this);
		
		if (statisticUI())
			UMengLog.onUIEnd(provideUIName());
	}
	
}
