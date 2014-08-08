package com.ixming.privacy.android.login.manager;

import org.apache.http.HttpStatus;
import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import android.content.Intent;
import android.text.TextUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.common.model.ResponseData.LoginResult;
import com.ixming.privacy.android.login.model.UserInfo;
import com.ixming.privacy.android.login.model.shared.SharedUtil;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class LoginManager {
	public final static String LOGIN_ACTION = "com.find.LOGIN_ACTION";

	private LoginManager() {
		aq = new AQuery(PAApplication.getAppContext());
		loadFromShare();
	}

	private static final String TAG = LoginManager.class.getSimpleName();
	private static LoginManager sInstance;
	AQuery aq;
	AjaxCallbackImpl ajaxCallback;

	public synchronized static LoginManager getInstance() {
		if (null == sInstance) {
			sInstance = new LoginManager();
		}
		return sInstance;
	}

	private UserInfo mUserInfo = new UserInfo();

	// private LoginManager() {
	// aq = new AQuery(PAApplication.getAppContext());
	// // super(PAApplication.getAppContext(), PAApplication.getHandler());
	// // mLoginTask = new HttpGetTask(new DefProgressDisplayer() {
	// // @Override
	// // public void onCancel() {
	// // cancelLogin();
	// // }
	// // });
	//
	// // loadFromShare();
	// }

	private synchronized void loadFromShare() {
		mUserInfo.setUsername(SharedUtil.getUsername());
		mUserInfo.setUser_token(SharedUtil.getUserToken());
	}

	private synchronized void storeToShare() {
		SharedUtil.setUsername(mUserInfo.getUsername());
		// SharedUtil.setPassword(mUserInfo.getPassword());
		SharedUtil.setUserToken(mUserInfo.getUser_token());
		// SharedUtil.setLastLoginDatetime(mUserInfo.getLastLoginDatetime());
	}

	/**
	 * 清除本地的用户隐私信息——密码和token
	 */
	public synchronized void clearUserPrivacyInfo() {
		// mUserInfo.setPassword(null);
		// mUserInfo.setUserToken(null);
		// mUserInfo.setLastLoginDatetime(0);
		storeToShare();
	}

	/**
	 * 判断当前是否有用户登录
	 */
	public synchronized boolean hasUser() {
		return !TextUtils.isEmpty(mUserInfo.getUser_token());
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

	public synchronized void logout(LogoutOperationCallback callback) {
		clearUserPrivacyInfo();
		LocalBroadcasts.sendLocalBroadcast(LocalBroadcastIntents.ACTION_LOGOUT);

		if (null != callback)
			callback.onLogoutSuccess();
	}

	/**
	 * 
	 * 检测登录状态，并根据状态回调
	 * 
	 * @see {@link LoginStateCallback}
	 */
	public void checkLoginState(LoginStateCallback callback) {
		if (null == callback) {
			return;
		}
		if (!hasUser()) {
			callback.onNonLogin();
			return;
		}
		callback.onAlreadyLogged();
	}

	private class AjaxCallbackImpl extends AjaxCallback<LoginResult> {
		@Override
		public void callback(String url, LoginResult object, AjaxStatus status) {
			if (status.getCode() == HttpStatus.SC_OK) {
				if (object.getStatus() == 200) {
					mUserInfo = object.getValue();
					onLoginSuccess();
				}
				;
				ToastUtils.showLongToast(result.getMsg());
			} else {
				ToastUtils.showLongToast(result.getMsg());
			}
		}
	}

	public UserInfo getmUserInfo() {
		return mUserInfo;
	}

	public void setmUserInfo(UserInfo mUserInfo) {
		this.mUserInfo = mUserInfo;
		onLoginSuccess();
	}

	private synchronized void onLoginSuccess() {
		storeToShare();
		LocalBroadcasts
				.sendLocalBroadcast(new Intent(LoginManager.LOGIN_ACTION)
						.putExtra(LoginOperationCallback.EXTRA_RESULT_CODE,
								LoginOperationCallback.CODE_SUCCESS));
	}

	private synchronized void onLoginFailed(int code) {
		// Intent intent = new Intent(LocalBroadcastIntents.ACTION_LOGIN);
		// intent.putExtra(LoginOperationCallback.EXTRA_RESULT_CODE, code);
		// LocalBroadcasts.sendLocalBroadcast(intent);
	}

}
