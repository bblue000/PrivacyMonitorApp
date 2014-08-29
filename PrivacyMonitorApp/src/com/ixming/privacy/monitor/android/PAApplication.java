package com.ixming.privacy.monitor.android;

import org.ixming.base.common.BaseApplication;
import org.ixming.base.utils.android.LogUtils;

import com.ixming.privacy.android.common.control.LocationController;
import com.ixming.privacy.android.common.model.AQueryConfig;
import com.ixming.privacy.android.common.statistics.UMengLog;
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

		AQueryConfig.config();
		UMengLog.init();

		LocationController.getInstance().checkLocationSetting();
	}

	/**
	 * 隐藏APP，并从桌面移除图标
	 */
	public static void hideApp() {
		getAppContext().getPackageManager().setComponentEnabledSetting(
				new ComponentName(getAppContext(), SplashActivity.class),
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
		killProcess();
	}

	/**
	 * 重新让A显示在launcher中
	 */
	public static void reshowApp() {
		getAppContext().getPackageManager().setComponentEnabledSetting(
				new ComponentName(getAppContext(), SplashActivity.class),
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
				PackageManager.DONT_KILL_APP);
	}

}
