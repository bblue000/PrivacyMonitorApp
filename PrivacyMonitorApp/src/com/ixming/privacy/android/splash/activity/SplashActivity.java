package com.ixming.privacy.android.splash.activity;

import org.ixming.base.common.activity.BaseActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ixming.privacy.android.login.activity.LoginActivity;
import com.ixming.privacy.android.login.manager.LoginManager;
import com.ixming.privacy.android.login.manager.LoginStateCallback;
import com.ixming.privacy.android.main.activity.NewMainActivity;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class SplashActivity extends BaseActivity implements LoginStateCallback {

	// 打开延时，至少2500ms
	private final int mShortestDelay = 2500;
	private Runnable mShortestJumpRunnable = new Runnable() {
		@Override
		public void run() {
			LoginManager.getInstance().checkLoginState(SplashActivity.this);
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
	}
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onNonLogin() {
		startActivity(NewMainActivity.class);
		finish();
	}

	@Override
	public void onAlreadyLogged() {
		startActivity(NewMainActivity.class);
		finish();
	}

	@Override
	public void onLoginExpire() {
		onNonLogin();
	}

}
