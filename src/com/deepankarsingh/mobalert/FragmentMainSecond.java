package com.deepankarsingh.mobalert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMainSecond extends Fragment {

	Button callb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_second,
				container, false);
		callb = (Button) rootView.findViewById(R.id.b_call);
		TextView tvsayhelp = (TextView) rootView
				.findViewById(R.id.TapOnThisButton);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/headliner.ttf");
		tvsayhelp.setTypeface(typeFace);

		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) getActivity()
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		callb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(getActivity());

				String emergencyNo = pref.getString(
						getString(R.string.key_emergency_no), "");
				if (emergencyNo.equals("")) {
					Toast.makeText(getActivity(),
							"Provide an emergency contact no. in Settings",
							Toast.LENGTH_LONG).show();
				} else {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + emergencyNo));
					startActivity(callIntent);
				}
			}
		});
		return rootView;
	}

	private class PhoneCallListener extends PhoneStateListener {

		private boolean isPhoneCalling = false;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {

			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				isPhoneCalling = true;
			}

			if (TelephonyManager.CALL_STATE_IDLE == state) {
				if (isPhoneCalling) {
					
					Intent i = getActivity().getPackageManager()
							.getLaunchIntentForPackage(
									getActivity().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					isPhoneCalling = false;
				}
			}
		}
	}

}
