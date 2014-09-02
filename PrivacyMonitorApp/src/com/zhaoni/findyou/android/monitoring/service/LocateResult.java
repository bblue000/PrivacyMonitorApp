package com.zhaoni.findyou.android.monitoring.service;

import java.util.Map;

import org.ixming.android.location.baidu.LocationInfo;
import org.ixming.android.location.baidu.OnLocationLoadListener;

import android.util.Log;

import com.androidquery.AQuery;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;
import com.zhaoni.findyou.android.common.model.StringValueResponseData;

public class LocateResult implements OnLocationLoadListener {

	private final String TAG = LocateResult.class.getSimpleName();
	
	@Override
	public void onLocationLoad(LocationInfo locationInfo) {
		Log.d(TAG, "------------------");
		Log.d(TAG, "onLocationLoad getAddress = " + locationInfo.getAddress());
		Log.d(TAG, "onLocationLoad getProvince = " + locationInfo.getProvince());
		Log.d(TAG, "onLocationLoad getCity = " + locationInfo.getCity());
		Log.d(TAG, "onLocationLoad getDistrict = " + locationInfo.getDistrict());
		Log.d(TAG, "onLocationLoad getStreet = " + locationInfo.getStreet());
		Log.d(TAG, "------------------");
		execute(locationInfo);
		// upload
//		LocalBroadcasts.sendLocalBroadcast(LocalBroadcasts.ACTION_UPDATE_LOCATION);
	}

	@Override
	public void onLocationFailed(String errorTip) {
		Log.w(TAG, "onLocationFailed errorTip = " + errorTip);
	}

	private void execute(LocationInfo locationInfo) {
		Log.d(TAG, "HandlerLocationInfoTask :: execute " + locationInfo);
//		final List<PrivacyLocaitonInfo> localData;
//		final PrivacyLocationInfoDBManager dbManager = PrivacyLocationInfoDBManager.getInstance();
//		// lock all next operations
//		synchronized (dbManager) {
//			// save into local db
//			dbManager.insert(info);
//			
//			localData = dbManager.findAll();
//			dbManager.deleteAll(); // clear now, if failed after this, rollback
//		}
		
		AQuery aQuery = new AQuery(PAApplication.getAppContext());
		
		SimpleAjaxCallback<StringValueResponseData> callback = new SimpleAjaxCallback<StringValueResponseData>(true);
		callback.logTag(TAG);
		
//		device_id		String（不可为空）
//		device_token	String（不可为空）
//		longitude		String（不可为空）
//		latitude		String（不可为空）
//		address			String（不可为空）
		Map<String, String> data = RequestPiecer.getBasicData();
		data.put("latitude", String.valueOf(locationInfo.getLatitude()));
		data.put("longitude", String.valueOf(locationInfo.getLongitude()));
		data.put("address", locationInfo.getAddress());
		aQuery.ajax(Config.URL_POST_LOCATION, data, StringValueResponseData.class, callback);
	}
}
