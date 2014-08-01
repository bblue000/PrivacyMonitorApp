package com.ixming.privacy.android.main.model;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

public class DatetimeUtils {

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sFormatter_Date = new SimpleDateFormat("yyyy年MM月dd日");
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sFormatter_Time = new SimpleDateFormat("HH:mm:ss");
	public static String format(long datetime) {
		return sFormatter.format(datetime);
	}
	
	public static String formatDate(long datetime) {
		return sFormatter_Date.format(datetime);
	}
	
	public static String formatTime(long datetime) {
		return sFormatter_Time.format(datetime);
	}
	
}
