package com.zhaoni.findyou.android.login.manager;

public interface LogoutOperationCallback {

	void onLogoutSuccess();
	
	void onLogoutError(int errorCode);
	
}
