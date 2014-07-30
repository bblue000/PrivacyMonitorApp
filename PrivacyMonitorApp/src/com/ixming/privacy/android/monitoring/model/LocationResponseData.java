package com.ixming.privacy.android.monitoring.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.ixming.privacy.android.common.model.BasicResponseData;
import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;

public class LocationResponseData extends BasicResponseData {

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
		return "LocationResponseData [data=" + data + ", getStatus()="
				+ getStatus() + ", getMsg()=" + getMsg() + "]";
	}
	
}
