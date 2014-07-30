package com.ixming.privacy.android.common;

import org.ixming.base.common.task.IProgressDisplayer;

import android.app.Dialog;
import android.content.DialogInterface;

public class DefProgressDisplayer implements IProgressDisplayer {

	private Dialog mProgressDialog;
	@Override
	public final void hideProgress() {
		Dialogs.hideDialog(mProgressDialog);
		mProgressDialog = null;
	}
	
	@Override
	public final void displayProgress() {
		try {
			mProgressDialog = Dialogs.showProgress();
		} catch (Throwable e) {
			mProgressDialog = Dialogs.showSysProgress();
		}
		mProgressDialog.setCancelable(true);
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setOnCancelListener(new Dialog.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				hideProgress();
				DefProgressDisplayer.this.onCancel();
			}
		});
	}
	
	protected void onCancel() {
		
	}

}
