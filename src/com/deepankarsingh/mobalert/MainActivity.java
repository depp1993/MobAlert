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

	// Instance variables for navigation drawer
	
	private DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mNavTitles;
	private FrameLayout mMainContainer;
	public int selectedPosition = -1;
	// Instance variable for Actionbar tabs and viewpager
	private ViewPager viewPager;
	private ActionBar actionBar;
	private ActionBar.Tab tab1;
	private ActionBar.Tab tab2;
	
	public int peopleFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//drawer layout in activity_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// Viewpager instance of main activity
		viewPager = (ViewPager) findViewById(R.id.pager);
		// Actionbar instance variable
		actionBar = getSupportActionBar();
		// Main container for diffrent fragments
		mMainContainer = (FrameLayout) findViewById(R.id.main_container);
		// Adding tabs to the action bar instance
		tab1 = actionBar.newTab();
		tab1.setText("Emergency Alert");
		tab1.setTabListener(this);
		actionBar.addTab(tab1);

		tab2 = actionBar.newTab();
		tab2.setText("Emergency Call");
		tab2.setTabListener(this);
		actionBar.addTab(tab2);

		// Navigation Drawer functions
		mTitle = mDrawerTitle = getTitle(); // returns the original title of the
											// application, mTitle gets updated
											// for each navigation
		mNavTitles = getResources().getStringArray(R.array.nav_array); // navigation titles
		
		mDrawerList = (ListView) findViewById(R.id.left_drawer); //left drawer listview
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START); //shadow settings for drawer layout
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mNavTitles)); //listview populated
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener()); //setting listener to the list items
		//DrawerItemClickListener is a sub class which implements OnClickListener fot ListView
		getActionBar().setDisplayHomeAsUpEnabled(true); //actionbar home button displayed and enabled as true 
		getActionBar().setHomeButtonEnabled(true); // to open drawer
		//Instance variable for the action bar toggle and setting actions to perform
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
		mDrawerLayout.setDrawerListener(mDrawerToggle); // setting Drawer listener to the navigation drawer
		selectItem(0); //selecting first Navigation item ( home ) as activity creates
	} // end of onCreate()
	
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	//select Item from the navigation drawer
	
	public void selectItem(int position) {

		if (position == 0) {

			//Viewpager configuration
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			selectedPosition = position;
			//setting viewpager adapter to create views
			viewPager.setAdapter(new MainPagerAdapter(
					getSupportFragmentManager()));
			actionBar.selectTab(tab1);
			//setting view pager onpage change listener to create views when page is scrolled
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
			new FragmentMain();
			FragmentMain.flag = 1;
			selectedPosition = position;
			viewPager.setVisibility(View.INVISIBLE);
			mMainContainer.setVisibility(View.VISIBLE);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

			Fragment frag = new FragmentPeople();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.main_container, frag);
			fragmentTransaction.commit();

			mDrawerList.setItemChecked(position, true);
			setTitle(mNavTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 2 && selectedPosition != 2) {
			new FragmentMain();
			FragmentMain.flag = 1;
			selectedPosition = position;
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));

			mDrawerList.setItemChecked(position, true);
			setTitle(mNavTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 3 && selectedPosition != 3) {
			new FragmentMain();
			FragmentMain.flag = 1;
			selectedPosition = position;
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			viewPager.setVisibility(View.INVISIBLE);
			mMainContainer.setVisibility(View.VISIBLE);

			Fragment frag = new FragmentHelp();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
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

	//Implemented methods for Tab Listener 
	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		viewPager.setCurrentItem(arg0.getPosition());
	}
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
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
			selectItem(0);
		} else {
			super.onBackPressed();
		}
	}
}
