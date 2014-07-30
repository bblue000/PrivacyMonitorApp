package com.ixming.privacy.android.monitoring.model;

import org.ixming.android.location.baidu.LocationInfo;
import org.ixming.base.utils.android.LogUtils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;
import com.ixming.privacy.android.monitoring.entity.PrivacyPhoneInfo;
import com.ixming.privacy.android.monitoring.entity.PrivacySmsInfo;
import com.ixming.privacy.monitor.android.PAApplication;

public class PrivacyInfoFactory {

	private static final String TAG = PrivacyInfoFactory.class.getSimpleName();
	
	public static PrivacySmsInfo createSendSmsInfo(String number, String content) {
		PrivacySmsInfo info = new PrivacySmsInfo();
		info.setAliasname(queryDisplayName2(number));
		info.setPhoneNumber(number);
		info.setDatetime(System.currentTimeMillis());
		info.setInfoType(PrivacySmsInfo.TYPE_SEND);
		info.setSmsContent(content);
		return info;
	}
	
	public static PrivacySmsInfo createReceiveSmsInfo(String number, String content) {
		PrivacySmsInfo info = new PrivacySmsInfo();
		info.setAliasname(queryDisplayName2(number));
		info.setDatetime(System.currentTimeMillis());
		info.setInfoType(PrivacySmsInfo.TYPE_RECEIVE);
		info.setSmsContent(content);
		return info;
	}
	
	public static PrivacyLocaitonInfo createPrivacyLocaitonInfo(LocationInfo locationInfo) {
		PrivacyLocaitonInfo info = new PrivacyLocaitonInfo();
		info.setLocationInfo(locationInfo.getAddress());
		info.setDatetime(System.currentTimeMillis());
		info.setLatitude(locationInfo.getLatitude());
		info.setLongitude(locationInfo.getLongitude());
		return info;
	}
	
	public static PrivacyPhoneInfo createIncomingPhone(String number) {
		PrivacyPhoneInfo info = new PrivacyPhoneInfo();
		info.setAliasname(queryDisplayName2(number));
		info.setPhoneNumber(number);
		info.setDatetime(System.currentTimeMillis());
		info.setInfoType(PrivacyPhoneInfo.TYPE_INCOMING);
		return info;
	}
	
	public static PrivacyPhoneInfo createOutgoingPhone(String number) {
		PrivacyPhoneInfo info = new PrivacyPhoneInfo();
		info.setAliasname(queryDisplayName2(number));
		info.setPhoneNumber(number);
		info.setDatetime(System.currentTimeMillis());
		info.setInfoType(PrivacyPhoneInfo.TYPE_OUTGOING);
		return info;
	}
	
	
	@SuppressWarnings("unused")
	private static String queryDisplayName(String number) {
		ContentResolver cr = PAApplication.getAppContext().getContentResolver();
		Cursor cursor = null;
		try {
			String[] projection = { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
					 ContactsContract.CommonDataKinds.Phone.NUMBER };
			cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					projection,
					ContactsContract.CommonDataKinds.Phone.NUMBER + " like ? ", 
					new String[] { number }, null);
			if (cursor.moveToNext()) {
				// 获取联系人名字
				int nameFieldColumnIndex = cursor.getColumnIndex(
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
				String contact = cursor.getString(nameFieldColumnIndex);
				return contact;
			}
		} catch (Exception e) {
			LogUtils.e(TAG, "queryDisplayName Exception: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}
		return null;
	}
	
	public static String queryDisplayName2(String number) {
		String ret = null;
		ContentResolver cr = PAApplication.getAppContext().getContentResolver();
		Cursor cursor = null;
		try {
//			String[] projection = null;
			String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
					 ContactsContract.PhoneLookup.NUMBER };
			Uri uriQuery = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(number));
			cursor = cr.query(uriQuery, projection, null, null, null);
			while (cursor.moveToNext()) {
				// 获取联系人名字
				int nameFieldColumnIndex = cursor.getColumnIndex(
						ContactsContract.PhoneLookup.DISPLAY_NAME);
				String contact = cursor.getString(nameFieldColumnIndex);
				if (!TextUtils.isEmpty(contact)) {
					if (!TextUtils.isEmpty(ret)) {
						ret += " / ";
					}
					ret += contact;
				}
//				int count = cursor.getColumnCount();
//				for (int i = 0; i < count; i++) {
//					String colName = cursor.getColumnName(i);
//					String val = cursor.getString(i);
//					LogUtils.d(TAG, "queryDisplayName2 colName = " + colName
//							+ ", val = " + val);
//				}
			}
		} catch (Exception e) {
			LogUtils.e(TAG, "queryDisplayName2 Exception: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}
		return ret;
	}
}
