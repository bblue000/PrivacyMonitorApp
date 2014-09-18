package com.zhaoni.findyou.android.main.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.zhaoni.findyou.android.main.control.PersonController;
import com.zhaoni.findyou.android.main.model.RespLocation;

public class PersonLocationV3 {
	
	public long startTime;
	public long endTime;
	
	public long minTimeSep;
	public long maxTimeSep;
	public float timeSepPerHeight;
	
	public LatLng points[];
	public final List<RespLocation> infoList = new ArrayList<RespLocation>();
	
	public PersonLocationV3(PersonController personController) {
		List<RespLocation> list = personController.getCurData();
		if (null == list || list.isEmpty()) {
			return ;
		}
		infoList.addAll(list);
		// sorted
		Collections.reverse(infoList);
		
		points = new LatLng[infoList.size()];
		long minTime = Long.MAX_VALUE;
		long maxTime = 0;
		long lastTime = -1;
		long minTimeSep = Long.MAX_VALUE;
		long maxTimeSep = 0;
		for (int i = 0; i < infoList.size(); i++) {
			RespLocation info = infoList.get(i);
			points[i] = new LatLng(info.getLatitude(), info.getLongitude());
			
			long cur = info.getDate_time();
			if (minTime > cur) {
				minTime = cur;
			}
			
			if (maxTime < cur) {
				maxTime = cur;
			}
			
			if (lastTime < 0) {
				lastTime = cur;
			} else {
				long curTimeSep = cur - lastTime;
				if (minTimeSep > curTimeSep) {
					minTimeSep = curTimeSep;
				}
				if (maxTimeSep < curTimeSep) {
					maxTimeSep = curTimeSep;
				}
			}
		}
		startTime = minTime;
		endTime = maxTime + 15 * 60 * 1000;
		
		this.minTimeSep = minTimeSep;
		this.maxTimeSep = maxTimeSep;
	}
	
}
