package com.deepankarsingh.mobalert;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deepankarsingh.mobalert.helper.DbConnect;

public class AddContact extends ActionBarActivity {

	private EditText ename;
	private EditText enumber;
	DbConnect connect = new DbConnect(this);
	private Button back;
	private Button add;
	private String name;
	private String number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		ename = (EditText) findViewById(R.id.ename);
		enumber = (EditText) findViewById(R.id.ephoneNo);
		add = (Button) findViewById(R.id.badd);
		back = (Button) findViewById(R.id.bback);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				name = ename.getText().toString();
				number = enumber.getText().toString();
				if (name.equals("") || number.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Enter both fields", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Contact Added",
							Toast.LENGTH_SHORT).show();
					connect.insert(name, number);
					onBackPressed();
				}

			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
}
