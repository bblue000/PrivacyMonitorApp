package com.ixming.privacy.android.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixming.privacy.monitor.android.R;

public class DialogUtil {
	private static final String TAG = Dialog.class.getSimpleName();

	public static Dialog getPromptDialog(
			Context context,
			int title,
			int message,
			final android.content.DialogInterface.OnClickListener confirmListener,
			final android.content.DialogInterface.OnClickListener cancelListener) {
		final Dialog dialog = new Dialog(context, R.style.dialog);
		LinearLayout view = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.custom_dialog, null);
		TextView titleTV = (TextView) view
				.findViewById(R.id.custom_dialog_title_tv);
		TextView contentTV = (TextView) view
				.findViewById(R.id.custom_dialog_content_tv);
		Button confirmBT = (Button) view
				.findViewById(R.id.custom_dialog_confirm_bt);
		Button cancelBT = (Button) view
				.findViewById(R.id.custom_dialog_cancel_bt);
		titleTV.setText(title);
		contentTV.setText(message);
		confirmBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (confirmListener != null)
					confirmListener.onClick(dialog, v.getId());
				dialog.dismiss();
			}
		});
		cancelBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cancelListener != null)
					cancelListener.onClick(dialog, v.getId());
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		return dialog;
	}

	public static Dialog getPromptDialog(
			Context context,
			int title,
			int message,
			int confirm,
			int cancel,
			final android.content.DialogInterface.OnClickListener confirmListener,
			final android.content.DialogInterface.OnClickListener cancelListener) {
		final Dialog dialog = new Dialog(context, R.style.dialog);
		LinearLayout view = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.custom_dialog, null);
		TextView titleTV = (TextView) view
				.findViewById(R.id.custom_dialog_title_tv);
		TextView contentTV = (TextView) view
				.findViewById(R.id.custom_dialog_content_tv);
		Button confirmBT = (Button) view
				.findViewById(R.id.custom_dialog_confirm_bt);
		Button cancelBT = (Button) view
				.findViewById(R.id.custom_dialog_cancel_bt);
		titleTV.setText(title);
		confirmBT.setText(confirm);
		cancelBT.setText(cancel);
		contentTV.setText(message);
		confirmBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (confirmListener != null)
					confirmListener.onClick(dialog, v.getId());
				dialog.dismiss();
			}
		});
		cancelBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cancelListener != null)
					cancelListener.onClick(dialog, v.getId());
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		return dialog;
	}

	// 保护网络请求的dialog
	public static ProgressDialog getCancelableProgressDialog(Context context,
			String msg) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(msg);
		// progressDialog.setMessage(context.getString(R.string.progress_dialog_prompt));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(true);
		progressDialog.dismiss();
		progressDialog.show();
		return progressDialog;

	}

	// 保护网络请求的dialog
	public static ProgressDialog getProgressDialog(Context context, String title) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(title);
		// progressDialog.setMessage(context.getString(R.string.progress_dialog_prompt));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(true);
		progressDialog.dismiss();
		progressDialog.show();
		return progressDialog;

	}

	/**
	 * 原生简单的dialog
	 * 
	 * @param context
	 * @param str
	 * @param okBtnOnClickListener
	 * @param cancelBtnOnClickListener
	 * @return
	 */
	// public static Builder showCustomDialog(Context context, String str,
	// OnClickListener okBtnOnClickListener,
	// OnClickListener cancelBtnOnClickListener) {
	// try {
	// Builder dialog;
	// dialog = new AlertDialog.Builder(context);
	// dialog.setMessage(str);
	// dialog.setTitle(context.getString(R.string.dialog_prompt));
	// dialog.setPositiveButton(context.getString(R.string.confirm),
	// okBtnOnClickListener);
	// dialog.setNegativeButton(context.getString(R.string.cancel),
	// cancelBtnOnClickListener);
	// dialog.show();
	// return dialog;
	// } catch (Exception e) {
	// Log.e(TAG, e.getMessage() + "");
	// }
	// return null;
	// }
}
