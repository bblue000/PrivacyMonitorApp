package com.zhaoni.findyou.android.main.view;

import com.zhaoni.findyou.android.PAApplication;

import android.text.TextPaint;
import android.text.style.SuperscriptSpan;
import android.util.TypedValue;

public class PASuperscriptSpan extends SuperscriptSpan {
	
	@Override
    public void updateDrawState(TextPaint tp) {
        super.updateDrawState(tp);
        tp.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, PAApplication.getAppContext().getResources().getDisplayMetrics()));
    }
	
	@Override
	public void updateMeasureState(TextPaint tp) {
		super.updateMeasureState(tp);
		tp.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, PAApplication.getAppContext().getResources().getDisplayMetrics()));
	}
}
