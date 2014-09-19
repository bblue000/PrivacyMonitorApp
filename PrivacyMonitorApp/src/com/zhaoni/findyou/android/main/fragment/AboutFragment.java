package com.zhaoni.findyou.android.main.fragment;

import org.ixming.base.common.activity.BaseFragment;
import org.ixming.base.utils.android.AndroidUtils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.activity.WebViewActivity;
import com.zhaoni.findyou.android.main.activity.FeedbackActivity;
import com.zhaoni.findyou.android.main.manager.AboutManager;

public class AboutFragment extends BaseFragment {

	@ViewInject(id = R.id.about_version_tv)
	private TextView mVersion_TV;
	@ViewInject(id = R.id.about_disclaimer_tv)
	private TextView mDisclaimer_TV;
	AboutManager manager;

	@Override
	public int provideLayoutResId() {
		return R.layout.fragment_about;
	}

	@Override
	public void initView(View view) {
		mDisclaimer_TV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		
		mVersion_TV.setText(getString(R.string.about_version_regex,
				AndroidUtils.getAppVersionName("1.0")));
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		manager = new AboutManager(handler, context);
	}

	@Override
	public void initListener() {
	}

	@OnClickMethodInject(id = R.id.about_feedback_btn)
	void gotoFeedback() {
		startActivity(FeedbackActivity.class);
	}

	@OnClickMethodInject(id = R.id.about_upgrade_btn)
	void gotoUpgrade() {
		// 此处检测\\
		manager.checkVersion();
	}
	
	@OnClickMethodInject(id = R.id.about_disclaimer_tv)
	void gotoDisclaimer() {
		WebViewActivity.startMe(fragmentActivity, mDisclaimer_TV.getText().toString(), 
				"file:///android_asset/disclaimer.html");
	}

}
