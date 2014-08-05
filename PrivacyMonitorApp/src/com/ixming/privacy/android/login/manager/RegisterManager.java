package com.ixming.privacy.android.login.manager;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class RegisterManager {
	AQuery aq;

	public RegisterManager() {
		aq = new AQuery(PAApplication.getAppContext());
	}

	public void register(String username, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("password", password);
		aq.ajax(Config.URL_POST_REGISTER, map, String.class,
				new AjaxCallback<String>() {
					@Override
					public void callback(String url, String json,
							AjaxStatus status) {
						super.callback(url, json, status);
						Log.i("RegisterManager", "json:" + json);
					}
				});
	}
}
