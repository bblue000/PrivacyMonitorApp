package com.ixming.privacy.android.main.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixming.privacy.android.common.model.BaseResponseData;

public class UserResponseData extends BaseResponseData {

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
		return "UserResponseData [data=" + data + ", getCode()=" + getCode()
				+ ", getMsg()=" + getMsg() + "]";
	}

}
