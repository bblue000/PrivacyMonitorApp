package com.ixming.privacy.android.monitoring.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ixming.base.network.json.GsonPool;
import org.ixming.base.utils.android.AndroidUtils;
import org.ixming.base.utils.android.DeviceUuidFactory;
import org.ixming.base.utils.android.LogUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;
import com.ixming.privacy.monitor.android.PAApplication;

public class RequestPiecer {

	private static final String TAG = RequestPiecer.class.getSimpleName();
	
	private static Gson mDefGson = GsonPool.getExposeRestrict();
	
	private static String KEY_USER_ID = "com.ixming.privacy.android.USER_ID";
	private static String KEY_USER_NAME = "com.ixming.privacy.android.USER_NAME";
	
	private static int sUserId;
	private static String sUserName;
	private static String sDeviceId;
	private static JSONObject getBasicData() {
		JSONObject json = new JSONObject();
		try {
//			if (sUserId <= 0) {
//				sUserId = AndroidUtils.getMetaData(KEY_USER_ID);
//				sUserName = AndroidUtils.getMetaData(KEY_USER_NAME);
//			}
//			
//			json.put("user_id", sUserId);
//			json.put("user_name", sUserName);
			
			if (null == sDeviceId) {
				DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(PAApplication.getAppContext());
				sDeviceId = deviceUuidFactory.getDeviceUuid();
			}
			json.put("device_type", "android");
			json.put("device_id", sDeviceId);
			json.put("app_version", AndroidUtils.getAppVersionName("1.0"));
		} catch (Exception e) {
			LogUtils.e(TAG, "getBasicData Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	static final String EMPTY_ARRAY = "[]";
	
	public static Map<String, String> getLocationJson(List<PrivacyLocaitonInfo> list) {
		Map<String, String> data = null;
		try {
			if (null != list && !list.isEmpty()) {
				JSONObject json = getBasicData();
				data = new HashMap<String, String>();
				String locData = mDefGson.toJson(list);
				json.put("locations", new JSONArray(locData));
				data.put("data", json.toString());
			}
		} catch (Exception e) {
			LogUtils.e(TAG, "getLocationJson Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return data;
	}
	
}
