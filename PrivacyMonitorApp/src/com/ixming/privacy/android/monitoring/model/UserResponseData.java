package com.ixming.privacy.android.monitoring.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixming.privacy.android.common.model.BasicResponseData;

public class UserResponseData extends BasicResponseData {

	@Expose
	@SerializedName("data")
	private List<UserInfo> data;

	public List<UserInfo> getData() {
		return data;
	}

	public void setData(List<UserInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "UserResponseData [data=" + data + ", getStatus()="
				+ getStatus() + ", getMsg()=" + getMsg() + "]";
	}

}
