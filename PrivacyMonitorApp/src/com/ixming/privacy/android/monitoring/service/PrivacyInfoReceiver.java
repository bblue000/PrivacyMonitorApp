package com.ixming.privacy.android.monitoring.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ixming.privacy.monitor.android.PAApplication;

public class PrivacyInfoReceiver extends BroadcastReceiver {

	private static final String TAG = PrivacyInfoReceiver.class.getSimpleName();
	
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String ACTION_WAP_PUSH_RECEIVED = "android.provider.Telephony.WAP_PUSH_RECEIVED";
	private static PrivacyInfoReceiver sActionsReceiver;
	public static final void registerMe() {
		if (null == sActionsReceiver) {
			sActionsReceiver = new PrivacyInfoReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
			filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
			filter.addAction(ACTION_SMS_RECEIVED);
			filter.addAction(ACTION_WAP_PUSH_RECEIVED);
			PAApplication.getAppContext().registerReceiver(sActionsReceiver, filter);
		}
	}
	
	public static final void unregisterMe() {
		try {
			PAApplication.getAppContext().unregisterReceiver(sActionsReceiver);
		} catch (Exception e) { }
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i(TAG, "onReceive action = " + action);
		if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
			//TODO 来电状态改变
			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
				Log.d(TAG, "onReceive phone state = ring ");
				String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				Log.d(TAG, "onReceive incoming phoneNumber = " + phoneNumber);
				onIncomingPhone(phoneNumber);
			} else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
				Log.d(TAG, "onReceive phone state = offhook ");
			}
		} else if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
			//TODO 拨打电话
			String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Log.d(TAG, "onReceive phoneNumber = " + phoneNumber);
			onOutgoingPhone(phoneNumber);
		} else {
			
		}
	}

	private void onIncomingPhone(String number) {
//		PrivacyPhoneInfo info = PrivacyInfoFactory.createIncomingPhone(number);;
//		LogUtils.d(TAG, "onIncomingPhone " + info);
//		PrivacyPhoneInfoDBManager.getInstance().insert(info);
	}
	
	private void onOutgoingPhone(String number) {
//		PrivacyPhoneInfo info = PrivacyInfoFactory.createOutgoingPhone(number);
//		LogUtils.d(TAG, "onOutgoingPhone " + info);
//		PrivacyPhoneInfoDBManager.getInstance().insert(info);
	}
}
