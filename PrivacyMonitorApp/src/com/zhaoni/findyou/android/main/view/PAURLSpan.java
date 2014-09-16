package com.zhaoni.findyou.android.main.view;

import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

public class PAURLSpan extends URLSpan {

	public PAURLSpan(String url) {
		super(url);
	}
	
	@Override
	public void onClick(View widget) {
		Log.e("yytest", "PAURLSpan onClick");
//		super.onClick(widget);
	}
	
}
