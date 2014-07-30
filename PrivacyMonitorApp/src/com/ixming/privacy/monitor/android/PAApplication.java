package com.ixming.privacy.monitor.android;

import org.ixming.base.common.BaseApplication;
import org.ixming.base.utils.android.LogUtils;

import com.ixming.privacy.android.monitoring.service.MainService;
import com.ixming.privacy.android.splash.activity.SplashActivity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Process;

public class PAApplication extends BaseApplication {

	private static final String TAG = PAApplication.class.getSimpleName();

	@Override
	public void onCreate() {
		LogUtils.d(TAG, "onCreate process = " + Process.myPid());
		super.onCreate();
		
		MainService.startMe("PAApplication onCreate");
	}
	
	
	public static void hideApp() {
		getAppContext().getPackageManager().setComponentEnabledSetting(
				new ComponentName(getAppContext(), SplashActivity.class),
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
		
		killProcess();
	}
	
}
