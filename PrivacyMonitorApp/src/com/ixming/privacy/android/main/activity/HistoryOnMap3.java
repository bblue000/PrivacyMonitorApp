package com.ixming.privacy.android.main.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ixming.privacy.android.main.control.MainController;
import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;

class HistoryOnMap3 {
	
	long startTime;
	long endTime;
	GeoPoint points[];
	final List<PrivacyLocaitonInfo> infoList = new ArrayList<PrivacyLocaitonInfo>();
	final List<PrivacyLocaitonInfo> shownList = new ArrayList<PrivacyLocaitonInfo>();
	public HistoryOnMap3() {
		initData();
	}
	
	void initData() {
		List<PrivacyLocaitonInfo> list = MainController.getInstance().getCurData();
		if (null == list || list.isEmpty()) {
			return ;
		}
		infoList.addAll(list);
		// sort
		Collections.sort(infoList);
		
		points = new GeoPoint[list.size()];
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
		
//		for (int i = 0; i < list.size(); i++) {
//			PrivacyLocaitonInfo info = list.get(i);
//			info.setLatitude(info.getLatitude());
//			info.setLongitude(info.getLongitude());
//			
//			points[i] = new GeoPoint((int) (info.getLatitude() * 1e6),
//					(int) (info.getLongitude() * 1e6));
//			if (minTime > info.getDatetime()) {
//				minTime = info.getDatetime();
//			}
//			
//			if (maxTime < info.getDatetime()) {
//				maxTime = info.getDatetime();
//			}
//		}
		
		startTime = minTime;
		endTime = maxTime + 15 * 60 * 1000;
	}
}
