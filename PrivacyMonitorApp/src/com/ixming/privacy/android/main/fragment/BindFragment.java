package com.ixming.privacy.android.main.fragment;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.BaseFragment;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.common.control.BindController;
import com.ixming.privacy.android.common.control.LocalUserController;
import com.ixming.privacy.android.common.control.LocationController;
import com.ixming.privacy.android.login.activity.LoginActivity;
import com.ixming.privacy.android.login.manager.LoginManager;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class BindFragment extends BaseFragment {

	@ViewInject(id = R.id.device_bind_obtain_et)
	private EditText mKeyInput_ET;
	@ViewInject(id = R.id.device_bind_obtain_btn)
	private Button mObtain_BT;
	

	@ViewInject(id = R.id.device_bind_open_loc_cb)
	private CheckBox mOpenLoc_CB;
	
	// 隐藏应用
	@ViewInject(id = R.id.device_bind_hide_layout)
	private View mHideApp_Layout;
	@ViewInject(id = R.id.device_bind_hide_btn)
	private Button mHideApp_BT;
	
	// 用户信息布局
	@ViewInject(id = R.id.device_bind_user_info_layout)
	private View mUserInfo_Layout;
	@ViewInject(id = R.id.device_bind_user_name_tv)
	private TextView mUsername_TV;
	@ViewInject(id = R.id.device_bind_user_exchange_tv)
	private TextView mExchange_TV;
	@ViewInject(id = R.id.device_bind_user_expire_tv)
	private TextView mExpire_TV;

	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (LocalBroadcastIntents.ACTION_LOGIN.equals(action)
					|| LocalBroadcastIntents.ACTION_LOGOUT.equals(action)) {
				updateUI();
			}
		}
	};
	@Override
	public int provideLayoutResId() {
		return R.layout.device_bind;
	}

	@Override
	public void initView(View view) {
		updateUI();
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		LocalBroadcasts.registerLocalReceiver(mReceiver,
				LocalBroadcastIntents.ACTION_LOGIN,
				LocalBroadcastIntents.ACTION_LOGOUT);
	}

	@Override
	public void initListener() {
		mOpenLoc_CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				LocationController.getInstance().setCurrentSetting(isChecked);
			}
		});
	}

	@Override
	public Handler provideActivityHandler() {
		return PAApplication.getHandler();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		LocalBroadcasts.unregisterLocalReceiver(mReceiver);
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
		startActivity(LoginActivity.class);
	}
	
	@OnClickMethodInject(id = R.id.device_bind_logout_btn)
	void logout() {
		LoginManager.getInstance().logout();
	}

	private void updateUI() {
		// device token的显示逻辑
		if (BindController.getInstance().hasDeviceToken()) {
			mKeyInput_ET.setText(BindController.getInstance().getDeviceToken());
			ViewUtils.setViewVisible(mHideApp_Layout);
		} else {
			mKeyInput_ET.setText(null);
			ViewUtils.setViewGone(mHideApp_Layout);
		}
		
		// user info的显示逻辑
		LocalUserController userController = LocalUserController.getInstance();
		if (userController.isUserLogining()) {
			// 设置user name
			mUsername_TV.setText(userController.getUsername());
			// 设置积分
			
			// 设置到期时间
			
			ViewUtils.setViewVisible(mUserInfo_Layout);
		} else {
			ViewUtils.setViewGone(mUserInfo_Layout);
		}
		
		mKeyInput_ET.setFocusable(true);
		mKeyInput_ET.setFocusableInTouchMode(false);
	}

}
