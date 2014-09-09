package com.zhaoni.findyou.android.monitoring.service;

import org.ixming.android.location.baidu.LocationModule;

import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents;
import com.zhaoni.findyou.android.common.control.LocationController;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class Alarm {

	private static AlarmManager sAlarmManager; 
	private static PendingIntent sLastestSender; 
	private static Intent sActionIntent;
	
	private Alarm() { }
	
	public static void alarm() {
		TriggerActionsReceiver.registerMe();
		
		if (null == sAlarmManager) {
			sAlarmManager = (AlarmManager) PAApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
		}
		
		if (null != sLastestSender) {
			sAlarmManager.cancel(sLastestSender);
		}
		
		// do
		LocationModule locationModule = new LocationModule(PAApplication.getAppContext(), true);
		LocateResult locateResult = new LocateResult();
		locateResult.setLocationModule(locationModule);
		locationModule.requestLocation(locateResult);
		
		// set next
		if (null == sActionIntent) {
			sActionIntent = new Intent(PAApplication.getAppContext(), TriggerActionsReceiver.class);  
			sActionIntent.setAction(LocalBroadcastIntents.Location.ACTION_LOCATION_ALARM);  
		}
	    sLastestSender = PendingIntent.getBroadcast(PAApplication.getAppContext(), 0, sActionIntent, 0); 
		sAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + LocationController.getInstance().getLocationIntervalRT(), 
//				5000,
				sLastestSender);
	}
	
	public static void stopAlarm() {
		if (null != sAlarmManager && null != sLastestSender) {
			sAlarmManager.cancel(sLastestSender);
		}
		
		sAlarmManager = null;
		sLastestSender = null;
	}
}
