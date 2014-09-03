package com.zhaoni.findyou.android.main.adapter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.ixming.base.common.adapter.AbsResLayoutAdapter;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.main.control.PersonController;
import com.zhaoni.findyou.android.main.control.PersonListController;
import com.zhaoni.findyou.android.main.model.RespLocation;

public class LocationDateAdapter extends AbsResLayoutAdapter<Long,
LocationDateAdapter.ViewHolder> {

	private GregorianCalendar mCalendar;
	public LocationDateAdapter(Context context) {
		super(context);
		mCalendar = new GregorianCalendar();
	}

	class ViewHolder {
		@ViewInject(id = R.id.privacy_location_date_item_day_tv)
		TextView mDay_TV;
		@ViewInject(id = R.id.privacy_location_date_item_month_tv)
		TextView mMonth_TV;
		@ViewInject(id = R.id.privacy_location_date_item_year_tv)
		TextView mYear_TV;
		
		@ViewInject(id = R.id.privacy_location_date_item_loc_tv)
		TextView mLoc_TV;
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
		int date = mCalendar.get(Calendar.DAY_OF_MONTH);
		holder.mDay_TV.setText(date < 10 ? "0" + date : String.valueOf(date));
		holder.mMonth_TV.setText((mCalendar.get(Calendar.MONTH) + 1) + "月");
		holder.mYear_TV.setText(mCalendar.get(Calendar.YEAR) + "年");
		
		PersonController personController = PersonListController.getInstance().getCurrentPersonController();
		RespLocation latestLoc = null;
		if (null != personController) {
			List<RespLocation> locList = personController.getLocationData(data);
			if (null != locList && !locList.isEmpty()) {
				latestLoc = locList.get(0);
			}
		}
		if (null == latestLoc) {
			ViewUtils.setViewGone(holder.mLoc_TV);
			holder.mLoc_TV.setText(null);
		} else {
			ViewUtils.setViewVisible(holder.mLoc_TV);
			holder.mLoc_TV.setText(latestLoc.getAddress());
		}
	}
	
}
