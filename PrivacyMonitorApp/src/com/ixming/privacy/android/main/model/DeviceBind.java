package com.ixming.privacy.android.main.model;

public class DeviceBind {

	private long id;
	private String device_id;
	private String device_token;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	@Override
	public String toString() {
		return "DeviceBind [id=" + id + ", device_id=" + device_id
				+ ", device_token=" + device_token + "]";
	}
	
}
