package com.ixming.privacy.android.main.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class PALinearLayout extends ViewGroup {

	public PALinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PALinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PALinearLayout(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		setClipChildren(false);
		setClipToPadding(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int width = getPaddingLeft() + getPaddingRight();
//		for (int i = 0; i < getChildCount(); i++) {
//			View child = getChildAt(i);
//			measureChild(child, widthMeasureSpec, heightMeasureSpec);
//			width += child.getMeasuredWidth();
//		}
//		super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
		
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = getPaddingLeft();
		int top = getPaddingTop();
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			child.layout(left, top, left + childWidth, top + childHeight);
			left = left + childWidth;
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		Log.e("yytest", "dispatchDraw");
		super.dispatchDraw(canvas);
//		canvas.translate(getPaddingLeft(), getPaddingTop());
//		getChildAt(0).draw(canvas);
	}
	
}
