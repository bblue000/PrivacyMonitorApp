package com.ixming.privacy.android.main.activity;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.inject4android.annotation.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ixming.privacy.android.main.adapter.ViewLocationAdapter;
import com.ixming.privacy.android.monitoring.db.manager.PrivacyLocationInfoDBManager;
import com.ixming.privacy.monitor.android.R;

public class PrivacyLocationActivity extends BaseActivity {

	@ViewInject(id = R.id.privacy_location_lv)
	private ListView mListView;
	
	private ViewLocationAdapter mAdapter;
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_privacy_location;
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(HistoryOnMapActivity.class);
			}
		});
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mAdapter = new ViewLocationAdapter(context);
		mAdapter.appendDataList(PrivacyLocationInfoDBManager.getInstance().findAll());
		
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
