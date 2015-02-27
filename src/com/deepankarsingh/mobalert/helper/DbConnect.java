package com.deepankarsingh.mobalert.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbConnect extends SQLiteOpenHelper {

	ContentValues contentValues;
	Context c;

	/******* Database Constants *******/
	private static final String DATABASE_NAME = "MobAlertContacts.db";
	private static final String DATABASE_TABLE = "Contacts";
	private static final int DATABASE_VERSION = 1;
	private static final String NAME = "name";
	private static final String NUMBER = "number";
	private static final String CREATE_TABLE = "CREATE TABLE " + DATABASE_TABLE
			+ "(" + NAME + " VARCHAR(40)," + NUMBER
			+ " VARCHAR(20) PRIMARY KEY)";
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ DATABASE_TABLE + ";";

	public DbConnect(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.c = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_TABLE);
		} catch (SQLException e) {
			Toast.makeText(c, "Couldn't Create Database Table",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL(DROP_TABLE);
		} catch (SQLException e) {
			Toast.makeText(c, "Couldn't Drop Existing Table", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
		onCreate(db);
	}

	public void insert(String name, String phone) {
		SQLiteDatabase db = getWritableDatabase();
		contentValues = new ContentValues();
		contentValues.put(NAME, name);
		contentValues.put(NUMBER, phone);
		db.insert(DATABASE_TABLE, null, contentValues);
	}

	public Cursor getname() {
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT " + NAME + " FROM " + DATABASE_TABLE;
		Cursor cr = db.rawQuery(query, null);
		return cr;
	}

	public Cursor getphone() {
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT " + NUMBER + " FROM " + DATABASE_TABLE;
		Cursor cr = db.rawQuery(query, null);
		return cr;
	}

	public void delete(String name, String number) {
		SQLiteDatabase db = getWritableDatabase();
		String[] whereArgs = { name, number };
		int count = db.delete(DATABASE_TABLE, NAME + "=? AND " + NUMBER + "=?",
				whereArgs);
		if (count != 0)
			Toast.makeText(c, "Entery deleted!! ", Toast.LENGTH_SHORT).show();
	}
}