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
	
	
	/**
	 * 当前用户监听人员变动广播
	 * 
	 * @author Yin Yong
	 */
	public interface MonitoringPerson {
		
		/**
		 * 本地数据变化，界面相应地更新
		 */
		String ACTION_DATA_CHANGED = "com.ixming.privacy.android.MonitoringPerson.ACTION_DATA_CHANGED";
		
		/**
		 * 需要从服务器重新请求数据
		 */
		String ACTION_DATA_INVALIDATE = "com.ixming.privacy.android.MonitoringPerson.ACTION_DATA_INVALIDATE";
		
	}
}
