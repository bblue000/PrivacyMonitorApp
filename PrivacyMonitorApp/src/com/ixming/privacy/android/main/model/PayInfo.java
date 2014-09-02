package com.ixming.privacy.android.main.model;

public class PayInfo {
	int id;
	long expirationdate;
	String username;
	String deviceId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getExpirationdate() {
		return expirationdate;
	}

	public void setExpirationdate(long expirationdate) {
		this.expirationdate = expirationdate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
