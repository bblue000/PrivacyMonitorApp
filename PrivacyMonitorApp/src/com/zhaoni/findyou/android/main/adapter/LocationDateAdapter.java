package com.zhaoni.findyou.android.main.adapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.ixming.base.common.adapter.AbsResLayoutAdapter;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhaoni.findyou.android.R;

public class LocationDateAdapter extends AbsResLayoutAdapter<Long,
LocationDateAdapter.ViewHolder> {

	private GregorianCalendar mCalendar;
	public LocationDateAdapter(Context context) {
		super(context);
		mCalendar = new GregorianCalendar();
	}

	class ViewHolder {
		@ViewInject(id = R.id.privacy_location_date_item_day_tv)
		TextView mDay_Layout;
		@ViewInject(id = R.id.privacy_location_date_item_month_tv)
		TextView mMonth_Layout;
		@ViewInject(id = R.id.privacy_location_date_item_year_tv)
		TextView mYear_Layout;
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
		mCalendar.setTimeInMillis(data);
		holder.mDay_Layout.setText(String.valueOf(mCalendar.get(Calendar.DAY_OF_MONTH)));
		holder.mMonth_Layout.setText((mCalendar.get(Calendar.MONTH) + 1) + "月");
		holder.mYear_Layout.setText(mCalendar.get(Calendar.YEAR) + "年");
	}
	
}
