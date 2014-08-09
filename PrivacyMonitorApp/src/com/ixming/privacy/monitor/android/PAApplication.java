package com.ixming.privacy.monitor.android;

import org.ixming.base.common.BaseApplication;
import org.ixming.base.utils.android.LogUtils;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.util.AQUtility;
import com.ixming.privacy.android.common.control.LocationController;
import com.ixming.privacy.android.common.model.AQueryTransformer;
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
		
		AQUtility.setDebug(true);
		AjaxCallback.setTransformer(new AQueryTransformer());
		
		LocationController.getInstance().checkLocationSetting();
	}
	
	
	public static void hideApp() {
		getAppContext().getPackageManager().setComponentEnabledSetting(
				new ComponentName(getAppContext(), SplashActivity.class),
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
		
		killProcess();
	}
	
}
