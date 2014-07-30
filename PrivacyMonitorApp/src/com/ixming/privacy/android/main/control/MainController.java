package com.ixming.privacy.android.main.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ixming.base.common.LocalBroadcasts;
import org.ixming.base.common.controller.BaseController;
import org.ixming.base.common.task.HttpGetTask;
import org.ixming.base.network.HttpRes;
import org.ixming.base.network.json.GsonHelper;
import org.ixming.base.network.json.GsonPool;
import org.ixming.base.taskcenter.callback.OnLoadListener;
import org.ixming.base.taskcenter.entity.ReqBean;
import org.ixming.base.utils.android.LogUtils;

import com.ixming.privacy.android.common.DefNonProgressDisplayer;
import com.ixming.privacy.android.common.DefProgressDisplayer;
import com.ixming.privacy.android.common.LocalBroadcastIntents;
import com.ixming.privacy.android.common.model.RequestPiecer;
import com.ixming.privacy.android.main.adapter.ViewPersonAdapter;
import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;
import com.ixming.privacy.android.monitoring.model.LocationResponseData;
import com.ixming.privacy.android.monitoring.model.UserInfo;
import com.ixming.privacy.android.monitoring.model.UserResponseData;
import com.ixming.privacy.monitor.android.Config;
import com.ixming.privacy.monitor.android.PAApplication;

public class MainController extends BaseController {

	private static MainController sInstance;
	
	public static MainController getInstance() {
		synchronized (MainController.class) {
			if (null == sInstance) {
				sInstance = new MainController();
			}
			return sInstance;
		}
	}
	
	private final String TAG = MainController.class.getSimpleName();
	private final long ONEDAY = 24 * 60 * 60 * 1000;
	private final List<PrivacyLocaitonInfo> mLocationInfoList = new ArrayList<PrivacyLocaitonInfo>();
	private final Map<Long, List<PrivacyLocaitonInfo>> mDateTimeData = new HashMap<Long, List<PrivacyLocaitonInfo>>();
	
	private long mCurTime;
	private List<PrivacyLocaitonInfo> mCurData = null; 
	
	private MainController() {
	}
	
