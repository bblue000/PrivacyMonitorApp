package com.zhaoni.findyou.android.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	// 简单判断手机号码正确性
	public static boolean checkMobile(String phone) {
		Pattern pattern = Pattern.compile("1\\d{10}");
		Matcher matcher = pattern.matcher(phone);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}
}
