package com.ixming.privacy.android.common;

import org.ixming.base.common.activity.ActivityControl;
import org.ixming.base.common.activity.BaseActivity;

import android.content.Intent;

import com.ixming.privacy.android.login.activity.LoginActivity;
import com.ixming.privacy.android.login.manager.LoginStateCallback;
import com.ixming.privacy.android.main.activity.NewMainActivity;

/**
 * 全局的一个LoginStateCallback子类，该类默认实现了 {@code LoginStateCallback#onNonLogin()}
 * 
 * 和 {@code LoginStateCallback#onLoginExpire()} 方法，用于未登录/登录过期的全局性处理。
 * 
 * <br/>
 * <br/>
 * 
 * 调用处仍需要实现 {@code LoginStateCallback#onAlreadyLogged()} 方法。 
 * 
 * @author Yin Yong
 *
 */
public class DefLoginStateCallback implements LoginStateCallback {

	@Override
	public void onNonLogin() {
		BaseActivity act = (BaseActivity) ActivityControl.getInstance().getTopActivity();
		if (null != act) {
			act.startActivity(LoginActivity.class, 0);
		} else {
			ActivityControl.startNewTaskActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
	}

	@Override
	public void onLoginExpire() {
		onNonLogin();
	}

	@Override
	public void onAlreadyLogged() {
		BaseActivity act = (BaseActivity) ActivityControl.getInstance().getTopActivity();
		if (null != act) {
			act.startActivity(NewMainActivity.class, 0);
		} else {
			ActivityControl.startNewTaskActivity(NewMainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
	}
}
