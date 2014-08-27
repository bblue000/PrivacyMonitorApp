package com.ixming.privacy.android.main.adapter;

import org.ixming.base.common.adapter.AbsResLayoutAdapter;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ixming.privacy.android.main.model.DatetimeUtils;
import com.ixming.privacy.monitor.android.R;

public class LocationDateAdapter extends AbsResLayoutAdapter<Long,
LocationDateAdapter.ViewHolder> {

	public LocationDateAdapter(Context context) {
		super(context);
		
	}

	class ViewHolder {
		@ViewInject(id = R.id.privacy_location_date_item_datetime_tv)
		TextView mDatetime_TV;
	}

	@Override
	protected int provideLayoutResId() {
		return R.layout.privacy_location_date_item;
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
		holder.mDatetime_TV.setText(DatetimeUtils.formatDate(data));
	}
	
}
