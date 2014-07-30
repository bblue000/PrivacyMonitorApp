package com.ixming.privacy.android.main.activity;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.android.ToastUtils;
import org.ixming.inject4android.annotation.ViewInject;

import com.ixming.privacy.android.main.adapter.ViewPersonAdapter;
import com.ixming.privacy.android.main.control.MainController;
import com.ixming.privacy.android.monitoring.model.UserInfo;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UserListActivity extends BaseActivity {

	@ViewInject(id = R.id.main_view_person_lv)
	private ListView mUser_LV;
	
	private MainController mMainController;
	private ViewPersonAdapter mViewPersonAdapter;
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_user_list;
	}

	@Override
	public void initView(View view) {
		
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mMainController = MainController.getInstance();
		mViewPersonAdapter = new ViewPersonAdapter(context);
		
		mUser_LV.setAdapter(mViewPersonAdapter);
		
		mMainController.checkUserData(mViewPersonAdapter);
	}
	
	@Override
	public void initListener() {
		mUser_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserInfo user = mViewPersonAdapter.getItem(position);
				mMainController.setCurrentUser(user);
				
				gotoViewLocation();
			}
		});
	}

	@Override
	public Handler provideActivityHandler() {

		return null;
	}
	
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
