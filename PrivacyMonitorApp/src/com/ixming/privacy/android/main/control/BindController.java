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
	public static interface RequestDeviceTokenCallback {
		void onDeviceTokenLoaded();
		
		void onError();
	}
	
	private static BindController sInstance = new BindController();
	public static BindController getInstance() {
		return sInstance;
	}
	
	private String mDeviceToken;
	private AjaxCallbackImpl mAjaxCallback;
	private final AQuery mAQuery;
	private BindController() {
		obtainLocalKey();
		mAQuery = new AQuery(PAApplication.getAppContext());
	}
	
	
	/**
	 * 获取当前DeviceToken
	 */
	public String getDeviceToken() {
		return mDeviceToken;
	}
	
	/**
	 * 判断有无DeviceToken
	 */
	public boolean hasDeviceToken() {
		return !TextUtils.isEmpty(mDeviceToken);
	}
	
	/**
	 * 设置DeviceToken
	 */
	public void setCurrentKey(String deviceToken) {
		AppSharedUtils.saveDeviceToken(deviceToken);
		mDeviceToken = deviceToken;
	}
	
	/**
	 * 请求DeviceToken，并通过回调返回
	 */
	public void requestKey(final RequestDeviceTokenCallback callback) {
		if (null != mAjaxCallback) {
			mAjaxCallback.setRequestKeyCallback(null);
		}
		
		mAjaxCallback = new AjaxCallbackImpl();
		mAjaxCallback.setRequestKeyCallback(callback);
		
		Map<String, String> param = RequestPiecer.getBasicData();
		mAQuery.ajax(Config.URL_POST_DEVICE, param, DeviceBindResult.class, mAjaxCallback);
	}
	
	private void obtainLocalKey() {
		mDeviceToken = AppSharedUtils.getDeviceToken();
	}
	
	private class AjaxCallbackImpl extends AjaxCallback<DeviceBindResult> {
		
		private RequestDeviceTokenCallback mRequestKeyCallback;
		public void setRequestKeyCallback(RequestDeviceTokenCallback callback) {
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
							mRequestKeyCallback.onDeviceTokenLoaded();
						} else {
							ToastUtils.showToast(result.getMsg());
							mRequestKeyCallback.onError();
						}
					} catch (Exception e) {
						LogUtils.e(TAG, "callback Exception: " + e.getMessage());
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
