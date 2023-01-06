package com.khanstech.offlinebrowser.others;

import com.khanstech.offlinebrowser.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class PhoneFunctionality {

	public final static long VIBRATE_TIME = (long) 500;

	public static void hideKeyboard(Activity parentAct) {
		InputMethodManager inputManager = (InputMethodManager) parentAct
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(parentAct.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void vibrateMobile(Context context) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(VIBRATE_TIME);
	}

	public static void showNotification(Class<?> cls, Context context, int id,
			int icon, String title, String text, boolean auto_cancel) {
		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		Notification notif = new Notification.Builder(context)
				.setContentTitle(title).setContentText(text).setSmallIcon(icon)
				.setContentIntent(pi).build();
		NotificationManager notifManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notif.flags |= auto_cancel ? Notification.FLAG_AUTO_CANCEL
				: Notification.FLAG_NO_CLEAR;
		notifManager.notify(id, notif);
	}

	public static void hideNotification(Context context, int id) {
		NotificationManager notifManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.cancel(id);
	}

	public static boolean isNotificationVisible(Class<?> cls, Context context,
			int id) {
		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getActivity(context, id, intent,
				PendingIntent.FLAG_NO_CREATE);
		return pi != null;
	}

	public static void showAlertDialog(Context context, String title,
			String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog.show();
	}

	public static void copyToClipboard(Context context, String msg) {
		ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Activity.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("", msg);
		clipboard.setPrimaryClip(clip);
		makeToast(context, "Copied to Clipboard");
	}

	public static void makeToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
