package com.zhaoni.findyou.android.login.manager;

public interface LoginStateCallback {

	/**
	 * 没有登录过
	 */
	public abstract void onNonLogin();
	
	/**
	 * 已经登录
	 */
	public abstract void onAlreadyLogged();
	
	/**
	 * 登录信息过期
	 */
	public abstract void onLoginExpire();
	
}
