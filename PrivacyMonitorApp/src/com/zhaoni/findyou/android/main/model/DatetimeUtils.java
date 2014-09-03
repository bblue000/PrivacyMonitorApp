package com.zhaoni.findyou.android.main.model;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

public class DatetimeUtils {

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
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
	
	public static String simpleFixTime(long datetime) {
		long sec = datetime / 1000L;
		if (sec == 0L) {
			return sec + "秒";
		}
		long min = sec / 60L;
		if (min == 0L) {
			return sec + "秒";
		}
		long halfHour = min / 30L;
		if (halfHour == 0L) {
			return min + "分钟";
		} else if (halfHour == 1L) {
			return "半小时";
		}
		long hour = min / 60L;
		if (hour == 0L) {
			return min + "分钟";
		}
		return hour + "小时";
	}
	
}
