package com.ixming.privacy.android.common;

import org.ixming.base.common.BaseApplication;
import org.ixming.base.common.activity.ActivityControl;
import org.ixming.base.utils.android.AndroidUtils;

import android.Manifest.permission;
import android.app.Dialog;
import android.view.WindowManager;

import com.ixming.privacy.monitor.android.R;

public class Dialogs {

	private Dialogs() {
	}
	
	public static Dialog showProgress() {
		Dialog dialog = new Dialog(ActivityControl.getInstance().getTopActivity(),
				R.style.AppTheme_Dialog_Transparent);
		dialog.setContentView(R.layout.progress_overlay);
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
		try {
			return dialog;
		} finally {
			dialog.show();
		}
	}
	
	public static Dialog showSysProgress() {
		AndroidUtils.requestPermission(permission.SYSTEM_ALERT_WINDOW);
		Dialog dialog = new Dialog(BaseApplication.getAppContext(),
				R.style.AppTheme_Dialog_Transparent);
		dialog.setContentView(R.layout.progress_overlay);
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		try {
			return dialog;
		} finally {
			dialog.show();
		}
	}
	
	public static void hideDialog(Dialog dialog) {
		if (null != dialog && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	
}
