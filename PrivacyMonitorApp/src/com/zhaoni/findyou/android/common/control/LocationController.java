package com.zhaoni.findyou.android.common.control;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.controller.BaseController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhaoni.findyou.android.common.LocalBroadcastIntents;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents.MonitorLocation;
import com.zhaoni.findyou.android.common.model.AppSharedUtils;
import com.zhaoni.findyou.android.monitoring.service.MainService;

/**
 * 
 * 定位相关的控制类
 * 
 * @author Yin Yong
 *
 */
public class LocationController extends BaseController {
	
	public static final long[] INTERVALS = {
			2 * 60 * 1000L, // 2分钟
			5 * 60 * 1000L,	// 5分钟
			15 * 60 * 1000L, // 15分钟
			30 * 60 * 1000L, // 30分钟
			60 * 60 * 1000L // 1小时
	};
	public static final long DEFAULT_INTERVAL = INTERVALS[0];
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
	private long mLocationInterval;
	private LocationController() {
		LocalBroadcasts.registerLocalReceiver(mReceiver,
				MonitorLocation.ACTION_SETTING_OPEN,
				MonitorLocation.ACTION_SETTING_CLOSE);
		obtainLocalValue();
		
		// TODO service may not be started
//		checkLocationSetting();
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
	
	public void setLocationInterval(long value) {
		AppSharedUtils.saveLocationInterval(value);
		mLocationInterval = value;
	}

	public long getLocationInterval() {
		return mLocationInterval;
	}
	
	private void obtainLocalValue() {
		mLocationSetting = AppSharedUtils.getLocationSetting();
		mLocationInterval = AppSharedUtils.getLocationInterval(DEFAULT_INTERVAL);
	}
}
