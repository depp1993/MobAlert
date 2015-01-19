package com.deepankarsingh.mobalert.helper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class NetworkGPSTracker extends Service implements LocationListener {

	private final Context mContext;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	boolean internetStatus = false;

	double latitude;
	double longitude;
	NetworkCheck obj;
	int bv;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5;
	Location location;
	protected LocationManager locationManager;
	MyGeoCoder objGeo;
	String geoLocation;

	public NetworkGPSTracker(Context mContext) {
		this.mContext = mContext;
		obj = new NetworkCheck(mContext);
		obj.execute();
		if (internetStatus) {
			location = getLocation();
			new MyGeoCoder(mContext, (OnTaskCompleted) mContext)
					.execute(location);
		} else {
			showInternetSettingsAlert();
		}
	}

	public Location getLocation() {
		try {

			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				Toast.makeText(mContext, "No Provider Enabled",
						Toast.LENGTH_LONG).show();
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					Log.d("Network Enabled", "Network");

					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();

						}
					}
				}

				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

						Log.d("GPS Enabled", "GPS");

						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(NetworkGPSTracker.this);
		}
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	public boolean isInternetOpen() {
		return this.internetStatus;
	}

	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}
		return latitude;
	}

	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}
		return longitude;
	}

	public String getGeocodedLocation() {
		return this.geoLocation;
	}

	public void showGpsSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setTitle("GPS Settings");
		alertDialog.setMessage("GPS is not enabled. Open GPS settings menu?");

		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						try {
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							mContext.startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(mContext,
									"Error : Open GPS settings manually",
									Toast.LENGTH_SHORT).show();
						}

					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}

	public void showInternetSettingsAlert() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		alertDialog.setTitle("Internet Settings");
		alertDialog
				.setMessage("Internet is not enabled. Open Internet settings menu?");
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (bv < Build.VERSION_CODES.JELLY_BEAN) {
							try {
								Intent intent = new Intent(
										Settings.ACTION_DATA_ROAMING_SETTINGS);
								ComponentName cName = new ComponentName(
										"com.android.phone",
										"com.android.phone.Settings");
								intent.setComponent(cName);
								startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(
										mContext,
										"Error : Open Internet settings manually",
										Toast.LENGTH_SHORT).show();
							}
						} else {

							try {
								Intent intent = new Intent();
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
								startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(
										mContext,
										"Error : Open Internet settings manually",
										Toast.LENGTH_SHORT).show();
							}
						}
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	public boolean hasActiveInternetConnection(Context context) {
		if (isNetworkAvailable()) {
			try {
				HttpURLConnection urlc = (HttpURLConnection) (new URL(
						"http://www.google.com").openConnection());
				urlc.setRequestProperty("User-Agent", "Test");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(1500);
				urlc.connect();
				return (urlc.getResponseCode() == 200);
			} catch (IOException e) {
				Log.e("Check", "Error checking internet connection", e);
			}
		} else {
			Log.d("Check", "No network available!");
		}
		return false;
	}

	public class NetworkCheck extends AsyncTask<Void, Void, Boolean> {
		Context context;

		public NetworkCheck(Context context) {
			this.context = context;

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return hasActiveInternetConnection(context);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == true) {
				internetStatus = true;
			} else {
				Toast.makeText(context, "No Internet Connection Found",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public class MyGeoCoder extends AsyncTask<Location, Void, String> {

		Context context;
		Geocoder geo;
		private OnTaskCompleted listener;

		public MyGeoCoder(Context context, OnTaskCompleted listener) {
			this.context = context;
			this.listener = listener;
			geo = new Geocoder(context, Locale.getDefault());
			objGeo.execute();
		}

		@Override
		protected String doInBackground(Location... params) {
			String info = null;
			Location loc = params[0];
			try {
				List<Address> ac = geo.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
				if (ac != null) {
					Address ad1 = ac.get(0);
					info = ad1.getAddressLine(0) + " " + ad1.getLocality()
							+ " " + ad1.getCountryName();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return info;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				geoLocation = result;
				Toast.makeText(
						context,
						"Latitude = " + latitude + "\nLongitude = " + longitude
								+ "\n" + result, Toast.LENGTH_LONG).show();
				listener.onTaskCompleted();
			}
		}
	}

	public interface OnTaskCompleted {
		void onTaskCompleted();
	}

}
