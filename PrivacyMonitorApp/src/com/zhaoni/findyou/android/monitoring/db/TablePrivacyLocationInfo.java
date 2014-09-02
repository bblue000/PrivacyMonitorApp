package com.zhaoni.findyou.android.monitoring.db;

import android.provider.BaseColumns;

public interface TablePrivacyLocationInfo extends BaseColumns {

	
	String TABLE_NAME = "privacy_loc_info";
	
	/**
	 * long
	 */
	String COLUMN_REMOTE_ID = "remote_id";
	
	/**
	 * String
	 */
	String COLUMN_LOCATION_INFO = "location_info";
	
	/**
	 * double
	 */
	String COLUMN_LATITUDE = "latitude";
	
	/**
	 * double
	 */
	String COLUMN_LONGITUDE = "longitude";
	
	/**
	 * long
	 */
	String COLUMN_DATETIME = "datetime";
}
