package com.zhaoni.findyou.android.common.model;

import java.io.UnsupportedEncodingException;

import org.ixming.base.network.json.GsonPool;
import org.ixming.base.utils.android.LogUtils;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;

public class AQueryTransformer implements Transformer {

	private static final String TAG = AQueryTransformer.class.getSimpleName();
	
	@Override
	public <T> T transform(String url, Class<T> type, String encoding,
			byte[] data, AjaxStatus status) {
		try {
			return GsonPool.getDefault().fromJson(new String(data, encoding), type);
		} catch (UnsupportedEncodingException e) {
			LogUtils.e(TAG, "transform Exception: " + e.getMessage());
			e.printStackTrace();
			return GsonPool.getDefault().fromJson(new String(data), type);
		}
	}

}
