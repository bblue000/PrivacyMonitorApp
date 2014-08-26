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
	Dialog dialog;

	public RegisterManager() {
		aq = new AQuery(PAApplication.getAppContext());
	}

	public void register(String username, String password, String checkcode) {
		mAjaxCallback = new AjaxCallbackImpl();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("password", password);
		map.put("type", 0);
		map.put("checkcode", checkcode);
		dialog = Dialogs.showProgress();
		aq.ajax(Config.URL_POST_REGISTER, map, RegisterResult.class,
				mAjaxCallback);

	}

	private class AjaxCallbackImpl extends AjaxCallback<RegisterResult> {
		@Override
		public void callback(String url, RegisterResult object,
				AjaxStatus status) {
			dialog.dismiss();
			if (status.getCode() == HttpStatus.SC_OK) {
				if (object.getStatus() == 200) {
					LoginManager.getInstance().setLoginUser(object.getValue());
					ToastUtils.showToast(result.getMsg());
				} else {
					ToastUtils.showToast(result.getMsg());
				}
			} else {
				ToastUtils.showToast(result.getMsg());
			}
		}
	}
}
