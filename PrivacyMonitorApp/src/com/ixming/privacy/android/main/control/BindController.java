package com.ixming.privacy.android.main.control;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.ixming.base.common.controller.BaseController;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.model.AppSharedUtils;
import com.ixming.privacy.android.common.model.RequestPiecer;
import com.ixming.privacy.android.main.model.entity.DeviceBindResult;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

import android.text.TextUtils;

public class BindController extends BaseController {

	private static final String TAG = BindController.class.getSimpleName();
	
	/**
	 * 请求标识码的回调
	 * 
	 * @author Yin Yong
	 *
	 */
	public static interface RequestKeyCallback {
		void onKeyLoaded();
		
		void onError();
	}
	
	private static BindController sInstance = new BindController();
	public static BindController getInstance() {
		return sInstance;
	}
	
	private BindController() {
		obtainLocalKey();
	}
	
	private String mCurrentKey;
	public String getCurrentKey() {
		return mCurrentKey;
	}
	
	public boolean hasKey() {
		return !TextUtils.isEmpty(mCurrentKey);
	}
	
	public void setCurrentKey(String key) {
		AppSharedUtils.saveDeviceToken(key);
		mCurrentKey = key;
	}
	
	private AjaxCallbackImpl mAjaxCallback;
	public void requestKey(final RequestKeyCallback callback) {
		if (null != mAjaxCallback) {
			mAjaxCallback.setRequestKeyCallback(null);
		}
		
		mAjaxCallback = new AjaxCallbackImpl();
		mAjaxCallback.setRequestKeyCallback(callback);
		
		Map<String, String> param = RequestPiecer.getBasicData();
		AQuery aQuery = new AQuery(PAApplication.getAppContext());
		aQuery.ajax(Config.URL_POST_DEVICE, param, DeviceBindResult.class, mAjaxCallback);
	}
	
	private void obtainLocalKey() {
		mCurrentKey = AppSharedUtils.getDeviceToken();
	}
	
	private class AjaxCallbackImpl extends AjaxCallback<DeviceBindResult> {
		
		private RequestKeyCallback mRequestKeyCallback;
		public void setRequestKeyCallback(RequestKeyCallback callback) {
			mRequestKeyCallback = callback;
		}
		
		@Override
		public void callback(String url, DeviceBindResult result, AjaxStatus status) {
			LogUtils.d(TAG, "callback code ---> " + status.getCode());
			LogUtils.d(TAG, "callback msg ---> " + status.getMessage());
			LogUtils.d(TAG, "callback result ---> " + result);
			if (null != mRequestKeyCallback) {
				if (status.getCode() == HttpStatus.SC_OK) {
					try {
						if (result.isOK()) {
							setCurrentKey(result.getValue().getDevice_token());
							mRequestKeyCallback.onKeyLoaded();
						} else {
							ToastUtils.showToast(result.getMsg());
							mRequestKeyCallback.onError();
						}
					} catch (Exception e) {
						e.printStackTrace();
						ToastUtils.showToast(R.string.device_bind_obtain_error);
						mRequestKeyCallback.onError();
					}
				} else {
					ToastUtils.showToast(status.getMessage());
					mRequestKeyCallback.onError();
				}
			}
			status.close();
		}
	}
}
