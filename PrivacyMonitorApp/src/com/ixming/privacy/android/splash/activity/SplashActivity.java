package com.ixming.privacy.android.splash.activity;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.android.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ixming.privacy.android.common.LocalBroadcastIntents.DeviceToken;
import com.ixming.privacy.android.common.control.BindController;
import com.ixming.privacy.android.main.activity.NewMainActivity;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class SplashActivity extends BaseActivity {

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
				startActivity(NewMainActivity.class);
				finish();
			} else if (DeviceToken.ACTION_DEVICE_TOKEN_FAILED.equals(action)) {
				ToastUtils.showToast("获取device_token失败");
				finish();
			} else {
				
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
				DeviceToken.ACTION_DEVICE_TOKEN_FAILED);
		PAApplication.getHandler().postDelayed(mShortestJumpRunnable, mShortestDelay);
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

}
