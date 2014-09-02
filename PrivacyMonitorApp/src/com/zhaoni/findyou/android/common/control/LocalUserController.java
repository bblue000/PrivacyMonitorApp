package com.zhaoni.findyou.android.common.control;

import org.ixming.base.common.controller.BaseController;

import android.text.TextUtils;

import com.zhaoni.findyou.android.login.model.UserInfo;
import com.zhaoni.findyou.android.login.model.shared.SharedUtil;

/**
 * 
 * 本地User信息的统一管理
 * 
 * @author Yin Yong
 *
 */
public class LocalUserController extends BaseController {

	private static LocalUserController sInstance = new LocalUserController();

	public static LocalUserController getInstance() {
		return sInstance;
	}
	
	// a user instance
	private final UserInfo mUserInfo = new UserInfo();
	private LocalUserController() {
		loadFromShare();
	}
	
	public void setUserInfo(UserInfo userInfo) {
		if (null == userInfo) {
			clearUserPrivacyInfo();
		} else {
			mUserInfo.setUsername(userInfo.getUsername());
			mUserInfo.setUser_token(userInfo.getUser_token());
			mUserInfo.setEmail(userInfo.getEmail());
			storeToShare();
		}
	}
	
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
	
	
	// 外部调用的API方法
	/**
	 * 清除本地的用户隐私信息——密码和token
	 */
	public synchronized void clearUserPrivacyInfo() {
		mUserInfo.setUsername(null);
		mUserInfo.setUser_token(null);
		// mUserInfo.setLastLoginDatetime(0);
		storeToShare();
	}
	
	/**
	 * 判断是否当前已登录
	 */
	public boolean isUserLogining() {
		return !TextUtils.isEmpty(mUserInfo.getUser_token());
	}
	
	// all getter methods
	public String getUsername() {
		return mUserInfo.getUsername();
	}
	
	public String getUser_token() {
		return mUserInfo.getUser_token();
	}
	
	public String getEmail() {
		return mUserInfo.getEmail();
	}
}
