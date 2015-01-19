package com.deepankarsingh.mobalert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.deepankarsingh.mobalert.helper.DbConnect;

public class AlertFromWidget extends Service implements LocationListener {

	double latitude;
	double longitude;
	int n;
	String geoLocation;
	String message;
	ProgressDialog cancelDialog;
	LocationManager loc;
	String provider;
	DbConnect dbObj;

	private boolean netThreadFailed = false;
	private boolean geoThreadFailed = false;
	private boolean netConnection = true;
	private boolean isGPSOn = true;
	private boolean locationFound = true;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		dbObj = new DbConnect(getApplicationContext());
		n = dbObj.getname().getCount();

		if (n == 0) {
			Toast.makeText(getApplicationContext(),
					"Add emergency contacts first", Toast.LENGTH_LONG).show();
			stopSelf();
		} else {
			NetworkCheck obj = new NetworkCheck(this);
			obj.execute();
			Toast.makeText(getApplicationContext(), "Sending Emergency Alert",
					Toast.LENGTH_SHORT).show();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		Log.d("Check", "isNetworkAvailable");
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
				netThreadFailed = true;
				return false;
			}
		}
		return false;
	}

	class NetworkCheck extends AsyncTask<Void, Void, Boolean> {

		Context context;

		public NetworkCheck(Context context) {
			this.context = context;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Log.d("Check", "doInBackground network check");
			return hasActiveInternetConnection(getApplicationContext());
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			// Debug
			if (result == true) {
				Log.d("Check", "returned true");
			} else {
				if (netThreadFailed == true)
					Log.d("Check", "returned false due to netThreadFailure");
				Log.d("Check", "returned false");
			}

			if (result == true) {
				Log.d("Check", "Calling location()");
				location();
			} else {
				Log.d("Check", "Toast No network/internet connection found");
				Toast.makeText(getApplicationContext(),
						"No Network/Internet Connection Found",
						Toast.LENGTH_LONG).show();
				netConnection = false;
				Log.d("Check", "calling send()");
				send();
			}
		}
	}

	public void location() {

		loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria cri = new Criteria();
		provider = loc.getBestProvider(cri, true);
		Log.d("Check", "In location()");
		Log.d("Check", provider);

		// gettting location update to all providers
		Log.d("Check", "requesting " + provider);
		loc.requestLocationUpdates(provider, 4000, 1, AlertFromWidget.this);
		Log.d("Check", "requesting PASSIVE");
		loc.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 4000, 1,
				AlertFromWidget.this);
		Log.d("Check", "requesting GPS");
		loc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 1,
				AlertFromWidget.this);
		Log.d("Check", "requesting NETWORK");
		loc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 1,
				AlertFromWidget.this);

		if (provider == null) {

			Log.d("Check", provider + " :provider is NULL");
			Toast.makeText(getApplicationContext(),
					"Location Not Recieved / No Location Provider Found",
					Toast.LENGTH_SHORT).show();
			isGPSOn = false;
			Log.d("Check", "setting isGPSOn=false and calling send()");
			send();

		} else {
			Log.d("Check", provider + " :provider is not NULL");
			Location l = loc.getLastKnownLocation(provider);

			// Debug
			if (l == null)
				Log.d("Check", "location l = null from provider:" + provider);
			else
				Log.d("Check", "location l != null from provider:" + provider);

			if (l != null) {
				Log.d("Check", "Location found: calling on location changed");
				Log.d("Check", "remove updates");
				loc.removeUpdates(AlertFromWidget.this);
				onLocationChanged(l);
			} else {

				if (provider.equals("passive")) {
					Log.d("Check",
							"provider was passive, again checking for Location updates from PASSIVE provider");
					loc.requestLocationUpdates(
							LocationManager.PASSIVE_PROVIDER, 1000, 1,
							AlertFromWidget.this);
					l = loc.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
				} else {

					Log.d("Check",
							"provider was "
									+ provider
									+ ", again checking for Location updates from NETWORK provider");
					loc.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 1000, 1,
							AlertFromWidget.this);

					l = loc.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				if (l == null) {

					Log.d("Check", "again location l = null");
					Toast.makeText(getApplicationContext(),
							"Location Not Recieved", Toast.LENGTH_LONG).show();
					Log.d("Check", "removing updates");
					loc.removeUpdates(AlertFromWidget.this);
					locationFound = false;
					Log.d("Check",
							"seting locationFound == false, toast Location not recieved , calling send()");
					send();
				} else {

					Log.d("Check",
							"location was null first but then got location : calling onLocationChanged(l)");
					Log.d("Check", "removing updates");
					loc.removeUpdates(AlertFromWidget.this);
					onLocationChanged(l);
				}
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		Log.d("Check", "got latitude = " + Double.toString(latitude)
				+ ", and longitude = " + Double.toString(longitude));
		Log.d("Check", "caliling geocoder");
		MyGeoCoder obj = new MyGeoCoder(this);
		obj.execute(location);
	}

	public class MyGeoCoder extends AsyncTask<Location, Void, String> {

		Context context;
		Geocoder geo;

		public MyGeoCoder(Context context) {
			this.context = context;
			geo = new Geocoder(context, Locale.getDefault());
		}

		@Override
		protected String doInBackground(Location... params) {
			Log.d("Check", "do In Backgorund of Geocoder");

			String info = "";
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
				Log.d("Check", "geocoder thread failed");
				Log.d("Check", "setting geoThreadFailed = true");
				geoThreadFailed = true;
				Log.d("Check", "retuning blank string as geocoded info");
				return "";
			}
			return info;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d("Check", "in on post execute of goecoder");

			if (result != null) {
				Log.d("Check", "got result which is not NULL");
				geoLocation = result;
				Log.d("Check", "caliling send()");
				send();
			} else {
				Toast.makeText(
						context,
						"Could not get the geocoded location. Sending your coordinates..",
						Toast.LENGTH_LONG).show();
				send();
			}
		}
	}

	public void send() {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);

		String startMessage = pref.getString(getString(R.string.key_message),
				getString(R.string.default_message));

		if (netConnection == false || netThreadFailed == true) {
			message = startMessage + "\n #MobAlert";

			Toast.makeText(getApplicationContext(), "Sent : " + message,
					Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(),
					"Check Network/Internet Connection", Toast.LENGTH_LONG)
					.show();

		} else if (isGPSOn == false) {
			message = startMessage + "\n #MobAlert";
			Toast.makeText(getApplicationContext(), "Sent : " + message,
					Toast.LENGTH_LONG).show();
			Toast.makeText(
					getApplicationContext(),
					"Select a Location Provider. Check GPS Status [Switch to High Accuracy]",
					Toast.LENGTH_LONG).show();

		} else if (locationFound == false) {

			message = startMessage + "\n #MobAlert";
			Toast.makeText(getApplicationContext(), "Sent : " + message,
					Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(),
					"Couldn't find current location", Toast.LENGTH_LONG).show();

		} else if (geoThreadFailed == true) {

			String locationmap = "maps.google.com/maps?q=" + latitude + ","
					+ longitude;
			message = startMessage + "\n" + locationmap + "\n #MobAlert";
			Toast.makeText(getApplicationContext(), "Sent : " + message,
					Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(),
					"GeoCoder Problem, re-run to send GeoCodedLocation",
					Toast.LENGTH_LONG).show();

		} else {

			if (netConnection == true && netThreadFailed == false
					&& geoThreadFailed == true && isGPSOn == true)
				Log.d("Check", "ALL FINE");
			String locationmap = "maps.google.com/maps?q=" + latitude + ","
					+ longitude;
			message = startMessage + "\nMy location : \n" + locationmap + "\n"
					+ geoLocation + "\n#MobAlert";
			Toast.makeText(getApplicationContext(), "Sent : " + message,
					Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Successfully Sent",
					Toast.LENGTH_LONG).show();
		}

		StringBuilder builder = new StringBuilder();
		String delim = "";
		Cursor phone = dbObj.getphone();
		Cursor name = dbObj.getname();
		ArrayList<String> name_array = new ArrayList<String>();
		ArrayList<String> phone_array = new ArrayList<String>();
		phone.moveToFirst();
		name.moveToFirst();
		for (int i = 0; i < n; i++) {
			phone_array.add(phone.getString(0));
			phone.moveToNext();
			name_array.add(name.getString(0));
			name.moveToNext();
		}

		try {
			SmsManager smsManager = SmsManager.getDefault();
			for (int i = 0; i < n; i++) {
				builder.append(delim).append(phone_array.get(i));
				delim = ";";
				smsManager.sendTextMessage(builder.toString(), null, message,
						null, null);
				Toast.makeText(
						this,
						"Emergency Message Sent to " + name_array.get(i)
								+ " : " + builder.toString(), Toast.LENGTH_LONG)
						.show();

				builder.setLength(0);
				delim = "";

			}
			dbObj.close();
			stopSelf();
		} catch (Exception e) {
			Toast.makeText(this, "SMS failed, please try again.",
					Toast.LENGTH_LONG).show();
		}
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
}
