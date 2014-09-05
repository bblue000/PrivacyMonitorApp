package com.zhaoni.findyou.android.main.fragment;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.BaseFragment;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bodong.dianjinweb.DianJinPlatform;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.CustomDialogBuilder;
import com.zhaoni.findyou.android.common.DropDownPop;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents;
import com.zhaoni.findyou.android.common.control.BindController;
import com.zhaoni.findyou.android.common.control.LocationController;
import com.zhaoni.findyou.android.common.dianjin.DianjinUtils;
import com.zhaoni.findyou.android.login.activity.LoginActivity;
import com.zhaoni.findyou.android.login.manager.LoginManager;
import com.zhaoni.findyou.android.main.adapter.LocationIntervalAdapter;
import com.zhaoni.findyou.android.main.manager.BindManager;
import com.zhaoni.findyou.android.main.model.DatetimeUtils;
import com.zhaoni.findyou.android.main.model.PayInfo;
import com.zhaoni.findyou.android.main.view.SettingsSwicher;

public class BindFragment extends BaseFragment implements
		ListView.OnItemClickListener {

	@ViewInject(id = R.id.device_bind_obtain_et)
	private EditText mKeyInput_ET;
	@ViewInject(id = R.id.device_bind_obtain_btn)
	private Button mObtain_BT;

	@ViewInject(id = R.id.device_bind_open_loc_cb)
	private SettingsSwicher mOpenLoc_CB;
	BindManager manager;
	// 隐藏应用
	@ViewInject(id = R.id.device_bind_hide_btn)
	private Button mHideApp_BT;
	@ViewInject(id = R.id.device_bind_login_btn)
	private Button login_BT;
	@ViewInject(id = R.id.device_bind_logout_btn)
	private Button logout_BT;

	// 用户信息布局
	// @ViewInject(id = R.id.device_bind_user_info_layout)
	// private View mUserInfo_Layout;
	// @ViewInject(id = R.id.device_bind_user_name_tv)
	// private TextView mUsername_TV;
	// @ViewInject(id = R.id.device_bind_user_expire_tv)
	// private TextView mExpire_TV;
	@ViewInject(id = R.id.device_bind_device_expire_tv)
	private TextView mDeviceExpire_TV;

	@ViewInject(id = R.id.device_bind_loc_freq_value_tv)
	private TextView mLocationInterval_TV;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (LocalBroadcastIntents.ACTION_LOGIN.equals(action)
					|| LocalBroadcastIntents.ACTION_LOGOUT.equals(action)) {
				updateUI();
			} else if (DianjinUtils.DIANJIN_ACTIVED_SUCCESSS_ACTION
					.equals(action)) {
				PayInfo payInfo = new PayInfo();
				Message msg = handler.obtainMessage();
				msg.what = BindManager.DEVICE_DATA_MSG;
				payInfo.setStatus(intent.getIntExtra(
						DianjinUtils.DIANJIN_STATUS_KEY, 1));
				payInfo.setExpirationdate(intent.getLongExtra(
						DianjinUtils.DIANJIN_NEW_DATE_KEY, 0));
				msg.obj = payInfo;
				handler.sendMessage(msg);
			}

		}
	};

	@Override
	public int provideLayoutResId() {
		return R.layout.fragment_settings;
	}

	@Override
	public void initView(View view) {
		/**
		 * 初始化点金广告平台
		 */
		DianJinPlatform.initialize(getActivity(), 57132,
				"10358b1bd5915b5e931d77d820a17247");
		DianJinPlatform.hideFloatView(getActivity());
		DianjinUtils.initCallBack();
		updateUI();
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		LocalBroadcasts.registerLocalReceiver(mReceiver,
				LocalBroadcastIntents.ACTION_LOGIN,
				LocalBroadcastIntents.ACTION_LOGOUT,
				DianjinUtils.DIANJIN_ACTIVED_SUCCESSS_ACTION);

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
	}

	@Override
	public Handler provideActivityHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case BindManager.DEVICE_DATA_MSG:
					PayInfo payInfo = (PayInfo) msg.obj;
					long theDate = payInfo.getExpirationdate();
					Date date = new Date(theDate);
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String reTime = format.format(date);
					mDeviceExpire_TV.setText(reTime);
					if (payInfo.getStatus() == 0) {
						mDeviceExpire_TV.setTextColor(Color.RED);
					}
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
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == CustomDialogBuilder.BUTTON_LEFT) {
					PAApplication.hideApp();
					getActivity().finish();
					PAApplication.killProcess();
				}
			}
		};
		new CustomDialogBuilder(context).title(R.string.dialog_prompt)
				.text(R.string.device_bind_hide_app_tip)
				.leftBtn(R.string.confirm, listener)
				.rightBtn(R.string.cancel, null).build().show();

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

	@OnClickMethodInject(id = R.id.device_bind_user_free_exchange_btn)
	// 获取免费时间
	void gotoFreeExchange() {
		DianJinPlatform.showOfferWall(getActivity());
	}

	private DropDownPop mLocationIntervalDrop;
	private LocationIntervalAdapter mLocationIntervalAdapter;

	@OnClickMethodInject(id = R.id.device_bind_loc_freq_value_tv)
	void chooseFreq() {
		if (null == mLocationIntervalDrop) {
			mLocationIntervalDrop = new DropDownPop(context);
		}
		if (null == mLocationIntervalAdapter) {
			mLocationIntervalAdapter = new LocationIntervalAdapter(context);
		}
		if (mLocationIntervalDrop.isShowing()) {
			mLocationIntervalDrop.dismiss();
		} else {
			mLocationIntervalDrop.showAsDropDown(mLocationInterval_TV,
					mLocationIntervalAdapter, this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LocationController.getInstance().setLocationInterval(
				mLocationIntervalAdapter.getItem(position));
		updateUI();
	}

	private void updateUI() {
		// device token的显示逻辑
		if (BindController.getInstance().hasDeviceToken()) {
			mKeyInput_ET.setText(BindController.getInstance().getDeviceToken());

		} else {
			mKeyInput_ET.setText(null);

		}

		// 设置是否开启定位
		mOpenLoc_CB.setChecked(LocationController.getInstance()
				.getLocationSetting());

		mLocationInterval_TV.setText(DatetimeUtils
				.simpleFixTime(LocationController.getInstance()
						.getLocationInterval()));
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
