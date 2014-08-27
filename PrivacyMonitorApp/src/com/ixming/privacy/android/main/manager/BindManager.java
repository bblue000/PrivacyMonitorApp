package com.ixming.privacy.android.main.manager;

import org.apache.http.HttpStatus;
import org.ixming.base.utils.android.Utils;

import android.os.Handler;
import android.os.Message;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.model.ResponseData.PayInfoResult;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class BindManager {
	AQuery aq;
	Handler handler;
	public static final int DEVICE_DATA_MSG = 1;

	public BindManager(Handler handler) {
		aq = new AQuery(PAApplication.getAppContext());
		this.handler = handler;
	}

	/**
	 * 请求 剩余时间
	 */
	public void requestDate() {
		AjaxCallback<PayInfoResult> callback = new AjaxCallback<PayInfoResult>() {

			@Override
			public void callback(String url, PayInfoResult object,
					AjaxStatus status) {
				if (status.getCode() == HttpStatus.SC_OK) {
					PayInfoResult result = (PayInfoResult) object;
					if (result != null && result.isOK()) {
						String date = result.getValue();
						Message msg = handler.obtainMessage();
						msg.what = DEVICE_DATA_MSG;
						msg.obj = date;
						handler.sendMessage(msg);
					}
				}
			}
		};
		String device_id = Utils.getDeviceId(PAApplication.getAppContext());
		aq.ajax(String.format(Config.URL_GET_PAYINFO, 0, device_id),
				PayInfoResult.class, callback);
	}
}
