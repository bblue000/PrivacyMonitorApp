package org.ixming.base.common;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

/**
 * 本地广播的封装
 * 
 * @author Yin Yong
 *
 */
public class LocalBroadcasts {

	private LocalBroadcasts() {}
	
	private static LocalBroadcastManager sLocalBroadcastManager;
	private synchronized static void checkLocalBroadcastManagerInstance() {
		if (null == sLocalBroadcastManager) {
			sLocalBroadcastManager = LocalBroadcastManager.getInstance(BaseApplication.getAppContext());
		}
	}
	
	/**
	 * 注册广播，参数<code> actions </code> 为注册的行为
	 */
	public static void registerLocalReceiver(BroadcastReceiver receiver, String...actions) {
		if (null == actions || actions.length == 0) {
			return ;
		}
		IntentFilter filter = new IntentFilter();
		for (int i = 0; i < actions.length; i++) {
			filter.addAction(actions[i]);
		}
		registerLocalReceiver(receiver, filter);
	}
	
	/**
	 * 注册本地和通用广播，参数<code> actions </code> 为注册的行为
	 */
	public static void registerReceiverBoth(BroadcastReceiver receiver, String...actions) {
		if (null == actions || actions.length == 0) {
			return ;
		}
		IntentFilter filter = new IntentFilter();
		for (int i = 0; i < actions.length; i++) {
			filter.addAction(actions[i]);
		}
		registerReceiverBoth(receiver, filter);
	}
	
	/**
	 * 注册广播
	 */
	public static void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		checkLocalBroadcastManagerInstance();
		sLocalBroadcastManager.registerReceiver(receiver, filter);
	}
	
	/**
	 * 注册广播
	 */
	public static void registerReceiverBoth(BroadcastReceiver receiver, IntentFilter filter) {
		registerLocalReceiver(receiver, filter);
		BaseApplication.getAppContext().registerReceiver(receiver, filter);
	}
	
	/**
	 * 注销指定广播
	 */
	public static void unregisterLocalReceiver(BroadcastReceiver receiver) {
		checkLocalBroadcastManagerInstance();
		try {
			sLocalBroadcastManager.unregisterReceiver(receiver);
		} catch (Exception ignore) { }
	}
	
	/**
	 * 注销指定广播
	 */
	public static void unregisterReceiverBoth(BroadcastReceiver receiver) {
		unregisterLocalReceiver(receiver);
		try {
			BaseApplication.getAppContext().unregisterReceiver(receiver);
		} catch (Exception ignore) { }
	}
	
	/**
	 * 提供action发送本地广播
	 */
	public static void sendLocalBroadcast(String action) {
		if (TextUtils.isEmpty(action)) {
			return ;
		}
		sendLocalBroadcast(new Intent(action));
	}
	
	/**
	 * 提供action发送本地和通用广播
	 */
	public static void sendBroadcastBoth(String action) {
		if (TextUtils.isEmpty(action)) {
			return ;
		}
		sendBroadcastBoth(new Intent(action));
	}
	
	/**
	 * 提供Intent发送本地广播
	 */
	public static void sendLocalBroadcast(Intent intent) {
		checkLocalBroadcastManagerInstance();
		sLocalBroadcastManager.sendBroadcast(intent);
	}
	
	/**
	 * 提供Intent发送本地和通用广播
	 */
	public static void sendBroadcastBoth(Intent intent) {
		sendLocalBroadcast(intent);
		BaseApplication.getAppContext().sendBroadcast(intent);
	}
	
}
