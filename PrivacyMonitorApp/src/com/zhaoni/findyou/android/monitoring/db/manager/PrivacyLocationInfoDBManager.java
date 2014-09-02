package com.zhaoni.findyou.android.monitoring.db.manager;

import java.util.List;

import org.ixming.db4android.dbmanager.DBManager;
import org.ixming.db4android.dbmanager.DBManagerFactory;

import com.zhaoni.findyou.android.monitoring.entity.PrivacyLocaitonInfo;

public class PrivacyLocationInfoDBManager {

	private static PrivacyLocationInfoDBManager sInstance;
	
	public synchronized static PrivacyLocationInfoDBManager getInstance() {
		if (null == sInstance) {
			sInstance = new PrivacyLocationInfoDBManager();
		}
		return sInstance;
	}
	
	private final DBManager<PrivacyLocaitonInfo> mManager;
	private PrivacyLocationInfoDBManager() {
		mManager = DBManagerFactory.getDBManager(PrivacyLocaitonInfo.class);
	}
	
	public List<PrivacyLocaitonInfo> findAll() {
		return mManager.queryList(null);
	}
	
	public boolean insertList(List<PrivacyLocaitonInfo> list) {
		return mManager.insertData(list);
	}
	
	public boolean insert(PrivacyLocaitonInfo item) {
		return mManager.insertData(item);
	}
	
	public int deleteAll() {
		return mManager.deleteAll();
	}
}
