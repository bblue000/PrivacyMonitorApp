package com.ixming.privacy.android.monitoring.entity;

import org.ixming.db4android.BaseSQLiteModel;
import org.ixming.db4android.annotation.Column;
import org.ixming.db4android.annotation.PrimaryKey;
import org.ixming.db4android.annotation.Table;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixming.privacy.android.monitoring.db.LocalDBProvider;
import com.ixming.privacy.android.monitoring.db.TablePrivacyLocationInfo;

@Table(authority = LocalDBProvider.AUTHORITY, name = TablePrivacyLocationInfo.TABLE_NAME)
public class PrivacyLocaitonInfo implements BaseSQLiteModel, Comparable<PrivacyLocaitonInfo> {

	@PrimaryKey
	@Column(name = TablePrivacyLocationInfo._ID)
	private long id;
	
	@Column(name = TablePrivacyLocationInfo.COLUMN_REMOTE_ID, asIndex = true)
	private long remoteId;
	
	@Expose
	@SerializedName("location_info")
	@Column(name = TablePrivacyLocationInfo.COLUMN_LOCATION_INFO)
	private String locationInfo;
	
	@Expose
	@SerializedName("lat")
	@Column(name = TablePrivacyLocationInfo.COLUMN_LATITUDE)
	private double latitude;
	
	@Expose
	@SerializedName("lng")
	@Column(name = TablePrivacyLocationInfo.COLUMN_LONGITUDE)
	private double longitude;
	
	@Expose
	@SerializedName("location_date")
	@Column(name = TablePrivacyLocationInfo.COLUMN_DATETIME)
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

	public String getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
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

	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "PrivacyLocaitonInfo [id=" + id + ", remoteId=" + remoteId
				+ ", locationInfo=" + locationInfo + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", datetime=" + datetime + "]";
	}

	@Override
	public int compareTo(PrivacyLocaitonInfo another) {
		
		return (int) (getDatetime() - another.getDatetime());
	}

}
