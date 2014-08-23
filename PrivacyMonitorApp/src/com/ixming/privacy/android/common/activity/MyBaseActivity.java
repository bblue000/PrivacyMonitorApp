package com.ixming.privacy.android.common.activity;

import org.ixming.base.common.activity.BaseActivity;

import com.ixming.privacy.android.common.statistics.UMengLog;

public abstract class MyBaseActivity extends BaseActivity {

	/**
	 * 统计时使用，提供当前Activity的特有统计名称
	 */
	protected String provideUIName() {
		return getClass().getSimpleName();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UMengLog.onActivityShow(this);
		
		UMengLog.onUIStart(provideUIName());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		UMengLog.onActivityHide(this);
		
		UMengLog.onUIEnd(provideUIName());
	}
	
}
