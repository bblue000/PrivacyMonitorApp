package com.zhaoni.findyou.android.main.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.controller.BaseController;
import org.ixming.base.utils.ObjectUtils;

import com.androidquery.AQuery;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents.MonitoringPerson;
import com.zhaoni.findyou.android.main.model.MonitoredPerson;

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
		broadcastDataListChanged();
		
		// 自动转到新增的用户
		setCurrentMonitoringPerson(person, false);
	}
	
	/*package*/ void onDeleteMonitoringPerson(MonitoredPerson person) {
		// 删除当前的person
		mMonitoringPersonList.remove(person);
		mMonitoringPersonControllerMap.remove(person);
		// data changed
		broadcastDataListChanged();
		
		// 因为当前的对象被删除，重新选择一项
		chooseADefaultMonitoringPerson();
	}
	
	/*package*/ void broadcastDataListChanged() {
		LocalBroadcasts.sendLocalBroadcast(MonitoringPerson.ACTION_DATA_LIST_CHANGED);
	}
	
	/*package*/ void broadcastDataListInvalidate() {
		LocalBroadcasts.sendLocalBroadcast(MonitoringPerson.ACTION_DATA_LIST_INVALIDATE);
	}
	
	/*package*/ void broadcastSelectPersonChanged() {
		LocalBroadcasts.sendLocalBroadcast(MonitoringPerson.ACTION_SELECT_PERSON_CHANGED);
	}
	
	/*package*/ void setMonitoringPersonList(List<MonitoredPerson> personList) {
		mMonitoringPersonList.clear();
		mMonitoringPersonControllerMap.clear();
		if (null != personList) {
			mMonitoringPersonList.addAll(personList);
		}
		// data changed
		broadcastDataListChanged();
		
		chooseADefaultMonitoringPerson();
	}
	
	private void chooseADefaultMonitoringPerson() {
		if (mMonitoringPersonList.isEmpty()) {
			setCurrentMonitoringPerson(null, false);
		} else {
			// choose first
			setCurrentMonitoringPerson(mMonitoringPersonList.get(0), false);
		}
	}
	
	public List<MonitoredPerson> getMonitoringPersonList() {
		return mMonitoringPersonList;
	}
	
	public void setCurrentMonitoringPerson(MonitoredPerson person) {
		setCurrentMonitoringPerson(person, true);
	}
	
	private void setCurrentMonitoringPerson(MonitoredPerson person, boolean byUser) {
		MonitoredPerson old = null;
		MonitoredPerson cur = null;
		if (null != mCurrentMonitoringPersonController) {
			old = mCurrentMonitoringPersonController.getMonitoringPerson();
		}
		
		if (null == person || !mMonitoringPersonList.contains(person)) {
			// clear current data
			mCurrentMonitoringPersonController = null;
			cur = null;
		} else {
			PersonController controller = mMonitoringPersonControllerMap.get(person);
			if (null == controller) {
				controller = new PersonController(person);
				mMonitoringPersonControllerMap.put(person, controller);
			}
			
			mCurrentMonitoringPersonController = controller;
			cur = person;
		}
		
		if (byUser || !ObjectUtils.equals(old, cur)) {
			broadcastSelectPersonChanged();
		}
	}
	
	public PersonController getCurrentPersonController() {
		return mCurrentMonitoringPersonController;
	}
	
	public boolean hasCurrentPerson() {
		return null != getCurrentPersonController();
	}
	
}
