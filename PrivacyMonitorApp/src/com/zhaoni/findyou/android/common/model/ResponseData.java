package com.zhaoni.findyou.android.common.model;

import com.zhaoni.findyou.android.login.model.UserInfo;
import com.zhaoni.findyou.android.main.model.PayInfo;

public class ResponseData {
	public class LoginResult extends BasicResponseData<UserInfo> {

	}

	public class RegisterResult extends BasicResponseData<UserInfo> {

	}

	public class ForgotPasswordResult extends BasicResponseData<String> {
	}

	public class CheckcodeResult extends BasicResponseData<String> {
	}

	public class PayInfoResult extends BasicResponseData<PayInfo> {

	}
}
