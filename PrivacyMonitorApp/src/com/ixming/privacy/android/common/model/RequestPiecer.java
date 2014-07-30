package com.ixming.privacy.android.common.model;

import java.util.HashMap;
import java.util.Map;

import org.ixming.base.network.json.GsonPool;
import org.ixming.base.utils.android.AndroidUtils;

import com.google.gson.Gson;

public class RequestPiecer {

	private static final String TAG = RequestPiecer.class.getSimpleName();
	
	private static Gson mDefGson = GsonPool.getExposeRestrict();
	
	public static Map<String, String> getBasicData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("device_type", "android");
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
	
	public static Map<String, String> getLocationJson(long userid) {
		Map<String, String> data = getBasicData();
		data.put("user_id", String.valueOf(userid));
		return data;
	}
	
}
