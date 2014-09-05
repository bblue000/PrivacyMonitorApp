package com.zhaoni.findyou.android;

public class Config {

	/**
	 * shared preference 的前缀
	 */
	public static final String SHAREPRE_PREFIX = "SP_NAME_MAIN_";
	// private static final String URL_PREFIX =
	// "http://10.101.2.60:8080/findlocation";// "http://10.101.2.60";
	/**
	 * 服务器地址
	 */
	private static final String URL_PREFIX = "http://121.40.105.126:8080/findlocation";
	/**
	 * 本机测试地址
	 */
	// private static final String URL_PREFIX =
	// "http://10.101.145.236:8080/findlocation";
	// private static final String URL_PREFIX =
	// "http://10.101.100.85:8080/findlocation";// "http://10.101.2.60";

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>
	// post data
	public static final String URL_POST_DEVICE = URL_PREFIX + "/device";
	public static final String URL_POST_LOCATION = URL_PREFIX + "/location";
	public static final String URL_POST_REGISTER = URL_PREFIX + "/register";
	public static final String URL_POST_RETRIEVE_PASSWORD = URL_PREFIX
			+ "/retrieve_password";
	public static final int MODE_POST_DEVICE = 0x00001;
	public static final int MODE_POST_LOCATION = 0x00002;
	public static final String URL_POST_LISTENER = URL_PREFIX + "/listener";
	public static final String URL_POST_FEEDBACK = URL_PREFIX + "/feedback";
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>
	// get data
	public static final String URL_GET_LOGIN = URL_PREFIX + "/login/%s/%s";
	/**
	 * /locations/{device_token}/{username}/{device_id}
	 */
	public static final String URL_GET_LOCATION = URL_PREFIX
			+ "/locations/%s/%s/%s";

	public static final int MODE_GET_LOGIN = 0x00001;
	public static final int MODE_GET_LOCATION = 0x00002;

	/**
	 * listeners/{username}/{device_id}
	 */
	public static final String URL_GET_LISTENERS = URL_PREFIX
			+ "/listeners/%s/%s";
	/**
	 * 获取设备剩余可使用时间
	 */
	public static final String URL_GET_PAYINFO = URL_PREFIX + "/payinfo/%s/%s";
	/**
	 * 增加时间
	 */
	public static final String URL_PUT_PAYINFO = URL_PREFIX + "/payinfo/%s/%s";
	/**
	 * 获取验证码
	 */
	public static final String URL_GET_CHECKCODE = URL_PREFIX + "/checkcode/%s";
	/**
	 * 获取最新版本信息
	 */
	public static final String URL_GET_VERISON = URL_PREFIX + "/version";
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>
	// put
	/**
	 * listener/{id}/{name}
	 */
	public static final String URL_PUT_LISTENER = URL_PREFIX
			+ "/listener/%s/%s";

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>
	// delete
	/**
	 * listener/{id}
	 */
	public static final String URL_DELETE_LISTENER = URL_PREFIX
			+ "/listener/%s";

}
