package com.zhaoni.findyou.android.main.adapter;

import org.ixming.base.common.adapter.AbsResLayoutAdapter;
import org.ixming.base.view.utils.ViewUtils;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.main.model.DatetimeUtils;
import com.zhaoni.findyou.android.main.model.RespLocation;

public class TimeLineAdapter extends AbsResLayoutAdapter<RespLocation, TimeLineAdapter.ViewHolder> {

	private int mNormalColor;
	private int mCurColor;
	public TimeLineAdapter(Context context) {
		super(context);
		mNormalColor = context.getResources().getColor(R.color.appbase_blue_disable);
		mCurColor = context.getResources().getColor(R.color.appbase_blue);
	}

	class ViewHolder {
		@ViewInject(id = R.id.timeline_tv)
		TextView mTime_TV;
		
		@ViewInject(id = R.id.timeline_sel_dot_v)
		View mSelDot_V;
	}

	@Override
	protected int provideLayoutResId() {
		return R.layout.timeline_item;
	}

	@Override
	protected ViewHolder newHolder(int position, View contentView) {
		ViewHolder holder = new ViewHolder();
		injectHolder(holder, contentView);
		return holder;
	}

	@Override
	protected void bindView(ViewHolder holder, RespLocation data, int position,
			View view) {
		if (mCurrent == position) {
			holder.mTime_TV.setTextColor(mCurColor);
			holder.mTime_TV.setTextSize(24);
			holder.mTime_TV.getPaint().setFakeBoldText(true);
			
			ViewUtils.setViewVisible(holder.mSelDot_V);
		} else {
			holder.mTime_TV.setTextColor(mNormalColor);
			holder.mTime_TV.setTextSize(18);
			holder.mTime_TV.getPaint().setFakeBoldText(false);
			ViewUtils.setViewGone(holder.mSelDot_V);
		}
		holder.mTime_TV.setText(DatetimeUtils.formatTime2(data.getDate_time()));
	}
	
	private int mCurrent;
	public boolean setCurrent(int position) {
		if (mCurrent != position) {
			mCurrent = position;
			notifyDataSetChanged();
			return true;
		}
		
		return false;
	}
	
}
