package com.ixming.privacy.android.common.control;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.controller.BaseController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.common.LocalBroadcastIntents.MonitorLocation;
import com.ixming.privacy.android.common.model.AppSharedUtils;
import com.ixming.privacy.android.monitoring.service.MainService;

/**
 * 
 * 定位相关的控制类
 * 
 * @author Yin Yong
 *
 */
public class LocationController extends BaseController {
	
	private static LocationController sInsController = new LocationController();
	public static LocationController getInstance() {
		return sInsController;
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (LocalBroadcastIntents.MonitorLocation.ACTION_SETTING_OPEN.equals(action)) {
				MainService.startMe("LocationController");
			} else if (LocalBroadcastIntents.MonitorLocation.ACTION_SETTING_CLOSE.equals(action)) {
				MainService.stopMe("LocationController");
			}
		}
	};
	
	private boolean mLocationSetting;
	private LocationController() {
		LocalBroadcasts.registerLocalReceiver(mReceiver,
				MonitorLocation.ACTION_SETTING_OPEN,
				MonitorLocation.ACTION_SETTING_CLOSE);
		obtainLocalValue();
	}
	
	public void checkLocationSetting() {
		if (getLocationSetting()) {
			LocalBroadcasts.sendLocalBroadcast(MonitorLocation.ACTION_SETTING_OPEN);
		} else {
			LocalBroadcasts.sendLocalBroadcast(MonitorLocation.ACTION_SETTING_CLOSE);
		}
	}
	
	public boolean getLocationSetting() {
		return mLocationSetting;
	}
	
	public void setCurrentSetting(boolean value) {
		AppSharedUtils.saveLocationSetting(value);
		mLocationSetting = value;
		
		if (mLocationSetting) {
			LocalBroadcasts.sendLocalBroadcast(MonitorLocation.ACTION_SETTING_OPEN);
		} else {
			LocalBroadcasts.sendLocalBroadcast(MonitorLocation.ACTION_SETTING_CLOSE);
		}
	}
	
	private void obtainLocalValue() {
		mLocationSetting = AppSharedUtils.getLocationSetting();
	}
}
