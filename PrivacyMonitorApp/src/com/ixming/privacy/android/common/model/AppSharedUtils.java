package com.ixming.privacy.android.common.model;

import org.ixming.base.utils.android.PreferenceUtils;

import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class AppSharedUtils {

	private AppSharedUtils(){}
	
	private static final String MAIN = Config.SHAREPRE_PREFIX + "APPMAIN";
	private static final String DEVICE_TOKEN = "DEVICE_TOKEN";
	
	public static void saveDeviceToken(String token) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(), MAIN, DEVICE_TOKEN, token);
	}
	
	public static String getDeviceToken() {
		return PreferenceUtils.getValue(PAApplication.getAppContext(), MAIN, DEVICE_TOKEN, "");
	}
	
}
