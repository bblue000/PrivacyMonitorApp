package com.zhaoni.findyou.android.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.zhaoni.findyou.android.R;
import com.zhaoni.findyou.android.main.fragment.AboutFragment;
import com.zhaoni.findyou.android.main.fragment.BindFragment;
import com.zhaoni.findyou.android.main.fragment.PersonListFragment;

public class IndexPagerAdapter extends FragmentPagerAdapter implements OnPageChangeListener {

	private Context mContext;
	private String[] mTitles;
	public IndexPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
		mTitles = mContext.getResources().getStringArray(R.array.tabs);
	}

	@Override
	public Fragment getItem(int page) {
		switch (page) {
		case 0:
			return new PersonListFragment();
		case 1:
			return new BindFragment();
		case 2:
			return new AboutFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}


}
