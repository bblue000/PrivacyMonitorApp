package com.ixming.privacy.android.main.activity;

import java.util.LinkedList;
import java.util.List;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.ObjectUtils;
import org.ixming.base.utils.android.ToastUtils;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import com.ixming.privacy.android.main.control.PersonController;
import com.ixming.privacy.android.main.control.PersonListController;
import com.ixming.privacy.android.main.model.DatetimeUtils;
import com.ixming.privacy.android.main.model.RespLocation;
import com.ixming.privacy.android.main.view.CustomVSeekBar;
import com.ixming.privacy.monitor.android.R;

public class PersonLocationV3Activity extends BaseActivity implements BaiduMap.OnMapLoadedCallback {

	@ViewInject(id = R.id.person_location_title_tv)
	private TextView mTitle_TV;
	@ViewInject(id = R.id.person_location_datetime_sb)
	private CustomVSeekBar mTime_SB;
	@ViewInject(id = R.id.person_location_map_container)
	private ViewGroup mMapContainer_Layout;
	
	private MapView mMapView;
	
	private BaiduMap mBaiduMap;
	
	private PersonController mPersonController;
	
	PersonLocationV3 mPersonLocation;
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

	@Override
	public void initView(View view) {
		ViewUtils.setViewGone(mTime_SB);
		
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
		
		ViewUtils.setViewVisible(mTime_SB);
		
		mPersonLocation = new PersonLocationV3(mPersonController);
		
		mTitle_TV.setText(mPersonController.getMonitoringPerson().getName() + " - "
				+ DatetimeUtils.formatDate(mPersonController.getCurTime()));
	}

	@Override
	public void initListener() {
		mTime_SB.setOnSeekBarChangeListener(new CustomVSeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(CustomVSeekBar seekBar, int progress,
					int max) {
				if (null == mPersonLocation) {
					return ;
				}
				long datetime = mPersonLocation.startTime + ((mPersonLocation.endTime - mPersonLocation.startTime)
						* progress) / max;
				invalidateUI(datetime);
			}
		});
	}

	@Override
	public Handler provideActivityHandler() {
		return null;
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
	}
	
	private void updateTitleTime(long datetime) {
		mTitle_TV.setText(mPersonController.getMonitoringPerson().getName() + " - "
				+ DatetimeUtils.format(datetime));
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
		RespLocation locationInfo = null;
		mOverlayManager.removeFromMap();
		mOverlayManager.clear();
		int i = 0;
		for (; i < mPersonLocation.infoList.size(); i++) {
			RespLocation info = mPersonLocation.infoList.get(i);
			if (info.getDate_time() > datetime) {
				break ;
			}
			
			if (null != locationInfo && ObjectUtils.equals(locationInfo.getAddress(), info.getAddress())) {
				continue;
			}
			
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
		
		
		float zoom = Math.max(mBaiduMap.getMaxZoomLevel(), mBaiduMap.getMapStatus().zoom);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(gotoPos, zoom));
		
		updateTitleTime(datetime);
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
