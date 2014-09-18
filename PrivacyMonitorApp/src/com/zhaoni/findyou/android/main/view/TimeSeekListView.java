package com.zhaoni.findyou.android.main.view;

import org.ixming.base.view.utils.ViewProperties;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class TimeSeekListView extends ListView {

	private int mItemHeight;
	
	private View mHeaderView;
	private View mFooterView;
	public TimeSeekListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TimeSeekListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimeSeekListView(Context context) {
		super(context);
		init();
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		mItemHeight = ViewProperties.getAsInt(getResources().getDisplayMetrics().density * 30);
		
		mHeaderView = new View(getContext());
		mHeaderView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mFooterView = new View(getContext());
		mFooterView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		addHeaderView(mHeaderView);
		addFooterView(mFooterView);
	}
	
	private int mSideHeight;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int halfSize = Math.max(0, (heightSize - getPaddingTop() - getPaddingBottom()) / 2 - mItemHeight / 2);
		if (mSideHeight != halfSize) {
			mSideHeight = halfSize;
			updateHeaderViewAndFooterView();
		}
	}
	
	private void updateHeaderViewAndFooterView() {
		LayoutParams lp = (LayoutParams) mHeaderView.getLayoutParams();
		lp.height = mSideHeight;
		mHeaderView.requestLayout();
		
		lp = (LayoutParams) mFooterView.getLayoutParams();
		lp.height = mSideHeight;
		mFooterView.requestLayout();
		
		requestLayout();
	}

	public int getSideHeight() {
		return mSideHeight;
	}

}
