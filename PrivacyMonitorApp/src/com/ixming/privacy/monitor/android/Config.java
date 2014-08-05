package com.ixming.privacy.monitor.android;

public class Config {

	/**
	 * shared preference 的前缀
	 */
	public static final String SHAREPRE_PREFIX = "SP_NAME_MAIN_";
	// private static final String URL_PREFIX =
	// "http://10.101.2.60:8080/findlocation";// "http://10.101.2.60";
	private static final String URL_PREFIX = "http://10.101.100.85:8080/findlocation";// "http://10.101.2.60";
	
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>
	// post data
	public static final String URL_POST_DEVICE = URL_PREFIX + "/device";
	public static final String URL_POST_LOCATION = URL_PREFIX + "/location";
	public static final String URL_POST_REGISTER = URL_PREFIX + "/register";
	public static final int MODE_POST_DEVICE = 0x00001;
	public static final int MODE_POST_LOCATION = 0x00002;

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>
	// get data
	public static final String URL_GET_LOGIN = URL_PREFIX + "/privacy/login";
	public static final String URL_GET_LOCATION = URL_PREFIX
			+ "/locations/%s/%s/%s";

	public static final int MODE_GET_LOGIN = 0x00001;
	public static final int MODE_GET_LOCATION = 0x00002;

}
