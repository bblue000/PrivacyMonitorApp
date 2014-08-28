package com.ixming.privacy.android.main.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.controller.BaseController;

import com.androidquery.AQuery;
import com.ixming.privacy.android.common.LocalBroadcastIntents.MonitoringPerson;
import com.ixming.privacy.android.main.model.MonitoredPerson;
import com.ixming.privacy.monitor.android.PAApplication;

public class PersonListController extends BaseController {

	private static final String TAG = PersonListController.class.getSimpleName();
	
	public static final boolean TEST = false;
	
	private static final PersonListController sInstance = new PersonListController();
	public static PersonListController getInstance() {
		return sInstance;
	}
	
	private final List<MonitoredPerson> mMonitoringPersonList = new ArrayList<MonitoredPerson>(5);
	private final Map<MonitoredPerson, PersonController> mMonitoringPersonControllerMap = new HashMap<MonitoredPerson, PersonController>(5);
	private PersonController mCurrentMonitoringPersonController;
	
	private AQuery mAQuery;
	private PersonListController() {
		if (TEST) {
			mMonitoringPersonList.add(new MonitoredPerson("123", "迷失的老人"));
			mMonitoringPersonList.add(new MonitoredPerson("123", "花心的老公"));
			mMonitoringPersonList.add(new MonitoredPerson("123", "美丽的老婆"));
		}
		mAQuery = new AQuery(PAApplication.getAppContext());
	}
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// API 操作
	private CallbackAddMonitoringPerson mCallbackAddMonitoringPerson;
	public void addMonitoringPerson(String name, String device_token) {
		if (PersonListController.TEST) {
			return ;
		}
		
		if (null != mCallbackAddMonitoringPerson) {
			mCallbackAddMonitoringPerson.cancelMe();
		}
		
		mCallbackAddMonitoringPerson = new CallbackAddMonitoringPerson();
		mCallbackAddMonitoringPerson.logTag(TAG);
		mCallbackAddMonitoringPerson.ajax(mAQuery, name, device_token);
	}
	
	private CallbackRequestMonitoringPerson mCallbackRequestMonitoringPerson;
	public void requestMonitoringPerson() {
		if (PersonListController.TEST) {
			return ;
		}
		
		if (null != mCallbackRequestMonitoringPerson) {
			mCallbackRequestMonitoringPerson.cancelMe();
		}
		
		mCallbackRequestMonitoringPerson = new CallbackRequestMonitoringPerson();
		mCallbackRequestMonitoringPerson.logTag(TAG);
		mCallbackRequestMonitoringPerson.ajax(mAQuery);
	}
	
	private CallbackUpdateMonitoringPerson mCallbackUpdateMonitoringPerson;
	public void updateMonitoringPerson(MonitoredPerson person, String newName) {
		if (PersonListController.TEST) {
			return ;
		}
		
		if (null != mCallbackUpdateMonitoringPerson) {
			mCallbackUpdateMonitoringPerson.cancelMe();
		}
		
		mCallbackUpdateMonitoringPerson = new CallbackUpdateMonitoringPerson();
		mCallbackUpdateMonitoringPerson.logTag(TAG);
		mCallbackUpdateMonitoringPerson.ajax(mAQuery, person, newName);
	}
	
	private CallbackDeleteMonitoringPerson mCallbackDeleteMonitoringPerson;
	public void deleteMonitoringPerson(MonitoredPerson person) {
		if (PersonListController.TEST) {
			return ;
		}
		
		if (null != mCallbackDeleteMonitoringPerson) {
			mCallbackDeleteMonitoringPerson.cancelMe();
		}
		
		mCallbackDeleteMonitoringPerson = new CallbackDeleteMonitoringPerson();
		mCallbackDeleteMonitoringPerson.logTag(TAG);
		mCallbackDeleteMonitoringPerson.ajax(mAQuery, person);
	}
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// 本地操作
	/*package*/ void onAddMonitoringPerson(MonitoredPerson person) {
		mMonitoringPersonList.add(person);
		// data changed
		broadcastChanged();
	}
	/*package*/ void onDeleteMonitoringPerson(MonitoredPerson person) {
		if (hasCurrentPerson()) {
			if (getCurrentPersonController().getMonitoringPerson() == person) {
				setCurrentMonitoringPerson(null);
			}
		}
		mMonitoringPersonList.remove(person);
		// data changed
		broadcastChanged();
	}
	
	/*package*/ void broadcastChanged() {
		LocalBroadcasts.sendLocalBroadcast(MonitoringPerson.ACTION_DATA_CHANGED);
	}
	
	/*package*/ void broadcastInvalidate() {
		LocalBroadcasts.sendLocalBroadcast(MonitoringPerson.ACTION_DATA_INVALIDATE);
	}
	
	/*package*/ void setMonitoringPersonList(List<MonitoredPerson> personList) {
		mMonitoringPersonList.clear();
		if (null != personList) {
			mMonitoringPersonList.addAll(personList);
		}
		// clear current person
		if (mMonitoringPersonList.isEmpty() && null != getCurrentPersonController()) {
			setCurrentMonitoringPerson(null);
		}
		// data changed
		broadcastChanged();
	}
	
	public List<MonitoredPerson> getMonitoringPersonList() {
		return mMonitoringPersonList;
	}
	
	public void setCurrentMonitoringPerson(MonitoredPerson person) {
		if (null == person || !mMonitoringPersonList.contains(person)) {
			// clear current data
			mCurrentMonitoringPersonController = null;
		} else {
			PersonController controller = mMonitoringPersonControllerMap.get(person);
			if (null == controller) {
				controller = new PersonController(person);
				mMonitoringPersonControllerMap.put(person, controller);
			}
			
			mCurrentMonitoringPersonController = controller;
		}
	}
	
	public PersonController getCurrentPersonController() {
		return mCurrentMonitoringPersonController;
	}
	
	public boolean hasCurrentPerson() {
		return null != getCurrentPersonController();
	}
	
}
