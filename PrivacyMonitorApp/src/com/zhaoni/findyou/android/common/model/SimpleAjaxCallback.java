package com.zhaoni.findyou.android.common.model;

import org.apache.http.HttpStatus;
import org.ixming.base.utils.android.LogUtils;
import org.ixming.base.utils.android.ToastUtils;

import android.text.TextUtils;

import com.androidquery.callback.AjaxStatus;

public class SimpleAjaxCallback<T extends BasicResponseData<?>> extends
		BasicAjaxCallback<T> {

	private static final String TAG = "AQuery";

	private static final Object HOLD = new Object();
	private boolean mIgnoreDefault;
	private Object mToken = HOLD;
	private String mTag = TAG;

	public SimpleAjaxCallback() {
	}

	/**
	 * 
	 * @param ignoreDefault
	 *            如果对onSuccess和onError不感兴趣，则设置为TRUE，不执行默认的Toast处理
	 */
	public SimpleAjaxCallback(boolean ignoreDefault) {
		mIgnoreDefault = ignoreDefault;
	}

	/**
	 * 如果token为null，当回调时，就不予执行
	 */
	public SimpleAjaxCallback<T> token(Object token) {
		mToken = token;
		return this;
	}

	public SimpleAjaxCallback<T> logTag(String tag) {
		if (!TextUtils.isEmpty(tag)) {
			mTag = tag;
		}
		return this;
	}

	public SimpleAjaxCallback<T> cancelMe() {
		return token(null);
	}

	public boolean isCanceled() {
		return null == mToken;
	}

	@Override
	public void callback(String url, T object, AjaxStatus status) {
		LogUtils.d(mTag, "callback code ---> " + status.getCode());
		LogUtils.d(mTag, "callback msg ---> " + status.getMessage());
		LogUtils.d(mTag, "callback result ---> " + object);
		if (null == mToken)
			return;

		boolean isOk = false;
		if (status.getCode() == HttpStatus.SC_OK) {
			if (null == object) {
				status.code(AjaxStatus.TRANSFORM_ERROR).message("数据为空");
			} else {
				isOk = true;
				status.code(object.getStatus()).message(object.getMsg());
			}
		}
		if (isOk) {
			if (!onSuccess(url, object.getValue(), status)) {
				// ToastUtils.showToast(status.getMessage());
			}
		} else {
			if (!onError(status)) {
				ToastUtils.showToast(status.getMessage());
			}
		}
		status.close();
	}

	/**
	 * 默认返回true（不弹出Toast）
	 * 
	 * @return 如果子类自行处理了回调，则返回TRUE，否则使用默认行为——弹出Toast
	 */
	protected boolean onSuccess(String url, Object object, AjaxStatus status) {
		return mIgnoreDefault;
	}

	/**
	 * @return 如果子类自行处理了回调，则返回TRUE，否则使用默认行为——弹出Toast
	 */
	protected boolean onError(AjaxStatus status) {
		return mIgnoreDefault;
	}
}
