package com.khanstech.offlinebrowser.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.khanstech.offlinebrowser.R;
import com.khanstech.offlinebrowser.database.Webpage;
import com.khanstech.offlinebrowser.others.MyWebViewClient;
import com.khanstech.offlinebrowser.others.PhoneFunctionality;
import com.khanstech.offlinebrowser.webview.ArchiveHelper;

public class ActivityMain extends Activity {

	public static TextView urlText;
	public static MenuItem progress;
	public SharedPreferences sharedPrefs;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webView = (WebView) findViewById(R.id.webViewMain);
		urlText = (TextView) findViewById(R.id.urlText);
		setTextViewListener();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Bundle extras = this.getIntent().getExtras();
		if (extras != null) {
			String url;
			if (!(Boolean) extras.get("isOnline")) {
				String title = (String) extras.get("title");
				url = (String) extras.get("url");
				loadWebArchive(title, url);
				PhoneFunctionality.makeToast(this, title + " Archive Loaded");
			} else {
				url = (String) extras.get("url");
				LoadWebpage(url);
			}
		} else {
			LoadWebpage(correctUrl(sharedPrefs.getString(
					ActivityAppSettings.HOMEPAGE, "about:blank")));
		}
		if (sharedPrefs.getBoolean(ActivityAppSettings.VIBRATE, false))
			PhoneFunctionality.vibrateMobile(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		progress = menu.findItem(R.id.menu_progress);
		progress.setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_home:
			LoadWebpage(correctUrl(sharedPrefs.getString(
					ActivityAppSettings.HOMEPAGE, "about:blank")));
			return true;
		case R.id.action_save:
			saveWebpage(urlText.getText().toString());
			return true;
		case R.id.action_savelist:
			startActivity(new Intent(this, ActivitySaveList.class));
			return true;
		case R.id.action_settings:
			startActivity(new Intent(this, ActivityAppSettings.class));
			return true;
		case R.id.action_cancel:
			webView.stopLoading();
			return true;
		case R.id.action_refresh:
			LoadWebpage(urlText.getText().toString());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setProgressBarView(boolean visible) {
		setProgressBarVisibility(visible);
	}

	// Send Button
	public void SendButton(View v) {
		PhoneFunctionality.hideKeyboard(this);
		LoadWebpage(correctUrl(urlText.getText().toString()));
	}

	// Settings Button
	public void SettingsButton(View v) {
		startActivity(new Intent(this, ActivityAppSettings.class));
	}

	// Home Button
	public void HomeButton(View v) {
		LoadWebpage(correctUrl(sharedPrefs.getString(
				ActivityAppSettings.HOMEPAGE, "about:blank")));
	}

	// Refresh Button
	public void RefreshButton(View v) {
		LoadWebpage(urlText.getText().toString());
	}

	private void saveWebArchive(Context context, Webpage wp, boolean saveLink) {
		ArchiveHelper helper = new ArchiveHelper(webView);
		helper.saveArchive(context, wp, saveLink);
	}

	private void loadWebArchive(String title, String url) {
		ArchiveHelper helper = new ArchiveHelper(webView);
		helper.loadArchive(title);
		urlText.setText(url);
		this.setTitle("Offline Page");
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void SetWebViewSettings(WebView webview) {
		webView.setWebViewClient(new MyWebViewClient());
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setBuiltInZoomControls(false);
		webview.getSettings().setDomStorageEnabled(
				sharedPrefs.getBoolean(ActivityAppSettings.DOM, false));
		webview.getSettings().setJavaScriptEnabled(
				sharedPrefs.getBoolean(ActivityAppSettings.JAVASCRIPT, false));
	}

	private void LoadWebpage(String url) {
		if (url != null) {
			SetWebViewSettings(webView);
			webView.loadUrl(url);
			urlText.setText(url);
			this.setTitle("Online Browsing");
		}
	}

	private void saveWebpage(final String url) {
		if (url.length() > 0) {
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialog_savepage);
			dialog.setTitle("Save Webpage");
			TextView urltext = (TextView) dialog.findViewById(R.id.labelURL);
			Button dialogButton = (Button) dialog.findViewById(R.id.buttonSave);
			final CheckBox saveLinkCB = (CheckBox) dialog
					.findViewById(R.id.checkBoxLinks);
			final TextView titletext = (TextView) dialog
					.findViewById(R.id.textTitle);
			urltext.setText(url);
			dialogButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (titletext.length() > 0) {
						dialog.dismiss();
						saveWebArchive(dialog.getContext(), new Webpage(
								titletext.getText().toString(), url),
								saveLinkCB.isChecked());
					} else
						PhoneFunctionality.makeToast(dialog.getContext(),
								"Enter Title To Save");
				}
			});
			dialog.show();
		} else
			PhoneFunctionality.makeToast(this, "Enter URL To Save");
	}

	private String correctUrl(String url) {
		if (url != "about:blank" & !url.contains("http://"))
			url = "http://" + url;
		return url.toLowerCase().trim();
	}

	private void setTextViewListener() {
		urlText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (v.getId()) {
				case R.id.urlText:
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						SendButton(v);
						return true;
					}
				}
				return false;
			}
		});
	}
}
