package com.zhaoni.findyou.android.splash.control;


import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.controller.BaseController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhaoni.findyou.android.common.LocalBroadcastIntents;
import com.zhaoni.findyou.android.common.control.BindController;
import com.zhaoni.findyou.android.monitoring.service.MainService;

/**
 * 处理启动/全局的数据/状态
 * 
 * @author Yin Yong
 *
 */
public class SplashController_ForTest extends BaseController {

	private static SplashController_ForTest sInstance;

	public synchronized static SplashController_ForTest getInstance() {
		if (null == sInstance) {
			sInstance = new SplashController_ForTest();
		}
		return sInstance;
	}

	private SplashController_ForTest() {
		 LocalBroadcasts.registerLocalReceiver(mReceiver,
				 LocalBroadcastIntents.MonitorLocation.ACTION_SETTING_OPEN,
				 LocalBroadcastIntents.MonitorLocation.ACTION_SETTING_CLOSE);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (LocalBroadcastIntents.MonitorLocation.ACTION_SETTING_OPEN.equals(action)) {
				MainService.startMe("SplashController");
			} else if (LocalBroadcastIntents.MonitorLocation.ACTION_SETTING_CLOSE.equals(action)) {
				MainService.stopMe("SplashController");
			}
			
		}
	};
	
	public void checkDeviceToken() {
		if (BindController.getInstance().hasDeviceToken()) {
			
		} else {
			
		}
	}

}
