package com.deepankarsingh.mobalert;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentHelp extends Fragment {

	public FragmentHelp() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_help, container,
				false);

		TextView t1 = (TextView) rootView
				.findViewById(R.id.tvaboutMobAlertHeader);
		TextView t2 = (TextView) rootView.findViewById(R.id.tvaboutMobAlert);
		TextView t3 = (TextView) rootView.findViewById(R.id.tvaskForHelpHeader);
		TextView t4 = (TextView) rootView.findViewById(R.id.tvaskForHelp);
		TextView t5 = (TextView) rootView.findViewById(R.id.tvfeaturesHeader);
		TextView t6 = (TextView) rootView.findViewById(R.id.tvfeaturesstart);
		TextView t7 = (TextView) rootView.findViewById(R.id.tvfeaturesbody1);
		TextView t8 = (TextView) rootView.findViewById(R.id.tvHelpScreen);
		TextView t9 = (TextView) rootView.findViewById(R.id.tvCallScreen);
		TextView t10 = (TextView) rootView
				.findViewById(R.id.tvNavigationScreen);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/timeburner.ttf");
		Typeface typeFace1 = Typeface.createFromAsset(
				getActivity().getAssets(), "fonts/robotothin.ttf");
		t1.setTypeface(typeFace);
		t2.setTypeface(typeFace1);
		t3.setTypeface(typeFace);
		t4.setTypeface(typeFace1);
		t5.setTypeface(typeFace);
		t6.setTypeface(typeFace1);
		t7.setTypeface(typeFace1);
		t8.setTypeface(typeFace1);
		t9.setTypeface(typeFace1);
		t10.setTypeface(typeFace1);
		return rootView;
	}
}
