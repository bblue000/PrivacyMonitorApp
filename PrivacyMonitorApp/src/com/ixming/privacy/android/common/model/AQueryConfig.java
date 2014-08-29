package com.ixming.privacy.android.common.model;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.util.AQUtility;

public class AQueryConfig {

	public static final int TIME_OUT = 3 * 1000;
	public static final int RETRY = 0;
	
	private AQueryConfig() {
	}
	
	public static void config() {
		AQUtility.setDebug(true);
		AjaxCallback.setTransformer(new AQueryTransformer());
		AjaxCallback.setTimeout(TIME_OUT);
	}
}