	private final long SHORTEST_TIME = 1 * 60 * 1000;
	private HttpGetTask mLocationTask;
	private long mLastCheckLocationTime;
	public void loadLocationInfoData() {
		long curTime = System.currentTimeMillis();
		if (!mUserChanged && curTime - mLastCheckLocationTime < SHORTEST_TIME) {
			checkLocationAndBroadcast(false);
			return ;
		}
		
		if (mUserChanged) {
			synchronized (mLocationInfoList) {
				mLocationInfoList.clear();
				
				calculate();
			}
		}
		
		if (null == mLocationTask) {
			mLocationTask = new HttpGetTask(new DefNonProgressDisplayer());
		}
		
		mLocationTask.execute(Config.URL_GET_LOCATION, RequestPiecer.getLocationJson(getCurUser().getId()),
				Config.MODE_GET_LOCATION, new OnLoadListener() {
			
			@Override
			public void onSuccess(Object obj, ReqBean reqMode) {
				HttpRes httpRes = (HttpRes) obj;
				try {
					String json = GsonHelper.getJson(httpRes);
					LocationResponseData result = GsonPool.getExposeRestrict().fromJson(
							json, LocationResponseData.class);
					
					LogUtils.d(TAG, "onSuccess result: " + result);
					if (null != result) {
						if (result.getStatus() == 200) {
							mLastCheckLocationTime = System.currentTimeMillis();
							
							synchronized (mLocationInfoList) {
								mLocationInfoList.clear();
								
								List<PrivacyLocaitonInfo> list = result.getData();
								if (null != list && !list.isEmpty()) {
									mLocationInfoList.addAll(list);
									Collections.sort(mLocationInfoList);
								}
								calculate();
							}
							
							PAApplication.getHandler().post(new Runnable() {
								
								@Override
								public void run() {
									checkLocationAndBroadcast(true);
								}
							});
						} else {
							checkLocationAndBroadcast(true);
						}
					}
				} catch (Exception e) {
					LogUtils.e(TAG, "onSuccess Exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(Object obj, ReqBean reqMode) {
				checkLocationAndBroadcast(true);
			}
		});
	}
	
	private void checkLocationAndBroadcast(boolean broadcast) {
		if (broadcast || !mLocationInfoList.isEmpty()) {
			LocalBroadcasts.sendLocalBroadcast(LocalBroadcastIntents.ACTION_UPDATE_LOCATION);
		}
	}
	
	private void calculate() {
		mDateTimeData.clear();
		if (mLocationInfoList.isEmpty()) {
			return ;
		}
		long lastDay = 0;
		long curDay = 0;
		List<PrivacyLocaitonInfo> curList = null;
		for (int i = 0; i < mLocationInfoList.size(); i++) {
			PrivacyLocaitonInfo info = mLocationInfoList.get(i);
			long datetime = info.getDatetime();
			curDay = datetime / ONEDAY;
			if (lastDay != curDay) {
				lastDay = curDay;
				mDateTimeData.put(curDay * ONEDAY, curList = new ArrayList<PrivacyLocaitonInfo>());
			} else {
				curList.add(info);
			}
		}
		LogUtils.d(TAG, "calculate mDateTimeData.size() = " + mDateTimeData.size());
	}
	
	public Collection<Long> getLocationDates() {
		return mDateTimeData.keySet();
	}
	
	public void setCurTime(long datetime) {
		mCurTime = (datetime / ONEDAY) * ONEDAY; // clear time, second, mills
		mCurData = mDateTimeData.get(mCurTime);
	}
	
	public long getCurTime() {
		return mCurTime;
	}
	
	public List<PrivacyLocaitonInfo> getCurData() {
		return mCurData;
	}
	
	
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>
	// get users
	private UserInfo mCurUser;
	private HttpGetTask mUserTask;
	private long mLastCheckUserTime;
	private List<UserInfo> mUserList;
	private boolean mUserChanged = false;
	public void checkUserData(final ViewPersonAdapter adapter) {
		long curTime = System.currentTimeMillis();
		if (curTime - mLastCheckUserTime < SHORTEST_TIME) {
			adapter.setData(mUserList);
			adapter.notifyDataSetChanged();
			return;
		}
		if (null == mUserTask) {
			mUserTask = new HttpGetTask(new DefProgressDisplayer() {
				@Override
				protected void onCancel() {
					// do nothing
					mUserTask.cancel();
				}
			});
		}
		
		mUserTask.execute(Config.URL_GET_USER, RequestPiecer.getUserJson(), Config.MODE_GET_USER,
			new OnLoadListener() {
				
				@Override
				public void onSuccess(Object obj, ReqBean reqMode) {
					HttpRes httpRes = (HttpRes) obj;
					try {
						String json = GsonHelper.getJson(httpRes);
						UserResponseData result = GsonPool.getExposeRestrict().fromJson(
								json, UserResponseData.class);
						
						LogUtils.d(TAG, "onSuccess result: " + result);
						if (null != result) {
							if (result.getStatus() == 200) {
								mLastCheckUserTime = System.currentTimeMillis();
								mUserList = result.getData();
								
								PAApplication.getHandler().post(new Runnable() {
									
									@Override
									public void run() {
										adapter.setData(mUserList);
										adapter.notifyDataSetChanged();
									}
								});
							} else {
								
							}
						}
					} catch (Exception e) {
						LogUtils.e(TAG, "onSuccess Exception: " + e.getMessage());
						e.printStackTrace();
					}
				}
				
				@Override
				public void onError(Object obj, ReqBean reqMode) {
					
				}
			});
	}
	
	public void setCurrentUser(UserInfo user) {
		if (null == mCurUser) {
			mUserChanged = true;
		} else {
			mUserChanged = !mCurUser.equals(user);
		}
		mCurUser = user;
	}
	
	public UserInfo getCurUser() {
		return mCurUser;
	}
	
}
