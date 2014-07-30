package com.ixming.privacy.android.login.manager;

public interface LogoutOperationCallback {

	void onLogoutSuccess();
	
	void onLogoutError(int errorCode);
	
}
