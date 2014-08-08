package com.ixming.privacy.android.main.fragment;

import org.ixming.base.common.activity.BaseFragment;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.ixming.privacy.android.login.activity.LoginActivity;
import com.ixming.privacy.android.login.manager.LoginManager;
import com.ixming.privacy.android.login.manager.LogoutOperationCallback;
import com.ixming.privacy.android.main.control.BindController;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class BindFragment extends BaseFragment implements
		BindController.RequestDeviceTokenCallback {

	@ViewInject(id = R.id.device_bind_obtain_et)
	private EditText mKeyInput_ET;
	@ViewInject(id = R.id.device_bind_obtain_btn)
	private Button mObtain_BT;
	@ViewInject(id = R.id.device_bind_hide_btn)
	private Button mHide_BT;
	AQuery aq;

	@Override
	public int provideLayoutResId() {
		return R.layout.device_bind;
	}

	@Override
	public void initView(View view) {
		aq = new AQuery(getActivity()); // 实例化框架
		aq.id(R.id.device_bind_login_btn).clicked(this);
		aq.id(R.id.device_bind_logout_btn).clicked(this);
		updateUI();

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
		case R.id.device_bind_login_btn:
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.device_bind_logout_btn:
			LoginManager.getInstance().logout(new LogoutOperationCallback() {
				@Override
				public void onLogoutSuccess() {
				}

				@Override
				public void onLogoutError(int errorCode) {
				}
			});
			break;
		}
	}

	@Override
	public Handler provideActivityHandler() {
		return PAApplication.getHandler();
	}

	@OnClickMethodInject(id = R.id.device_bind_obtain_btn)
	void obtainKey() {
		BindController.getInstance().requestKey(this);
	}

	@OnClickMethodInject(id = R.id.device_bind_hide_btn)
	void hideApp() {
		PAApplication.hideApp();
		getActivity().finish();
		PAApplication.killProcess();
	}

	private void updateUI() {
		if (BindController.getInstance().hasDeviceToken()) {
			mKeyInput_ET.setText(BindController.getInstance().getDeviceToken());
			ViewUtils.setViewVisible(mHide_BT);
		} else {
			mKeyInput_ET.setText(null);
			ViewUtils.setViewInvisible(mHide_BT);
		}
		// if(LoginManager.getInstance().)
		mKeyInput_ET.setFocusable(true);
		mKeyInput_ET.setFocusableInTouchMode(false);
	}

	@Override
	public void onDeviceTokenLoaded() {
		updateUI();
	}

	@Override
	public void onError() {

	}

}
