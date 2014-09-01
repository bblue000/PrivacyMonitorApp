package com.zhaoni.findyou.android.common.model;

import org.ixming.base.io.IOConstants;

import com.androidquery.callback.AjaxCallback;

public class BasicAjaxCallback<T> extends AjaxCallback<T> {

	public BasicAjaxCallback() {
		encoding(IOConstants.DEF_CHARSET);
		timeout(AQueryConfig.TIME_OUT);
		retry(AQueryConfig.RETRY);
	}
	
}
