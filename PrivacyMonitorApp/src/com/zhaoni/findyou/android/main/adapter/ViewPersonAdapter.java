package com.zhaoni.findyou.android.main.adapter;

import org.ixming.base.common.adapter.AbsResLayoutAdapter;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.monitoring.model.UserInfo;

public class ViewPersonAdapter extends AbsResLayoutAdapter<UserInfo, ViewPersonAdapter.ViewHolder> {

	public ViewPersonAdapter(Context context) {
		super(context);
		
	}

	class ViewHolder {
		@ViewInject(id = R.id.main_view_person_name_tv)
		TextView mName_TV;
	}

	@Override
	protected int provideLayoutResId() {
		return R.layout.main_view_person_item;
	}

	@Override
	protected ViewHolder newHolder(int position, View contentView) {
		ViewHolder holder = new ViewHolder();
		injectHolder(holder, contentView);
		return holder;
	}

	@Override
	protected void bindView(ViewHolder holder, UserInfo data, int position,
			View view) {
		holder.mName_TV.setText(data.getUsefulName());
	}
	
}
