package com.zhaoni.findyou.android.splash.activity;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.utils.android.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents.DeviceToken;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents.MonitoringPerson;
import com.zhaoni.findyou.android.common.activity.MyBaseActivity;
import com.zhaoni.findyou.android.common.control.BindController;
import com.zhaoni.findyou.android.common.statistics.UMengLog;
import com.zhaoni.findyou.android.main.activity.NewMainActivity;
import com.zhaoni.findyou.android.main.control.PersonListController;

public class SplashActivity extends MyBaseActivity {

	// 打开延时，至少2500ms
	private final int mShortestDelay = 1000;
	private Runnable mShortestJumpRunnable = new Runnable() {
		@Override
		public void run() {
			BindController bindController = BindController.getInstance();
			if (bindController.hasDeviceToken()) {
				startActivity(NewMainActivity.class);
				finish();
			} else {
				bindController.requestKey();
			}
		}
	};

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (DeviceToken.ACTION_DEVICE_TOKEN_LOADED.equals(action)) {
				PersonListController.getInstance().addMonitoringPerson(
						context.getString(R.string.person_list_self_name),
						BindController.getInstance().getDeviceToken());
				
				startActivity(NewMainActivity.class);
				finish();
			} else if (DeviceToken.ACTION_DEVICE_TOKEN_FAILED.equals(action)) {
				ToastUtils.showToast("获取'标识码'失败");
				startActivity(NewMainActivity.class);
				finish();
			} else if (MonitoringPerson.ACTION_DATA_LIST_CHANGED.equals(action)) {
			}
		}
	};

	@Override
	public boolean useInjectBeforeInitView() {
		return false;
	};

	@Override
	public int provideLayoutResId() {
		return R.layout.activity_splash;
	}

	@Override
	public void initView(View view) {

	}

	@Override
	public void initListener() {
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		LocalBroadcasts.registerLocalReceiver(mReceiver,
				DeviceToken.ACTION_DEVICE_TOKEN_LOADED,
				DeviceToken.ACTION_DEVICE_TOKEN_FAILED,
				MonitoringPerson.ACTION_DATA_LIST_CHANGED);
		PAApplication.getHandler().postDelayed(mShortestJumpRunnable,
				mShortestDelay);

		// 启动页面调用
		UMengLog.onSplash(this);
	}

	@Override
	public Handler provideActivityHandler() {
		return null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PAApplication.getHandler().removeCallbacks(mShortestJumpRunnable);
		LocalBroadcasts.unregisterLocalReceiver(mReceiver);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
