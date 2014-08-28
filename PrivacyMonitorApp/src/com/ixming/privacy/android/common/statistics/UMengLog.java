package com.ixming.privacy.android.common.statistics;

import org.ixming.base.utils.android.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.ixming.privacy.monitor.android.PAApplication;
import com.umeng.analytics.MobclickAgent;

public class UMengLog {

	// 正式版的KEY：5243ccac56240b98cf06f6f8
	private static Context context;
	
	/**
	 * 初始化设置，在Application中调用
	 */
	@SuppressWarnings("deprecation")
	public static void init() {
		context = PAApplication.getAppContext();
		
		//设置是否debug模式，在debug模式下，会有log输出，方便调试 (默认true)
		MobclickAgent.setDebugMode(true) ;
		// 设置是否允许收集地理位置信息 （默认true）
		MobclickAgent.setAutoLocation(true) ;
		// 设置session启动的沉默参数，详见30秒规则 （默认30000）
		MobclickAgent.setSessionContinueMillis(30000L); 
		
		// 设置是否打开页面路径访问功能（默认打开）
		MobclickAgent.openActivityDurationTrack(true) ;
		// 设置openGL 信息，辅助统计GPU 信息
		//MobclickAgent.setOpenGLContext(GL10 gl)
		
		// 捕获程序崩溃日志，并在程序下次启动时发送到服务器
		MobclickAgent.setCatchUncaughtExceptions(true);
	}
	
	/**
	 * 打印错误信息
	 */
	public static void reportError(Throwable error) {
		MobclickAgent.reportError(context, error);
	}
	
	/**
	 * 此方法会把传入的错误信息发回服务器
	 */
	public static void reportError(String error) {
		// 此方法会把传入的错误信息发回服务器
		MobclickAgent.reportError(context, error);
	}
	
	/**
	 * 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
	 * 
	 * 您需要在程序的入口 Activity 中添加
	 */
	public static void onSplash(Activity splash) {
		MobclickAgent.updateOnlineConfig(splash);
	}
	
	/**
	 * 如果开发者调用 Process.kill 或者 System.exit 之类的方法杀死进程，请务必再次之前调用此方法，用来保存统计数据。
	 */
	public static void onKillProcess() {
		// 如果开发者调用 Process.kill 或者 System.exit 之类的方法杀死进程，请务必再次之前调用此方法，用来保存统计数据。
		MobclickAgent.onKillProcess(context) ;
	}
	
	/**
	 * 当Activity显示在屏幕时调用
	 */
	public static void onActivityShow(Activity act) {
		MobclickAgent.onResume(act);
	}

	/**
	 * 当Activity不再显示在屏幕时调用
	 */
	public static void onActivityHide(Activity act) {
		MobclickAgent.onPause(act);
	}
	
	/**
	 * 当特定UI（可能是Activity，可能是Fragment，也可能是自定义View）显示在屏幕时调用
	 */
	public static void onUIStart(String page) {
		MobclickAgent.onPageStart(page);
	}
	
	/**
	 * 当特定UI（可能是Activity，可能是Fragment，也可能是自定义View）不再显示在屏幕时调用
	 */
	public static void onUIEnd(String page) {
		MobclickAgent.onPageEnd(page);
	}
	
	public static void logTestDeviceJson() {
		try {
			Context context = PAApplication.getAppContext();
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			LogUtils.d("yytest", "" + json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}