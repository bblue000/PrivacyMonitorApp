package com.ixming.privacy.android.main.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.ixming.base.common.controller.BaseController;
import org.ixming.base.utils.android.AndroidUtils;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.main.model.MonitoredPerson;
import com.ixming.privacy.android.main.model.RespLocation;
import com.ixming.privacy.android.main.model.entity.RespLocationResult;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class PersonController extends BaseController {
	
	private static final String TAG = PersonController.class.getSimpleName();
	
	public static interface LoactionDataLoadListener {
		void onLocationLoadSuccess();

		void onLocationLoadFailed();
	}
	
	private final long ONEDAY = 24 * 60 * 60 * 1000;
	private final List<RespLocation> mLocationInfoList = new ArrayList<RespLocation>(10);
	private final Map<Long, List<RespLocation>> mDateTimeData = new HashMap<Long, List<RespLocation>>();
	private long mCurTime;
	private List<RespLocation> mCurData = null; 
	
	private AjaxCallbackImpl mAjaxCallback;
	
	private MonitoredPerson	mMonitoredPerson;
	protected PersonController(MonitoredPerson person) {
		mMonitoredPerson = person;
	}
	
	public void requestLoactionData(LoactionDataLoadListener listener) {
		if (null != mAjaxCallback) {
			mAjaxCallback.setLoactionDataLoadListener(null);
		}
		
		mAjaxCallback = new AjaxCallbackImpl();
		mAjaxCallback.setLoactionDataLoadListener(listener);
		AQuery aQuery = new AQuery(PAApplication.getAppContext());
		aQuery.ajax(String.format(Config.URL_GET_LOCATION,
					mMonitoredPerson.getDevice_token(),
					mMonitoredPerson.getNote_name(),
					AndroidUtils.getDeviceId()),
				RespLocationResult.class, mAjaxCallback);
	}
	
	private void calculate() {
		mDateTimeData.clear();
		if (mLocationInfoList.isEmpty()) {
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
	
	private void setLocationData(List<RespLocation> locationList) {
		mLocationInfoList.clear();
		
		if (null != locationList) {
			mLocationInfoList.addAll(locationList);
		}
		
		calculate();
	}
	
	private class AjaxCallbackImpl extends AjaxCallback<RespLocationResult> {
		
		private LoactionDataLoadListener mLoactionDataLoadListener;
		public void setLoactionDataLoadListener(LoactionDataLoadListener loactionDataLoadListener) {
			mLoactionDataLoadListener = loactionDataLoadListener;
		}
		
		@Override
		public void callback(String url, RespLocationResult result, AjaxStatus status) {
			LogUtils.d(TAG, "callback code ---> " + status.getCode());
			LogUtils.d(TAG, "callback msg ---> " + status.getMessage());
			LogUtils.d(TAG, "callback result ---> " + result);
			if (null != mLoactionDataLoadListener) {
				if (status.getCode() == HttpStatus.SC_OK) {
					try {
						if (result.isOK()) {
							setLocationData(result.getValue());
							mLoactionDataLoadListener.onLocationLoadSuccess();
						} else {
							ToastUtils.showToast(result.getMsg());
							mLoactionDataLoadListener.onLocationLoadFailed();
						}
					} catch (Exception e) {
						e.printStackTrace();
						ToastUtils.showToast(R.string.persion_location_data_obtain_error);
						mLoactionDataLoadListener.onLocationLoadFailed();
					}
					
				} else {
					ToastUtils.showToast(status.getMessage());
					mLoactionDataLoadListener.onLocationLoadFailed();
				}
			}
			status.close();
		}
	}

}
