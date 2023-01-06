package com.khanstech.offlinebrowser.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import com.khanstech.offlinebrowser.R;

public class ActivitySplash extends Activity {

	private static int secs_delay = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		final Object activity;
		activity = ActivityMain.class;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				startActivity(new Intent(ActivitySplash.this,
						(Class<?>) activity));
				finish();
			}
		}, secs_delay * 800);
	}
}
