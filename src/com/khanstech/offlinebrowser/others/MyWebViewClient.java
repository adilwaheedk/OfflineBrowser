package com.khanstech.offlinebrowser.others;

import com.khanstech.offlinebrowser.activity.ActivityMain;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

	public MyWebViewClient() {
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		if (ActivityMain.progress != null)
			ActivityMain.progress.setVisible(true);
		ActivityMain.urlText.setText(url);
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		String data = "<center><font size=10>"
				+ failingUrl
				+ "<br>"
				+ description
				+ "<br>1. Check Network Connection.<br>2. Check Address for typing errors.</font><center>";
		view.loadData(data, "text/html", "utf-8");
		ActivityMain.urlText.setText("Error");
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		ActivityMain.progress.setVisible(false);
	}
}