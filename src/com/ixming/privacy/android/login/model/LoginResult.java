package com.ixming.privacy.android.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixming.privacy.android.common.model.BaseResponseData;

public class LoginResult extends BaseResponseData {

	@Expose
	@SerializedName("data")
	private String user_token;

	public String getUser_token() {
		return user_token;
	}

	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}

	@Override
	public String toString() {
		return "LoginResult [user_token=" + user_token + ", getCode()="
				+ getCode() + ", getMsg()=" + getMsg() + "]";
	}

}
