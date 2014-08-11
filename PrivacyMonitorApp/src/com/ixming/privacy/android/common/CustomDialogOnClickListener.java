package com.ixming.privacy.android.common;

import android.content.DialogInterface.OnClickListener;

public interface CustomDialogOnClickListener extends OnClickListener {

	/**
	 * 如果需要dismiss对话框，则返回True。
	 * 
	 * 如果返回true，点击事件将不执行
	 */
	boolean triggerDismiss();
	
}
