package com.ixming.privacy.android.common.model;

public class BasicResponseData {

	private int status;
	private String msg;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "BaseResponseData [status=" + status + ", msg=" + msg + "]";
	}

}
