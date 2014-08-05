package com.ixming.privacy.android.login.controll;

import com.ixming.privacy.android.login.manager.RegisterManager;

public class RegisterControll {
	private static RegisterControll instance = null;
	RegisterManager manger = null;

	private RegisterControll() {
		manger = new RegisterManager();
	}

	public static RegisterControll getInstance() {
		if (instance == null) {
			instance = new RegisterControll();
		}
		return instance;
	}

	public void register(String username, String password) {
		manger.register(username, password);
	}
}
