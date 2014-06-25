package com.ixming.privacy.android.main.db;

import android.provider.BaseColumns;

public interface TablePrivacyPhoneInfo extends BaseColumns {

	
	String TABLE_NAME = "privacy_phone_info";
	
	/**
	 * long
	 */
	String COLUMN_REMOTE_ID = "remote_id";
	
	/**
	 * int
	 */
	String COLUMN_INFO_TYPE = "info_type";
	
	/**
	 * string
	 */
	String COLUMN_PHONE_NUMBER = "phone_number";
	
	/**
	 * String
	 */
	String COLUMN_ALIASNAME = "alias_name";
	
	/**
	 * long
	 */
	String COLUMN_DATETIME = "datetime";
}
