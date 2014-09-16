package com.zhaoni.findyou.android.monitoring.service;

import java.util.Map;

import org.ixming.android.location.baidu.LocationInfo;
import org.ixming.android.location.baidu.LocationModule;
import org.ixming.android.location.baidu.OnLocationLoadListener;
import org.ixming.base.utils.android.LogUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents;
import com.zhaoni.findyou.android.common.control.LocationController;
import com.zhaoni.findyou.android.common.model.AppSharedUtils;
import com.zhaoni.findyou.android.common.model.DummyValueResponseData;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;

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
		LogUtils.d("yytest", "alarm");
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
	
	/**
	 * 第一次需要默认给用户看到数据
	 */
	public static void firstInterval() {
		if (!AppSharedUtils.getLocationFirstInterval()) {
			new FirstInterval().goon();
		}
	}
	
	private static class FirstInterval implements OnLocationLoadListener {
		private final String TAG = FirstInterval.class.getSimpleName();
		private LocationModule mLocationModule;
		private long mSuccessCount;
		private final long TARGET_SUCCESS_COUNT = 3;
		public FirstInterval() {
			mLocationModule = new LocationModule(PAApplication.getAppContext(), true);
		}
		
		public void goon() {
			mLocationModule.requestLocation(this);
		}
		
		@Override
		public void onLocationLoad(LocationInfo locationInfo) {
			execute(locationInfo);
		}

		@Override
		public void onLocationFailed(String errorTip) {
			LogUtils.w(TAG, "FirstInterval errorTip = " + errorTip);
			goon();
		}

		private void execute(LocationInfo locationInfo) {
			LogUtils.w(TAG, "FirstInterval :: execute " + locationInfo);
			
			AQuery aQuery = new AQuery(PAApplication.getAppContext());
			
			SimpleAjaxCallback<DummyValueResponseData> callback = new SimpleAjaxCallback<DummyValueResponseData>(true) {
				
				@Override
				protected boolean onSuccess(String url, Object object,
						AjaxStatus status) {
					mSuccessCount ++;
					if (mSuccessCount >= TARGET_SUCCESS_COUNT) {
						if (null != mLocationModule) {
							mLocationModule.stopLocation();
						}
						AppSharedUtils.saveLocationFirstInterval(true);
					} else {
						goon();
					}
					LogUtils.w(TAG, "FirstInterval :: onSuccess mSuccessCount = " + mSuccessCount);
					return super.onSuccess(url, object, status);
				}
				
				@Override
				protected boolean onError(AjaxStatus status) {
					goon();
					return super.onError(status);
				}
			};
			callback.logTag(TAG);
			
//			device_id		String（不可为空）
//			device_token	String（不可为空）
//			longitude		String（不可为空）
//			latitude		String（不可为空）
//			address			String（不可为空）
			Map<String, String> data = RequestPiecer.getBasicData();
			data.put("latitude", String.valueOf(locationInfo.getLatitude()));
			data.put("longitude", String.valueOf(locationInfo.getLongitude()));
			data.put("address", locationInfo.getAddress());
			aQuery.ajax(Config.URL_POST_LOCATION, data, DummyValueResponseData.class, callback);
		}
	}
}
