package com.zhaoni.findyou.android.main.activity;

import java.util.LinkedList;
import java.util.List;

import org.ixming.base.utils.android.ToastUtils;
import org.ixming.inject4android.annotation.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.activity.MyBaseActivity;
import com.zhaoni.findyou.android.main.adapter.TimeLineAdapter;
import com.zhaoni.findyou.android.main.control.PersonController;
import com.zhaoni.findyou.android.main.control.PersonListController;
import com.zhaoni.findyou.android.main.model.DatetimeUtils;
import com.zhaoni.findyou.android.main.model.RespLocation;
import com.zhaoni.findyou.android.main.view.TimeSeekListView;

public class PersonLocationV3Activity extends MyBaseActivity implements BaiduMap.OnMapLoadedCallback {

	@ViewInject(id = R.id.person_location_title_name_tv)
	private TextView mTitleName_TV;
	@ViewInject(id = R.id.person_location_title_date_tv)
	private TextView mTitleDate_TV;
	@ViewInject(id = R.id.person_location_curtime_tv)
	private TextView mCurTime_TV;
//	@ViewInject(id = R.id.person_location_datetime_sb)
//	private CustomVSeekBar mTime_SB;
//	@ViewInject(id = R.id.person_location_datetime_tsv)
//	private TimeSeekView mTime_TSV;
	@ViewInject(id = R.id.person_location_datetime_lv)
	private TimeSeekListView mTime_LV;
	@ViewInject(id = R.id.person_location_map_container)
	private ViewGroup mMapContainer_Layout;
	
	private MapView mMapView;
	
	private BaiduMap mBaiduMap;
	
	private PersonController mPersonController;
	
	private PersonLocationV3 mPersonLocation;
	
