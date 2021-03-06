package com.zhaoni.findyou.android.monitoring.service;

import org.ixming.base.utils.android.LogUtils;

import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class TriggerActionsReceiver extends BroadcastReceiver {

	private final String TAG = TriggerActionsReceiver.class.getSimpleName();
	private static TriggerActionsReceiver sActionsReceiver;
	public static final void registerMe() {
		if (null == sActionsReceiver) {
			Context context = PAApplication.getAppContext();
			
			sActionsReceiver = new TriggerActionsReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SHUTDOWN);
			context.registerReceiver(sActionsReceiver, filter);
			
			
			filter = new IntentFilter();
			filter.addAction(Intent.ACTION_USER_PRESENT);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			context.registerReceiver(sActionsReceiver, filter);
			
			
			filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			context.registerReceiver(sActionsReceiver, filter);
			
			
			filter = new IntentFilter();
			filter.addAction(Intent.ACTION_DATE_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			context.registerReceiver(sActionsReceiver, filter);
			
			filter = new IntentFilter();
			filter.addAction(LocalBroadcastIntents.Location.ACTION_LOCATION_ALARM);
			context.registerReceiver(sActionsReceiver, filter);
			
			filter = new IntentFilter();
			filter.addAction(LocalBroadcastIntents.DeviceToken.ACTION_DEVICE_TOKEN_LOADED);
			filter.addAction(LocalBroadcastIntents.DeviceToken.ACTION_DEVICE_TOKEN_FAILED);
			context.registerReceiver(sActionsReceiver, filter);
		}
	}
	
	public static final void unregisterMe() {
		if (null == sActionsReceiver) {
			return ;
		}
		try {
			PAApplication.getAppContext().unregisterReceiver(sActionsReceiver);
		} catch (Exception e) { }
		
		sActionsReceiver = null;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogUtils.d(TAG, "onReceive action = " + action);
		if (LocalBroadcastIntents.Location.ACTION_LOCATION_ALARM.equals(action)) {
			Alarm.alarm();
		} else if (LocalBroadcastIntents.DeviceToken.ACTION_DEVICE_TOKEN_LOADED.equals(action)) {
			Alarm.restart();
		} else if (LocalBroadcastIntents.DeviceToken.ACTION_DEVICE_TOKEN_FAILED.equals(action)) {
			Alarm.stopAlarm();
		} else {
			MainService.startMe(TAG);
		}
	}

}
