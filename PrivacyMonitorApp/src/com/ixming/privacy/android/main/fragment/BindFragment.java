package com.ixming.privacy.android.main.fragment;

import org.ixming.base.common.activity.BaseFragment;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.bodong.dianjinweb.DianJinPlatform;
import com.ixming.privacy.android.common.control.BindController;
import com.ixming.privacy.android.common.control.LocationController;
import com.ixming.privacy.android.login.activity.LoginActivity;
import com.ixming.privacy.android.login.manager.LoginManager;
import com.ixming.privacy.android.login.manager.LogoutOperationCallback;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class BindFragment extends BaseFragment {

	@ViewInject(id = R.id.device_bind_obtain_et)
	private EditText mKeyInput_ET;
	@ViewInject(id = R.id.device_bind_obtain_btn)
	private Button mObtain_BT;
	@ViewInject(id = R.id.device_bind_hide_btn)
	private Button mHide_BT;
	@ViewInject(id = R.id.device_bind_open_loc_cb)
	private CheckBox mOpenLoc_CB;
	AQuery aq;

	@Override
	public int provideLayoutResId() {
		return R.layout.device_bind;
	}

	@Override
	public void initView(View view) {
		aq = new AQuery(getActivity());
		/**
		 * 初始化点金广告平台
		 */
		DianJinPlatform.initialize(getActivity(), 56265,
				"99ee203fea26f7099ec69ff692a947ec");
		DianJinPlatform.hideFloatView(getActivity());
		updateUI();
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DianJinPlatform.destory(getActivity());
	}

	@Override
	public void initListener() {
		mOpenLoc_CB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						LocationController.getInstance().setCurrentSetting(
								isChecked);
					}
				});
		aq.id(R.id.device_bind_user_free_exchange_btn).clicked(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.device_bind_user_free_exchange_btn:
			DianJinPlatform.showOfferWall(getActivity());
			break;
		}
	}

	@Override
	public Handler provideActivityHandler() {
		return PAApplication.getHandler();
	}

	@OnClickMethodInject(id = R.id.device_bind_obtain_btn)
	void obtainKey() {
		BindController.getInstance().requestKey();
	}

	@OnClickMethodInject(id = R.id.device_bind_hide_btn)
	void hideApp() {
		PAApplication.hideApp();
		getActivity().finish();
		PAApplication.killProcess();
	}

	@OnClickMethodInject(id = R.id.device_bind_login_btn)
	void login() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		getActivity().startActivity(intent);
	}

	@OnClickMethodInject(id = R.id.device_bind_logout_btn)
	void logout() {
		LoginManager.getInstance().logout(new LogoutOperationCallback() {
			@Override
			public void onLogoutSuccess() {
			}

			@Override
			public void onLogoutError(int errorCode) {
			}
		});
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

}
