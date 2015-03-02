package com.deepankarsingh.mobalert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.deepankarsingh.mobalert.R;

public class FetchAddressIntentService extends IntentService {

	private static final String TAG = "fetch-address-intent-service";
	protected ResultReceiver mReceiver;

	public FetchAddressIntentService() {
		super(TAG);
	}

	@Override
	public void onHandleIntent(Intent intent) {
		String errorMessage = "";
		Log.d("FetchAddressIntentService", "onHandelIntent");
		mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
		if (mReceiver == null) {
			Log.wtf(TAG,
					"No receiver received. There is nowhere to send the results.");
			return;
		}

		Location location = intent
				.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

		if (location == null) {
			errorMessage = getString(R.string.no_location_data_provided);
			Log.wtf(TAG, errorMessage);
			deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
			return;
		}

		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		List<Address> addresses = null;

		try {
			addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
		} catch (IOException ioException) {
			// Catch network or other I/O problems.
			errorMessage = getString(R.string.service_not_available);
			Log.e(TAG, errorMessage, ioException);
		} catch (IllegalArgumentException illegalArgumentException) {
			// Catch invalid latitude or longitude values.
			errorMessage = getString(R.string.invalid_lat_long_used);
			Log.e(TAG,
					errorMessage + ". " + "Latitude = "
							+ location.getLatitude() + ", Longitude = "
							+ location.getLongitude(), illegalArgumentException);
		}

		if (addresses == null || addresses.size() == 0) {
			if (errorMessage.isEmpty()) {
				errorMessage = getString(R.string.no_address_found);
				Log.e(TAG, errorMessage);
			}
			deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
		} else {
			Address address = addresses.get(0);
			ArrayList<String> addressFragments = new ArrayList<String>();
			for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
				addressFragments.add(address.getAddressLine(i));
			}
			Log.i(TAG, getString(R.string.address_found));
			deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(
					System.getProperty("line.separator"), addressFragments));
		}
	}

	private void deliverResultToReceiver(int resultCode, String message) {
		Log.d("FetchAddressIntentService", "deliverResultToReceiver");
		Bundle bundle = new Bundle();
		bundle.putString(Constants.RESULT_DATA_KEY, message);
		mReceiver.send(resultCode, bundle);
	}
}
