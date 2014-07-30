package com.ixming.privacy.android.main.control;

import java.util.ArrayList;
import java.util.List;

import org.ixming.base.common.controller.BaseController;

import com.ixming.privacy.android.main.model.MonitoredPerson;

public class PersonListController extends BaseController {

	private static PersonListController sInstance = new PersonListController();
	public static PersonListController getInstance() {
		return sInstance;
	}
	
	public static interface MonitoringPersonLoadListener {
		void onLoadSuccess() ;
		
		void onLoadFailed();
	}
	
	
	private List<MonitoredPerson> mMonitoringPersonList = new ArrayList<MonitoredPerson>(5);
	private List<PersonController> mMonitoringPersonControllerList = new ArrayList<PersonController>(5);
	private MonitoredPerson mCurrentMonitoringPerson;
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
	}
	
	public MonitoredPerson getCurrentMonitoringPerson() {
		return mCurrentMonitoringPerson;
	}
	
//	public PersonController getCurrentPersonController() {
//		if (null == getCurrentMonitoringPerson()) {
//			return null;
//		}
//		
//	}
	
}
