package org.ixming.android.location.baidu;

public interface OnLocationLoadListener {

	void onLocationLoad(LocationInfo locationInfo);

	void onLocationFailed(String errorTip);

}
