package com.ixming.privacy.android.main.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.ixming.privacy.android.main.control.PersonController;
import com.ixming.privacy.android.main.model.RespLocation;

class PersonLocationV3 {
	
	long startTime;
	long endTime;
	LatLng points[];
	final List<RespLocation> infoList = new ArrayList<RespLocation>();
	
	public PersonLocationV3(PersonController personController) {
		List<RespLocation> list = personController.getCurData();
		if (null == list || list.isEmpty()) {
			return ;
		}
		infoList.addAll(list);
		// sorted
		Collections.reverse(infoList);
		
		points = new LatLng[list.size()];
		long minTime = Long.MAX_VALUE;
		long maxTime = 0;
		for (int i = 0; i < list.size(); i++) {
			RespLocation info = list.get(i);
			points[i] = new LatLng(info.getLatitude(), info.getLongitude());
			if (minTime > info.getDate_time()) {
				minTime = info.getDate_time();
			}
			
			if (maxTime < info.getDate_time()) {
				maxTime = info.getDate_time();
			}
		}
		startTime = minTime;
		endTime = maxTime + 15 * 60 * 1000;
	}
	
}
