package com.khanstech.offlinebrowser.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class LinkCRUD {

	private DatabaseHelper dbh;

	public LinkCRUD(Context ctx) {
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

	public void addLink(Link m) {
		ContentValues cv = setValuesTo(m);
		dbh.sqldb.insert(DatabaseHelper.TABLE_LINKS, null, cv);
	}

	public int deleteLink(String title) {
		return dbh.sqldb.delete(DatabaseHelper.TABLE_LINKS,
				DatabaseHelper.TITLE + " =? ", new String[] { title });
	}

	public int deleteAllLinks() {
		return dbh.sqldb.delete(DatabaseHelper.TABLE_LINKS, null,
				new String[] {});
	}

	public ArrayList<String> selectLinksByWebId(String title) {
		ArrayList<String> urls = new ArrayList<String>();
		String selectQuery = "SELECT " + DatabaseHelper.URL + " FROM "
				+ DatabaseHelper.TABLE_LINKS + " WHERE " + DatabaseHelper.TITLE
				+ " = '" + title + "'";
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				urls.add(c.getString(c.getColumnIndex(DatabaseHelper.URL)));
			} while (c.moveToNext());
		}
		c.close();
		return urls;
		// Cursor c = dbh.sqldb
		// .query(DatabaseHelper.TABLE_LINKS, null, DatabaseHelper.WEB_ID
		// + " =? ", new String[] { String.valueOf(web_id) },
		// null, null, null, null);
		// if (c != null)
		// if (c.moveToFirst()) {
		// do {
		// urls.add(c.getString(c.getColumnIndex(DatabaseHelper.URL)));
		// } while (c.moveToNext());
		// }
		// c.close();
		// return urls;
	}

	public ArrayList<Link> selectAllLinks() {
		ArrayList<Link> linksList = new ArrayList<Link>();
		String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_LINKS;
		Cursor c = dbh.sqldb.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				linksList.add(getValuesFrom(c));
			} while (c.moveToNext());
		}
		c.close();
		return linksList;
	}

	private ContentValues setValuesTo(Link m) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.TITLE, m.getTitle());
		cv.put(DatabaseHelper.URL, m.getUrl());
		return cv;
	}

	private Link getValuesFrom(Cursor c) {
		Link m = new Link();
		m.setTitle(c.getString(c.getColumnIndex(DatabaseHelper.TITLE)));
		m.setUrl(c.getString(c.getColumnIndex(DatabaseHelper.URL)));
		return m;
	}
}
