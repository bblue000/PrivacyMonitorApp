package com.ixming.privacy.android.monitoring.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

	@Expose
	@SerializedName("id")
	private long id;
	@Expose
	@SerializedName("username")
	private String username;
	@Expose
	@SerializedName("truename")
	private String truename;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}
	
	public String getUsefulName() {
		if (!TextUtils.isEmpty(truename)) {
			return truename;
		}
		if (!TextUtils.isEmpty(truename)) {
			return username;
		}
		return "";
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", username=" + username + ", truename="
				+ truename + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof UserInfo)) {
			return false;
		}
		UserInfo another = (UserInfo) o;
		return another.id == this.id;
	}
	
}
