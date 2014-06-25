package com.ixming.privacy.android.login.manager;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.manager.BaseManager;
import org.ixming.base.common.task.HttpGetTask;
import org.ixming.base.network.HttpRes;
import org.ixming.base.network.json.GsonHelper;
import org.ixming.base.network.json.GsonPool;
import org.ixming.base.taskcenter.entity.ReqBean;
import org.ixming.base.utils.android.LogUtils;

import android.content.Intent;
import android.text.TextUtils;

import com.ixming.privacy.android.common.DefProgressDisplayer;
import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.common.model.RequestPiecer;
import com.ixming.privacy.android.login.model.LoginResult;
import com.ixming.privacy.android.login.model.shared.SharedUtil;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class LoginManager extends BaseManager {

	private static final String TAG = LoginManager.class.getSimpleName();
	
	private static  LoginManager sInstance;
	public synchronized static LoginManager getInstance() {
		if (null == sInstance) {
			sInstance = new LoginManager();
		}
		return sInstance;
	}
	
	
	private class UserInfo {
		String username;
		String password;
		String userToken;
		long lastLoginDatetime;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getUserToken() {
			return userToken;
		}
		public void setUserToken(String userToken) {
			this.userToken = userToken;
		}
		public long getLastLoginDatetime() {
			return lastLoginDatetime;
		}
		public void setLastLoginDatetime(long lastLoginDatetime) {
			this.lastLoginDatetime = lastLoginDatetime;
		}
	}
	private final UserInfo mUserInfo = new UserInfo();
	private HttpGetTask mLoginTask;
	private LoginManager() {
		super(PAApplication.getAppContext(), PAApplication.getHandler());
		mLoginTask = new HttpGetTask(new DefProgressDisplayer(){
			@Override
			public void onCancel() {
				cancelLogin();
			}
		});
		
		loadFromShare();
	}
	
	private synchronized void loadFromShare() {
		mUserInfo.setUsername(SharedUtil.getUsername());
		mUserInfo.setPassword(SharedUtil.getPassword());
		mUserInfo.setUserToken(SharedUtil.getUserToken());
		mUserInfo.setLastLoginDatetime(SharedUtil.getLastLoginDatetime());
	}
	
	private synchronized void storeToShare() {
		SharedUtil.setUsername(mUserInfo.getUsername());
		SharedUtil.setPassword(mUserInfo.getPassword());
		SharedUtil.setUserToken(mUserInfo.getUserToken());
		SharedUtil.setLastLoginDatetime(mUserInfo.getLastLoginDatetime());
	}

	/**
	 * 清除本地的用户隐私信息——密码和token
	 */
	public synchronized void clearUserPrivacyInfo() {
		mUserInfo.setPassword(null);
		mUserInfo.setUserToken(null);
		mUserInfo.setLastLoginDatetime(0);
		storeToShare();
	}
	
	/**
	 * 判断当前是否有用户登录
	 */
	public synchronized boolean hasUser() {
		return !TextUtils.isEmpty(mUserInfo.getUserToken());
	}
	
	public synchronized void login(String username, String password) {
		mUserInfo.setUsername(username);
		mUserInfo.setPassword(password);
		
		mLoginTask.execute(Config.URL_GET_LOGIN,  RequestPiecer.getLoginJson(username, password),
				Config.MODE_GET_LOGIN, this);
	}
	
	public synchronized void cancelLogin() {
		LogUtils.d(TAG, "cancelLogin");
		mLoginTask.cancel();
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
			return ;
		}
		if (!hasUser()) {
			callback.onNonLogin();
			return ;
		}
		callback.onAlreadyLogged();
	}
	
	@Override
	public void onSuccess(Object obj, ReqBean reqMode) {
		HttpRes httpRes = (HttpRes) obj;
		try {
			String json = GsonHelper.getJson(httpRes);
			LoginResult result = GsonPool.getExposeRestrict().fromJson(
					json, LoginResult.class);
			if (null != result) {
				if (result.getCode() == 200) {
					onLoginSuccess(result);
				} else {
//					toastShow(result.getMsg());
					onLoginFailed(result.getCode());
				}
			}
		} catch (Exception e) {
			LogUtils.e(TAG, "onSuccess Exception: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != httpRes) 
				httpRes.abort();
		}
	}

	@Override
	public void onError(Object obj, ReqBean reqMode) {
//		toastShow(R.string.login_failed);
		onLoginFailed(0);
	}

	private synchronized void onLoginSuccess(LoginResult result) {
		mUserInfo.setUserToken(result.getUser_token());
		mUserInfo.setLastLoginDatetime(System.currentTimeMillis());
		storeToShare();
		
		LocalBroadcasts.sendLocalBroadcast(new Intent(LocalBroadcastIntents.ACTION_LOGIN)
				.putExtra(LoginOperationCallback.EXTRA_RESULT_CODE, LoginOperationCallback.CODE_SUCCESS));
	}
	
	private synchronized void onLoginFailed(int code) {
		Intent intent = new Intent(LocalBroadcastIntents.ACTION_LOGIN);
		intent.putExtra(LoginOperationCallback.EXTRA_RESULT_CODE, code);
		LocalBroadcasts.sendLocalBroadcast(intent);
	}
	
}
