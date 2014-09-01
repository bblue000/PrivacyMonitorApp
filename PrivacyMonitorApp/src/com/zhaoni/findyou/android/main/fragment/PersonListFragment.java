package com.zhaoni.findyou.android.main.fragment;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.activity.BaseFragment;
import org.ixming.base.utils.android.ToastUtils;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.CustomDialogBuilder;
import com.zhaoni.findyou.android.common.CustomDialogOnClickListener;
import com.zhaoni.findyou.android.common.DropDownPop;
import com.zhaoni.findyou.android.common.LocalBroadcastIntents.MonitoringPerson;
import com.zhaoni.findyou.android.main.activity.PersonLocationV3Activity;
import com.zhaoni.findyou.android.main.adapter.LocationDateAdapter;
import com.zhaoni.findyou.android.main.adapter.MonitoredPersonAdapter;
import com.zhaoni.findyou.android.main.control.PersonController;
import com.zhaoni.findyou.android.main.control.PersonListController;
import com.zhaoni.findyou.android.main.model.MonitoredPerson;
import com.zhaoni.findyou.android.main.view.PADrawerLayout;

public class PersonListFragment extends BaseFragment
implements OnItemClickListener, PersonController.LocationDataLoadListener {

	@ViewInject(id = R.id.person_list_top_layout)
	private View mTop_Layout;
	@ViewInject(id = R.id.person_list_selected_layout)
	private PADrawerLayout mPersonSel_Layout;
	@ViewInject(id = R.id.person_list_top_operate_layout)
	private View mTopOperate_Layout;
	
	@ViewInject(id = R.id.person_list_top_operate_add_btn, parentId = R.id.person_list_top_operate_layout)
	private View mTopOperateAdd_Layout;
	@ViewInject(id = R.id.person_list_top_operate_update_btn, parentId = R.id.person_list_top_operate_layout)
	private View mTopOperateUpdate_Layout;
	@ViewInject(id = R.id.person_list_top_operate_delete_btn, parentId = R.id.person_list_top_operate_layout)
	private View mTopOperateDelete_Layout;

	
	@ViewInject(id = R.id.person_list_selected_tv)
	private TextView mPersonSel_TV;
	@ViewInject(id = R.id.person_list_operate_btn)
	private View mPersonOperate_V;
	
	@ViewInject(id = R.id.person_list_date_lv)
	private ListView mPersonDate_LV;
	
	@ViewInject(id = R.id.person_list_date_empty_tv)
	private View mPersonDateEmpty_TV;
	
//	@ViewInject(id = R.id.person_list_operate_layout)
//	private View mPersonOperate_Layout;
	
	private DropDownPop mSelPersonDropDownPop;
	
	private PersonListController mPersonListController;
	
	private MonitoredPersonAdapter mMonitoredPersonAdapter;
	private LocationDateAdapter mLocationDateAdapter;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (MonitoringPerson.ACTION_SELECT_PERSON_CHANGED.equals(action)) {
				resetSelCurrentMonitoringPerson(true);
			}
		}
	};
	
	@Override
	public int provideLayoutResId() {
		return R.layout.person_list;
	}

	@Override
	public void initView(View view) {
		mPersonSel_Layout.setMinDrawerMargin(0);
		mPersonSel_Layout.setScrimColor(0x00000000);
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mPersonListController = PersonListController.getInstance();
		
		LocalBroadcasts.registerLocalReceiver(mReceiver,
				MonitoringPerson.ACTION_DATA_LIST_CHANGED,
				MonitoringPerson.ACTION_DATA_LIST_INVALIDATE,
				MonitoringPerson.ACTION_SELECT_PERSON_CHANGED);
		
		mSelPersonDropDownPop = new DropDownPop(context);
		
		mLocationDateAdapter = new LocationDateAdapter(context);
		mPersonDate_LV.setAdapter(mLocationDateAdapter);
		
		resetSelCurrentMonitoringPerson(false);
		
		// request my monitoring person
		mPersonListController.requestMonitoringPerson();
	}

	@Override
	public void initListener() {
		mPersonDate_LV.setOnItemClickListener(this);
		
		// 背景布局事件
		final View.OnTouchListener rootViewTouchListener = new View.OnTouchListener() {
			private Rect mRect = new Rect();
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (isDrawerOpen()) {
					if (mRect.isEmpty()) {
						mPersonSel_Layout.getGlobalVisibleRect(mRect);
					}
					if (!mRect.contains((int) event.getRawX(), (int) event.getRawY())) {
						hideDrawer();
						return true;
					}
				}
				return false;
			}
		};
		getRootView().setOnTouchListener(rootViewTouchListener);
		
		mPersonDate_LV.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (isDrawerOpen()) {
					// 如果抽屉布局已经打开，则转发给背景布局
					return rootViewTouchListener.onTouch(v, event);
				}
				return false;
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LocalBroadcasts.unregisterLocalReceiver(mReceiver);
	}
	
	@OnClickMethodInject(id = R.id.person_list_selected_inner_layout)
	void choosePerson() {
		if (mSelPersonDropDownPop.isShowing()) {
			mSelPersonDropDownPop.dismiss();
		} else {
			if (null == mMonitoredPersonAdapter) {
				mMonitoredPersonAdapter = new MonitoredPersonAdapter(context);
			}
			mMonitoredPersonAdapter.setData(mPersonListController.getMonitoringPersonList());
			mSelPersonDropDownPop.showAsDropDown(mTop_Layout, mPersonSel_Layout, mMonitoredPersonAdapter,
					getString(R.string.person_list_empty_tip), this);
		}
	}
	
	@OnClickMethodInject(id = R.id.person_list_operate_btn)
	void operatePerson() {
		if (mPersonListController.hasCurrentPerson()) {
			mTopOperateUpdate_Layout.setEnabled(true);
			mTopOperateDelete_Layout.setEnabled(true);
		} else {
			mTopOperateUpdate_Layout.setEnabled(false);
			mTopOperateDelete_Layout.setEnabled(false);
		}
		if (mPersonSel_Layout.isDrawerVisible(Gravity.RIGHT)) {
			hideDrawer();
		} else {
			showDrawer();
		}
		
	}
	
	private void hideDrawer() {
		if (isDrawerOpen()) {
			mPersonSel_Layout.closeDrawer(Gravity.RIGHT);
		}
	}
	
	private void showDrawer() {
		if (!isDrawerOpen()) {
			mPersonSel_Layout.openDrawer(Gravity.RIGHT);
		}
	}
	
	private boolean isDrawerOpen() {
		return mPersonSel_Layout.isDrawerVisible(Gravity.RIGHT);
	}
	
	@OnClickMethodInject(id = R.id.person_list_top_operate_add_btn, parentId = R.id.person_list_top_operate_layout)
	void addPerson() {
		hideDrawer();
		
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
			})
			.rightBtn(R.string.cancel, null);
		
		customDialogBuilder.build().show();
	}
	
