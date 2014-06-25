package com.ixming.privacy.android.main.entity;

import org.ixming.db4android.BaseSQLiteModel;
import org.ixming.db4android.annotation.Column;
import org.ixming.db4android.annotation.PrimaryKey;
import org.ixming.db4android.annotation.Table;

import com.ixming.privacy.android.main.db.LocalDBProvider;
import com.ixming.privacy.android.main.db.TablePrivacySmsInfo;

@Table(authority = LocalDBProvider.AUTHORITY, name = TablePrivacySmsInfo.TABLE_NAME)
public class PrivacySmsInfo implements BaseSQLiteModel {

	public static final int TYPE_SEND = 0x1;
	public static final int TYPE_RECEIVE = 0x2;
	
	@PrimaryKey
	@Column(name = TablePrivacySmsInfo._ID)
	private long id;
	
	@Column(name = TablePrivacySmsInfo.COLUMN_REMOTE_ID, asIndex = true)
	private long remoteId;
	
	// 类型：发送还是接收
	@Column(name = TablePrivacySmsInfo.COLUMN_INFO_TYPE)
	private int infoType;
	
	// 发送/接收的号码
	@Column(name = TablePrivacySmsInfo.COLUMN_PHONE_NUMBER)
	private String phoneNumber;

	// 发送/接收的号码在手机中的名称
	@Column(name = TablePrivacySmsInfo.COLUMN_ALIASNAME)
	private String aliasname;
	
	// 内容
	@Column(name = TablePrivacySmsInfo.COLUMN_SMS_CONTENT)
	private String smsContent;
	
	// 时间
	@Column(name = TablePrivacySmsInfo.COLUMN_DATETIME)
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

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "PrivacySmsInfo [id=" + id + ", remoteId=" + remoteId
				+ ", infoType=" + infoType + ", phoneNumber=" + phoneNumber
				+ ", aliasname=" + aliasname + ", smsContent=" + smsContent
				+ ", datetime=" + datetime + "]";
	}

}
