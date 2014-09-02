package com.zhaoni.findyou.android.main.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ixming.base.common.controller.BaseController;
import org.ixming.base.utils.android.AndroidUtils;
import org.ixming.base.utils.android.LogUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;
import com.zhaoni.findyou.android.main.model.MonitoredPerson;
import com.zhaoni.findyou.android.main.model.RespLocation;
import com.zhaoni.findyou.android.main.model.entity.RespLocationResult;

public class PersonController extends BaseController {
	
	private static final String TAG = PersonController.class.getSimpleName();
	
	public static interface LocationDataLoadListener {
		void onLocationLoadSuccess();

		void onLocationLoadFailed();
	}
	
	private final long ONEDAY = 24 * 60 * 60 * 1000;
	private final List<RespLocation> mLocationInfoList = new ArrayList<RespLocation>(50);
	private final Map<Long, List<RespLocation>> mDateTimeData = new LinkedHashMap<Long, List<RespLocation>>(5);
	private long mCurTime;
	private List<RespLocation> mCurData = null; 
	
	private AjaxCallbackImpl mAjaxCallback;
	
	private MonitoredPerson	mMonitoredPerson;
	protected PersonController(MonitoredPerson person) {
		mMonitoredPerson = person;
		// for testing
		if (PersonListController.TEST) {
			calculate();
		}
	}
	
	public void requestLocationData(LocationDataLoadListener listener) {
		if (PersonListController.TEST) {
			return ;
		}
		
		if (null != mAjaxCallback) {
			mAjaxCallback.setLoactionDataLoadListener(null);
		}
		
		mAjaxCallback = new AjaxCallbackImpl();
		mAjaxCallback.setLoactionDataLoadListener(listener);
		AQuery aQuery = new AQuery(PAApplication.getAppContext());
		aQuery.ajax(String.format(Config.URL_GET_LOCATION,
					mMonitoredPerson.getDevice_token(),
					RequestPiecer.getUserName(),
					AndroidUtils.getDeviceId()),
				RespLocationResult.class, mAjaxCallback);
	}
	
	public MonitoredPerson getMonitoringPerson() {
		return mMonitoredPerson;
	}

	public Collection<Long> getLocationDates() {
		return mDateTimeData.keySet();
	}
	
	public void setCurTime(long datetime) {
		mCurTime = (datetime / ONEDAY) * ONEDAY; // clear time, second, mills
		mCurData = mDateTimeData.get(mCurTime);
	}
	
	public long getCurTime() {
		return mCurTime;
	}
	
	public List<RespLocation> getCurData() {
		return mCurData;
	}
	
	private void calculate() {
		mDateTimeData.clear();
		if (mLocationInfoList.isEmpty()) {
			if (PersonListController.TEST) {
				mDateTimeData.put(new Date().getTime(), null);
				mDateTimeData.put(new Date().getTime(), null);
			}
			return ;
		}
		long lastDay = 0;
		long curDay = 0;
		List<RespLocation> curList = null;
		for (int i = 0; i < mLocationInfoList.size(); i++) {
			RespLocation info = mLocationInfoList.get(i);
			long datetime = info.getDate_time();
			curDay = datetime / ONEDAY;
			if (lastDay != curDay) {
				lastDay = curDay;
				mDateTimeData.put(curDay * ONEDAY, curList = new ArrayList<RespLocation>());
			} else {
				curList.add(info);
			}
		}
		LogUtils.d(TAG, "calculate mDateTimeData.size() = " + mDateTimeData.size());
	}
	
	private void setLocationData(List<RespLocation> locationList) {
		mLocationInfoList.clear();
		
		if (null != locationList) {
			mLocationInfoList.addAll(locationList);
			Collections.sort(mLocationInfoList);
		}
		
		calculate();
	}
	
	private class AjaxCallbackImpl extends SimpleAjaxCallback<RespLocationResult> {
		
		private LocationDataLoadListener mLoactionDataLoadListener;
		public AjaxCallbackImpl() {
			logTag(TAG);
		}
		public void setLoactionDataLoadListener(LocationDataLoadListener loactionDataLoadListener) {
			mLoactionDataLoadListener = loactionDataLoadListener;
			token(loactionDataLoadListener);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected boolean onSuccess(String url, Object object, AjaxStatus status) {
			if (null != mLoactionDataLoadListener) {
				setLocationData((List<RespLocation> ) object);
				mLoactionDataLoadListener.onLocationLoadSuccess();
			}
			return true;
		}
		
		@Override
		protected boolean onError(AjaxStatus status) {
			if (null != mLoactionDataLoadListener) {
				mLoactionDataLoadListener.onLocationLoadFailed();
			}
			return true;
		}
		
	}

}