//	@OnClickMethodInject(id = R.id.person_list_update_btn)
	@OnClickMethodInject(id = R.id.person_list_top_operate_update_btn, parentId = R.id.person_list_top_operate_layout)
	void updateCurrentPerson() {
		hideDrawer();
		
		final MonitoredPerson person = mPersonListController.getCurrentPersonController()
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
					mPersonListController.updateMonitoringPerson(person, name_ET.getText().toString());
				}

				@Override
				public boolean triggerDismiss() {
					if (TextUtils.isEmpty(name_ET.getText())) {
						name_ET.setError(getString(R.string.person_operate_update_name_empty_tip));
						return false;
					}
					if (name_ET.getText().toString().equals(person.getName())) {
						name_ET.setError(getString(R.string.person_operate_update_name_same_tip));
						return false;
					}
					return true;
				}
			});
		
		customDialogBuilder.build().show();
	}

//	@OnClickMethodInject(id = R.id.person_list_delete_btn)
	@OnClickMethodInject(id = R.id.person_list_top_operate_delete_btn, parentId = R.id.person_list_top_operate_layout)
	void deleteCurrentPerson() {
		hideDrawer();
		
		final MonitoredPerson person = mPersonListController.getCurrentPersonController()
				.getMonitoringPerson();
		
		CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(context);
		customDialogBuilder.title(R.string.dialog_prompt);
		
		customDialogBuilder.text(getString(R.string.person_operate_delete_text, person.getName()));
		customDialogBuilder.leftBtn(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPersonListController.deleteMonitoringPerson(person);
			}
			
		});
		
		customDialogBuilder.rightBtn(R.string.cancel, null);
		
		customDialogBuilder.build().show();
	}
	
	private void updateCurrentMonitoringUI() {
		if (!mPersonListController.hasCurrentPerson()) {
			ViewUtils.setViewGone(mPersonDate_LV);
			ViewUtils.setViewGone(mPersonDateEmpty_TV);
		} else {
			if (mLocationDateAdapter.isEmpty()) {
				ViewUtils.setViewGone(mPersonDate_LV);
				ViewUtils.setViewVisible(mPersonDateEmpty_TV);
			} else {
				ViewUtils.setViewVisible(mPersonDate_LV);
				ViewUtils.setViewGone(mPersonDateEmpty_TV);
			}
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
			mPersonListController.setCurrentMonitoringPerson(mMonitoredPersonAdapter.getItem(position));
		}
	}
	
	private void gotoTarget(long time) {
		PersonController personController = mPersonListController.getCurrentPersonController();
		personController.setCurTime(time);
		startActivity(PersonLocationV3Activity.class);
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
		PersonController personController = mPersonListController.getCurrentPersonController();
		if (null == personController) {
			
		} else {
			// 隐藏列表和列表为空时的界面
			hideCurrentMonitoringTmp();
			
			if (PersonListController.TEST) {
				onLocationLoadSuccess();
			} else {
				personController.requestLocationData(this);
			}
			
		}
	}
	
	private void resetSelCurrentMonitoringPerson(boolean loadData) {
		// 更新数据
		MonitoredPerson person = null;
		if (mPersonListController.hasCurrentPerson()) {
			PersonController personController = mPersonListController.getCurrentPersonController();
			person = personController.getMonitoringPerson();
			mLocationDateAdapter.setData(personController.getLocationDates());
		} else {
			mLocationDateAdapter.setData(null);
		}
		
		setPersonSelName(person);
		
		mLocationDateAdapter.notifyDataSetChanged();
		updateCurrentMonitoringUI();
		
		if (loadData) {
			loadLocationData();
		}
	}
	
	@Override
	public void onLocationLoadSuccess() {
		if (mPersonListController.hasCurrentPerson()) {
			mLocationDateAdapter.setData(mPersonListController.getCurrentPersonController().getLocationDates());
		} else {
			mLocationDateAdapter.setData(null);
		}
		mLocationDateAdapter.notifyDataSetChanged();
		updateCurrentMonitoringUI();
	}

	@Override
	public void onLocationLoadFailed() {
		// TODO 未必需要删除原先的数据，保留原先状态即可
//		mLocationDateAdapter.setData(null);
//		mLocationDateAdapter.notifyDataSetChanged();
		updateCurrentMonitoringUI();
		ToastUtils.showToast(R.string.person_location_data_obtain_error);
	}

}
