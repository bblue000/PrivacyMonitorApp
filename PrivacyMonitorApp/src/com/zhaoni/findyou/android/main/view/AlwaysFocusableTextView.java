package com.zhaoni.findyou.android.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class AlwaysFocusableTextView extends TextView {

	public AlwaysFocusableTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public AlwaysFocusableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public AlwaysFocusableTextView(Context context) {
		super(context);
		
	}

	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
	
}
