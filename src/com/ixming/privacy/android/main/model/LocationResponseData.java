package com.ixming.privacy.android.main.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.ixming.privacy.android.common.model.BaseResponseData;
import com.ixming.privacy.android.main.entity.PrivacyLocaitonInfo;

public class LocationResponseData extends BaseResponseData {

	@Expose
	private List<PrivacyLocaitonInfo> data;

	public List<PrivacyLocaitonInfo> getData() {
		return data;
	}

	public void setData(List<PrivacyLocaitonInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LocationResponseData [data=" + data + ", getCode()="
				+ getCode() + ", getMsg()=" + getMsg() + "]";
	}
	
}
