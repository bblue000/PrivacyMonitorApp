package com.ixming.privacy.android.common;

import org.ixming.base.utils.android.AndroidUtils;
import org.ixming.base.view.FixedRelativeLayout;
import org.ixming.base.view.utils.ViewUtils;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ixming.privacy.monitor.android.R;


public class DropDownPop {

	private final Context mContext;
	private final PopupWindow mWindow;
	private final float mWidthScale;
	
	private FixedRelativeLayout mLayout;
	private ListView mListView;
	private TextView mEmptyView;
	private DataSetObserver mDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			checkListData();
		}
	};
	
	public DropDownPop(Context context) {
		this(context, 1.0F);
	}
	
	public DropDownPop(Context context, float widthScale) {
		mContext = context;
		mWidthScale = widthScale;
		
		mWindow = new PopupWindow(mContext);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mLayout = (FixedRelativeLayout) inflater.inflate(R.layout.main_popup, null);
		mListView = (ListView) mLayout.findViewById(R.id.main_popup_lv);
		mEmptyView = (TextView) mLayout.findViewById(R.id.main_popup_empty_tv);
		
		mLayout.setMaxHeight(AndroidUtils.getDisplayHeight() / 2);
		// should contain this statement。否则会有黑色背景
		mWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources()));
		
		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setOutsideTouchable(true);
		mWindow.setContentView(mLayout);
		mWindow.update();
		
		mLayout.setClickable(true);
		mLayout.setFocusable(true);
		mLayout.setFocusableInTouchMode(true);
		
		mListView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (isShowing() && keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
					return true;
				}
				return false;
			}
		});
		
		mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				unregisterDataSetObserver();
			}
		});
	}
	
	public void showAsDropDown(View view, ListAdapter adapter, OnItemClickListener listener) {
		showAsDropDown(view, adapter, null, listener);
	}
	
	/**
	 * 
	 * @param view
	 * @param adapter
	 * @param empty 当数据为空时显示文本
	 * @param listener
	 */
	public void showAsDropDown(View view, ListAdapter adapter, String empty, OnItemClickListener listener) {
		showAsDropDown(view, view, adapter, empty, listener);
	}
	
	public void showAsDropDown(View dropDownLay, View widthLay, ListAdapter adapter, OnItemClickListener listener) {
		showAsDropDown(dropDownLay, widthLay, adapter, null, listener);
	}
	
	public void showAsDropDown(View dropDownLay, View widthLay, ListAdapter adapter, String empty, OnItemClickListener listener) {
		if (mWindow.isShowing()) {
			mWindow.dismiss();
		}
		int width;
		if (widthLay.getWidth() <= 0) {
			width = AndroidUtils.getDisplayWidth();
		} else {
			width = (int) (widthLay.getWidth() * mWidthScale);
		}
		
		mWindow.setWidth(width);
		mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		
//		unregisterDataSetObserver();
		
		mEmptyView.setText(empty);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListenerImpl(listener));
		adapter.registerDataSetObserver(mDataSetObserver);
		mListView.requestFocus();
		
		checkListData();
		
//		mWindow.setAnimationStyle(R.style.DropDownWindowAnimation);
		mWindow.showAsDropDown(dropDownLay, offsetX(dropDownLay, widthLay), 0);
	}
	
	private int offsetX(View dropDownLay, View widthLay) {
		if (dropDownLay == widthLay) {
			return 0;
		}
		return ViewUtils.getViewScreenRect(widthLay).left - ViewUtils.getViewScreenRect(dropDownLay).left;
	}
	
	private void checkListData() {
		ListAdapter adapter = mListView.getAdapter();
		if (null == adapter) {
			return;
		}
		if (adapter.isEmpty()) {
			ViewUtils.setViewGone(mListView);
			ViewUtils.setViewVisible(mEmptyView);
		} else {
			ViewUtils.setViewGone(mEmptyView);
			ViewUtils.setViewVisible(mListView);
		}
	}
	
	private void unregisterDataSetObserver() {
		if (null != mListView.getAdapter()) {
			mListView.getAdapter().unregisterDataSetObserver(mDataSetObserver);
		}
	}
	
	public void dismiss() {
		mWindow.dismiss();
	}
	
	public boolean isShowing() {
		return mWindow.isShowing();
	}
	
	private class OnItemClickListenerImpl implements OnItemClickListener {

		private OnItemClickListener mWrapped;
		public OnItemClickListenerImpl(OnItemClickListener another) {
			mWrapped = another;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (null != mWrapped) {
				mWrapped.onItemClick(parent, view, position, id);
			}
			dismiss();
		}
	}
	
}
