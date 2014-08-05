package com.ixming.privacy.android.main.model;

import java.io.Serializable;

/**
 * 被监测的对象
 * 
 * @author Yin Yong
 *
 */
public class MonitoredPerson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 标识码
	 */
	private String device_token;
	
	/**
	 * 备注名
	 */
	private String note_name;
	
	public MonitoredPerson() {
	}
	
	public MonitoredPerson(String device_token, String note_name) {
		this();
		this.device_token = device_token;
		this.note_name = note_name;
	}



	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public String getNote_name() {
		return note_name;
	}

	public void setNote_name(String note_name) {
		this.note_name = note_name;
	}

	@Override
	public String toString() {
		return "MonitoredPerson [device_token=" + device_token + ", note_name="
				+ note_name + "]";
	}
	
	
}
