package com.ixming.privacy.android.main.activity;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.main.adapter.LocationDateAdapter;
import com.ixming.privacy.android.main.control.MainController;
import com.ixming.privacy.monitor.android.R;

public class PrivacyLocationDateActivity extends BaseActivity {

	@ViewInject(id = R.id.privacy_location_date_lv)
	private ListView mListView;
	
	@ViewInject(id = R.id.privacy_location_date_empty_tv)
	private TextView mEmpty_TV;
	
	private LocationDateAdapter mAdapter;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			String action = intent.getAction();
			if (LocalBroadcastIntents.ACTION_UPDATE_LOCATION.equals(action)) {
				setData();
			}
		}
	};
	
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_privacy_location_date;
	}

	@Override
	public void initView(View view) {
		ViewUtils.setViewGone(mEmpty_TV);
		ViewUtils.setViewGone(mListView);
	}

	@Override
	public void initListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainController.getInstance().setCurTime(mAdapter.getItem(position));
				startActivity(HistoryOnMapActivity3.class);
			}
		});
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		if (!checkHasUser()) {
			customBack();
			return ;
		}
		
		
		LocalBroadcasts.registerLocalReceiver(mReceiver, LocalBroadcastIntents.ACTION_UPDATE_LOCATION);
		
		mAdapter = new LocationDateAdapter(context);
		mListView.setAdapter(mAdapter);
		
		MainController.getInstance().loadLocationInfoData();
	}

	@Override
	public Handler provideActivityHandler() {

		return null;
	}

	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//TODO 如果没有了用户数据时不能进入该界面的
		if (!checkHasUser()) {
			customBack();
			return ;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcasts.unregisterLocalReceiver(mReceiver);
	}
	
	private boolean checkHasUser() {
		return null != MainController.getInstance().getCurUser();
	}

	private void setData() {
		mAdapter.setData(MainController.getInstance().getLocationDates());
		mAdapter.notifyDataSetChanged();
		if (mAdapter.isEmpty()) {
			ViewUtils.setViewVisible(mEmpty_TV);
			ViewUtils.setViewGone(mListView);
		} else {
			ViewUtils.setViewVisible(mListView);
			ViewUtils.setViewGone(mEmpty_TV);
		}
	}
	
}
