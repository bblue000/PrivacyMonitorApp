package com.ixming.privacy.android.main.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ixming.base.common.controller.BaseController;

import com.ixming.privacy.android.main.model.MonitoredPerson;

public class PersonListController extends BaseController {

	public static interface MonitoringPersonLoadListener {
		void onMPLoadSuccess();

		void onMPLoadFailed();
	}
	
	private static PersonListController sInstance = new PersonListController();
	public static PersonListController getInstance() {
		return sInstance;
	}
	
	private final List<MonitoredPerson> mMonitoringPersonList = new ArrayList<MonitoredPerson>(5);
	private final Map<MonitoredPerson, PersonController> mMonitoringPersonControllerMap = new HashMap<MonitoredPerson, PersonController>(5);
	private MonitoredPerson mCurrentMonitoringPerson;
	private PersonController mCurrentMonitoringPersonController;
	private PersonListController() {
		mMonitoringPersonList.add(new MonitoredPerson("123", "迷失的老人"));
		mMonitoringPersonList.add(new MonitoredPerson("123", "花心的老公"));
		mMonitoringPersonList.add(new MonitoredPerson("123", "美丽的老婆"));
	}
	
	public void requestMonitoringPerson(MonitoringPersonLoadListener listener) {
		
	}
	
	public List<MonitoredPerson> getMonitoringPersonList() {
		return mMonitoringPersonList;
	}
	
	public void setCurrentMonitoringPerson(MonitoredPerson person) {
		mCurrentMonitoringPerson = person;
		
		PersonController controller = mMonitoringPersonControllerMap.get(person);
		if (null == controller) {
			controller = new PersonController(person);
			mMonitoringPersonControllerMap.put(person, controller);
		}
		
		mCurrentMonitoringPersonController = controller;
	}
	
	public MonitoredPerson getCurrentMonitoringPerson() {
		return mCurrentMonitoringPerson;
	}
	
	public PersonController getCurrentPersonController() {
		return mCurrentMonitoringPersonController;
	}
	
}
