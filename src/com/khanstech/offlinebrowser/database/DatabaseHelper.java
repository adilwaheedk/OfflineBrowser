package com.khanstech.offlinebrowser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper sInstance;
	private static final String DATABASE_NAME = "OfflineBrowserDB";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_WEBPAGES = "Webpages";
	public static final String TABLE_LINKS = "Links";
	public static final String ID = "_id";
	public static final String TITLE = "title";
	public static final String URL = "url";
	public static final String XML = "xml";

	public SQLiteDatabase sqldb;

	// Create Table Queries
	private static final String CREATE_TABLE_WEBPAGES = "CREATE TABLE "
			+ TABLE_WEBPAGES + " ( " + ID + " INTEGER PRIMARY KEY, " + TITLE
			+ " TEXT UNIQUE, " + URL + " TEXT )";

	private static final String CREATE_TABLE_LINKS = "CREATE TABLE "
			+ TABLE_LINKS + " ( " + TITLE + " TEXT, " + URL + " TEXT )";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DatabaseHelper getInstance(Context context) {
		if (sInstance == null)
			sInstance = new DatabaseHelper(context.getApplicationContext());
		return sInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_WEBPAGES);
		db.execSQL(CREATE_TABLE_LINKS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_WEBPAGES);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LINKS);
		onCreate(db);
	}

}
