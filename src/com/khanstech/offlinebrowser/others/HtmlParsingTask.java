package com.khanstech.offlinebrowser.others;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.khanstech.offlinebrowser.database.Link;
import com.khanstech.offlinebrowser.database.LinkCRUD;
import com.khanstech.offlinebrowser.database.Webpage;

public class HtmlParsingTask extends AsyncTask<Boolean, Void, Void> {

	private Webpage wp;
	private Context context;
	private ProgressDialog pd;

	public HtmlParsingTask(Context context, Webpage wp) {
		this.context = context;
		this.wp = wp;
	}

	@Override
	protected void onPreExecute() {
		pd = ProgressDialog.show(context, "Saving Links of " + wp.getTitle(),
				"Please wait for a moment...", false);
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Boolean... params) {
		try {
			List<Link> list = HtmlParser.getLinksFromURL(wp.getTitle(),
					wp.getUrl());
			LinkCRUD crud = new LinkCRUD(context);
			crud.openDB();
			if (params[0])
				crud.deleteLink(wp.getTitle());
			for (int i = 0; i < list.size(); i++) {
				crud.addLink(list.get(i));
			}
			crud.closeDB();

		} catch (RuntimeException e) {
			Log.i("error", "RuntimeException: " + e.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		pd.dismiss();
	}
}
