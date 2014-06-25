package com.ixming.privacy.android.main.db.manager;

import java.util.List;

import org.ixming.db4android.dbmanager.DBManager;
import org.ixming.db4android.dbmanager.DBManagerFactory;

import com.ixming.privacy.android.main.entity.PrivacyPhoneInfo;

public class PrivacyPhoneInfoDBManager {

	private static PrivacyPhoneInfoDBManager sInstance;
	
	public synchronized static PrivacyPhoneInfoDBManager getInstance() {
		if (null == sInstance) {
			sInstance = new PrivacyPhoneInfoDBManager();
		}
		return sInstance;
	}
	
	private final DBManager<PrivacyPhoneInfo> mManager;
	private PrivacyPhoneInfoDBManager() {
		mManager = DBManagerFactory.getDBManager(PrivacyPhoneInfo.class);
	}
	
	public List<PrivacyPhoneInfo> findAll() {
		return mManager.queryList(null);
	}
	
	public boolean insertList(List<PrivacyPhoneInfo> list) {
		return mManager.insertData(list);
	}
	
	public boolean insert(PrivacyPhoneInfo item) {
		return mManager.insertData(item);
	}
}
