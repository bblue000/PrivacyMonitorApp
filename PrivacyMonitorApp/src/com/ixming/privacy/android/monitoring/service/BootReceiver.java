package com.ixming.privacy.android.monitoring.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private final String TAG = BootReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		Log.d(TAG, "onReceive action = " + action);

		MainService.startMe(TAG);
	}

}
