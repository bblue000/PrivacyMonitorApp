package com.ixming.privacy.android.login.manager.test;

import java.util.concurrent.atomic.AtomicInteger;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.manager.BaseManager;
import org.ixming.base.taskcenter.entity.ReqBean;
import org.ixming.base.utils.android.LogUtils;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.login.manager.LoginOperationCallback;
import com.ixming.privacy.android.login.manager.LoginStateCallback;
import com.ixming.privacy.android.login.manager.LogoutOperationCallback;
import com.ixming.privacy.android.login.model.LoginResult;
import com.ixming.privacy.android.login.model.shared.SharedUtil;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class LoginManager_ForTest extends BaseManager {

	private static final String TAG = LoginManager_ForTest.class.getSimpleName();
	
	private static  LoginManager_ForTest sInstance;
	public synchronized static LoginManager_ForTest getInstance() {
		if (null == sInstance) {
			sInstance = new LoginManager_ForTest();
		}
		return sInstance;
	}
	
	private final int STATE_IDEL = 0;
	private final int STATE_LOGIN_PROCESS = 1;
	
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
	private final AtomicInteger mState = new AtomicInteger(STATE_IDEL);
	private LoginTask mLoginTask;
	private LoginManager_ForTest() {
		super(PAApplication.getAppContext(), PAApplication.getHandler());
		
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
	
	public synchronized void login(String username, String password, LoginOperationCallback callback) {
		mUserInfo.setUsername(username);
		mUserInfo.setPassword(password);
		
		switch (mState.get()) {
		case STATE_IDEL:
			// do login
			mLoginTask = new LoginTask(username, password, callback);
			mLoginTask.execute();
			
			mState.set(STATE_LOGIN_PROCESS);
			break;
		case STATE_LOGIN_PROCESS:
			toastShow(R.string.login_already_process);
			cancelLogin();
			
			mLoginTask = new LoginTask(username, password, callback);
			mLoginTask.execute();
			break;
		default:
			break;
		}
	}
	
	public synchronized void cancelLogin() {
		LogUtils.d(TAG, "cancelLogin");
		if (null != mLoginTask)
			mLoginTask.cancel(true);
		
		mLoginTask = null;
	}
	
	private synchronized void onLoginSuccess(LoginResult result) {
		mUserInfo.setUserToken(result.getUser_token());
		mUserInfo.setLastLoginDatetime(System.currentTimeMillis());
		storeToShare();
		
		LocalBroadcasts.sendLocalBroadcast(LocalBroadcastIntents.ACTION_LOGIN);
	}

	private class LoginTask extends AsyncTask<Void, Void, LoginResult> {
		@SuppressWarnings("unused")
		private String mUsername;
		@SuppressWarnings("unused")
		private String mPassword;
		private LoginOperationCallback mCallback;
		
		public LoginTask(String username, String password,
				LoginOperationCallback callback) {
			super();
			this.mUsername = username;
			this.mPassword = password;
			this.mCallback = callback;
		}

		@Override
		protected LoginResult doInBackground(Void... params) {
			//FIXME test codes
			try {
				Thread.sleep(3000L);
			} catch (Exception e) { }
			long timeMills = System.currentTimeMillis();
			int d = (int) (timeMills % 3);
			
			LoginResult result = new LoginResult();
			result.setCode(d);
			result.setUser_token(String.valueOf(timeMills));
			return result;
		}
		
		@Override
		protected void onCancelled(LoginResult result) {
			LogUtils.d(TAG, "LoginResult onCancelled");
			mState.set(STATE_IDEL);
			mLoginTask = null;
		}
		
		@Override
		protected void onPostExecute(LoginResult result) {
			LogUtils.d(TAG, "LoginResult onPostExecute");
			int d = result.getCode();
			d = 2;
			if (d == 0) {
				if (null != mCallback)
					mCallback.onLoginError(LoginOperationCallback.CODE_NON_USER);
			} else if (d == 1) {
				if (null != mCallback)
					mCallback.onLoginError(LoginOperationCallback.CODE_PASSWORD_ERROR);
			} else {
				onLoginSuccess(result);
				if (null != mCallback)
					mCallback.onLoginSuccess();
			}
			mState.set(STATE_IDEL);
			mLoginTask = null;
		}
		
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
		new CheckLoginStateTask(callback).execute();
	}
	
	private class CheckLoginStateTask extends AsyncTask<Void, Void, Boolean> {

		private LoginStateCallback mCallback;
		public CheckLoginStateTask(LoginStateCallback callback) {
			mCallback = callback;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			//FIXME test codes
			long timeMills = System.currentTimeMillis();
			try {
				Thread.sleep(1000L * (timeMills % 3 + 1));
			} catch (Exception e) { }
			
			long d = timeMills % 2;
			if (d == 0 && timeMills - mUserInfo.getLastLoginDatetime() > 10 * 60 * 1000) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			result = true;
			if (result) {
				mCallback.onAlreadyLogged();
			} else {
				logout(null);
				mCallback.onLoginExpire();
			}
		}
	}
	
	
	@Override
	public void onSuccess(Object obj, ReqBean reqMode) {
		
	}

	@Override
	public void onError(Object obj, ReqBean reqMode) {
		
	}

}
