package com.ixming.privacy.android.common;

public interface LocalBroadcastIntents {

	/**
	 * 当用户登录时广播
	 */
	//String ACTION_LOGIN = "com.ixming.privacy.android.ACTION_LOGIN";
	/**
	 * 当用户注销时广播
	 */
	String ACTION_LOGOUT = "com.ixming.privacy.android.ACTION_LOGOUT";
	String ACTION_UPDATE_LOCATION = "com.ixming.privacy.android.ACTION_UPDATE_LOCATION";
	
	
	/**
	 * 获取device_token后发送的广播
	 * 
	 * @author Yin Yong
	 */
	public interface DeviceToken {
		
		String ACTION_DEVICE_TOKEN_LOADED = "com.ixming.privacy.android.DeviceToken.ACTION_DEVICE_TOKEN_LOADED";
		String ACTION_DEVICE_TOKEN_FAILED = "com.ixming.privacy.android.DeviceToken.ACTION_DEVICE_TOKEN_FAILED";
		
	}
	
	
	/**
	 * 获取device_token后发送的广播
	 * 
	 * @author Yin Yong
	 */
	public interface MonitorLocation {
		
		String ACTION_SETTING_OPEN = "com.ixming.privacy.android.MonitorLocation.ACTION_SETTING_OPEN";
		String ACTION_SETTING_CLOSE = "com.ixming.privacy.android.MonitorLocation.ACTION_SETTING_CLOSE";
		
		
	}
}
