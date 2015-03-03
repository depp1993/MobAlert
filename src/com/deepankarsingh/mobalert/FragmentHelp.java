package com.deepankarsingh.mobalert;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentHelp extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_help, container,
				false);

		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/timeburner.ttf");
		Typeface typeFace1 = Typeface.createFromAsset(
				getActivity().getAssets(), "fonts/robotothin.ttf");

		TextView t1 = (TextView) rootView
				.findViewById(R.id.tvaboutMobAlertHeader);
		t1.setTypeface(typeFace);

		TextView t2 = (TextView) rootView.findViewById(R.id.tvaboutMobAlert);
		t2.setTypeface(typeFace1);

		TextView t3 = (TextView) rootView.findViewById(R.id.tvaskForHelpHeader);
		t3.setTypeface(typeFace);

		TextView t4 = (TextView) rootView.findViewById(R.id.tvaskForHelp);
		t4.setTypeface(typeFace1);

		TextView t5 = (TextView) rootView.findViewById(R.id.tvfeaturesHeader);
		t5.setTypeface(typeFace);

		TextView t6 = (TextView) rootView.findViewById(R.id.tvfeaturesstart);
		t6.setTypeface(typeFace1);

		TextView t7 = (TextView) rootView.findViewById(R.id.tvfeaturesbody1);
		t7.setTypeface(typeFace1);

		TextView t8 = (TextView) rootView.findViewById(R.id.tvHelpScreen);
		t8.setTypeface(typeFace1);

		TextView t9 = (TextView) rootView.findViewById(R.id.tvCallScreen);
		t9.setTypeface(typeFace1);

		TextView t10 = (TextView) rootView
				.findViewById(R.id.tvNavigationScreen);
		t10.setTypeface(typeFace1);

		TextView t11 = (TextView) rootView.findViewById(R.id.tvfeaturesbody2);
		t11.setTypeface(typeFace1);
		TextView t12 = (TextView) rootView
				.findViewById(R.id.tvNotifyingcontactsScreen);
		t12.setTypeface(typeFace1);

		TextView t13 = (TextView) rootView.findViewById(R.id.tvfeaturesbody3);
		t13.setTypeface(typeFace1);

		TextView t14 = (TextView) rootView.findViewById(R.id.tvAlertMessage);
		t14.setTypeface(typeFace1);

		TextView t15 = (TextView) rootView.findViewById(R.id.tvfeaturesbody4);
		t15.setTypeface(typeFace1);

		TextView t16 = (TextView) rootView.findViewById(R.id.tvPeopleHeader);
		t16.setTypeface(typeFace);

		TextView t17 = (TextView) rootView.findViewById(R.id.tvAddPeople);
		t17.setTypeface(typeFace1);

		TextView t18 = (TextView) rootView.findViewById(R.id.tvPeopleAdd);
		t18.setTypeface(typeFace1);

		TextView t19 = (TextView) rootView.findViewById(R.id.tvPeopleDelete);
		t19.setTypeface(typeFace1);

		TextView t20 = (TextView) rootView.findViewById(R.id.tvSettingsHeader);
		t20.setTypeface(typeFace);

		TextView t21 = (TextView) rootView.findViewById(R.id.tvSettingsInfo);
		t21.setTypeface(typeFace1);

		TextView t22 = (TextView) rootView.findViewById(R.id.tvSettings);
		t22.setTypeface(typeFace1);

		TextView t23 = (TextView) rootView.findViewById(R.id.tvWidgetHeader);
		t23.setTypeface(typeFace);

		TextView t24 = (TextView) rootView.findViewById(R.id.tvWidget);
		t24.setTypeface(typeFace1);

		TextView t25 = (TextView) rootView.findViewById(R.id.tvWidgetiimage);
		t25.setTypeface(typeFace1);

		return rootView;
	}
}
