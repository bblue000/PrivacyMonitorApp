package com.zhaoni.findyou.android.main.control;


import java.util.Map;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;
import com.zhaoni.findyou.android.main.model.MonitoredPerson;
import com.zhaoni.findyou.android.main.model.entity.MonitoredPersonSingleResult;

class CallbackAddMonitoringPerson extends SimpleAjaxCallback<MonitoredPersonSingleResult> {

	public void ajax(AQuery aQuery, String name, String device_token) {
//		device_id		String必不可少的参数	
//		username		String必不可少的参数
//		device_token	String必不可少的参数
//		name			String必不可少的参数
		Map<String, String> params = RequestPiecer.getBasicData();
		params.put("name", name);
		params.put("device_token", device_token);
		aQuery.ajax(Config.URL_POST_LISTENER, params, MonitoredPersonSingleResult.class, this);
	}
	
	@Override
	protected boolean onSuccess(String url, Object object, AjaxStatus status) {
		PersonListController.getInstance().onAddMonitoringPerson((MonitoredPerson) object);
		return true;
	}
	
}
