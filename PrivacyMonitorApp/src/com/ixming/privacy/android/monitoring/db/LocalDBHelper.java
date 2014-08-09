package com.ixming.privacy.android.monitoring.db;

import org.ixming.db4android.dbmanager.SQLiteUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;

/**
 * a example of SQLiteOpenHelper()
 */
/*package*/class LocalDBHelper extends SQLiteOpenHelper {
	final String TAG = LocalDBHelper.class.getSimpleName();
	
	/*package*/LocalDBHelper(Context context, int dataBaseVersion) {
		super(context, LocalDBProvider.DATABASE_NAME, null, dataBaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		modifyAllTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			if (oldVersion < LocalDBProvider.VERSION_1_0) {
				modifyTables_V1_0(db);
				oldVersion = LocalDBProvider.VERSION_1_0;
			}
			if (oldVersion < LocalDBProvider.VERSION_1_1) {
				modifyTables_V1_1(db);
				oldVersion = LocalDBProvider.VERSION_1_1;
			}
		}
	}

	private void modifyAllTables(SQLiteDatabase db) {
		modifyTables_V1_0(db);
		modifyTables_V1_1(db);
	}
	
	// =====================================================
	private void modifyTables_V1_0(SQLiteDatabase db){
		SQLiteUtils.createTableAndIndices(db, PrivacyLocaitonInfo.class);
	}
	
	private void modifyTables_V1_1(SQLiteDatabase db){
		
	}
}
