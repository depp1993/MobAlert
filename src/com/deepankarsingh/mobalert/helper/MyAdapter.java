package com.deepankarsingh.mobalert.helper;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deepankarsingh.mobalert.R;

public class MyAdapter extends BaseAdapter {

	Context context;
	ArrayList<String> name;
	ArrayList<String> number;
	LayoutInflater inflate;

	public MyAdapter(Context context, ArrayList<String> name,
			ArrayList<String> number) {
		this.context = context;
		this.name = name;
		this.number = number;
		inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return name.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflate.inflate(R.layout.contact_list_item, null);
		}

		TextView tname = (TextView) convertView.findViewById(R.id.tcontactName);
		TextView tnumber = (TextView) convertView
				.findViewById(R.id.tcontactPhone);
		tname.setText(name.get(position));
		tnumber.setText(number.get(position));
		return convertView;
	}
}
