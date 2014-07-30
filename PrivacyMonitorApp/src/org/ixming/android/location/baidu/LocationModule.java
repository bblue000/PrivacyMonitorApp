package org.ixming.android.location.baidu;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationModule {
	
	private static final String TAG = LocationModule.class.getSimpleName();
	
	private final int INTERVAL = 2 * 60 * 1000; // 5分钟一次
	private final long TIMEOUT = 10 * 1000;
	private final Handler mHandler;
	private final TimeoutRunnable mTimeoutRunnable = new TimeoutRunnable();
	private final LocationClient mLocationClient;
	private final LocationClientOption mLocationClientOption;
	private final MyLocationListener mBDLocationListener;
	
	/**
	 * 需要在主线程
	 */
	public LocationModule(Context context) { 
		this(context, true);
	}
	
	public LocationModule(Context context, boolean openGps) { 
		mHandler = new Handler();
		
		mLocationClientOption = new LocationClientOption();
		mLocationClientOption.setLocationMode(LocationMode.Hight_Accuracy);
//		mLocationClientOption.setLocationMode(LocationMode.Battery_Saving);
		mLocationClientOption.setCoorType("gcj02");
		mLocationClientOption.setNeedDeviceDirect(true);
		mLocationClientOption.setIsNeedAddress(true);
		// 不使用GPS定位，因为通知栏有图标，太过明显
		mLocationClientOption.setOpenGps(openGps);
		
		mLocationClient = new LocationClient(context);
		mLocationClient.setLocOption(mLocationClientOption);
		
		mBDLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mBDLocationListener);
	}
	
	public void startContinuousLocation(final OnLocationLoadListener listener) {
		mLocationClientOption.setScanSpan(INTERVAL);
		
		mBDLocationListener.setOnLocationLoadListener(listener);
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		mLocationClient.requestLocation();
	}
	
	public void requestLocation(final OnLocationLoadListener listener) {
		mHandler.postDelayed(mTimeoutRunnable
				.setOnLocationLoadListener(listener), TIMEOUT);
		
		mLocationClientOption.setScanSpan(0);
		
		mBDLocationListener.setOnLocationLoadListener(listener);
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		mLocationClient.requestLocation();
	}
	
	public void stopLocation() {
		stopLocationClient(mLocationClient);
		
		mBDLocationListener.setOnLocationLoadListener(null);
	}
	
	protected void preventRTimeoutRunnable() {
		mHandler.removeCallbacks(mTimeoutRunnable.reset());
	}
	
	private void stopLocationClient(LocationClient client) {
		if (null != client) {
			if (client.isStarted()) {
				client.stop();
			}
		}
	}
	
	private class MyLocationListener implements BDLocationListener {
		private OnLocationLoadListener mOnLocationLoadListener;
		public MyLocationListener setOnLocationLoadListener(OnLocationLoadListener listener) {
			synchronized (mBDLocationListener) {
				mOnLocationLoadListener = listener;
			}
			return this;
		}
		
		@Override
		public void onReceivePoi(BDLocation location) { }
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null == mOnLocationLoadListener) {
				return;
			}
			synchronized (mBDLocationListener) {
				boolean isHandlered = false;
				try {
					if (null == location) {
						mOnLocationLoadListener.onLocationFailed(mErrorMessages.get(ERROR_COMMON));
						isHandlered = true;
					} else {
						String city = location.getCity();
						if (TextUtils.isEmpty(city)) {
							mOnLocationLoadListener.onLocationFailed(mErrorMessages.get(location.getLocType()));
						} else {
							mOnLocationLoadListener.onLocationLoad(new LocationInfo(location));
						}
						isHandlered = true;
					}
				} catch (Exception e) {
					Log.e(TAG, "onReceiveLocation Exception: " + 	e.getMessage());
				} finally {
					preventRTimeoutRunnable();
					if (!isHandlered) {
						mOnLocationLoadListener.onLocationFailed(mErrorMessages.get(ERROR_COMMON));
					}
					// if ScanSpan < 1000, one time
					if (mLocationClientOption.getScanSpan() < LocationClientOption.MIN_SCAN_SPAN) {
						stopLocation();
					}
				}
			}
		}
	}

	private class TimeoutRunnable implements Runnable {
		private OnLocationLoadListener mListener;
		public TimeoutRunnable setOnLocationLoadListener(OnLocationLoadListener listener) {
			mListener = listener;
			return this;
		}
		
		public TimeoutRunnable reset() {
			mListener = null;
			return this;
		}
		
		@Override
		public void run() {
			if (null != mListener) {
				mListener.onLocationFailed(mErrorMessages.get(ERROR_TIMEOUT));
			}
			stopLocationClient(mLocationClient);
		}
	}
	
	private final int ERROR_COMMON = Integer.MIN_VALUE;
	private final int ERROR_TIMEOUT = Integer.MIN_VALUE + 1;
	private final SparseArray<String> mErrorMessages;
	{
		mErrorMessages = new SparseArray<String>(){
			@Override
			public String get(int key) {
				int index = indexOfKey(key);
				if (index < 0) {
					return "请求位置信息失败";
				}
				return super.valueAt(index);
			}
		};
		mErrorMessages.put(ERROR_COMMON, "请求位置信息失败");
		mErrorMessages.put(ERROR_TIMEOUT, "请求位置信息超时");
		mErrorMessages.put(62, "请求位置信息超时");
//	     61 ： GPS定位结果
//	     62 ： 扫描整合定位依据失败。此时定位结果无效。
//	     63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
//	     65 ： 定位缓存的结果。
//	     161： 表示网络定位结果
//	     162~167： 服务端定位失败。 
		mErrorMessages.put(162, "服务端定位失败");
		mErrorMessages.put(163, "服务端定位失败");
		mErrorMessages.put(164, "服务端定位失败");
		mErrorMessages.put(165, "服务端定位失败");
		mErrorMessages.put(166, "服务端定位失败");
		mErrorMessages.put(167, "服务端定位失败");
	}
}
