package com.ixming.privacy.android.main.control;

import java.util.List;

import org.ixming.base.utils.android.AndroidUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.ixming.privacy.android.common.model.RequestPiecer;
import com.ixming.privacy.android.common.model.SimpleAjaxCallback;
import com.ixming.privacy.android.main.model.MonitoredPerson;
import com.ixming.privacy.android.main.model.entity.MonitoredPersonListResult;
import com.ixming.privacy.monitor.android.Config;

class CallbackRequestMonitoringPerson extends SimpleAjaxCallback<MonitoredPersonListResult> {
	
	public void ajax(AQuery aQuery) {
		aQuery.ajax(String.format(Config.URL_GET_LISTENERS,
				RequestPiecer.getUserName(),
				AndroidUtils.getDeviceId()), MonitoredPersonListResult.class, this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean onSuccess(String url, Object object, AjaxStatus status) {
		PersonListController.getInstance().setMonitoringPersonList((List<MonitoredPerson>) object);
		return true;
	}
	
}
