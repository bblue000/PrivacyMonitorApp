package com.ixming.privacy.android.main.control;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.model.RequestPiecer;
import com.ixming.privacy.android.common.model.SimpleAjaxCallback;
import com.ixming.privacy.android.main.model.MonitoredPerson;
import com.ixming.privacy.android.main.model.entity.MonitoredPersonSingleResult;
import com.ixming.privacy.monitor.android.Config;

class CallbackUpdateMonitoringPerson extends SimpleAjaxCallback<MonitoredPersonSingleResult> {

	private String mNewName;
	private MonitoredPerson mPerson;
	public void ajax(AQuery aQuery, MonitoredPerson person, String newName) {
		mPerson = person;
		mNewName = newName;
		method(AQuery.METHOD_PUT);
		aQuery.ajax(String.format(Config.URL_PUT_LISTENER, 
				String.valueOf(mPerson.getId()),
				/* new name*/ mNewName),
				RequestPiecer.getBasicData(),
				MonitoredPersonSingleResult.class, this);
	}
	
	@Override
	protected boolean onSuccess(String url, Object object, AjaxStatus status) {
		mPerson.setName(mNewName);
		PersonListController.getInstance().broadcastChanged();
		return true;
	}
	
}
