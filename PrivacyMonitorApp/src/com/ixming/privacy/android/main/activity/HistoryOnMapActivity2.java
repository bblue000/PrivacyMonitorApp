package com.ixming.privacy.android.main.activity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.android.ToastUtils;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.ResTargetType;
import org.ixming.inject4android.annotation.ResInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ixming.privacy.android.main.control.MainController;
import com.ixming.privacy.android.main.model.DatetimeUtils;
import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;
import com.ixming.privacy.monitor.android.R;

public class HistoryOnMapActivity2 extends BaseActivity implements MKGeneralListener {

	@ViewInject(id = R.id.history_onmap_title_tv)
	/**
	 *  MapView 是地图主控件
	 */
	private TextView mTitle_TV = null;
	
	@ViewInject(id = R.id.history_onmap_time_sb)
	/**
	 *  MapView 是地图主控件
	 */
	private SeekBar mSeekBar = null;
	
	@ViewInject(id = R.id.history_onmap_datetime_tv)
	/**
	 *  MapView 是地图主控件
	 */
	private TextView mDatetime_TV = null;
	
	@ViewInject(id = R.id.history_onmap_mv)
	/**
	 *  MapView 是地图主控件
	 */
	private MapView mMapView = null;
	
	/**
	 *  用MapController完成地图控制 
	 */
	private MapController mMapController = null;
	
	private BMapManager mBMapManager = null;
	
	private HistoryOnMap2 mHistoryOnMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mBMapManager = new BMapManager(getApplicationContext());
		mBMapManager.init(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_history_onmap_2;
	}

	@Override
	public void initView(View view) {
		ViewUtils.setViewGone(mSeekBar);
		ViewUtils.setViewGone(mDatetime_TV);
		
		mTitle_TV.setText(DatetimeUtils.formatDate(MainController.getInstance().getCurTime()));
	}

