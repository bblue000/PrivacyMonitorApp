package com.ixming.privacy.android.common.dianjin;

import org.ixming.base.utils.android.LogUtils;

import com.bodong.dianjinweb.DianJinPlatform;
import com.bodong.dianjinweb.listener.AppActiveListener;

public class DianjinUtils {
	public static void initCallBack() {
		DianJinPlatform.setAppActivedListener(new AppActiveListener() {
			@Override
			public void onSuccess(long arg0) {
				LogUtils.i(getClass(), "execute dianjin callBack onSuccess!!!!");
				LogUtils.i(getClass(), "execute dianjin callBack reward):"
						+ arg0);
				LogUtils.i(getClass(), "execute dianjin callBack reward):"
						+ arg0);
				LogUtils.i(getClass(), "execute dianjin callBack reward):"
						+ arg0);
				LogUtils.i(getClass(), "execute dianjin callBack reward):"
						+ arg0);

			}

			@Override
			public void onError(int arg0, String arg1) {
				LogUtils.i(getClass(), "execute dianjin callBack onError!!!!");
			}
		});
	}
}
