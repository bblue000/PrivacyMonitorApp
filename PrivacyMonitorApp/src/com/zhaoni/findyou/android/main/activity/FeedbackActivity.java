package com.zhaoni.findyou.android.main.activity;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.inject4android.annotation.OnClickMethodInject;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.CustomDialogBuilder;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public class FeedbackActivity extends BaseActivity {

	@Override
	public int provideLayoutResId() {
		return R.layout.activity_feedback;
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
	}

	@Override
	public void initListener() {
	}
	
	@OnClickMethodInject(id = R.id.feedback_confirm_btn)
	void submit() {
		// 此处检测
		checkUpgrade();
	}
	
	// 检测有新版本后调用
	private void checkUpgrade() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == CustomDialogBuilder.BUTTON_LEFT) {
					// 下载新版本
				}
			}
		};
		new CustomDialogBuilder(context).title(R.string.dialog_prompt)
				.text(R.string.upgrade_tip)
				.leftBtn(R.string.confirm, listener)
				.rightBtn(R.string.cancel, null).build().show();
	}

}
