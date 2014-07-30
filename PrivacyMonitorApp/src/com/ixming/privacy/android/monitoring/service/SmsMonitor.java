package com.ixming.privacy.android.monitoring.service;

import org.ixming.base.utils.android.LogUtils;

import com.ixming.privacy.monitor.android.PAApplication;

import android.database.ContentObserver;
import android.net.Uri;

public class SmsMonitor {

	public SmsMonitor() {
		
		PAApplication.getAppContext().getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true,
				new ContentObserver(PAApplication.getHandler()) {
					@Override
					public void onChange(boolean selfChange) {
						
						LogUtils.d("yytest", "content://sms/inbox");
						
					}
					
//					@SuppressLint("NewApi")
//					@Override
//					public void onChange(boolean selfChange, Uri uri) {
//						super.onChange(selfChange, uri);
//						LogUtils.d("yytest", "onChange 2 " + uri);
//					}
					
					
				});
		
		PAApplication.getAppContext().getContentResolver().registerContentObserver(
				Uri.parse("content://sms/sent"), true,
				new ContentObserver(PAApplication.getHandler()) {
					@Override
					public void onChange(boolean selfChange) {
						LogUtils.d("yytest", "content://sms/sent");
					}
				});
		
	}
	
}
