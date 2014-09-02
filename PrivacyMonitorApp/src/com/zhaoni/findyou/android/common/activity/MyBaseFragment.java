package com.zhaoni.findyou.android.common.activity;

import org.ixming.base.common.activity.BaseFragment;

import com.zhaoni.findyou.android.common.statistics.UMengLog;

public abstract class MyBaseFragment extends BaseFragment {

	/**
	 * 统计时使用，提供当前fragment的特有统计名称
	 */
	protected String provideUIName() {
		return getClass().getSimpleName();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		UMengLog.onUIStart(provideUIName());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UMengLog.onUIEnd(provideUIName());
	}
	
}
