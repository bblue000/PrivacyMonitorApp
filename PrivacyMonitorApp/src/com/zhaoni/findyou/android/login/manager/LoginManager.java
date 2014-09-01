package com.zhaoni.findyou.android.login.manager;

import org.apache.http.HttpStatus;
import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import android.content.Intent;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents;
import com.zhaoni.findyou.android.common.control.LocalUserController;
import com.zhaoni.findyou.android.common.model.ResponseData.LoginResult;
import com.zhaoni.findyou.android.login.model.UserInfo;

public class LoginManager {

	private static final String TAG = LoginManager.class.getSimpleName();
	private static LoginManager sInstance;

	public synchronized static LoginManager getInstance() {
		if (null == sInstance) {
			sInstance = new LoginManager();
		}
		return sInstance;
	}

	AQuery aq;
	AjaxCallbackImpl ajaxCallback;
	private LocalUserController mLocalUserController;

	private LoginManager() {
		aq = new AQuery(PAApplication.getAppContext());
		mLocalUserController = LocalUserController.getInstance();
	}

	public void setLoginUser(UserInfo userInfo) {
		mLocalUserController.setUserInfo(userInfo);
		if (mLocalUserController.isUserLogining()) {
			onLoginSuccess();
		}
	}

	public synchronized void login(String username, String password) {
		if (ajaxCallback != null) {

		}
		ajaxCallback = new AjaxCallbackImpl();
		aq.ajax(String.format(Config.URL_GET_LOGIN, username, password),
				LoginResult.class, ajaxCallback);
	}

	public synchronized void cancelLogin() {
		LogUtils.d(TAG, "cancelLogin");
	}

	public void logout() {
		mLocalUserController.clearUserPrivacyInfo();
		LocalBroadcasts.sendLocalBroadcast(LocalBroadcastIntents.ACTION_LOGOUT);
	}

	private class AjaxCallbackImpl extends AjaxCallback<LoginResult> {
		@Override
		public void callback(String url, LoginResult object, AjaxStatus status) {
			if (status.getCode() == HttpStatus.SC_OK) {
				if (object.getStatus() == 200) {
					LoginManager.getInstance().setLoginUser(object.getValue());
				}
				ToastUtils.showLongToast(result.getMsg());
			} else {
				ToastUtils.showLongToast(result.getMsg());
			}
		}
	}

	private synchronized void onLoginSuccess() {
		LocalBroadcasts.sendLocalBroadcast(new Intent(
				LocalBroadcastIntents.ACTION_LOGIN).putExtra(
				LoginOperationCallback.EXTRA_RESULT_CODE,
				LoginOperationCallback.CODE_SUCCESS));
	}

	private synchronized void onLoginFailed(int code) {
		// Intent intent = new Intent(LocalBroadcastIntents.ACTION_LOGIN);
		// intent.putExtra(LoginOperationCallback.EXTRA_RESULT_CODE, code);
		// LocalBroadcasts.sendLocalBroadcast(intent);
	}

}
