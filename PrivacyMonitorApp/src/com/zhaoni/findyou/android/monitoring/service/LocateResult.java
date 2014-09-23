package com.zhaoni.findyou.android.monitoring.service;

import java.util.Map;

import org.ixming.android.location.baidu.LocationInfo;
import org.ixming.android.location.baidu.LocationModule;
import org.ixming.android.location.baidu.OnLocationLoadListener;
import org.ixming.base.utils.android.LogUtils;

import com.androidquery.AQuery;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.model.DummyValueResponseData;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;

public class LocateResult implements OnLocationLoadListener {

	private final String TAG = LocateResult.class.getSimpleName();
	private LocationModule mLocationModule;
	public LocateResult() {
	}
	
	public void setLocationModule(LocationModule locationModule) {
		mLocationModule = locationModule;
	}
	
	@Override
	public void onLocationLoad(LocationInfo locationInfo) {
		LogUtils.d(TAG, "alarm onLocationLoad");
		if (null != mLocationModule) {
			mLocationModule.stopLocation();
			mLocationModule = null;
		}
		LogUtils.d(TAG, "------------------");
		LogUtils.d(TAG, "onLocationLoad getAddress = " + locationInfo.getAddress());
		LogUtils.d(TAG, "onLocationLoad getProvince = " + locationInfo.getProvince());
		LogUtils.d(TAG, "onLocationLoad getCity = " + locationInfo.getCity());
		LogUtils.d(TAG, "onLocationLoad getDistrict = " + locationInfo.getDistrict());
		LogUtils.d(TAG, "onLocationLoad getStreet = " + locationInfo.getStreet());
		LogUtils.d(TAG, "------------------");
		execute(locationInfo);
		// upload
//		LocalBroadcasts.sendLocalBroadcast(LocalBroadcasts.ACTION_UPDATE_LOCATION);
	}

	@Override
	public void onLocationFailed(String errorTip) {
		LogUtils.d(TAG, "alarm onLocationFailed");
		if (null != mLocationModule) {
			mLocationModule.stopLocation();
			mLocationModule = null;
		}
		LogUtils.w(TAG, "onLocationFailed errorTip = " + errorTip);
	}

	private void execute(LocationInfo locationInfo) {
		LogUtils.d("yytest", "alarm :: execute " + locationInfo);
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
		
		SimpleAjaxCallback<DummyValueResponseData> callback = new SimpleAjaxCallback<DummyValueResponseData>(true);
		callback.logTag("yytest");
		
//		device_id		String（不可为空）
//		device_token	String（不可为空）
//		longitude		String（不可为空）
//		latitude		String（不可为空）
//		address			String（不可为空）
		Map<String, String> data = RequestPiecer.getBasicData();
		data.put("latitude", String.valueOf(locationInfo.getLatitude()));
		data.put("longitude", String.valueOf(locationInfo.getLongitude()));
		data.put("address", locationInfo.getAddress());
		data.put("source_flag", "1");
		aQuery.ajax(Config.URL_POST_LOCATION, data, DummyValueResponseData.class, callback);
	}
}
