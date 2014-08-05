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
import com.ixming.privacy.android.main.activity.PersonLocationV3Activity;
import com.ixming.privacy.android.main.adapter.LocationDateAdapter;
import com.ixming.privacy.android.main.adapter.MonitoredPersonAdapter;
import com.ixming.privacy.android.main.control.PersonListController;
import com.ixming.privacy.android.main.control.PersonController;
import com.ixming.privacy.android.main.model.MonitoredPerson;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class PersionListFragment extends BaseFragment
implements OnItemClickListener, PersonListController.MonitoringPersonLoadListener,
PersonController.LocationDataLoadListener {

	@ViewInject(id = R.id.person_list_selected_layout)
	private View mPersonSel_Layout;
	@ViewInject(id = R.id.person_list_selected_tv)
	private TextView mPersonSel_TV;
	@ViewInject(id = R.id.person_list_selected_add_btn)
	private Button mPersonSelAdd_BT;
	@ViewInject(id = R.id.person_list_date_lv)
	private ListView mPersonDate_LV;
	@ViewInject(id = R.id.person_list_date_empty_tv)
	private View mPersonDateEmpty_TV;
	@ViewInject(id = R.id.person_list_operate_layout)
	private View mPersonOperate_Layout;
	
	private DropDownPop mDropDownPop;
	
	private MonitoredPersonAdapter mMonitoredPersonAdapter;
	private LocationDateAdapter mLocationDateAdapter;
	
	@Override
	public int provideLayoutResId() {
		return R.layout.person_list;
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mDropDownPop = new DropDownPop(context);
		mLocationDateAdapter = new LocationDateAdapter(context);
		mPersonDate_LV.setAdapter(mLocationDateAdapter);
		PersonListController.getInstance().setCurrentMonitoringPerson(null);
		
		updateCurrentMonitoringUI();
		
		// request my monitoring person
		PersonListController.getInstance().requestMonitoringPerson(this);
	}

	@Override
	public void initListener() {
		mPersonDate_LV.setOnItemClickListener(this);
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
	
	@OnClickMethodInject(id = R.id.person_list_selected_add_btn)
	void addPerson() {
		
	}
	
	@OnClickMethodInject(id = R.id.person_list_delete_btn)
	void deleteCurrentPerson() {
		
	}
	
	@OnClickMethodInject(id = R.id.person_list_update_btn)
	void updateCurrentPerson() {
		
	}
	
	private void updateCurrentMonitoringUI() {
		if (null == PersonListController.getInstance().getCurrentPersonController()) {
			ViewUtils.setViewGone(mPersonDate_LV);
			ViewUtils.setViewGone(mPersonDateEmpty_TV);
			ViewUtils.setViewGone(mPersonOperate_Layout);
		} else {
			if (mLocationDateAdapter.isEmpty()) {
				ViewUtils.setViewGone(mPersonDate_LV);
				ViewUtils.setViewVisible(mPersonDateEmpty_TV);
			} else {
				ViewUtils.setViewVisible(mPersonDate_LV);
				ViewUtils.setViewGone(mPersonDateEmpty_TV);
			}
			ViewUtils.setViewVisible(mPersonOperate_Layout);
		}
	}
	
	private void hideCurrentMonitoringTmp() {
		ViewUtils.setViewGone(mPersonDate_LV);
		ViewUtils.setViewGone(mPersonDateEmpty_TV);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == mPersonDate_LV) {
			gotoTarget(mLocationDateAdapter.getItem(position));
		} else {
			setCurrentMonitoringPerson(mMonitoredPersonAdapter.getItem(position));
		}
	}
	
	private void gotoTarget(long time) {
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		if (null == personController) {
			return ;
		}
		personController.setCurTime(time);
		getFragmentActivity().startActivity(PersonLocationV3Activity.class);
	}
	
	private void setCurrentMonitoringPerson(MonitoredPerson person) {
		PersonListController.getInstance().setCurrentMonitoringPerson(person);
		
		if (null == person) {
			mPersonSel_TV.setText(null);
		} else {
			mPersonSel_TV.setText(person.getNote_name());
		}
		
		updateCurrentMonitoringUI();
		
		// request current monitoring person's data
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		if (null == personController) {
			
		} else {
			// 隐藏列表和列表为空时的界面
			hideCurrentMonitoringTmp();
			personController.requestLoactionData(this);
		}
		
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
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		if (null == personController) {
			mLocationDateAdapter.setData(null);
		} else {
			mLocationDateAdapter.setData(personController.getLocationDates());
		}
		mLocationDateAdapter.notifyDataSetChanged();
		updateCurrentMonitoringUI();
	}

	@Override
	public void onLocationLoadFailed() {
		mLocationDateAdapter.setData(null);
		mLocationDateAdapter.notifyDataSetChanged();
		updateCurrentMonitoringUI();
	}


}
