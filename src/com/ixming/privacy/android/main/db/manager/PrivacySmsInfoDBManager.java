package com.ixming.privacy.android.main.db.manager;

import java.util.List;

import org.ixming.db4android.dbmanager.DBManager;
import org.ixming.db4android.dbmanager.DBManagerFactory;

import com.ixming.privacy.android.main.entity.PrivacySmsInfo;

public class PrivacySmsInfoDBManager {

	private static PrivacySmsInfoDBManager sInstance;
	
	public synchronized static PrivacySmsInfoDBManager getInstance() {
		if (null == sInstance) {
			sInstance = new PrivacySmsInfoDBManager();
		}
		return sInstance;
	}
	
	private final DBManager<PrivacySmsInfo> mManager;
	private PrivacySmsInfoDBManager() {
		mManager = DBManagerFactory.getDBManager(PrivacySmsInfo.class);
	}
	
	public List<PrivacySmsInfo> findAll() {
		return mManager.queryList(null);
	}
	
	public boolean insertList(List<PrivacySmsInfo> list) {
		return mManager.insertData(list);
	}
	
	public boolean insert(PrivacySmsInfo item) {
		return mManager.insertData(item);
	}
}
