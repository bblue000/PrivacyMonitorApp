package com.ixming.privacy.android.common.model;

import java.util.HashMap;
import java.util.Map;

import org.ixming.base.utils.android.AndroidUtils;

import com.ixming.privacy.android.login.manager.LoginManager;

public class RequestPiecer {

	public static Map<String, String> getBasicData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("device_id", AndroidUtils.getDeviceId());
		data.put("device_type", "android");
		data.put("device_token", AppSharedUtils.getDeviceToken());
		data.put("app_version", AndroidUtils.getAppVersionName("1.0"));
		
		LoginManager loginManager = LoginManager.getInstance();
		if (loginManager.hasUser()) {
			data.put("username", loginManager.getmUserInfo().getUsername());
			data.put("user_token", loginManager.getmUserInfo().getUser_token());
		}
		return data;
	}
	
	static final String EMPTY_ARRAY = "[]";
	
	public static String getUserName() {
		LoginManager loginManager = LoginManager.getInstance();
		if (loginManager.hasUser()) {
			return loginManager.getmUserInfo().getUsername();
		}
		return "-1";
	}
}
