package com.zhaoni.findyou.android.login.activity;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.StringUtils;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.login.manager.ForgotPasswordManager;

public class ForgotPasswordActivity extends BaseActivity {
	AQuery aq = null;
	EditText email_ET;

	@Override
	public int provideLayoutResId() {
		return R.layout.activity_forgot_password;
	}

	@Override
	public void initView(View view) {
		aq = new AQuery(this);
		aq.id(R.id.retrieve_submit_btn).clicked(this);
		email_ET = aq.id(R.id.retrieve_email_et).getEditText();
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {

	}

	@Override
	public void initListener() {
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.retrieve_submit_btn:
			ForgotPasswordManager fpm = new ForgotPasswordManager();
			String email = email_ET.getText().toString();
			if (StringUtils.isEmpty(email)) {
				email_ET.setError("不能为空");
				break;
			}
			fpm.sendEmail(email);
			break;
		default:
			break;
		}
	}

	@Override
	public Handler provideActivityHandler() {
		return null;
	}

}
