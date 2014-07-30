package com.ixming.privacy.android.monitoring.service;

import com.ixming.privacy.monitor.android.PAApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

public class TriggerActionsReceiver extends BroadcastReceiver {

	private final String TAG = TriggerActionsReceiver.class.getSimpleName();
	private static TriggerActionsReceiver sActionsReceiver;
	public static final void registerMe() {
		if (null == sActionsReceiver) {
			sActionsReceiver = new TriggerActionsReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SHUTDOWN);
			PAApplication.getAppContext().registerReceiver(sActionsReceiver, filter);
			
			
			filter = new IntentFilter();
			filter.addAction(Intent.ACTION_USER_PRESENT);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			PAApplication.getAppContext().registerReceiver(sActionsReceiver, filter);
			
			
			filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			PAApplication.getAppContext().registerReceiver(sActionsReceiver, filter);
			
			
			filter = new IntentFilter();
			filter.addAction(Intent.ACTION_DATE_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
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
		Log.d(TAG, "onReceive action = " + action);
		
		MainService.startMe(TAG);
	}

}
