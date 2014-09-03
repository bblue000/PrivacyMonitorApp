package com.zhaoni.findyou.android.main.adapter;

import org.ixming.base.common.adapter.AbsResLayoutAdapter;
import org.ixming.inject4android.annotation.ViewInject;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.control.LocationController;
import com.zhaoni.findyou.android.main.model.DatetimeUtils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class LocationIntervalAdapter extends
		AbsResLayoutAdapter<Long, LocationIntervalAdapter.ViewHolder> {

	public LocationIntervalAdapter(Context context) {
		super(context);
		for (long date : LocationController.INTERVALS) {
			appendData(date);
		}
	}

	class ViewHolder {
		@ViewInject(id = R.id.location_interval_tv)
		TextView mName_TV;
	}

	@Override
	protected int provideLayoutResId() {
		return R.layout.location_interval_item;
	}

	@Override
	protected ViewHolder newHolder(int position, View contentView) {
		ViewHolder holder = new ViewHolder();
		injectHolder(holder, contentView);
		return holder;
	}

	@Override
	protected void bindView(ViewHolder holder, Long data, int position,
			View view) {
		holder.mName_TV.setText(DatetimeUtils.simpleFixTime(data));
	}
	
}
