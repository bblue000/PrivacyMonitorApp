package org.ixming.android.location.baidu;

import com.baidu.location.BDLocation;

public class LocationInfo {

	private BDLocation mLocation;
	
	LocationInfo(BDLocation loc) {
		mLocation = loc;
	}
	
	public double getLatitude() {
		return mLocation.getLatitude();
	}
	
	public double getLongitude() {
		return mLocation.getLongitude();
	}
	
	/**
	 * 获取详细地址信息
	 */
	public String getAddress() {
		return mLocation.getAddrStr() + "附近";
	}
	
	/**
	 * 获取省份
	 */
	public String getProvince() {
		return mLocation.getProvince();
	}
	
	/**
	 * 获取城市
	 */
	public String getCity() {
		return mLocation.getCity();
	}
	
	/**
	 * 获取区县信息（比如浦东新区）
	 */
	public String getDistrict() {
		return mLocation.getDistrict();
	}
	
	/**
	 * 获取街道
	 */
	public String getStreet() {
		return mLocation.getStreet();
	}
}
