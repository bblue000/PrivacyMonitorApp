package com.ixming.privacy.android.main.control;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.model.RequestPiecer;
import com.ixming.privacy.android.common.model.SimpleAjaxCallback;
import com.ixming.privacy.android.main.model.MonitoredPerson;
import com.ixming.privacy.android.main.model.entity.MonitoredPersonSingleResult;
import com.ixming.privacy.monitor.android.Config;

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
