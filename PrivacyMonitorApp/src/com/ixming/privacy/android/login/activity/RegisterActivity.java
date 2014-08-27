package com.ixming.privacy.android.login.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.StringUtils;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.ixming.privacy.android.login.controll.RegisterController;
import com.ixming.privacy.android.login.manager.LoginManager;
import com.ixming.privacy.android.login.manager.RegisterManager;
import com.ixming.privacy.android.utils.Utils;
import com.ixming.privacy.monitor.android.R;

public class RegisterActivity extends BaseActivity {
	AQuery aq;
	EditText usernameET;
	EditText passwordET;
	EditText confimPasswordET;
	EditText checkcodeET;
	Button getCheckcodeBT;
	RegisterController controller;
	final int TIMER_COUNT = 60;
	int count = TIMER_COUNT;
	final int UPDATE_TIME_MSG = 1;
	String checkcodeText = "";

	@Override
	public int provideLayoutResId() {
		return R.layout.activity_register;
	}

	@Override
	public void initView(View view) {
		aq = new AQuery(this);
		usernameET = aq.id(R.id.login_username_et).getEditText();
		passwordET = aq.id(R.id.login_password_et).getEditText();
		checkcodeET = aq.id(R.id.login_checkcode_et).getEditText();
		confimPasswordET = aq.id(R.id.login_confim_password_et).getEditText();
		getCheckcodeBT = aq.id(R.id.get_checkcode_btn).getButton();
		registerReceiver();
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		controller = RegisterController.getInstance();
		checkcodeText = getResources()
				.getString(R.string.send_checkcode_prompt);
	}

	@Override
	public void initListener() {
		aq.id(R.id.register_submit_btn).clicked(this);
		aq.id(R.id.get_checkcode_btn).clicked(this);
		getCheckcodeBT.setOnClickListener(this);
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(RegisterManager.REGISTER_SUCCESS_ACTION);
		LocalBroadcasts.registerLocalReceiver(receiver, filter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.register_submit_btn:
			Log.i("RegisterActivity", "onclick register!!");
			register();
			break;
		case R.id.get_checkcode_btn:
			getCheckcode();
			break;
		}

	}

	private void register() {
		String username = usernameET.getText().toString();
		String password = passwordET.getText().toString();
		String checkcode = checkcodeET.getText().toString();
		String confimPassword = confimPasswordET.getText().toString();
		if (StringUtils.isEmpty(username)) {
			usernameET.setError(getString(R.string.login_username_empty));
			return;
		}
		if (StringUtils.isEmpty(password)) {
			passwordET.setError(getString(R.string.login_password_empty));
			return;
		}
		if (!password.equals(confimPassword)) {
			passwordET
					.setError(getString(R.string.login_confim_password_error));
			return;
		}
		if (StringUtils.isEmpty(checkcode)) {
			checkcodeET.setError(getString(R.string.login_checkcode_empty));
			return;
		}
		if (checkcode.length() != 4) {
			checkcodeET.setError(getString(R.string.login_checkcode_error));
			return;
		}
		controller.register(username, password, checkcode);
	}

	private void getCheckcode() {
		if (count == TIMER_COUNT) {
			String mobile = usernameET.getText().toString();
			if (Utils.checkMobile(mobile)) {
				handler.sendEmptyMessageDelayed(UPDATE_TIME_MSG, 1000);
				getCheckcodeBT.setBackgroundResource(R.color.gray);
				count--;
				controller.getCheckcode(mobile);
			} else {
				usernameET
						.setError(getString(R.string.login_username_format_error));
			}
		}
	}

	public boolean isEmail(String strEmail) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(strEmail);
		return matcher.matches();
	}

	@Override
	public Handler provideActivityHandler() {
		// TODO Auto-generated method stub
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case UPDATE_TIME_MSG:
					if (count > 0) {
						getCheckcodeBT.setText(checkcodeText.replace("%s",
								count + ""));
						sendEmptyMessageDelayed(UPDATE_TIME_MSG, 1000);
						count--;
					} else {
						getCheckcodeBT.setText(R.string.register_get_checkcode);
						getCheckcodeBT
								.setBackgroundResource(R.drawable.button_seletor);
						count = TIMER_COUNT;
					}
					break;

				default:
					break;
				}
			}
		};
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.i(getClass(), "execute  onReceive !!!");
			String action = intent.getAction();
			if (RegisterManager.REGISTER_SUCCESS_ACTION.equals(action)) {
				RegisterActivity.this.finish();
				ToastUtils.showLongToast(R.string.login_register_success);
				// 清理所有其他Activity
			}
		}
	};
}
