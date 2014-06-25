package com.ixming.privacy.monitor.android;

import org.ixming.base.common.BaseApplication;
import org.ixming.base.utils.android.LogUtils;

import android.os.Process;

public class PAApplication extends BaseApplication {

	private static final String TAG = PAApplication.class.getSimpleName();

	@Override
	public void onCreate() {
		LogUtils.d(TAG, "onCreate process = " + Process.myPid());
		super.onCreate();
	}
	
	
}
