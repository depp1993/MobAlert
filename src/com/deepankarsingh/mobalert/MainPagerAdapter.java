package com.deepankarsingh.mobalert;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
	Context context;
	FragmentManager fragmentManager;

	public MainPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
		this.fragmentManager = fm;
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment frag = null;
		if (arg0 == 0) {
			frag = new FragmentMain();
		}
		if (arg0 == 1) {
			frag = new FragmentMainSecond();
		}
		return frag;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
