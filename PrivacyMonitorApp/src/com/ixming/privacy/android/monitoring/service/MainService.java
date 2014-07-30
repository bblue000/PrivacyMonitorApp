package com.ixming.privacy.android.monitoring.service;

import org.ixming.android.location.baidu.LocationModule;
import org.ixming.base.utils.android.LogUtils;

import com.ixming.privacy.monitor.android.PAApplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class MainService extends Service {

	private static final String TAG = MainService.class.getSimpleName();
	
	private LocationModule mLocationModule;
	private LocateResult mLocateResult;
	
	private static MainService sMainService;
	public synchronized static boolean existsMe() {
		return null != sMainService;
	}
	
	public synchronized static void startMe(String tag) {
		LogUtils.d(TAG, "onCreate tag = " + tag + ", process = " + Process.myPid());
		if (!MainService.existsMe()) {
			PAApplication.getAppContext().startService(new Intent(
					PAApplication.getAppContext(),
					MainService.class));
			
		}
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		synchronized (MainService.class) {
			sMainService = this;	
		}
		mLocationModule = new LocationModule(this, true);
		mLocateResult = new LocateResult();
		mLocationModule.startContinuousLocation(mLocateResult);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart startId = " + startId);
//		PrivacyInfoReceiver.registerMe();
		TriggerActionsReceiver.registerMe();
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		if (null != mLocationModule)
			mLocationModule.stopLocation();
		mLocationModule = null;
		
//		PrivacyInfoReceiver.unregisterMe();
		TriggerActionsReceiver.unregisterMe();
		
		synchronized (MainService.class) {
			sMainService = null;	
		}
		
		// restart
		startMe(TAG);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
