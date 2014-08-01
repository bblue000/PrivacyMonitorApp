package com.ixming.privacy.android.monitoring.service;

import org.apache.http.HttpStatus;
import org.ixming.android.location.baidu.LocationInfo;
import org.ixming.android.location.baidu.OnLocationLoadListener;
import org.ixming.base.utils.android.LogUtils;

import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.model.BasicAjaxCallback;
import com.ixming.privacy.android.common.model.RequestPiecer;
import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;
import com.ixming.privacy.android.monitoring.model.PrivacyInfoFactory;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

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
		PrivacyLocaitonInfo info = PrivacyInfoFactory.createPrivacyLocaitonInfo(locationInfo);
		Log.d(TAG, "HandlerLocationInfoTask :: execute " + info);
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
		
		BasicAjaxCallback<String> callback = new BasicAjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				LogUtils.d(TAG, "callback code ---> " + status.getCode());
				LogUtils.d(TAG, "callback msg ---> " + status.getMessage());
				LogUtils.d(TAG, "callback result ---> " + object);
				if (status.getCode() == HttpStatus.SC_OK) {
					// do nothing
					Log.d(TAG, "HandlerLocationInfoTask :: execute onSuccess");
				} else {
					Log.w(TAG, "HandlerLocationInfoTask :: execute onError");
//					dbManager.insertList(localData);
				}
			}
		};
		
		aQuery.ajax(Config.URL_POST_LOCATION, RequestPiecer.postLocationJson(info), String.class, callback);
	}
	
}
