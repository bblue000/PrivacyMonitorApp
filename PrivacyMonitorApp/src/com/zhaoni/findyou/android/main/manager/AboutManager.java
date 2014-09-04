package com.zhaoni.findyou.android.main.manager;

import org.ixming.base.utils.android.AndroidUtils;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.zhaoni.findyou.android.Config;
import com.zhaoni.findyou.android.PAApplication;
import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.common.CustomDialogBuilder;
import com.zhaoni.findyou.android.common.Dialogs;
import com.zhaoni.findyou.android.common.model.ResponseData.VersionInfoResult;
import com.zhaoni.findyou.android.common.model.SimpleAjaxCallback;
import com.zhaoni.findyou.android.main.model.VersionInfo;

public class AboutManager {
	AQuery aq;
	Handler handler;
	Context context;
	Dialog dialog;

	public AboutManager(Handler handler, Context context) {
		this.handler = handler;
		this.context = context;
		aq = new AQuery(PAApplication.getAppContext());
	}

	/**
	 * 获取最新版本
	 */
	public void checkVersion() {
		SimpleAjaxCallback<VersionInfoResult> callback = new SimpleAjaxCallback<VersionInfoResult>() {
			@Override
			protected boolean onSuccess(String url, Object object,
					AjaxStatus status) {
				dialog.dismiss();
				int currentVersionCode = AndroidUtils.getAppVersionCode();
				VersionInfo versionInfo = (VersionInfo) object;
				if (versionInfo.getVersion_code() > currentVersionCode) {
					checkUpgrade(versionInfo.getDownload_url());
				} else {
					ToastUtils.showLongToast(R.string.version_prompt);
				}
				LogUtils.i(getClass(), "versionInfo:" + versionInfo.toString());
				return true;
			}

			@Override
			protected boolean onError(AjaxStatus status) {
				dialog.dismiss();
				return true;
			}
		};
		dialog = Dialogs.showProgress();
		aq.ajax(Config.URL_GET_VERISON, VersionInfoResult.class, callback);
	}

	// 检测有新版本后调用
	private void checkUpgrade(final String download_url) {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == CustomDialogBuilder.BUTTON_LEFT) {
					// 下载新版本
					AndroidUtils.callHTTPDownload(context, "下载新版本", download_url);
				}
			}
		};
		new CustomDialogBuilder(context).title(R.string.dialog_prompt)
				.text(R.string.upgrade_tip).leftBtn(R.string.confirm, listener)
				.rightBtn(R.string.cancel, null).build().show();
	}
}
