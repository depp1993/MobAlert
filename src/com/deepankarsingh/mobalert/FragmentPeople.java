package com.deepankarsingh.mobalert;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.deepankarsingh.mobalert.helper.DbConnect;
import com.deepankarsingh.mobalert.helper.MyAdapter;

public class FragmentPeople extends Fragment {

	protected static final int PICK_CONTACT_FROM_LIST = 5;
	int n;
	ImageButton baddNew;
	ImageButton baddContact;
	ListView alertcontacts;
	DbConnect connect;
	Cursor name;
	Cursor phone;
	MainActivity obj = new MainActivity();

	@Override
	public void onResume() {
		super.onResume();
		showListView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_people, container,
				false);
		baddNew = (ImageButton) rootView.findViewById(R.id.baddNew);
		baddContact = (ImageButton) rootView
				.findViewById(R.id.baddNew_fromContacts);
		alertcontacts = (ListView) rootView.findViewById(R.id.lcontact_list);
		baddNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), AddContact.class));
			}
		});

		baddContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(i, PICK_CONTACT_FROM_LIST);
			}
		});

		showListView();

		alertcontacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String delname = (((TextView) view
						.findViewById(R.id.tcontactName)).getText().toString());
				String delnum = (((TextView) view
						.findViewById(R.id.tcontactPhone)).getText().toString());

				deleteAlert(delname, delnum);
			}
		});
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (PICK_CONTACT_FROM_LIST):
			if (resultCode == Activity.RESULT_OK) {
				String p_no = null;
				String c_name = null;
				String id = null;

				Uri contactData = data.getData();
				Cursor c = getActivity().getContentResolver().query(
						contactData, null, null, null, null);

				if (c.moveToFirst()) {
					id = c.getString(c
							.getColumnIndex(ContactsContract.Contacts._ID));
					c_name = c.getString(c
							.getColumnIndex(Contacts.DISPLAY_NAME));

				}
				Cursor pCur = getActivity().getContentResolver().query(
						Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + id, null, null);
				if (pCur.moveToFirst()) {
					p_no = pCur.getString(pCur.getColumnIndex(Phone.NUMBER));

				}
				connect.insert(c_name, p_no);
				obj.peopleFlag = 1;
				showListView();

			}
		}
	}

	public void showListView() {
		connect = new DbConnect(getActivity());
		name = connect.getname();
		phone = connect.getphone();
		n = name.getCount();

		ArrayList<String> name_array = new ArrayList<String>();
		ArrayList<String> phone_array = new ArrayList<String>();
		name.moveToFirst();
		phone.moveToFirst();
		for (int i = 0; i < n; i++) {
			name_array.add(name.getString(0));
			name.moveToNext();
			phone_array.add(phone.getString(0));
			phone.moveToNext();
		}

		MyAdapter adap = new MyAdapter(getActivity(), name_array, phone_array);
		alertcontacts.setAdapter(adap);
	}

	private void deleteAlert(final String name, final String num) {
		AlertDialog.Builder alert;
		alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("Delete " + name + " from Emergency Contacts?");
		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				connect.getdeletenum(num);
				connect.getdeletename(name);
				showListView();
			}
		});
		alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		alert.show();
	}
}
