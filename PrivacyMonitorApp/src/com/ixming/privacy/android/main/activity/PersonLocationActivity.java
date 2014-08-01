package com.ixming.privacy.android.main.activity;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.inject4android.annotation.ViewInject;

import com.baidu.mapapi.map.MapView;
import com.ixming.privacy.android.main.control.PersonController;
import com.ixming.privacy.android.main.control.PersonListController;
import com.ixming.privacy.android.main.view.VSeekBar;
import com.ixming.privacy.monitor.android.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class PersonLocationActivity extends BaseActivity {

	@ViewInject(id = R.id.person_location_datetime_sb)
	private VSeekBar mTime_SB;
	@ViewInject(id = R.id.person_location_mv)
	private MapView mMapView;
	@ViewInject(id = R.id.person_location_title_tv)
	private TextView mTitle_TV;
	
	private PersonController mPersonController;
	@Override
	public int provideLayoutResId() {
		return R.layout.person_location;
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mPersonController = PersonListController.getInstance().getCurrentPersonController();
		if (null == mPersonController) {
			finish();
			return ;
		}
		
		
	}

	@Override
	public void initListener() {
	}

	@Override
	public Handler provideActivityHandler() {
		return null;
	}

}
