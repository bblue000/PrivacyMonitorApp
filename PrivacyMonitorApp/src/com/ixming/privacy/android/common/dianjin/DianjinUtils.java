package com.ixming.privacy.android.common.dianjin;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.Utils;

import android.content.Intent;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bodong.dianjinweb.DianJinPlatform;
import com.bodong.dianjinweb.listener.AppActiveListener;
import com.ixming.privacy.android.common.model.ResponseData.PayInfoResult;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class DianjinUtils {
	public static final String DIANJIN_NEW_DATE_KEY = "DIANJIN_NEW_DATE_KEY";
	public static final String DIANJIN_ACTIVED_SUCCESSS_ACTION = "DIANJIN_ACTIVED_SUCCESSS_ACTION";

	public static void initCallBack() {
		DianJinPlatform.setAppActivedListener(new AppActiveListener() {
			@Override
			public void onSuccess(long arg0) {
				LogUtils.i(getClass(), "execute dianjin callBack onSuccess!!!!");
				AQuery aq = new AQuery(PAApplication.getAppContext());
				AjaxCallback<PayInfoResult> callback = new AjaxCallback<PayInfoResult>() {
					@Override
					public void callback(String url, PayInfoResult object,
							AjaxStatus status) {
						if (status.getCode() == HttpStatus.SC_OK) {
							LogUtils.d(DianjinUtils.class, "request result msg"
									+ object.getMsg());
							Intent intent = new Intent();
							intent.setAction(DIANJIN_ACTIVED_SUCCESSS_ACTION);
							if (object.getValue() != null) {
								intent.putExtra(DIANJIN_NEW_DATE_KEY, object
										.getValue().getExpirationdate());

							}
							LocalBroadcasts.sendLocalBroadcast(intent);
						}
					}
				};

				callback.method(AQuery.METHOD_PUT);
				long time = arg0 * 60 * 1000;
				String device_id = Utils.getDeviceId(PAApplication
						.getAppContext());
				Map<String, String> params = new HashMap<String, String>();
				aq.ajax(String.format(Config.URL_PUT_PAYINFO, device_id, time),
						params, PayInfoResult.class, callback);
			}

			@Override
			public void onError(int arg0, String arg1) {
				LogUtils.i(getClass(), "execute dianjin callBack onError!!!!");
			}
		});
	}
}
