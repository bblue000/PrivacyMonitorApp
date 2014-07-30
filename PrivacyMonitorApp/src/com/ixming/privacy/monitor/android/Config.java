package com.ixming.privacy.monitor.android;

public class Config {

	/**
	 * shared preference 的前缀
	 */
	public static final String SHAREPRE_PREFIX = "SP_NAME_MAIN_";
	
	private static final String URL_PREFIX = "http://10.101.2.60:8080/findlocation";//"http://10.101.2.60";
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>
	// post data
	public static final String URL_POST_LOCATION = URL_PREFIX + "/privacy/location/post";
	public static final String URL_POST_PHONE = URL_PREFIX + "/privacy/phone/post";
	public static final String URL_POST_SMS = URL_PREFIX + "/privacy/sms/post";
	public static final String URL_POST_DEVICE = URL_PREFIX + "/device";
	
	public static final int MODE_POST_LOCATION = 0x00001;
	public static final int MODE_POST_PHONE = 0x00002;
	public static final int MODE_POST_SMS = 0x00003;
	public static final int MODE_POST_DEVICE = 0x00004;
	
	
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>
	// get data
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
