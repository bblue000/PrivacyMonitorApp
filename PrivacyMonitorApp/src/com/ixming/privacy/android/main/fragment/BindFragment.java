package com.ixming.privacy.android.main.fragment;

import org.ixming.base.common.activity.BaseFragment;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ixming.privacy.android.main.control.BindController;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class BindFragment extends BaseFragment implements BindController.RequestKeyCallback {

	@ViewInject(id = R.id.device_bind_obtain_et)
	private EditText mKeyInput_ET;
	@ViewInject(id = R.id.device_bind_obtain_btn)
	private Button mObtain_BT;
	@ViewInject(id = R.id.device_bind_hide_btn)
	private Button mHide_BT;
	
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
		
	}

	@Override
	public void initListener() {
		
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
		if (BindController.getInstance().hasKey()) {
			mKeyInput_ET.setText(BindController.getInstance().getCurrentKey());
			ViewUtils.setViewVisible(mHide_BT);
		} else {
			mKeyInput_ET.setText(null);
			ViewUtils.setViewInvisible(mHide_BT);
		}
		mKeyInput_ET.setFocusable(true);
		mKeyInput_ET.setFocusableInTouchMode(false);
	}

	@Override
	public void onKeyLoaded() {
		updateUI();
	}

	@Override
	public void onError() {
		
	}

}
