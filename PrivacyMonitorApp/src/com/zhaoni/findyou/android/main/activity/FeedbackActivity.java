package com.zhaoni.findyou.android.main.activity;

import java.util.Map;

import org.ixming.base.common.activity.BaseActivity;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;
import org.ixming.base.utils.android.Utils;
import org.ixming.inject4android.annotation.OnClickMethodInject;
import org.ixming.inject4android.annotation.ViewInject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.Dialogs;
import com.zhaoni.findyou.android.common.model.RequestPiecer;
import com.zhaoni.findyou.android.common.model.ResponseData.FeedbackResult;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;

public class FeedbackActivity extends BaseActivity {
	@ViewInject(id = R.id.feedback_et)
	EditText feedback_ET;
	@ViewInject(id = R.id.feedback_contact_et)
	EditText feedback_contact_ET;
	AQuery aq;

	@Override
	public int provideLayoutResId() {
		return R.layout.activity_feedback;
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData(View view, Bundle savedInstanceState) {
		aq = new AQuery(this);
	}

	@Override
	public void initListener() {
	}

	@OnClickMethodInject(id = R.id.feedback_confirm_btn)
	void submit() {
		// 提交用户反馈信息
		final Dialog dialog = Dialogs.showProgress();
		String content = feedback_ET.getText().toString();
		String contact_info = feedback_contact_ET.getText().toString();
		if (Utils.isNull(content)) {
			ToastUtils.showLongToast(R.string.feeback_empty);
			return;
		}
		SimpleAjaxCallback<FeedbackResult> callback = new SimpleAjaxCallback<FeedbackResult>() {
			@Override
			protected boolean onSuccess(String url, Object object,
					AjaxStatus status) {
				LogUtils.i(getClass(), "execute request feedback success!");
				dialog.dismiss();
				ToastUtils.showLongToast(R.string.feedback_commit_prompt);
				FeedbackActivity.this.finish();
				return true;
			}

			@Override
			protected boolean onError(AjaxStatus status) {
				dialog.dismiss();
				return super.onError(status);
			}
		};
		Map<String, String> params = RequestPiecer.getBasicData();
		params.put("content", content);
		params.put("contact_info", contact_info);
		aq.ajax(Config.URL_POST_FEEDBACK, params, FeedbackResult.class,
				callback);
	}
}
