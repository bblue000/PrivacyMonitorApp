package com.zhaoni.findyou.android.common.model;

import org.ixming.base.utils.android.PreferenceUtils;

import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;

public class AppSharedUtils {

	private AppSharedUtils(){}
	
	private static final String MAIN = Config.SHAREPRE_PREFIX + "APPMAIN";
	private static final String DEVICE_TOKEN = "DEVICE_TOKEN";
	private static final String LOCATION_SETTING = "LOCATION_SETTING";
	private static final String LOCATION_INTERVAL = "LOCATION_INTERVAL";
	private static final String LOCATION_FIRST_INTERVAL = "LOCATION_FIRST_INTERVAL";
	
	public static void saveDeviceToken(String token) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(), MAIN, DEVICE_TOKEN, token);
	}
	
	public static String getDeviceToken() {
		return PreferenceUtils.getValue(PAApplication.getAppContext(), MAIN, DEVICE_TOKEN, "");
	}
	
	public static void saveLocationSetting(boolean value) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(), MAIN, LOCATION_SETTING, value);
	}
	
	public static boolean getLocationSetting() {
		return PreferenceUtils.getValue(PAApplication.getAppContext(), MAIN, LOCATION_SETTING, true);
	}
	
	public static void saveLocationInterval(long value) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(), MAIN, LOCATION_INTERVAL, value);
	}
	
	public static long getLocationInterval(long def) {
		return PreferenceUtils.getValue(PAApplication.getAppContext(), MAIN, LOCATION_INTERVAL, def);
	}
	
	public static void saveLocationFirstInterval(boolean value) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(), MAIN, LOCATION_FIRST_INTERVAL, value);
	}
	
	public static boolean getLocationFirstInterval() {
		return PreferenceUtils.getValue(PAApplication.getAppContext(), MAIN, LOCATION_FIRST_INTERVAL, false);
	}
	
}
