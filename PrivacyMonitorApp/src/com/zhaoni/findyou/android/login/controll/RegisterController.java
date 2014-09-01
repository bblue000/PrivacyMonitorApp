package com.zhaoni.findyou.android.login.controll;

import com.zhaoni.findyou.android.login.manager.RegisterManager;

public class RegisterController {
	private static RegisterController instance = null;
	RegisterManager manger = null;

	private RegisterController() {
		manger = new RegisterManager();
	}

	public static RegisterController getInstance() {
		if (instance == null) {
			instance = new RegisterController();
		}
		return instance;
	}

	public void register(String username, String password, String checkcode) {
		manger.register(username, password, checkcode);
	}

	public void getCheckcode(String mobile) {
		manger.getCheckcode(mobile);
	}

}
