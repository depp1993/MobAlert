package com.deepankarsingh.mobalert;

import java.text.DateFormat;
import java.util.Date;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationProvider implements LocationListener {

	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	public Location mCurrentLocation;
	protected LocationRequest mLocationRequest;
	protected String mLastUpdateTime;
	private boolean mRequestingLocation = false;

	public LocationProvider() {
		if (!mRequestingLocation) {
			createLocationRequest();
			mCurrentLocation = LocationServices.FusedLocationApi
					.getLastLocation(MainActivity.mGoogleApiClient);
			findLocation();
			mRequestingLocation = true;
		}
	}

	private void findLocation() {
		if (MainActivity.mLastLocationUpdated) {
			Log.d("LocationProvider", "mLastLocationUpdated == true");
			startLocationUpdates();
		} else {
			Log.d("LocationProvider", "mLastLocationUpdated == false");
			mCurrentLocation = LocationServices.FusedLocationApi
					.getLastLocation(MainActivity.mGoogleApiClient);
			if (mCurrentLocation == null)
				Log.d("LocationProvider", "mCurrentLocation still NULL");
			startLocationUpdates();
		}
	}

	public Location getLocation() {
		Log.d("LocationProvider", "Location: " + mCurrentLocation.getLatitude()
				+ "\n" + mCurrentLocation.getLongitude());
		mRequestingLocation = false;
		return mCurrentLocation;
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest
				.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	protected void startLocationUpdates() {
		Log.d("LocationProvider", "startLocationUpdates");
		LocationServices.FusedLocationApi.requestLocationUpdates(
				MainActivity.mGoogleApiClient, mLocationRequest, this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				MainActivity.mGoogleApiClient, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		mCurrentLocation = location;
		mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
		Log.d("onLocationChanged", mCurrentLocation.getLatitude() + "\n"
				+ mCurrentLocation.getLongitude() + "\n" + mLastUpdateTime);
	}
}
