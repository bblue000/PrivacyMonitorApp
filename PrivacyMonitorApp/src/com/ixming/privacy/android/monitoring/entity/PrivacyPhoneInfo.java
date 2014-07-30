package com.ixming.privacy.android.monitoring.entity;

import org.ixming.db4android.BaseSQLiteModel;
import org.ixming.db4android.annotation.Column;
import org.ixming.db4android.annotation.PrimaryKey;
import org.ixming.db4android.annotation.Table;

import com.ixming.privacy.android.monitoring.db.LocalDBProvider;
import com.ixming.privacy.android.monitoring.db.TablePrivacyPhoneInfo;

@Table(authority = LocalDBProvider.AUTHORITY, name = TablePrivacyPhoneInfo.TABLE_NAME)
public class PrivacyPhoneInfo implements BaseSQLiteModel {

	public static final int TYPE_INCOMING = 0X1;
	public static final int TYPE_OUTGOING = 0X2;
	
	@PrimaryKey
	@Column(name = TablePrivacyPhoneInfo._ID)
	private long id;
	
	@Column(name = TablePrivacyPhoneInfo.COLUMN_REMOTE_ID, asIndex = true)
	private long remoteId;
	
	// 类型：发送还是接收
	@Column(name = TablePrivacyPhoneInfo.COLUMN_INFO_TYPE)
	private int infoType;
	
	// 发送/接收的号码
	@Column(name = TablePrivacyPhoneInfo.COLUMN_PHONE_NUMBER)
	private String phoneNumber;

	// 发送/接收的号码在手机中的名称
	@Column(name = TablePrivacyPhoneInfo.COLUMN_ALIASNAME)
	private String aliasname;
	
	// 时间
	@Column(name = TablePrivacyPhoneInfo.COLUMN_DATETIME)
	private long datetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAliasname() {
		return aliasname;
	}

	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}

	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "PrivacyPhoneInfo [id=" + id + ", remoteId=" + remoteId
				+ ", infoType=" + infoType + ", phoneNumber=" + phoneNumber
				+ ", aliasname=" + aliasname + ", datetime=" + datetime + "]";
	}

}