	@Override
	public void initListener() {
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (null == mHistoryOnMap) {
					return ;
				}
				long time = mHistoryOnMap.startTime + ((mHistoryOnMap.endTime - mHistoryOnMap.startTime)
						* seekBar.getProgress()) / 100;
				invalidate(time);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
			}
		});
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		mHistoryOnMap = new HistoryOnMap2();
		mAlphaDr.setAlpha((int) (0xff * 0.8));
		mOverlay = new MyOverlay(mAlphaDr, mMapView);
		mMapView.getOverlays().add(mOverlay);
		
		mMapController = mMapView.getController();
		/**
         *  设置地图是否响应点击事件  .
         */
        mMapController.enableClick(true);
        
        mMapController.setZoomGesturesEnabled(true);
        mMapController.setScrollGesturesEnabled(true);
        mMapController.setRotationGesturesEnabled(false);
        mMapController.setOverlookingGesturesEnabled(false);
        //设置指南针显示在左上角
        mMapController.setCompassMargin(100, 100);
        
        
        mMapView.setDoubleClickZooming(true);
        mMapView.setBuiltInZoomControls(false);
        
        mMapController.setZoom(14);
        
        testDraw();
	}

	@Override
	public Handler provideActivityHandler() {
		return null;
	}
	
	private MyOverlay mOverlay;
	private Map<PrivacyLocaitonInfo, OverlayItem> mLocMap = new LinkedHashMap<PrivacyLocaitonInfo, OverlayItem>();
	@ResInject(id = R.drawable.location_marker_alpha, type = ResTargetType.Drawable)
	private Drawable mAlphaDr;
	@ResInject(id = R.drawable.location_marker, type = ResTargetType.Drawable)
	private Drawable mDr;
	private OverlayItem mCurOverlayItem;
	private LinkedList<OverlayItem> mOverlayItemCopy = new LinkedList<OverlayItem>();
	private void testDraw() {
		if (null == mHistoryOnMap.infoList || mHistoryOnMap.infoList.isEmpty()) {
			return ;
		}
		mMapController.animateTo(mHistoryOnMap.points[0]);
		
		ViewUtils.setViewVisible(mSeekBar);
		ViewUtils.setViewVisible(mDatetime_TV);
		
		invalidate(mHistoryOnMap.startTime);
	}
	
	
	private void invalidate(long time) {
		
		OverlayItem lastItem = mCurOverlayItem;
		GeoPoint gotoPos = null;
//		if (mHistoryOnMap.shownList.size() > 0) {
//			PrivacyLocaitonInfo first = mHistoryOnMap.shownList.get(0);
//			PrivacyLocaitonInfo last = mHistoryOnMap.shownList.get(mHistoryOnMap.shownList.size() - 1);
//			
//			if (first.getDatetime() <= time && last.getDatetime() >= time) {
//				// 在当前列表中
//				int i = mHistoryOnMap.shownList.size() - 1;
//				for (; i >= 0; i--) {
//					PrivacyLocaitonInfo info = mHistoryOnMap.shownList.get(i);
//					if (info.getDatetime() > time) {
//						OverlayItem item = mLocMap.remove(info);
//						if (null != item) {
//							mOverlay.removeItem(item);
//						}
//						mHistoryOnMap.shownList.remove(i);
//					} else {
//						break;
//					}
//				}
////				if (!mHistoryOnMap.shownList.isEmpty()) {
////					gotoPos = mHistoryOnMap.shownList.get(mHistoryOnMap.shownList.size() - 1);
////				}
//			} else {
//				int i = mHistoryOnMap.shownList.size();
//				mOverlayItemCopy.clear();
//				for (; i < mHistoryOnMap.infoList.size(); i++) {
//					PrivacyLocaitonInfo info = mHistoryOnMap.infoList.get(i);
//					if (info.getDatetime() > time) {
//						break ;
//					}
//					OverlayItem item = new OverlayItem(mHistoryOnMap.points[i], info.getLocationInfo(), "");
//					mOverlayItemCopy.add(item);
//					mLocMap.put(info, item);
//					mHistoryOnMap.shownList.add(info);
//					gotoPos = mHistoryOnMap.points[i];
//					mCurOverlayItem = item;
//				}
//				if (null != mCurOverlayItem) {
//					mCurOverlayItem.setMarker(mDr);
//				}
//				mOverlay.addItem(mOverlayItemCopy);
//				mOverlayItemCopy.clear();
//			}
//		} else {
//			mOverlay.removeAll();
//			mHistoryOnMap.shownList.clear();
//			mOverlayItemCopy.clear();
//			for (int i = 0; i < mHistoryOnMap.infoList.size(); i++) {
//				PrivacyLocaitonInfo info = mHistoryOnMap.infoList.get(i);
//				if (info.getDatetime() > time) {
//					break ;
//				}
//				OverlayItem item = new OverlayItem(mHistoryOnMap.points[i], info.getLocationInfo(), "");
//				mOverlayItemCopy.add(item);
//				mLocMap.put(info, item);
//				mHistoryOnMap.shownList.add(info);
//				gotoPos = mHistoryOnMap.points[i];
//				mCurOverlayItem = item;
//			}
//			if (null != mCurOverlayItem) {
//				mCurOverlayItem.setMarker(mDr);
//			}
//			mOverlay.addItem(mOverlayItemCopy);
//			mOverlayItemCopy.clear();
//		}
		
		mOverlay.removeAll();
		mHistoryOnMap.shownList.clear();
		mOverlayItemCopy.clear();
		for (int i = 0; i < mHistoryOnMap.infoList.size(); i++) {
			PrivacyLocaitonInfo info = mHistoryOnMap.infoList.get(i);
			if (info.getDatetime() > time) {
				break ;
			}
			OverlayItem item = new OverlayItem(mHistoryOnMap.points[i], info.getLocationInfo(), "");
			mOverlayItemCopy.add(item);
			mLocMap.put(info, item);
			mHistoryOnMap.shownList.add(info);
			gotoPos = mHistoryOnMap.points[i];
			mCurOverlayItem = item;
		}
		if (null != mCurOverlayItem) {
			mCurOverlayItem.setMarker(mDr);
		}
		mOverlay.addItem(mOverlayItemCopy);
		mOverlayItemCopy.clear();
		
		mDatetime_TV.setText(DatetimeUtils.format(time));
		
		if (null != gotoPos) {
			mMapController.animateTo(gotoPos);
			if (null != lastItem && null != mCurOverlayItem) {
//				mMapController.zoomToSpanWithAnimation(
//					Math.abs(lastItem.getPoint().getLatitudeE6() - mCurOverlayItem.getPoint().getLatitudeE6()), 
//					Math.abs(lastItem.getPoint().getLongitudeE6() - mCurOverlayItem.getPoint().getLongitudeE6()), 
//					MapController.DEFAULT_ANIMATION_TIME);
			} else {
				mMapController.setZoom(14);
			}
		}
		if (null != lastItem) {
			lastItem.setMarker(mAlphaDr);
		}
		
		//将overlay添加到mapView中  
//		mMapView.getOverlays().add(overlay);
		
		mMapView.refresh();
	}

	@Override
	public void onClick(View v) {
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
		mMapView.destroy();
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetNetworkState(int state) {
		switch (state) {
		case MKEvent.ERROR_NETWORK_CONNECT:
			ToastUtils.showToast(R.string.error_network);
			break;

		default:
			break;
		}
	}

	@Override
	public void onGetPermissionState(int state) {
	}
	
	public class MyOverlay extends ItemizedOverlay<OverlayItem> {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index){
			OverlayItem item = getItem(index);
			ToastUtils.showLongToast(item.getTitle());
			return true;
		}
		
    }
	
}
