package com.ixming.privacy.android.common;

import org.ixming.base.view.utils.ViewUtils;

import com.ixming.privacy.monitor.android.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialogBuilder {
    public static final int BUTTON_LEFT = -1;

    public static final int BUTTON_MID = -2;

    public static final int BUTTON_RIGHT = -3;
    
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private Dialog mDialog;
	private LinearLayout mRootView;
	
	private View mTitleView;
	private TextView mTitle_TV;
	
	private View mContentView;
	
	private View mBtnsView;
	private static final int TAG_BUTTON_TYPE = 0x04000001;
	private static final int TAG_LISTENER = 0x04000002;
	private static final int TAG_DIALOG = 0x04000003;
	public CustomDialogBuilder(Context context) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		
		mRootView = new LinearLayout(mContext);
		mRootView.setOrientation(LinearLayout.VERTICAL);
		
		mDialog = new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(mRootView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	
	
	
	private void checkTitleView() {
		if (null == mTitleView) {
			mTitleView = mLayoutInflater.inflate(R.layout.custom_dialog_title, mRootView, false);
			mTitle_TV = (TextView) mTitleView.findViewById(R.id.custom_dialog_title_tv);
		}
	}
	public CustomDialogBuilder title(String title) {
		checkTitleView();
		mTitle_TV.setText(title);
		return this;
	}
	
	public CustomDialogBuilder title(int resId) {
		return title(mContext.getString(resId));
	}
	
	
	
	
	public CustomDialogBuilder text(String text) {
		mContentView = mLayoutInflater.inflate(R.layout.dialog_context_simple_text, mRootView, false);
		TextView title_TV = (TextView) mContentView.findViewById(R.id.dialog_simple_text_tv);
		title_TV.setText(text);
		return this;
	}
	
	public CustomDialogBuilder content(View view) {
		mContentView = view;
		return this;
	}
	
	public CustomDialogBuilder content(int viewResId) {
		return content(mLayoutInflater.inflate(viewResId, mRootView, false));
	}
	
	/**
	 * get content view
	 */
	public View content() {
		return mContentView;
	}
	
	
	
	
	private void checkBtnsView() {
		if (null == mBtnsView) {
			mBtnsView = mLayoutInflater.inflate(R.layout.custom_dialog_btns, mRootView, false);
		}
	}
	private void configBtns(int layoutId, int viewId, int btnType, String label, DialogInterface.OnClickListener listener) {
		View layout = mBtnsView.findViewById(layoutId);
		ViewUtils.setViewVisible(layout);
		Button btn = (Button) layout.findViewById(viewId);
		btn.setText(label);
		btn.setTag(TAG_BUTTON_TYPE, btnType);
		btn.setTag(TAG_LISTENER, listener);
		btn.setTag(TAG_DIALOG, mDialog);
		btn.setOnClickListener(mBtnsClickListener);
	}
	public CustomDialogBuilder leftBtn(String label, DialogInterface.OnClickListener listener) {
		checkBtnsView();
		configBtns(R.id.custom_dialog_btns_left_layout, R.id.custom_dialog_btns_left_bt, BUTTON_LEFT, label, listener);
		return this;
	}
	public CustomDialogBuilder leftBtn(int labelRestId, DialogInterface.OnClickListener listener) {
		return leftBtn(mContext.getString(labelRestId), listener);
	}
	public CustomDialogBuilder midBtn(String label, DialogInterface.OnClickListener listener) {
		checkBtnsView();
		configBtns(R.id.custom_dialog_btns_mid_layout, R.id.custom_dialog_btns_mid_bt, BUTTON_MID, label, listener);
		return this;
	}
	public CustomDialogBuilder midBtn(int labelRestId, DialogInterface.OnClickListener listener) {
		return midBtn(mContext.getString(labelRestId), listener);
	}
	public CustomDialogBuilder rightBtn(String label, DialogInterface.OnClickListener listener) {
		checkBtnsView();
		configBtns(R.id.custom_dialog_btns_right_layout, R.id.custom_dialog_btns_right_bt, BUTTON_RIGHT, label, listener);
		return this;
	}
	public CustomDialogBuilder rightBtn(int labelRestId, DialogInterface.OnClickListener listener) {
		return rightBtn(mContext.getString(labelRestId), listener);
	}
	
	
	
	
	public Dialog build() {
		if (null != mTitleView) {
			mRootView.addView(mTitleView);
		}
		if (null != mContentView) {
			mRootView.addView(mContentView);
		}
		if (null != mBtnsView) {
			mRootView.addView(mBtnsView);
		}
		return mDialog;
	}
	
	
	
	
	private View.OnClickListener mBtnsClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			boolean triggerDismiss = false;
			
			DialogInterface.OnClickListener listener = (DialogInterface.OnClickListener) v.getTag(TAG_LISTENER);
			if (listener instanceof CustomDialogOnClickListener) {
				if (((CustomDialogOnClickListener) listener).triggerDismiss()) {
					triggerDismiss = true;
				}
			} else {
				triggerDismiss = true;
			}
			
			// 如果triggerDismiss，则认为
			if (triggerDismiss && null != listener) {
				listener.onClick((Dialog) v.getTag(TAG_DIALOG), (Integer) v.getTag(TAG_BUTTON_TYPE));
			}
			Dialog dialog = (Dialog) v.getTag(TAG_DIALOG);
			if (triggerDismiss && null != dialog) {
				dialog.dismiss();
			}
		}
	};
}
