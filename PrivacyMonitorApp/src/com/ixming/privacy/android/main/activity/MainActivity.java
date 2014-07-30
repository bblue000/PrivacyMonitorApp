package com.ixming.privacy.android.main.activity;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.android.ToastUtils;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.ResTargetType;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ResInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ixming.privacy.android.common.DropDownPop;
import com.ixming.privacy.android.main.adapter.ViewPersonAdapter;
import com.ixming.privacy.android.main.control.MainController;
import com.ixming.privacy.android.monitoring.model.UserInfo;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class MainActivity extends BaseActivity {

	@ViewInject(id = R.id.main_view_person_layout)
	private View mViewPerson_Layout;
	@ViewInject(id = R.id.main_view_person_tv, parentId = R.id.main_view_person_layout)
	private TextView mViewPerson_TV;
	
	@ViewInject(id = R.id.main_view_phone_btn)
	private Button mPrivacyPhone_BT;
	@ViewInject(id = R.id.main_view_sms_btn)
	private Button mPrivacySms_BT;
	@ViewInject(id = R.id.main_view_location_btn)
	private Button mPrivacyLocation_BT;
	
	@ResInject(id = R.string.main_view_person_progress_hint, type = ResTargetType.String)
	private String mEmptyUserHint;
	
	private DropDownPop mDropDownPop;
	
	private MainController mMainController;
	private ViewPersonAdapter mViewPersonAdapter;
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_main;
	}

	@Override
	public void initView(View view) {
		mDropDownPop = new DropDownPop(context);
		
		ViewUtils.setViewGone(mPrivacyPhone_BT);
		ViewUtils.setViewGone(mPrivacySms_BT);
		ViewUtils.setViewVisible(mPrivacyLocation_BT);
	}

	@Override
	public void initListener() {
		
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mMainController = MainController.getInstance();
		mViewPersonAdapter = new ViewPersonAdapter(context);
	}

	@Override
	public Handler provideActivityHandler() {
		return new Handler();
	}

	@Override
	public void onClick(View v) {
		
	}
	
	@OnClickMethodInject(id = R.id.main_view_person_layout)
	void toggleViewPersonState() {
		if (mDropDownPop.isShowing()) {
			mDropDownPop.dismiss();
		} else {
			mMainController.checkUserData(mViewPersonAdapter);
			
			mDropDownPop.showAsDropDown(mViewPerson_Layout, mViewPersonAdapter,
					mEmptyUserHint, new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					UserInfo user = mViewPersonAdapter.getItem(position);
					mMainController.setCurrentUser(user);
					
					mViewPerson_TV.setText(getString(R.string.main_view_person_current, 
							user.getUsefulName()));
				}
			});
		}
	}
	
	@OnClickMethodInject(id = R.id.main_view_phone_btn)
	void gotoViewPhone() {
		if (checkHasUserSelected()) {
		}
	}
	
	@OnClickMethodInject(id = R.id.main_view_sms_btn)
	void gotoViewSms() {
		if (checkHasUserSelected()) {
		}
	}
	
	@OnClickMethodInject(id = R.id.main_view_location_btn)
	void gotoViewLocation() {
		if (checkHasUserSelected()) {
			startActivity(PrivacyLocationDateActivity.class);
		}
	}
	
	private boolean checkHasUserSelected() {
		if (null == mMainController.getCurUser()) {
			ToastUtils.showToast(R.string.main_view_persion_none_hint);
			return false;
		}
		return true;
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			PAApplication.killProcess();
			return true;
		}
		return false;
	};
}
