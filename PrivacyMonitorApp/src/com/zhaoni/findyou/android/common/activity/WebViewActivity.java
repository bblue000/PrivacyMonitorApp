package com.zhaoni.findyou.android.common.activity;

import org.ixming.inject4android.annotation.ViewInject;

import com.zhaoni.findyou.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class WebViewActivity extends MyBaseActivity {

	public static void startMe(Activity from, String title, String url) {
		from.startActivity(new Intent(from, WebViewActivity.class)
				.putExtra("title", title)
				.putExtra("url", url));
	}
	
	@ViewInject(id = R.id.activity_webview_wv)
	private WebView mWeb_WV;
	
	@ViewInject(id = R.id.activity_webview_title_tv)
	private TextView mWebTitle_TV;
	
	private String mTitle;
	private String mUrl;
	@Override
	public int provideLayoutResId() {

		return R.layout.activity_webview;
	}

	@Override
	public void initView(View view) {
		
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		loadFromIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		loadFromIntent(intent);
	}
	
	private void loadFromIntent(Intent intent) {
		mTitle = intent.getStringExtra("title");
		mUrl = intent.getStringExtra("url");
		
		mWebTitle_TV.setText(mTitle);
		mWeb_WV.loadUrl(mUrl);
	}

	@Override
	public void initListener() {
	}

}
