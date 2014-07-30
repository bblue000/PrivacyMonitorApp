package com.ixming.privacy.android.splash.control;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.ActivityControl;
import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.common.controller.BaseController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ixming.privacy.android.common.DefLoginStateCallback;
import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.login.manager.LoginManager;
import com.ixming.privacy.android.main.activity.MainActivity;

public class SplashController_ForTest extends BaseController {

	private static SplashController_ForTest sInstance;
	
	public synchronized static SplashController_ForTest getInstance() {
		if (null == sInstance) {
			sInstance = new SplashController_ForTest();
		}
		return sInstance;
	}
	
	private SplashController_ForTest() {
		LocalBroadcasts.registerLocalReceiver(mReceiver, LocalBroadcastIntents.ACTION_LOGIN);
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (LocalBroadcastIntents.ACTION_LOGIN.equals(action)) {
				BaseActivity act = (BaseActivity) ActivityControl.getInstance().getTopActivity();
				if (null == act) {
					ActivityControl.getInstance().clearAll();
					ActivityControl.startNewTaskActivity(MainActivity.class, 0);
				} else {
					act.startActivity(MainActivity.class);
				}
				return ;
			}
		}
	};
	
	public void checkJump() {
		LoginManager.getInstance().checkLoginState(new DefLoginStateCallback(){
			@Override
			public void onAlreadyLogged() {
				ActivityControl.getInstance().clearAll();
				
				ActivityControl.startNewTaskActivity(MainActivity.class, 0);
			}
		});
	}
	
}
