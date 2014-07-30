package com.ixming.privacy.android.login.model.shared;

import org.ixming.base.utils.android.PreferenceUtils;

import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class SharedUtil {
	
	// 主节点
	private final static String SP_NAME_MAIN_LOGIN = Config.SHAREPRE_PREFIX + "LOGIN";
	
	private static final String USER_NAME = "USER_NAME";
	private static final String USER_PWD = "USER_PWD";
	private static final String USER_TOKEN = "USER_TOKEN";
	private static final String LAST_LOGIN_DATETIME = "LAST_LOGIN_DATETIME";
	
	public static String getUsername() {
		return PreferenceUtils.getValue(PAApplication.getAppContext(),
				SP_NAME_MAIN_LOGIN, USER_NAME, null);
	}
	
	public static void setUsername(String username) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(),
				SP_NAME_MAIN_LOGIN, USER_NAME, username);
	}
	
	public static String getPassword() {
		return PreferenceUtils.getValue(PAApplication.getAppContext(),
				SP_NAME_MAIN_LOGIN, USER_PWD, null);
	}
	
	public static void setPassword(String pwd) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(),
				SP_NAME_MAIN_LOGIN, USER_PWD, pwd);
	}
	
	public static String getUserToken() {
		return PreferenceUtils.getValue(PAApplication.getAppContext(),
				SP_NAME_MAIN_LOGIN, USER_TOKEN, null);
	}
	
	public static void setUserToken(String token) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(),
				SP_NAME_MAIN_LOGIN, USER_TOKEN, token);
	}
	
	public static long getLastLoginDatetime() {
		return PreferenceUtils.getValue(PAApplication.getAppContext(),
				SP_NAME_MAIN_LOGIN, LAST_LOGIN_DATETIME, 0L);
	}
	
	public static void setLastLoginDatetime(long datetime) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(),
				SP_NAME_MAIN_LOGIN, LAST_LOGIN_DATETIME, datetime);
	}
}
