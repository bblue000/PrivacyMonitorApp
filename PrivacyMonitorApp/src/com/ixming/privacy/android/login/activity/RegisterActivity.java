package com.ixming.privacy.android.login.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.StringUtils;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.ixming.privacy.android.login.controll.RegisterController;
import com.ixming.privacy.android.login.manager.RegisterManager;
import com.ixming.privacy.monitor.android.R;

public class RegisterActivity extends BaseActivity {
	AQuery aq;
	EditText usernameET;
	EditText passwordET;
	EditText confimPasswordET;
	RegisterController controller;

	@Override
	public int provideLayoutResId() {
		return R.layout.activity_register;
	}

	@Override
	public void initView(View view) {
		aq = new AQuery(this);
		usernameET = aq.id(R.id.login_username_et).getEditText();
		passwordET = aq.id(R.id.login_password_et).getEditText();
		confimPasswordET = aq.id(R.id.login_confim_password_et).getEditText();

	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		controller = RegisterController.getInstance();
	}

	@Override
	public void initListener() {
		aq.id(R.id.register_submit_btn).clicked(this);
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
		}
	}

	private void register() {
		String username = usernameET.getText().toString();
		String password = passwordET.getText().toString();
		String confimPassword = confimPasswordET.getText().toString();
		if (StringUtils.isEmpty(username)) {
			ToastUtils.showLongToast(R.string.login_username_empty);
			return;
		}
		if (StringUtils.isEmpty(password)) {
			ToastUtils.showLongToast(R.string.login_password_empty);
			return;
		}
		if (!password.equals(confimPassword)) {
			LogUtils.i(getClass(), "password:" + password + "confimPassword:"
					+ confimPassword);
			ToastUtils.showLongToast(R.string.login_confim_password_error);
			return;
		}
		if (isEmail(username)) {
			ToastUtils.showLongToast(R.string.login_email_error);
			return;
		}
		controller.register(username, password);
	}

	public boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	@Override
	public Handler provideActivityHandler() {
		// TODO Auto-generated method stub
		return null;
	}

}