	private TimeLineAdapter mTimeLineAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        // 注意：该方法要在setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());	
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public int provideLayoutResId() {
		return R.layout.person_location;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initView(View view) {
//		ViewUtils.setViewGone(mTime_SB);
//		mTime_SB.setChangingWhenDragging(true);
		
		BaiduMapOptions options = new BaiduMapOptions();
		options.compassEnabled(false)
			.mapType(BaiduMap.MAP_TYPE_NORMAL)
			.overlookingGesturesEnabled(false)
			.rotateGesturesEnabled(false)
			.zoomControlsEnabled(false)
			.zoomGesturesEnabled(true)
			.scaleControlEnabled(true)
			.scrollGesturesEnabled(true)
			;
		mMapView = new MapView(context, options);
		mMapView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mMapContainer_Layout.addView(mMapView);
		
		mBaiduMap = mMapView.getMap();
		
		//普通地图  
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setOnMapLoadedCallback(this);
		mBaiduMap.setMyLocationEnabled(false);
		
		
//		/**
//         *  设置地图是否响应点击事件  .
//         */
//        mMapController.enableClick(true);
//        //设置指南针显示在左上角
//        mMapController.setCompassMargin(100, 100);
//        
//        mMapView.setDoubleClickZooming(true);
//        mMapView.setBuiltInZoomControls(false);
//        
//        mMapController.setZoom(14);
		
		
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mPersonController = PersonListController.getInstance().getCurrentPersonController();
		if (null == mPersonController) {
			finish();
			return ;
		}
		
//		ViewUtils.setViewVisible(mTime_SB);
		
		mPersonLocation = new PersonLocationV3(mPersonController);
//		mTime_TSV.setPersonLocationV3(mPersonLocation);
		
		mTimeLineAdapter = new TimeLineAdapter(context);
		mTime_LV.setAdapter(mTimeLineAdapter.setData(mPersonLocation.infoList));
		updateTitleTime(mPersonController.getCurTime());
	}

	@Override
	public void initListener() {
//		mTime_SB.setOnSeekBarChangeListener(new CustomVSeekBar.OnSeekBarChangeListener() {
//			@Override
//			public void onProgressChanged(CustomVSeekBar seekBar, int progress,
//					int max) {
//				if (null == mPersonLocation) {
//					return ;
//				}
//				long datetime = mPersonLocation.startTime + ((mPersonLocation.endTime - mPersonLocation.startTime)
//						* progress) / max;
//				
//				handler.removeMessages(0);
//				handler.sendMessageDelayed(Message.obtain(handler, 0, datetime), 25);
//			}
//
//			@Override
//			public void onCursorPositionChanged(CustomVSeekBar seekBar, int yPosInGlobal) {
//			}
//			
//		});
		
		mTime_LV.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (null == mPersonLocation || null == mTime_LV.getAdapter()) {
					return ;
				}
				final int adapterCount = mTime_LV.getAdapter().getCount();
				// 查找出第一个符合条件的Item
				 // 获得了第一个显示的Location Item
	            final int offset = mTime_LV.getSideHeight();
	            int firstVisibleLocationItem = -1;
	            RespLocation curLocation = null;
	            for (int i = 0; i < view.getChildCount(); i++) {
	            	View childView = view.getChildAt(i);
	                if (childView.getVisibility() != View.GONE) {
	                    int bottom = childView.getBottom() - view.getPaddingTop();
	                    if (bottom >= offset) {
	                    	Object item = view.getAdapter().getItem(firstVisibleItem + i);
	                    	if (item instanceof RespLocation) {
	                    		firstVisibleLocationItem = firstVisibleItem + i;
	                    		curLocation = (RespLocation) item;
	                    		break;
	                    	}
	                    }
	                }
	            }
	            
	            if (firstVisibleLocationItem < 0) {
	            	return ;
	            }
	            
	            int current = Math.max(0, Math.min(firstVisibleLocationItem - 1, adapterCount - 2 - 1));
//				Log.e("yytest", "totalItemCount = " + totalItemCount);
//				Log.e("yytest", "adapter count = " + mTimeLineAdapter.getCount());
//				Log.e("yytest", "view child count = " + view.getChildCount());
				Log.d("yytest", "current = " + current);
				Log.d("yytest", "firstVisibleLocationItem = " + firstVisibleLocationItem);
	            
				if (mTimeLineAdapter.setCurrent(current)) {
					handler.removeMessages(0);
					handler.sendMessageDelayed(Message.obtain(handler, 0, curLocation.getDate_time()), 500);
				}
				
			}
		});
	}
	
	@Override
	public Handler provideActivityHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					long datetime = (Long) msg.obj;
					invalidateUI(datetime);
					break;
				}
			}
		};
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
		handler.removeMessages(0);
	}
	
	private void updateTitleTime(long datetime) {
		mTitleName_TV.setText(mPersonController.getMonitoringPerson().getName());
		mTitleDate_TV.setText(DatetimeUtils.formatDate(datetime));
		
		mCurTime_TV.setText(DatetimeUtils.formatTime(datetime));
	}
	
	private MyOverlayManager mOverlayManager;
	private MarkerOptions mCurrentOverlay;
	private void invalidateUI(long datetime) {
		if (null == mOverlayManager) {
			mOverlayManager = new MyOverlayManager(mBaiduMap);
		}
		
		mBaiduMap.clear();
		
		mCurrentOverlay = null;
		LatLng gotoPos = null;
		@SuppressWarnings("unused")
		RespLocation locationInfo = null;
		long nearestTime = datetime;
		mOverlayManager.removeFromMap();
		mOverlayManager.clear();
		int i = 0;
		for (; i < mPersonLocation.infoList.size(); i++) {
			RespLocation info = mPersonLocation.infoList.get(i);
			if (info.getDate_time() > datetime) {
				break ;
			}
			
			nearestTime = info.getDate_time();
//			if (null != locationInfo && ObjectUtils.equals(locationInfo.getAddress(), info.getAddress())) {
//				continue;
//			}
			
			if (null != mCurrentOverlay) {
//				mBaiduMap.addOverlay(mCurrentOverlay.icon(mOverlayManager.mOtherMarker));
				mOverlayManager.addMarkerOptions(mCurrentOverlay, false);
			}
			mCurrentOverlay = mOverlayManager.createMarker(mPersonLocation.points[i], info.getAddress());
			gotoPos = mPersonLocation.points[i];
			
			locationInfo = info;
		}
		
		if (null != mCurrentOverlay) {
//			mBaiduMap.addOverlay(mCurrentOverlay.icon(mOverlayManager.mCurrentMarker));
			mOverlayManager.addMarkerOptions(mCurrentOverlay, true);
//			mBaiduMap.addOverlay(new TextOptions().align(TextOptions.ALIGN_CENTER_HORIZONTAL, TextOptions.ALIGN_TOP)
//					.text(mCurrentOverlay.getTitle()).position(mCurrentOverlay.getPosition())
//					.fontSize(24));
		} else {
			
		}
		mOverlayManager.addToMap();
//		mOverlayManager.zoomToSpan();
		
		float zoom = Math.max(14.0f/*mBaiduMap.getMaxZoomLevel()*/, mBaiduMap.getMapStatus().zoom);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(gotoPos, zoom));
		
		updateTitleTime(nearestTime);
	}

	@Override
	public void onMapLoaded() {
		// set initialized zoom
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(14.0f));
		
		invalidateUI(mPersonLocation.startTime);
	}

	private class MyOverlayManager extends OverlayManager {
		//构建Marker图标  
		private BitmapDescriptor mOtherMarker = BitmapDescriptorFactory.fromResource(R.drawable.location_marker_alpha);  
		
		private List<OverlayOptions> mOverlayOptionsList = new LinkedList<OverlayOptions>();
		
		private TextView mCurrentMarker_TV;
		public MyOverlayManager(BaiduMap baiduMap) {
			super(baiduMap);
			baiduMap.setOnMarkerClickListener(this);
			
			mCurrentMarker_TV = new TextView(context);
			mCurrentMarker_TV.setTextColor(getResources().getColor(R.color.deep_sky_blue));
			mCurrentMarker_TV.setTextSize(16);
			float density = getResources().getDisplayMetrics().density;
			mCurrentMarker_TV.setShadowLayer(1.0f * density, 2.5f * density, 2.5f * density, getResources().getColor(R.color.color_88888888));
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mCurrentMarker_TV.setLayoutParams(lp);
			mCurrentMarker_TV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.location_marker);
			mCurrentMarker_TV.requestLayout();
		}
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker.getIcon() == mOtherMarker) {
				ToastUtils.showToast(marker.getTitle());
			} else {
			}
			return true;
		}

		@Override
		public List<OverlayOptions> getOverlayOptions() {
			return mOverlayOptionsList;
		}
		
		public void addMarkerOptions(MarkerOptions options, boolean isCur) {
			mCurrentMarker_TV.setText(options.getTitle());
			mOverlayOptionsList.add(options.icon(isCur ? BitmapDescriptorFactory.fromView(mCurrentMarker_TV) : mOtherMarker));
		}
		
		public void clear() {
			mOverlayOptionsList.clear();
		}
		
		public MarkerOptions createMarker(LatLng point, String title) {
			return new MarkerOptions().position(point).title(title);
		}
		
	}
}
