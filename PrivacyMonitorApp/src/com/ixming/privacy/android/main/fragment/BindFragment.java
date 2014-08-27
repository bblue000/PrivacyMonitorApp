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
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bodong.dianjinweb.DianJinPlatform;
import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.common.control.BindController;
import com.ixming.privacy.android.common.control.LocalUserController;
import com.ixming.privacy.android.common.control.LocationController;
import com.ixming.privacy.android.login.activity.LoginActivity;
import com.ixming.privacy.android.login.manager.LoginManager;
import com.ixming.privacy.android.main.manager.BindManager;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class BindFragment extends BaseFragment {

	@ViewInject(id = R.id.device_bind_obtain_et)
	private EditText mKeyInput_ET;
	@ViewInject(id = R.id.device_bind_obtain_btn)
	private Button mObtain_BT;

	@ViewInject(id = R.id.device_bind_open_loc_cb)
	private CheckBox mOpenLoc_CB;
	AQuery aq;
	BindManager manager;
	// 隐藏应用
	@ViewInject(id = R.id.device_bind_hide_layout)
	private View mHideApp_Layout;
	@ViewInject(id = R.id.device_bind_hide_btn)
	private Button mHideApp_BT;
	@ViewInject(id = R.id.device_bind_login_btn)
	private Button login_BT;
	@ViewInject(id = R.id.device_bind_logout_btn)
	private Button logout_BT;

	// 用户信息布局
	@ViewInject(id = R.id.device_bind_user_info_layout)
	private View mUserInfo_Layout;
	@ViewInject(id = R.id.device_bind_user_name_tv)
	private TextView mUsername_TV;
	@ViewInject(id = R.id.device_bind_user_expire_tv)
	private TextView mExpire_TV;
	@ViewInject(id = R.id.device_bind_device_expire_tv)
	private TextView mDeviceExpire_TV;

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
		LocalBroadcasts.registerLocalReceiver(mReceiver,
				LocalBroadcastIntents.ACTION_LOGIN,
				LocalBroadcastIntents.ACTION_LOGOUT);
		manager = new BindManager(handler);
		manager.requestDate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DianJinPlatform.destory(getActivity());
		LocalBroadcasts.unregisterLocalReceiver(mReceiver);
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
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case BindManager.DEVICE_DATA_MSG:
					String date = (String) msg.obj;
					mDeviceExpire_TV.setText(date);
					break;
				}
			}
		};
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
		updateUI();
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

		/**
		 * 该代码 1.0不放开
		 */
		// user info的显示逻辑
		// LocalUserController userController =
		// LocalUserController.getInstance();
		// if (userController.isUserLogining()) {
		// // 设置user name
		// mUsername_TV.setText(userController.getUsername());
		// // 设置到期时间
		//
		// ViewUtils.setViewVisible(mUserInfo_Layout);
		// ViewUtils.setViewVisible(logout_BT);
		// ViewUtils.setViewGone(login_BT);
		// } else {
		// ViewUtils.setViewGone(mUserInfo_Layout);
		// ViewUtils.setViewVisible(login_BT);
		// ViewUtils.setViewGone(logout_BT);
		// }

		mKeyInput_ET.setFocusable(true);
		mKeyInput_ET.setFocusableInTouchMode(false);
	}

}
