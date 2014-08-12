package com.ixming.privacy.android.login.controll;

import com.ixming.privacy.android.login.manager.RegisterManager;

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

	public void register(String username, String password) {
		manger.register(username, password);
	}
}