package com.deepankarsingh.mobalert;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class CallFromWidget extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		String emergencyNo = pref.getString(
				getString(R.string.key_emergency_no), "");

		if (emergencyNo.equals("")) {
			Toast.makeText(this,
					"Provide an emergency contact no. in Settings",
					Toast.LENGTH_LONG).show();
		} else {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + emergencyNo));
			startActivity(callIntent);
		}
		finish();

	}
}
