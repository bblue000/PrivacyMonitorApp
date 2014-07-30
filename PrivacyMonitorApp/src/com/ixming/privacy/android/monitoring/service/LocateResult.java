package com.ixming.privacy.android.monitoring.service;

import java.util.List;

import org.ixming.android.location.baidu.LocationInfo;
import org.ixming.android.location.baidu.OnLocationLoadListener;
import org.ixming.base.network.HttpClientUtil;
import org.ixming.base.taskcenter.async.TaskHandler;
import org.ixming.base.taskcenter.callback.OnLoadListener;
import org.ixming.base.taskcenter.entity.ReqBean;

import android.util.Log;

import com.ixming.privacy.android.monitoring.db.manager.PrivacyLocationInfoDBManager;
import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;
import com.ixming.privacy.android.monitoring.model.PrivacyInfoFactory;
import com.ixming.privacy.android.monitoring.model.RequestPiecer;
import com.ixming.privacy.monitor.android.Config;

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
		
		new HandlerLocationInfoTask(locationInfo).execute();
		
		// upload
//		LocalBroadcasts.sendLocalBroadcast(LocalBroadcasts.ACTION_UPDATE_LOCATION);
	}

	@Override
	public void onLocationFailed(String errorTip) {
		Log.w(TAG, "onLocationFailed errorTip = " + errorTip);
	}

	private class HandlerLocationInfoTask {

		private LocationInfo mLocationInfo;
		public HandlerLocationInfoTask(LocationInfo locationInfo) {
			mLocationInfo = locationInfo;
		}
		
		public void execute() {
			final List<PrivacyLocaitonInfo> localData;
			final PrivacyLocationInfoDBManager dbManager = PrivacyLocationInfoDBManager.getInstance();
			// lock all next operations
			synchronized (dbManager) {
				PrivacyLocaitonInfo info = PrivacyInfoFactory.createPrivacyLocaitonInfo(mLocationInfo);
				Log.d(TAG, "HandlerLocationInfoTask :: execute " + info);
				// save into local db
				dbManager.insert(info);
				
				localData = dbManager.findAll();
				dbManager.deleteAll(); // clear now, if failed after this, rollback
			}
			
			TaskHandler.execHttpReqTask(Config.URL_POST_LOCATION, Config.MODE_POST_LOCATION,
					RequestPiecer.getLocationJson(localData),
					new OnLoadListener() {
						
						@Override
						public void onSuccess(Object obj, ReqBean reqMode) {
							// do nothing
							Log.d(TAG, "HandlerLocationInfoTask :: execute onSuccess");
						}
						
						@Override
						public void onError(Object obj, ReqBean reqMode) {
							Log.w(TAG, "HandlerLocationInfoTask :: execute onError");
							dbManager.insertList(localData);
						}
						
					}, HttpClientUtil.POST);
		}
		
	}
	
}
