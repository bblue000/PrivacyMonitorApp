package com.ixming.privacy.android.main.control;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.ixming.base.common.controller.BaseController;
import org.ixming.base.network.json.GsonPool;
import org.ixming.base.utils.android.AndroidUtils;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.PreferenceUtils;
import org.ixming.base.utils.android.ToastUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.model.BasicResponseData;
import com.ixming.privacy.android.main.model.DeviceBindResult;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

import android.os.AsyncTask;
import android.text.TextUtils;

public class BindController extends BaseController {

	private static final String TAG = BindController.class.getSimpleName();
	
	public static interface RequestKeyCallback {
		void onKeyLoaded(String key);
		
		void onError();
	}
	
	private static BindController sInstance = new BindController();
	public static BindController getInstance() {
		return sInstance;
	}
	
	private BindController() {
		obtainLocalKey();
	}
	
	private final String MAIN = Config.SHAREPRE_PREFIX + "BIND";
	private final String KEY = "bind_key";
	
	private String mCurrentKey;
	public String getCurrentKey() {
		return mCurrentKey;
	}
	
	public boolean hasKey() {
		return !TextUtils.isEmpty(mCurrentKey);
	}
	
	public void setCurrentKey(String key) {
		PreferenceUtils.saveValue(PAApplication.getAppContext(), MAIN, KEY, key);
		mCurrentKey = key;
	}
	
	public void requestKey(final RequestKeyCallback callback) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("device_id", AndroidUtils.getDeviceId());
		AQuery aQuery = new AQuery(PAApplication.getAppContext());
		aQuery.ajax(Config.URL_POST_DEVICE, param, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String json, AjaxStatus status) {
				LogUtils.d(TAG, "callback code ---> " + status.getCode());
				LogUtils.d(TAG, "callback msg ---> " + status.getMessage());
				if (status.getCode() == HttpStatus.SC_OK) {
					BasicResponseData result = GsonPool.getDefault().fromJson(json, BasicResponseData.class);
//					callback.onKeyLoaded(result.getDevice_token());
				} else {
					ToastUtils.showToast(status.getMessage());
				}
				status.close();
			}
		});
	}
	
	private void obtainLocalKey() {
		mCurrentKey = PreferenceUtils.getValue(PAApplication.getAppContext(), MAIN, KEY, null);
	}
	
	class T extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			return null;
		}
		
	}
	
}
