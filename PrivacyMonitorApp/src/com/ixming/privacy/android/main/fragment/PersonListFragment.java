package com.ixming.privacy.android.main.fragment;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.BaseFragment;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ixming.privacy.android.common.CustomDialogBuilder;
import com.ixming.privacy.android.common.CustomDialogOnClickListener;
import com.ixming.privacy.android.common.DropDownPop;
import com.ixming.privacy.android.common.LocalBroadcastIntents.MonitoringPerson;
import com.ixming.privacy.android.main.activity.PersonLocationV3Activity;
import com.ixming.privacy.android.main.adapter.LocationDateAdapter;
import com.ixming.privacy.android.main.adapter.MonitoredPersonAdapter;
import com.ixming.privacy.android.main.control.PersonListController;
import com.ixming.privacy.android.main.control.PersonController;
import com.ixming.privacy.android.main.model.MonitoredPerson;
import com.ixming.privacy.monitor.android.PAApplication;
import com.ixming.privacy.monitor.android.R;

public class PersonListFragment extends BaseFragment
implements OnItemClickListener, PersonController.LocationDataLoadListener {

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
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (MonitoringPerson.ACTION_DATA_CHANGED.equals(action)) {
				resetSelCurrentMonitoringPerson();
			}
		}
	};
	
	@Override
	public int provideLayoutResId() {
		return R.layout.person_list;
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		LocalBroadcasts.registerLocalReceiver(mReceiver,
				MonitoringPerson.ACTION_DATA_CHANGED,
				MonitoringPerson.ACTION_DATA_INVALIDATE);
		
		mDropDownPop = new DropDownPop(context);
		mLocationDateAdapter = new LocationDateAdapter(context);
		mPersonDate_LV.setAdapter(mLocationDateAdapter);
		
		resetSelCurrentMonitoringPerson();
		
		// request my monitoring person
		PersonListController.getInstance().requestMonitoringPerson();
	}

	@Override
	public void initListener() {
		mPersonDate_LV.setOnItemClickListener(this);
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
	
	@OnClickMethodInject(id = R.id.person_list_selected_layout)
	void choosePerson() {
		if (mDropDownPop.isShowing()) {
			mDropDownPop.dismiss();
		} else {
			if (null == mMonitoredPersonAdapter) {
				mMonitoredPersonAdapter = new MonitoredPersonAdapter(context);
			}
			mMonitoredPersonAdapter.setData(PersonListController.getInstance().getMonitoringPersonList());
			mDropDownPop.showAsDropDown(mPersonSel_Layout, mMonitoredPersonAdapter,
					getString(R.string.person_list_empty_tip), this);
		}
	}
	
	@OnClickMethodInject(id = R.id.person_list_selected_add_btn)
	void addPerson() {
		CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(context);
		customDialogBuilder.title(R.string.person_operate_add_title);
		
		customDialogBuilder.content(R.layout.dialog_content_add_person);
		View content = customDialogBuilder.content();
		final EditText deviceToken_ET = (EditText) content.findViewById(R.id.person_operate_add_device_token_et);
		final EditText name_ET = (EditText) content.findViewById(R.id.person_operate_add_name_et);
		
		customDialogBuilder.leftBtn(R.string.person_operate_add_btn, new CustomDialogOnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PersonListController.getInstance().addMonitoringPerson(name_ET.getText().toString()
							, deviceToken_ET.getText().toString());
				}

				@Override
				public boolean triggerDismiss() {
					if (TextUtils.isEmpty(deviceToken_ET.getText())) {
						deviceToken_ET.setError(getString(R.string.person_operate_add_device_token_empty_tip));
						return false;
					}
					if (TextUtils.isEmpty(name_ET.getText())) {
						name_ET.setError(getString(R.string.person_operate_add_name_empty_tip));
						return false;
					}
					return true;
				}
			});
		
		customDialogBuilder.build().show();
	}
	
	@OnClickMethodInject(id = R.id.person_list_delete_btn)
	void deleteCurrentPerson() {
		
	}
	
	@OnClickMethodInject(id = R.id.person_list_update_btn)
	void updateCurrentPerson() {
		final MonitoredPerson person = PersonListController.getInstance().getCurrentPersonController()
				.getMonitoringPerson();
		
		CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(context);
		customDialogBuilder.title(R.string.person_operate_update_title);
		
		customDialogBuilder.content(R.layout.dialog_content_update_person);
		View content = customDialogBuilder.content();
		final EditText name_ET = (EditText) content.findViewById(R.id.person_operate_update_name_et);
		name_ET.setText(person.getName());
		name_ET.setSelection(0, name_ET.getText().length());
		
		customDialogBuilder.leftBtn(R.string.person_operate_update_btn, new CustomDialogOnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PersonListController.getInstance().updateMonitoringPerson(person, name_ET.getText().toString());
				}

				@Override
				public boolean triggerDismiss() {
					if (TextUtils.isEmpty(name_ET.getText())) {
						name_ET.setError(getString(R.string.person_operate_update_name_empty_tip));
						return false;
					}
					return true;
				}
			});
		
		customDialogBuilder.build().show();
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
			userSelCurrentMonitoringPerson(mMonitoredPersonAdapter.getItem(position));
		}
	}
	
	private void gotoTarget(long time) {
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		personController.setCurTime(time);
		getFragmentActivity().startActivity(PersonLocationV3Activity.class);
	}
	
	private void userSelCurrentMonitoringPerson(MonitoredPerson person) {
		PersonListController.getInstance().setCurrentMonitoringPerson(person);
		
		setPersonSelName(person);
		
		updateCurrentMonitoringUI();
		
		loadLocationData();
	}
	
	private void setPersonSelName(MonitoredPerson person) {
		if (null == person) {
			mPersonSel_TV.setText(null);
		} else {
			mPersonSel_TV.setText(person.getName());
		}
	}
	
	private void loadLocationData() {
		// request current monitoring person's data
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		if (null == personController) {
			
		} else {
			// 隐藏列表和列表为空时的界面
			hideCurrentMonitoringTmp();
			personController.requestLocationData(this);
		}
	}
	
	private void resetSelCurrentMonitoringPerson() {
		// 更新数据
		MonitoredPerson person = null;
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		if (null != personController) {
			person = personController.getMonitoringPerson();
			mLocationDateAdapter.setData(personController.getLocationDates());
		} else {
			mLocationDateAdapter.setData(null);
		}
		
		setPersonSelName(person);
		
		mLocationDateAdapter.notifyDataSetChanged();
		updateCurrentMonitoringUI();
	}
	
	public void onMPLoadSuccess() {
		mMonitoredPersonAdapter.setData(PersonListController.getInstance().getMonitoringPersonList());
		mMonitoredPersonAdapter.notifyDataSetChanged();
	}

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
