package com.ixming.privacy.android.main.adapter;

import org.ixming.base.common.adapter.AbsResLayoutAdapter;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ixming.privacy.android.monitoring.entity.PrivacyLocaitonInfo;
import com.ixming.privacy.android.monitoring.model.DatetimeUtils;
import com.ixming.privacy.monitor.android.R;

public class ViewLocationAdapter extends AbsResLayoutAdapter<PrivacyLocaitonInfo,
ViewLocationAdapter.ViewHolder> {

	public ViewLocationAdapter(Context context) {
		super(context);
		
	}

	class ViewHolder {
		@ViewInject(id = R.id.privacy_location_item_datetime_tv)
		TextView mDatetime_TV;
		@ViewInject(id = R.id.privacy_location_item_location_tv)
		TextView mLocation_TV;
	}

	@Override
	protected int provideLayoutResId() {
		return R.layout.privacy_location_item;
	}

	@Override
	protected ViewHolder newHolder(int position, View contentView) {
		ViewHolder holder = new ViewHolder();
		injectView(holder, contentView);
		return holder;
	}

	@Override
	protected void bindView(ViewHolder holder, PrivacyLocaitonInfo data, int position,
			View view) {
		holder.mDatetime_TV.setText(DatetimeUtils.format(data.getDatetime()));
		holder.mLocation_TV.setText(data.getLocationInfo());
	}
	
}
