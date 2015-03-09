package com.deepankarsingh.mobalert;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ActionBarActivity implements TabListener,
		ConnectionCallbacks, OnConnectionFailedListener {

	private DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mAppTitle;
	private CharSequence mTitle;
	private String[] mNavTitles;
	private FrameLayout mMainContainer;
	protected LocationProvider loc;
	public int selectedPosition = -1;

	protected ViewPager viewPager;
	private ActionBar actionBar;
	private ActionBar.Tab tab1;
	private ActionBar.Tab tab2;

	public static GoogleApiClient mGoogleApiClient;

	public int peopleFlag = 0;

	protected void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		if (status != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(status, MainActivity.this, 1)
					.show();
		} else {
			buildGoogleApiClient();
			setContentView(R.layout.activity_main);
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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

			mTitle = mAppTitle = getTitle();
			mNavTitles = getResources().getStringArray(R.array.nav_array);

			mDrawerList = (ListView) findViewById(R.id.left_drawer);
			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
					GravityCompat.START);
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,
					R.layout.drawer_list_item, mNavTitles));
			mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.ic_drawer, R.string.drawer_open,
					R.string.drawer_close) {

				public void onDrawerClosed(View view) {
					actionBar.setTitle(mTitle);
				}

				public void onDrawerOpened(View drawerView) {
					actionBar.setTitle(mAppTitle);
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);
			selectItem(0);
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!enabled) {
			Toast.makeText(getApplicationContext(), "GPS not enabled",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
			finish();
		} else {
			loc = new LocationProvider();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(getApplicationContext(),
				"Couldn't connect to Google Play Service. Retrying...",
				Toast.LENGTH_SHORT).show();
		mGoogleApiClient.connect();
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		actionBar.setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
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

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public void selectItem(int position) {

		if (position == 0 && selectedPosition == 0) {
			mDrawerLayout.closeDrawer(mDrawerList);
			mDrawerList.setItemChecked(position, true);
		}
		if (position == 1 && selectedPosition == 1) {
			mDrawerLayout.closeDrawer(mDrawerList);
			mDrawerList.setItemChecked(position, true);
		}
		if (position == 2 && selectedPosition == 2) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 3 && selectedPosition == 3) {
			mDrawerLayout.closeDrawer(mDrawerList);
			mDrawerList.setItemChecked(position, true);
		}

		if (position == 0 && selectedPosition != 0) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			selectedPosition = position;
			viewPager.setAdapter(new MainPagerAdapter(
					getSupportFragmentManager()));
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
			setTitle(mAppTitle);
			viewPager.setVisibility(View.VISIBLE);
			mMainContainer.setVisibility(View.INVISIBLE);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 1 && selectedPosition != 1) {
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
			FragmentMain.flag = 1;
			selectedPosition = position;
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (position == 3 && selectedPosition != 3) {
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

	}

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
	public void onBackPressed() {
		if (selectedPosition != 0) {
			selectItem(0);
			mDrawerList.setItemChecked(0, true);
		} else {
			super.onBackPressed();
		}
	}
}
