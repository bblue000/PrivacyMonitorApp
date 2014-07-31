package com.ixming.privacy.android.main.fragment;

import org.ixming.base.common.activity.BaseFragment;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ixming.privacy.android.common.DropDownPop;
import com.ixming.privacy.android.main.adapter.LocationDateAdapter;
import com.ixming.privacy.android.main.adapter.MonitoredPersonAdapter;
import com.ixming.privacy.android.main.control.PersonListController;
import com.ixming.privacy.android.main.control.PersonController;
import com.ixming.privacy.android.main.model.MonitoredPerson;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class PersionListFragment extends BaseFragment
implements OnItemClickListener, PersonListController.MonitoringPersonLoadListener,
PersonController.LoactionDataLoadListener {

	@ViewInject(id = R.id.person_list_selected_layout)
	private View mPersonSel_Layout;
	@ViewInject(id = R.id.person_list_selected_tv)
	private TextView mPersonSel_TV;
	@ViewInject(id = R.id.person_list_selected_operate_btn)
	private Button mPersonSelOp_BT;
	@ViewInject(id = R.id.person_list_date_lv)
	private ListView mPersonDate_LV;
	@ViewInject(id = R.id.person_list_delete_btn)
	private Button mPersonDelete_BT;
	
	private DropDownPop mDropDownPop;
	
	private MonitoredPersonAdapter mMonitoredPersonAdapter;
	private LocationDateAdapter mLocationDateAdapter;
	
	@Override
	public int provideLayoutResId() {
		return R.layout.person_list;
	}

	@Override
	public void initView(View view) {
		updateCurrentMonitoringUI();
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mDropDownPop = new DropDownPop(context);
		// request my monitoring person
		PersonListController.getInstance().requestMonitoringPerson(this);
	}

	@Override
	public void initListener() {
		
	}

	@Override
	public Handler provideActivityHandler() {
		return PAApplication.getHandler();
	}
	
	@OnClickMethodInject(id = R.id.person_list_selected_layout)
	void choosePerson() {
		if (mDropDownPop.isShowing()) {
			mDropDownPop.dismiss();
		} else {
			if (null == mMonitoredPersonAdapter) {
				mMonitoredPersonAdapter = new MonitoredPersonAdapter(context);
			}
			mMonitoredPersonAdapter.setData(PersonListController.getInstance().getMonitoringPersonList());
			mDropDownPop.showAsDropDown(mPersonSel_Layout, mMonitoredPersonAdapter, "暂时没有", this);
		}
	}
	
	@OnClickMethodInject(id = R.id.person_list_selected_operate_btn)
	void operateCurrentPerson() {
		
	}
	
	@OnClickMethodInject(id = R.id.person_list_delete_btn)
	void deleteCurrentPerson() {
		
	}
	
	private void updateCurrentMonitoringUI() {
		if (null == PersonListController.getInstance().getCurrentMonitoringPerson()) {
			ViewUtils.setViewGone(mPersonDate_LV);
			ViewUtils.setViewGone(mPersonDelete_BT);
		} else {
			ViewUtils.setViewVisible(mPersonDate_LV);
			ViewUtils.setViewVisible(mPersonDelete_BT);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setCurrentMonitoringPerson(mMonitoredPersonAdapter.getItem(position));
	}
	
	private void setCurrentMonitoringPerson(MonitoredPerson person) {
		if (null == person) {
			return ;
		}
		mPersonSel_TV.setText(person.getNote_name());
		
		PersonListController.getInstance().setCurrentMonitoringPerson(person);
		updateCurrentMonitoringUI();
		// request current monitoring person's data
		
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		if (null == personController) {
			return ;
		}
		personController.requestLoactionData(this);
	}
	
	@Override
	public void onMPLoadSuccess() {
		mMonitoredPersonAdapter.setData(PersonListController.getInstance().getMonitoringPersonList());
		mMonitoredPersonAdapter.notifyDataSetChanged();
	}

	@Override
	public void onMPLoadFailed() {
		
	}

	@Override
	public void onLocationLoadSuccess() {
		if (null == mLocationDateAdapter) {
			mLocationDateAdapter = new LocationDateAdapter(context);
			mPersonDate_LV.setAdapter(mLocationDateAdapter);
		}
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		if (null == personController) {
			return ;
		}
		mLocationDateAdapter.setData(personController.getLocationDates());
	}

	@Override
	public void onLocationLoadFailed() {
		
	}


}
