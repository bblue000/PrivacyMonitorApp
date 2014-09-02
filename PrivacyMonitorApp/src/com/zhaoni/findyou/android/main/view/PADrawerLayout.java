package com.zhaoni.findyou.android.main.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;

public class PADrawerLayout extends DrawerLayout {

	public PADrawerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public PADrawerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public PADrawerLayout(Context context) {
		super(context);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		View viewChild = getChildAt(0);
		measureChild(viewChild, widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(viewChild.getMeasuredHeight()
				+ getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY));
	}

	public void setMinDrawerMargin(int dp) {
		try {
			Field f = DrawerLayout.class.getDeclaredField("mMinDrawerMargin");
			f.setAccessible(true);
			f.set(this, (int) (dp * getResources().getDisplayMetrics().density + 0.5f));
		} catch (Exception e) {
		}
		invalidate();
	}
}
