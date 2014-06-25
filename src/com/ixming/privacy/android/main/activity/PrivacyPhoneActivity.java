package com.ixming.privacy.android.main.activity;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.inject4android.annotation.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.ixming.privacy.android.main.adapter.ViewPhoneAdapter;
import com.ixming.privacy.android.main.db.manager.PrivacyPhoneInfoDBManager;
import com.ixming.privacy.monitor.android.R;

public class PrivacyPhoneActivity extends BaseActivity {

	@ViewInject(id = R.id.privacy_phone_lv)
	private ListView mListView;
	
	private ViewPhoneAdapter mAdapter;
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_privacy_phone;
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mAdapter = new ViewPhoneAdapter(context);
		mAdapter.appendDataList(PrivacyPhoneInfoDBManager.getInstance().findAll());
		
		mListView.setAdapter(mAdapter);
	}

	@Override
	public Handler provideActivityHandler() {

		return null;
	}

	@Override
	public void onClick(View v) {
	}

}
