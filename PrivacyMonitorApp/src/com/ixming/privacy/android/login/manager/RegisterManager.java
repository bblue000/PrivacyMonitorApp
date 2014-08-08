package com.ixming.privacy.android.login.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.ixming.base.utils.android.ToastUtils;

import android.app.Dialog;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.Dialogs;
import com.ixming.privacy.android.common.model.ResponseData.RegisterResult;
import com.ixming.privacy.android.login.model.UserInfo;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class RegisterManager {
	AQuery aq;
	public static final String REGISTER_SUCCESS_ACTION = "com.find.REGISTER_SUCCESS_ACTION";
	private AjaxCallbackImpl mAjaxCallback;

	public RegisterManager() {
		aq = new AQuery(PAApplication.getAppContext());
	}

	public void register(String username, String password) {
		mAjaxCallback = new AjaxCallbackImpl();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("password", password);
		final Dialog dialog = Dialogs.showProgress();
		aq.ajax(Config.URL_POST_REGISTER, map, RegisterResult.class,
				mAjaxCallback);

	}

	private class AjaxCallbackImpl extends AjaxCallback<RegisterResult> {
		@Override
		public void callback(String url, RegisterResult object,
				AjaxStatus status) {
			if (status.getCode() == HttpStatus.SC_OK) {
				LoginManager.getInstance().setmUserInfo(object.getValue());
				ToastUtils.showToast(result.getMsg());
			} else {
				ToastUtils.showToast(result.getMsg());
			}
		}
	}
}
