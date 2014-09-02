package com.zhaoni.findyou.android.main.model;

import java.io.Serializable;

import org.ixming.base.utils.ObjectUtils;

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
//	"name": "小龙女",
//    "id": 3,
//    "username": "123@qq.com",
//    "device_id": "123",
//    "device_token": "123"
	
	private long id;
	/**
	 * 监听者的username
	 */
	private String username;
	/**
	 * 监听者的device_id
	 */
	private String device_id;
	
	/**
	 * 被监听者的别名
	 */
	private String name;
	
	/**
	 * 被监听者的device_token标识码
	 */
	private String device_token;
	
	public MonitoredPerson() {
	}
	
	public MonitoredPerson(String device_token, String name) {
		this.device_token = device_token;
		this.name = name;
	}

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

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	@Override
	public String toString() {
		return "MonitoredPerson [id=" + id + ", username=" + username
				+ ", device_id=" + device_id + ", name=" + name
				+ ", device_token=" + device_token + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MonitoredPerson)) {
			return false;
		}
		
		if (o == this) {
			return true;
		}
		
		MonitoredPerson another = (MonitoredPerson) o;
		return ObjectUtils.equals(name, another.name)
				&& id == another.id
				&& ObjectUtils.equals(username, another.username)
				&& ObjectUtils.equals(device_token, another.device_token)
				&& ObjectUtils.equals(device_id, another.device_id)
				;
	}
	
}
