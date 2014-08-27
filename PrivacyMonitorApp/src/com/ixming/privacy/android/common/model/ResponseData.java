package com.ixming.privacy.android.common.model;

import com.ixming.privacy.android.login.model.UserInfo;

public class ResponseData {
	public class LoginResult extends BasicResponseData<UserInfo> {

	}

	public class RegisterResult extends BasicResponseData<UserInfo> {

	}

	public class ForgotPasswordResult extends BasicResponseData<String> {
	}

	public class CheckcodeResult extends BasicResponseData<String> {
	}
}
