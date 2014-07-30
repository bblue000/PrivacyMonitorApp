package com.ixming.privacy.android.main.activity;

import java.util.List;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.android.ToastUtils;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ixming.privacy.android.monitoring.db.manager.PrivacyLocationInfoDBManager;
import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;
import com.ixming.privacy.monitor.android.R;

public class HistoryOnMapActivity extends BaseActivity implements MKGeneralListener {

	@ViewInject(id = R.id.history_onmap_time_sb)
	/**
	 *  MapView 是地图主控件
	 */
	private SeekBar mSeekBar = null;
	
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
	
	private TimeSeek mTimeSeek;
	
	HistoryOnMap2 mHistoryOnMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mBMapManager = new BMapManager(getApplicationContext());
		mBMapManager.init(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public int provideLayoutResId() {
		return R.layout.activity_history_onmap;
	}

	@Override
	public void initView(View view) {
		ViewUtils.setViewGone(mSeekBar);
	}

	@Override
	public void initListener() {
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (null == mTimeSeek) {
					return ;
				}
				int index = -1;
				long time = mTimeSeek.startTime + progress;
				if (time > (mTimeSeek.startTime + mTimeSeek.endTime) / 2) {
					for (int i = 0; i < mTimeSeek.infoList.size(); i++) {
						PrivacyLocaitonInfo info = mTimeSeek.infoList.get(i);
						if (info.getDatetime() >= time) {
							index = i;
							break;
						}
					}
				} else {
					for (int i = mTimeSeek.infoList.size() - 1; i >= 0; i--) {
						PrivacyLocaitonInfo info = mTimeSeek.infoList.get(i);
						if (info.getDatetime() <= time) {
							index = i;
							break;
						}
					}
				}
				invalidate(mTimeSeek.points, index);
			}
		});
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
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
        
        mMapController.setZoom(16);
        
        testDraw();
	}

	@Override
	public Handler provideActivityHandler() {

		return null;
	}
	
	private Geometry geometry = new Geometry();
	private Symbol symbol = new Symbol();
	private GraphicsOverlay overlay;
	private void testDraw() {
		PrivacyLocationInfoDBManager manager = PrivacyLocationInfoDBManager.getInstance();
		List<PrivacyLocaitonInfo> list = manager.findAll();
		if (null == list || list.isEmpty()) {
			return ;
		}
		
		PrivacyLocaitonInfo first = list.get(0);
		mMapController.animateTo(new GeoPoint((int) (first.getLatitude() * 1e6),
				(int) (first.getLongitude() * 1e6)));
		
		GeoPoint points[] = new GeoPoint[list.size()];
		long minTime = Long.MAX_VALUE;
		long maxTime = 0;
		for (int i = 0; i < list.size(); i++) {
			PrivacyLocaitonInfo info = list.get(i);
			info.setLatitude(info.getLatitude() + Math.random() * 0.04);
			info.setLongitude(info.getLongitude() + Math.random() * 0.04);
			
			points[i] = new GeoPoint((int) (info.getLatitude() * 1e6),
					(int) (info.getLongitude() * 1e6));
			if (minTime > info.getDatetime()) {
				minTime = info.getDatetime();
			}
			
			if (maxTime < info.getDatetime()) {
				maxTime = info.getDatetime();
			}
		}

		if (maxTime - minTime > 0) {
			ViewUtils.setViewVisible(mSeekBar);
			mSeekBar.setMax((int) (maxTime - minTime));
			mTimeSeek = new TimeSeek(minTime, maxTime, list, points);
		}
		
		geometry.setPolyLine(points);
		
		Symbol.Color color = symbol.new Color(0xBB, 0x48, 0x9F, 0xFF);
		symbol.setLineSymbol(color, 7);
		
		Graphic graphic = new Graphic(geometry, symbol);
		overlay = new GraphicsOverlay(mMapView);
		overlay.setData(graphic);
		
		//将overlay添加到mapView中  
		mMapView.getOverlays().add(overlay);
		
		mMapView.refresh();
	}
	
	private void invalidate(GeoPoint pointsSrc[], int index) {
		GeoPoint points[] = new GeoPoint[index + 1];
		
		for (int i = 0; i < pointsSrc.length; i++) {
			points[i] = pointsSrc[i];
			if (index == i) {
				break;
			}
		}
		
		geometry = new Geometry();
		geometry.setPolyLine(points);
		
		Symbol symbol = new Symbol();//创建样式 
		Symbol.Color color = symbol.new Color(0xBB, 0x48, 0x9F, 0xFF);
		symbol.setLineSymbol(color, 7);
		
//		mMapView.getOverlays().remove(overlay);
		
		Graphic graphic = new Graphic(geometry, symbol);
//		overlay = new GraphicsOverlay(mMapView);
		overlay.removeAll();
		overlay.setData(graphic);
		
		//将overlay添加到mapView中  
//		mMapView.getOverlays().add(overlay);
		
		mMapView.refresh();
	}
	
//	public void s() {
//		GroundOverlay mGroundOverlay = new GroundOverlay(mMapView);
//		GeoPoint leftBottom = new GeoPoint((int) (mLat5 * 1E6),(int) (mLon5 * 1E6));
//		GeoPoint rightTop = new GeoPoint((int) (mLat6 * 1E6),(int) (mLon6 * 1E6));
//		Drawable d = getResources().getDrawable(R.drawable.small_dot);
//		Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//		Ground mGround = new Ground(bitmap, leftBottom, rightTop); 
//		
//		mMapView.getOverlays().add(mGroundOverlay);  
//		mGroundOverlay.addGround(mGround);  
//		mMapView.refresh();
//	}

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
			ToastUtils.showToast("网络出错啦！");
			break;

		default:
			break;
		}
	}

	@Override
	public void onGetPermissionState(int state) {
	}
	
	
	
	// >>>>>>>>>>>>>>>>>
	private class TimeSeek {
		long startTime;
		long endTime;
		List<PrivacyLocaitonInfo> infoList;
		GeoPoint points[];
		public TimeSeek(long startTime, long endTime,
				List<PrivacyLocaitonInfo> infoList, GeoPoint[] points) {
			super();
			this.startTime = startTime;
			this.endTime = endTime;
			this.infoList = infoList;
			this.points = points;
		}
	}
}
