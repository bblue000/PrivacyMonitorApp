package com.ixming.privacy.android.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponseData {

	@Expose
	@SerializedName("code")
	private int code;
	@Expose
	@SerializedName("msg")
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "BaseResponseData [code=" + code + ", msg=" + msg + "]";
	}
	
}
