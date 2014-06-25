package com.ixming.privacy.android.login.manager;

/**
 * 
 * 用户登录操作的回调
 * 
 * @author Yin Yong
 *
 */
public interface LoginOperationCallback {

	public static final String EXTRA_RESULT_CODE = "extra_result_code";
	
	public static final int CODE_SUCCESS = 0x01;
	public static final int CODE_NON_USER = -0x01;
	public static final int CODE_PASSWORD_ERROR = -0x02;
	
	/**
	 * 登录操作成功后的回调
	 */
	public abstract void onLoginSuccess();
	
	/**
	 * 登录操作失败时的回调
	 * 
	 * @see {@link #CODE_NON_USER}
	 * @see {@link #CODE_PASSWORD_ERROR}
	 */
	public abstract void onLoginError(int errorCode);
	
}
