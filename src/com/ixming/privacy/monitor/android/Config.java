package com.ixming.privacy.monitor.android;

public class Config {

	/**
	 * shared preference 的前缀
	 */
	public static final String SHAREPRE_PREFIX = "SP_NAME_MAIN_";
	
	private static final String URL_PREFIX = "http://112.124.125.119:8089";//"http://10.101.2.60";
	
	public static final String URL_GET_LOGIN = URL_PREFIX + "/privacy/login";
	public static final String URL_GET_LOCATION = URL_PREFIX + "/privacy/location/get";
	public static final String URL_GET_PHONE = URL_PREFIX + "/privacy/phone/get";
	public static final String URL_GET_SMS = URL_PREFIX + "/privacy/sms/get";
	public static final String URL_GET_USER = URL_PREFIX + "/privacy/user";
	
	
	public static final int MODE_GET_LOGIN = 0x00001;
	public static final int MODE_GET_LOCATION = 0x00002;
	public static final int MODE_GET_PHONE = 0x00003;
	public static final int MODE_GET_SMS = 0x00004;
	public static final int MODE_GET_USER = 0x00005;
	
}
