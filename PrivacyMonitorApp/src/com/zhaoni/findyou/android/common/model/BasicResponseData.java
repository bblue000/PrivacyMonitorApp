package com.zhaoni.findyou.android.common.model;

import org.apache.http.HttpStatus;

public class BasicResponseData<T> {

	private int status;
	private String msg;
	private T value;

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
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	public boolean isOK() {
		return this.status == HttpStatus.SC_OK;
	}

	@Override
	public String toString() {
		return "BasicResponseData [status=" + status + ", msg=" + msg
				+ ", value=" + value + "]";
	}

}
