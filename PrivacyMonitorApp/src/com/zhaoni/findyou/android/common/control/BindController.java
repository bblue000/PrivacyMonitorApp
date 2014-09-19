package com.zhaoni.findyou.android.common.control;

import java.util.Map;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.controller.BaseController;

import android.text.TextUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents.DeviceToken;
import com.zhaoni.findyou.android.common.model.AppSharedUtils;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;
import com.zhaoni.findyou.android.main.model.DeviceBind;
import com.zhaoni.findyou.android.main.model.entity.DeviceBindResult;

/**
 * 
 * 绑定本机的控制类
 * 
 * @author Yin Yong
 * 
 */
public class BindController extends BaseController {

	private static final String TAG = BindController.class.getSimpleName();

	private static BindController sInstance = new BindController();

	public static BindController getInstance() {
		return sInstance;
	}

	private String mDeviceToken;
	private AjaxCallbackImpl mAjaxCallback;
	private final AQuery mAQuery;
	private final Object mToken = new Object();

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
	 * 判断有无DeviceToken
	 */
	public boolean hasDeviceTokenRT() {
		obtainLocalKey();
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
	public void requestKey() {
		if (null != mAjaxCallback) {
			mAjaxCallback.token(null);
		}
		mAjaxCallback = new AjaxCallbackImpl();
		mAjaxCallback.token(mToken);
		Map<String, String> param = RequestPiecer.getBasicData();
		mAQuery.ajax(Config.URL_POST_DEVICE, param, DeviceBindResult.class,
				mAjaxCallback);
	}

	private void obtainLocalKey() {
		mDeviceToken = AppSharedUtils.getDeviceToken();
	}

	private class AjaxCallbackImpl extends SimpleAjaxCallback<DeviceBindResult> {

		public AjaxCallbackImpl() {
			super(false);
			logTag(TAG);
		}

		@Override
		protected boolean onSuccess(String url, Object object, AjaxStatus status) {
			try {
				setCurrentKey(((DeviceBind) object).getDevice_token());
				LocalBroadcasts
						.sendBroadcastBoth(DeviceToken.ACTION_DEVICE_TOKEN_LOADED);
			} catch (Exception e) {
				e.printStackTrace();
				LocalBroadcasts
						.sendBroadcastBoth(DeviceToken.ACTION_DEVICE_TOKEN_FAILED);
			}
			
			return true;
		}

		@Override
		protected boolean onError(AjaxStatus status) {
			LocalBroadcasts
					.sendBroadcastBoth(DeviceToken.ACTION_DEVICE_TOKEN_FAILED);
			return true;
		}
	}
}
