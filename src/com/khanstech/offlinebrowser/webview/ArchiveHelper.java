package com.khanstech.offlinebrowser.webview;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import com.khanstech.offlinebrowser.database.Webpage;
import com.khanstech.offlinebrowser.database.WebpageCRUD;
import com.khanstech.offlinebrowser.others.HtmlParsingTask;
import com.khanstech.offlinebrowser.others.MyWebViewClient;
import com.khanstech.offlinebrowser.others.PhoneFunctionality;

public class ArchiveHelper {

	private WebView webView;
	private String path, extension;
	private int sdk;

	public ArchiveHelper(WebView webView) {
		this.webView = webView;
		path = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "OfflineBrowser";
		sdk = android.os.Build.VERSION.SDK_INT;
		extension = sdk < 19 ? ".xml" : ".mht";
	}

	public void saveArchive(Context context, Webpage wp, boolean saveLink) {
		try {
			if (webView != null) {
				WebpageCRUD crud = new WebpageCRUD(context);
				crud.openDB();
				boolean isUpdate = false;
				if (!crud.isWebpageExist(wp.getTitle(), wp.getUrl()))
					crud.addWebpage(wp);
				else {
					isUpdate = true;
					crud.updateWebpage(wp);
				}
				crud.closeDB();
				File directory = new File(path);
				if (!directory.exists())
					directory.mkdirs();
				webView.saveWebArchive(directory.toString() + File.separator
						+ wp.getTitle() + extension);
				if (saveLink) {
					HtmlParsingTask task = new HtmlParsingTask(context, wp);
					task.execute(new Boolean[] { isUpdate });
				}
				PhoneFunctionality.makeToast(context, wp.getTitle() + " Archive Saved");
			}
		} catch (Exception e) {
			Log.e("error", "saveArchive " + e.getMessage());
		}
	}

	public void loadArchive(String fileName) {
		try {
			if (webView != null) {
				if (sdk < 19) {

					FileInputStream fis = new FileInputStream(path
							+ File.separator + fileName + extension);
					WebArchiveReader reader = new WebArchiveReader() {
						void onFinished(WebView v) {
							webView.setWebViewClient(new MyWebViewClient());
						}
					};
					if (reader.readWebArchive(fis)) {
						reader.loadToWebView(webView);
					}

				} else
					webView.loadUrl(path + File.separator + fileName
							+ extension);
			}
		} catch (Exception e) {
			Log.e("error", "loadArchive " + e.getMessage());
		}
	}

	public boolean deleteArchive(String title) {
		try {
			File file = new File(path + File.separator + title + extension);
			if (file.delete()) {
				if (new File(path).listFiles().length == 0)
					new File(path).delete();
				return true;
			}
		} catch (Exception e) {
			Log.e("error", "deleteArchive " + e.getMessage());
		}
		return false;
	}
}
