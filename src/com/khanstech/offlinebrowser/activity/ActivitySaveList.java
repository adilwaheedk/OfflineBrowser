package com.khanstech.offlinebrowser.activity;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.khanstech.offlinebrowser.R;
import com.khanstech.offlinebrowser.database.LinkCRUD;
import com.khanstech.offlinebrowser.database.Webpage;
import com.khanstech.offlinebrowser.database.WebpageCRUD;
import com.khanstech.offlinebrowser.others.ActionBarCallBack;
import com.khanstech.offlinebrowser.others.PhoneFunctionality;
import com.khanstech.offlinebrowser.others.WebpageAdapter;

public class ActivitySaveList extends Activity {

	public TextView textNoWebpage;
	private ListView listView;
	private WebpageAdapter adapter;
	private List<Webpage> wpList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_savelist);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		textNoWebpage = (TextView) findViewById(R.id.textNoWebpage);
		textNoWebpage.setVisibility(View.INVISIBLE);
		listView = (ListView) findViewById(R.id.listviewWebpages);
		populateWebpages(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_savelist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			return true;
		case R.id.action_select_multiple:
			startActionMode(new ActionBarCallBack(this, adapter));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void populateWebpages(Context context) {
		WebpageCRUD crud = new WebpageCRUD(context);
		crud.openDB();
		wpList = crud.selectAllWebpages();
		crud.closeDB();
		adapter = new WebpageAdapter(this, wpList);
		listView.setAdapter(adapter);
		if (wpList.isEmpty())
			textNoWebpage.setVisibility(View.VISIBLE);
	}

	public void loadPage(Webpage wp, boolean isOnline) {
		String title = wp.getTitle();
		String url = wp.getUrl();
		Intent i = new Intent(ActivitySaveList.this, ActivityMain.class);
		i.putExtra("isOnline", isOnline);
		i.putExtra("title", title);
		i.putExtra("url", url);
		startActivity(i);
	}

	public void loadLinks(final Webpage wp) {
		LinkCRUD crud = new LinkCRUD(this);
		crud.openDB();
		List<String> links = crud.selectLinksByWebId(wp.getTitle());
		crud.closeDB();
		if (links.size() > 0) {
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialog_links);
			dialog.setTitle(wp.getTitle() + " Child Links");
			ListView linksListView = (ListView) dialog
					.findViewById(R.id.linksListView);
			final ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1, links);
			linksListView.setAdapter(arrAdapter);
			linksListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					wp.setUrl(arrAdapter.getItem(position));
					loadPage(wp, true);
				}
			});
			dialog.show();
		} else
			PhoneFunctionality.makeToast(this, wp.getTitle()
					+ " Child Links Not Available");
	}
}
