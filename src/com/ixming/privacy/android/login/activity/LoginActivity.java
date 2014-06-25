package com.ixming.privacy.android.login.activity;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.BaseActivity;
import org.ixming.inject4android.InjectorUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.login.manager.LoginManager;
import com.ixming.privacy.android.login.manager.LoginOperationCallback;
import com.ixming.privacy.android.main.activity.MainActivity;
import com.ixming.privacy.monitor.android.R;

public class LoginActivity extends BaseActivity {

	@ViewInject(id = R.id.login_username_et)
	private EditText mUsername_ET;
	@ViewInject(id = R.id.login_password_et)
	private EditText mPassword_ET;
	
	private LoginManager mLoginManager;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (LocalBroadcastIntents.ACTION_LOGIN.equals(action)) {
				int code = intent.getIntExtra(LoginOperationCallback.EXTRA_RESULT_CODE, 0);
				switch (code) {
				case LoginOperationCallback.CODE_SUCCESS:
					startActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					finish();
					break;
				default:
					break;
				}
				
			}
		}
	};
	
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_login;
	}

	@Override
	public void initView(View view) {
		InjectorUtils.defaultInstance().inject(this);
	}

	@Override
	public void initListener() {
		
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		LocalBroadcasts.registerLocalReceiver(mReceiver, LocalBroadcastIntents.ACTION_LOGIN);
		mLoginManager = LoginManager.getInstance();
	}
    
	@Override
	public Handler provideActivityHandler() {
		return null;
	}

	@Override
	public void onClick(View v) {

	}

	@OnClickMethodInject(id = R.id.login_submit_btn)
	void submit() {
		mUsername_ET.setError(null);
		mPassword_ET.setError(null);
		
		CharSequence username = mUsername_ET.getText();
		if (TextUtils.isEmpty(username)) {
			mUsername_ET.setError(getString(R.string.login_username_empty));
			return ;
		}
		CharSequence password = mPassword_ET.getText();
		if (TextUtils.isEmpty(password)) {
			mPassword_ET.setError(getString(R.string.login_password_empty));
			return ;
		}
		
		mLoginManager.login(username.toString(), password.toString());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// clear all operations perhaps in process
		mLoginManager.cancelLogin();
		LocalBroadcasts.unregisterLocalReceiver(mReceiver);
	}
	
}
