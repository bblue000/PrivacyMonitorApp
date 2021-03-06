package com.zhaoni.findyou.android.login.manager;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.ResponseData.ForgotPasswordResult;

public class ForgotPasswordManager {
	AQuery aq = new AQuery(PAApplication.getAppContext());

	public void sendEmail(String email) {
		Map<String, String> map = RequestPiecer.getForgotPasswordJson(email);
		aq.ajax(Config.URL_POST_RETRIEVE_PASSWORD, map,
				ForgotPasswordResult.class, ajaxCallback);
	}

	AjaxCallback<ForgotPasswordResult> ajaxCallback = new AjaxCallback<ForgotPasswordResult>() {
		public void callback(String url, ForgotPasswordResult object,
				AjaxStatus status) {
			if (status.getCode() == HttpStatus.SC_OK) {
				if (object.getStatus() == 200) {
					// 找回密码成功
					ToastUtils.showToast(object.getMsg());
				}
			}
			LogUtils.i(getClass(), object.getMsg());
		};
	};

}
