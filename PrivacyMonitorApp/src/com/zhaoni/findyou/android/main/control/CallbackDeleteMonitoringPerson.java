package com.zhaoni.findyou.android.main.control;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;
import com.zhaoni.findyou.android.main.model.MonitoredPerson;
import com.zhaoni.findyou.android.main.model.entity.MonitoredPersonSingleResult;

class CallbackDeleteMonitoringPerson extends SimpleAjaxCallback<MonitoredPersonSingleResult> {

	private MonitoredPerson mPerson;
	public void ajax(AQuery aQuery, MonitoredPerson person) {
		mPerson = person;
		method(AQuery.METHOD_DELETE);
		aQuery.ajax(String.format(Config.URL_DELETE_LISTENER, 
				String.valueOf(mPerson.getId())),
				RequestPiecer.getBasicData(),
				MonitoredPersonSingleResult.class, this);
	}
	
	@Override
	protected boolean onSuccess(String url, Object object, AjaxStatus status) {
		PersonListController.getInstance().onDeleteMonitoringPerson(mPerson);
		return true;
	}
	
}
