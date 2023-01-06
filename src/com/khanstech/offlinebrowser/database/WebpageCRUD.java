package com.khanstech.offlinebrowser.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class WebpageCRUD {

	private DatabaseHelper dbh;

	public WebpageCRUD(Context ctx) {
		dbh = new DatabaseHelper(ctx);
	}

	public void openDB() throws NullPointerException {
		dbh.sqldb = dbh.getWritableDatabase();
	}

	public void closeDB() {
		dbh.sqldb = dbh.getReadableDatabase();
		if (dbh.sqldb != null && dbh.sqldb.isOpen())
			dbh.sqldb.close();
	}

	public void addWebpage(Webpage m) {
		ContentValues cv = setValuesTo(m);
		dbh.sqldb.insert(DatabaseHelper.TABLE_WEBPAGES, null, cv);
	}

	public int deleteWebpage(String title) {
		return dbh.sqldb.delete(DatabaseHelper.TABLE_WEBPAGES,
				DatabaseHelper.TITLE + " =? ", new String[] { title });
	}

	public int deleteAllWebpages() {
		return dbh.sqldb.delete(DatabaseHelper.TABLE_WEBPAGES, null,
				new String[] {});
	}

	public int updateWebpage(Webpage m) {
		ContentValues cv = setValuesTo(m);
		int noOfRows = dbh.sqldb.update(DatabaseHelper.TABLE_WEBPAGES, cv,
				DatabaseHelper.TITLE + " =? ", new String[] { m.getTitle() });
		return noOfRows;
	}

	public boolean isWebpageExist(String title, String url) {
		Cursor c = dbh.sqldb.query(DatabaseHelper.TABLE_WEBPAGES,
				new String[] { DatabaseHelper.ID }, DatabaseHelper.TITLE
						+ " =? OR " + DatabaseHelper.URL + " =?", new String[] {
						title, url }, null, null, null, "1");
		if (c.isNull(0))
			return false;
		else
			return true;
	}

	public Webpage selectWebpageByTitle(String title) {
		Cursor c = dbh.sqldb.query(DatabaseHelper.TABLE_WEBPAGES, null,
				DatabaseHelper.TITLE + " =? ", new String[] { title }, null,
				null, null, null);
		if (c != null)
			c.moveToFirst();
		Webpage m = getValuesFrom(c);
		return m;
	}

	public ArrayList<Webpage> selectAllWebpages() {
		ArrayList<Webpage> machineList = new ArrayList<Webpage>();
		String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_WEBPAGES;
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				machineList.add(getValuesFrom(c));
			} while (c.moveToNext());
		}
		c.close();
		return machineList;
	}

	private ContentValues setValuesTo(Webpage m) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.TITLE, m.getTitle());
		cv.put(DatabaseHelper.URL, m.getUrl());
		return cv;
	}

	private Webpage getValuesFrom(Cursor c) {
		Webpage m = new Webpage();
		m.setTitle(c.getString(c.getColumnIndex(DatabaseHelper.TITLE)));
		m.setUrl(c.getString(c.getColumnIndex(DatabaseHelper.URL)));
		return m;
	}
}
