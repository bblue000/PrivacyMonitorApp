package com.ixming.privacy.android.common.model;

import java.util.HashMap;
import java.util.Map;

import org.ixming.base.utils.android.AndroidUtils;

public class RequestPiecer {

	public static Map<String, String> getBasicData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("device_id", AndroidUtils.getDeviceId());
		data.put("device_type", "android");
		data.put("device_token", AppSharedUtils.getDeviceToken());
		data.put("app_version", AndroidUtils.getAppVersionName("1.0"));
		return data;
	}
	
	static final String EMPTY_ARRAY = "[]";
	
	public static Map<String, String> getLoginJson(String username, String password) {
		Map<String, String> data = getBasicData();
		data.put("username", username);
		data.put("password", password);
		return data;
	}
	
	public static Map<String, String> getUserJson() {
		Map<String, String> data = getBasicData();
		return data;
	}
	
}
