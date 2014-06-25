package com.ixming.privacy.android.main.adapter;

import org.ixming.base.common.adapter.AbsResLayoutAdapter;
import org.ixming.inject4android.annotation.ViewInject;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ixming.privacy.android.main.entity.PrivacyPhoneInfo;
import com.ixming.privacy.android.main.model.DatetimeUtils;
import com.ixming.privacy.monitor.android.R;

public class ViewPhoneAdapter extends AbsResLayoutAdapter<PrivacyPhoneInfo, ViewPhoneAdapter.ViewHolder> {

	public ViewPhoneAdapter(Context context) {
		super(context);
		
	}

	class ViewHolder {
		@ViewInject(id = R.id.privacy_phone_item_datetime_tv)
		TextView mDatetime_TV;
		@ViewInject(id = R.id.privacy_phone_item_phonenumber_tv)
		TextView mPhoneNumber_TV;
		@ViewInject(id = R.id.privacy_phone_item_aliasname_tv)
		TextView mAliasname_TV;
		@ViewInject(id = R.id.privacy_phone_item_type_tv)
		TextView mType_TV;
	}

	@Override
	protected int provideLayoutResId() {
		return R.layout.privacy_phone_item;
	}

	@Override
	protected ViewHolder newHolder(int position, View contentView) {
		ViewHolder holder = new ViewHolder();
		injectView(holder, contentView);
		return holder;
	}

	@Override
	protected void bindView(ViewHolder holder, PrivacyPhoneInfo data, int position,
			View view) {
		holder.mDatetime_TV.setText(DatetimeUtils.format(data.getDatetime()));
		holder.mPhoneNumber_TV.setText(data.getPhoneNumber());
		holder.mAliasname_TV.setText(data.getAliasname());
//		holder.mType_TV.setText(data.getInfoType());
	}
	
}
