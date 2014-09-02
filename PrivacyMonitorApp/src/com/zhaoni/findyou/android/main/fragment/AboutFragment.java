package com.zhaoni.findyou.android.main.fragment;

import org.ixming.base.common.activity.BaseFragment;
import org.ixming.inject4android.annotation.OnClickMethodInject;

import com.zhaoni.findyou.android.R;

import android.os.Bundle;
import android.view.View;

public class AboutFragment extends BaseFragment {

	@Override
	public int provideLayoutResId() {
		return R.layout.fragment_about;
	}

	@Override
	public void initView(View view) {
		
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
	}

	@Override
	public void initListener() {
	}
	
	@OnClickMethodInject(id = R.id.about_feedback_btn)
	void gotoFeedback() {
//		startActivity(clz);
	}
	
	@OnClickMethodInject(id = R.id.about_upgrade_btn)
	void gotoUpgrade() {
		
	}

}
