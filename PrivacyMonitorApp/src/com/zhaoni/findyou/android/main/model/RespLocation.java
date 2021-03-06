package com.zhaoni.findyou.android.main.model;

public class RespLocation implements Comparable<RespLocation> {
	
//	id: 104,
//	device_id: "ac9a60d9-f6b3-39d6-9386-dae23528f9ad",
//	device_token: "kap0l8",
//	latitude: "31.244732",
//	longitude: "121.443873",
//	address: "上海市普陀区新会路422号",
//	date_time: 1406804503193
	
	private long id;
	private String device_id;
	private String device_token;
	private double latitude;
	private double longitude;
	private String address;
	private long date_time;

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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public long getDate_time() {
		return date_time;
	}

	public void setDate_time(long date_time) {
		this.date_time = date_time;
	}

	@Override
	public String toString() {
		return "RespLocation [id=" + id + ", device_id=" + device_id
				+ ", device_token=" + device_token + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", address=" + address
				+ ", date_time=" + date_time + "]";
	}
	
	@Override
	public int compareTo(RespLocation another) {
		
		return (int) (another.getDate_time() - getDate_time());
	}

}
