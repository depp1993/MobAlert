package com.deepankarsingh.mobalert.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbConnect extends SQLiteOpenHelper {

	ContentValues cp;
	Context c;
	private static final String database = "MobAlertContacts.db";
	private static final String table = "contacts";

	public DbConnect(Context context) {
		super(context, database, null, 1);
		this.c = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "create table " + table
				+ "(name text(40),number text(20) primary key)";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query = "drop table if exists " + table;
		db.execSQL(query);
		onCreate(db);
	}

	public void insert(String name, String phone) {
		SQLiteDatabase db = getWritableDatabase();
		cp = new ContentValues();
		cp.put("name", name);
		cp.put("number", phone);
		db.insert(table, null, cp);
	}

	public Cursor getname() {
		SQLiteDatabase db = getReadableDatabase();
		String query = "select name from " + table;
		Cursor cr = db.rawQuery(query, null);
		return cr;
	}

	public Cursor getphone() {
		SQLiteDatabase db = getReadableDatabase();
		String query = "select number from " + table;
		Cursor cr = db.rawQuery(query, null);
		return cr;

	}

	public void getdeletenum(String s) {

		SQLiteDatabase db = getWritableDatabase();
		String query = "delete from " + table + " where number='" + s + "'";
		db.execSQL(query);
		Toast.makeText(c, "Entery deleted!! ", Toast.LENGTH_SHORT).show();
	}

	public void getdeletename(String s) {

		SQLiteDatabase db = getWritableDatabase();
		String query = "delete from " + table + " where name='" + s + "'";
		db.execSQL(query);
		Toast.makeText(c, "Entery deleted!! ", Toast.LENGTH_SHORT).show();
	}

}