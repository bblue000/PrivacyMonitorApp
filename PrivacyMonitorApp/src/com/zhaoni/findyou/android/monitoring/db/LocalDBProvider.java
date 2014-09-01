package com.zhaoni.findyou.android.monitoring.db;

import org.ixming.db4android.provider.BaseDBProvider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDBProvider extends BaseDBProvider {

	public static final String AUTHORITY = "com.zhaoni.findyou.android.monitoring.db.provider";
	
	/*package*/static final String DATABASE_NAME = "androidprojecttemplate.db";
	
	/*package*/static int VERSION_1_0 = 1;
	/*package*/static int VERSION_1_1 = 2;
	
	/**
	 * 给数据库框架提供AUTHORITY
	 */
	@Override
	protected String provideAuthority() {
		return AUTHORITY;
	}

	/**
	 * 给数据库框架提供一个新的SQLiteOpenHelper对象
	 */
	@Override
	protected SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
		return new LocalDBHelper(context, getCurrentVersion());
	}
	
	/**
	 * 获得当前的数据库版本。<br/><br/>
	 * 此处需要持续更新。
	 */
	protected int getCurrentVersion() {
		return VERSION_1_0;
	}

}
