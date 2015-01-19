package com.deepankarsingh.mobalert;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements TabListener {

	public int selectedPosition = -1;
	private DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	ViewPager viewPager;
	ActionBar actionBar;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mNavTitles;
	FrameLayout mMainContainer;
	ActionBar.Tab tab1;
	ActionBar.Tab tab2;
	public int peopleFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();
		mMainContainer = (FrameLayout) findViewById(R.id.main_container);

		tab1 = actionBar.newTab();
		tab1.setText("Emergency Alert");
		tab1.setTabListener(this);
		actionBar.addTab(tab1);

		tab2 = actionBar.newTab();
		tab2.setText("Emergency Call");
		tab2.setTabListener(this);
		actionBar.addTab(tab2);

		mTitle = mDrawerTitle = getTitle();
		mNavTitles = getResources().getStringArray(R.array.nav_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mNavTitles));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);

			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		selectItem(0);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		viewPager.setCurrentItem(arg0.getPosition());

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public void selectItem(int position) {

		if (position == 0) {

			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			selectedPosition = position;
			viewPager.setAdapter(new MainPagerAdapter(
					getSupportFragmentManager(), getApplicationContext()));
			actionBar.selectTab(tab1);
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					actionBar.setSelectedNavigationItem(arg0);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					if (arg0 == ViewPager.SCROLL_STATE_IDLE) {

					}
					if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) {

					}
					if (arg0 == ViewPager.SCROLL_STATE_SETTLING) {

					}
				}
			});
			viewPager.setVisibility(View.VISIBLE);
			mMainContainer.setVisibility(View.INVISIBLE);

		}
		if (position == 1 && selectedPosition != 1) {

			selectedPosition = position;
			viewPager.setVisibility(View.INVISIBLE);
			mMainContainer.setVisibility(View.VISIBLE);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

			Fragment frag = new FragmentPeople();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.frag_slide_in,
					R.anim.frag_slide_out);
			fragmentTransaction.replace(R.id.main_container, frag);
			fragmentTransaction.commit();

			mDrawerList.setItemChecked(position, true);
			setTitle(mNavTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 2 && selectedPosition != 2) {
			selectedPosition = position;
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));

			mDrawerList.setItemChecked(position, true);
			setTitle(mNavTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 3 && selectedPosition != 3) {
			selectedPosition = position;
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			viewPager.setVisibility(View.INVISIBLE);
			mMainContainer.setVisibility(View.VISIBLE);

			Fragment frag = new FragmentHelp();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.frag_slide_in,
					R.anim.frag_slide_out);
			fragmentTransaction.replace(R.id.main_container, frag);
			fragmentTransaction.commit();

			mDrawerList.setItemChecked(position, true);
			setTitle(mNavTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 0 && selectedPosition == 0) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 1 && selectedPosition == 1) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 2 && selectedPosition == 2) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 3 && selectedPosition == 3) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	private void selectItemwithoutanim(int position) {
		selectedPosition = position;

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		selectedPosition = position;
		viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),
				getApplicationContext()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				actionBar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (arg0 == ViewPager.SCROLL_STATE_IDLE) {

				}
				if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) {

				}
				if (arg0 == ViewPager.SCROLL_STATE_SETTLING) {

				}
			}
		});
		viewPager.setVisibility(View.VISIBLE);
		mMainContainer.setVisibility(View.INVISIBLE);

		mDrawerList.setItemChecked(position, true);
		setTitle(mNavTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (peopleFlag == 1) {
			selectItem(1);
			peopleFlag = 0;
		}
	}

	@Override
	public void onBackPressed() {
		if (selectedPosition != 0) {
			selectItemwithoutanim(0);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
