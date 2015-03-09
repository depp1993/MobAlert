package com.deepankarsingh.mobalert;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.deepankarsingh.mobalert.helper.DbConnect;

public class Alert extends ActionBarActivity {

	private TextView tvInformation;
	private TextView tvMessageHeader;
	private TextView tvMessage;
	private DbConnect dbObj;
	private Location currentLocation;
	private LocationProvider location;
	protected String mAddressOutput;
	private AddressResultReceiver mResultReceiver;
	private String message;
	private double latitude;
	private double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbObj = new DbConnect(getApplicationContext());
		setContentView(R.layout.activity_alert);

		tvMessage = (TextView) findViewById(R.id.tvMessage);
		tvInformation = (TextView) findViewById(R.id.tvInfo);
		tvMessageHeader = (TextView) findViewById(R.id.tvMessageHeader);

		tvMessageHeader.setText("Sending Message.....");
		tvMessageHeader.setVisibility(View.VISIBLE);
		mResultReceiver = new AddressResultReceiver(new Handler());
		location = new LocationProvider();
		currentLocation = location.getLocation();

		if (currentLocation != null) {

			latitude = currentLocation.getLatitude();
			longitude = currentLocation.getLongitude();

			if (!Geocoder.isPresent()) {
				Toast.makeText(this, R.string.no_geocoder_available,
						Toast.LENGTH_LONG).show();
				sendMessage(getString(R.string.no_geocoder_available)); // 1 -
																		// Geocoder
																		// is
																		// not
																		// present,
																		// Location
																		// found

			} else {
				// Log.d("Alert", "Geocoder present");
				startIntentService();
			}
		} else {
			// Location could not be updated
			Toast.makeText(getApplicationContext(), R.string.loc_not_found,
					Toast.LENGTH_LONG).show();
			sendMessage(getString(R.string.loc_not_found)); // 2 - Location not
															// found
		}
	}

	protected void startIntentService() {
		Intent intent = new Intent(this, FetchAddressIntentService.class);
		intent.putExtra(Constants.RECEIVER, mResultReceiver);
		intent.putExtra(Constants.LOCATION_DATA_EXTRA, currentLocation);
		startService(intent);
	}

	class AddressResultReceiver extends ResultReceiver {
		public AddressResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {

			if (resultCode == Constants.SUCCESS_RESULT) {

				mAddressOutput = resultData
						.getString(Constants.RESULT_DATA_KEY);
				// Log.i("Alert", mAddressOutput);
				sendMessage(getString(R.string.address_found)); // 3 - Address
																// found,
																// Location
																// found

			} else {
				sendMessage(getString(R.string.no_address_found)); // 1 -
																	// Geocoder
																	// error ,
																	// Location
																	// found
			}
		}
	}

	public void sendMessage(String locinfo) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);

		String startMessage = pref.getString(getString(R.string.key_message),
				getString(R.string.default_message));

		message = startMessage + "\n #MobAlert";

		tvMessageHeader.setText("Sent Message:");
		tvMessageHeader.setVisibility(View.VISIBLE);

		if (locinfo.equals(getString(R.string.address_found))) {

			String locationmap = "maps.google.com/maps?q=" + latitude + ","
					+ longitude;

			message = startMessage + "\n" + locationmap + "\n" + mAddressOutput
					+ "\n #MobAlert";

			tvMessage.setText(message);
			tvMessage.setVisibility(View.VISIBLE);

		} else if (locinfo.equals(getString(R.string.no_address_found))
				|| locinfo.equals(getString(R.string.no_geocoder_available))) {

			String locationmap = "maps.google.com/maps?q=" + latitude + ","
					+ longitude;
			message = startMessage + "\n" + locationmap + "\n #MobAlert";
			tvMessage.setText(message);
			tvMessage.setVisibility(View.VISIBLE);
			tvInformation.setText("Info : GeoCoder ran into problem.");
			tvInformation.setVisibility(View.VISIBLE);

		} else if (locinfo.equals(getString(R.string.loc_not_found))) {

			tvMessage.setText(message);
			tvMessage.setVisibility(View.VISIBLE);
			tvInformation.setText("Location Not Found");
			tvInformation.setVisibility(View.VISIBLE);
		}

		StringBuilder builder = new StringBuilder();
		String delim = "";
		Cursor phone = dbObj.getphone();
		ArrayList<String> phone_array = new ArrayList<String>();
		while (phone.moveToNext()) {
			phone_array.add(phone.getString(phone
					.getColumnIndex(DbConnect.NUMBER)));
		}

		int n = phone_array.size();

		try {
			SmsManager smsManager = SmsManager.getDefault();
			for (int i = 0; i < n; i++) {
				builder.append(delim).append(phone_array.get(i));
				delim = ";";
				ArrayList<String> divMessage = smsManager
						.divideMessage(message);
				for (int j = 0; j < divMessage.size(); j++) {
					smsManager.sendTextMessage(divMessage.get(j), null,
							message, null, null);

				}
				builder.setLength(0);
				delim = "";
			}
			Toast.makeText(this, "Emergency Message Sent!!", Toast.LENGTH_LONG)
					.show();
			dbObj.close();
		} catch (Exception e) {
			Toast.makeText(this, "SMS failed, please try again.",
					Toast.LENGTH_LONG).show();
		}
	}
}
