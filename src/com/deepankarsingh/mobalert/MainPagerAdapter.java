package com.deepankarsingh.mobalert;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

	//View pager Adapter class to create two fragment views on viewpager
	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	//getItem is called everytime to create a view on view pager
	@Override
	public Fragment getItem(int arg0) {
		Fragment frag = null;
		//two pages : emergency call and emergency message
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
