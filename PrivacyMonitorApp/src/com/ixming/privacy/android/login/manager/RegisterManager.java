package com.ixming.privacy.android.login.manager;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.Dialogs;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class RegisterManager {
	AQuery aq;
	public static final String REGISTER_SUCCESS_ACTION = "com.find.REGISTER_SUCCESS_ACTION";

	public RegisterManager() {
		aq = new AQuery(PAApplication.getAppContext());
	}

	public void register(String username, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("password", password);
		final Dialog dialog = Dialogs.showProgress();
		aq.ajax(Config.URL_POST_REGISTER, map, String.class,
				new AjaxCallback<String>() {
					@Override
					public void callback(String url, String json,
							AjaxStatus status) {
						super.callback(url, json, status);
						Log.i("RegisterManager", "json:" + json);
						LocalBroadcastManager.getInstance(
								PAApplication.getAppContext()).sendBroadcast(
								new Intent(REGISTER_SUCCESS_ACTION));
						dialog.dismiss();
					}
				});
		// 关闭
	}
}
