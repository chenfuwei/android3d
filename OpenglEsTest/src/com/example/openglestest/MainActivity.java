package com.example.openglestest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
MyTDView mview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mview = new MyTDView(this);
		mview.requestFocus();
		mview.setFocusableInTouchMode(true);
		setContentView(mview);
	}
	
	@Override
		protected void onResume() {
			super.onResume();
			mview.onResume();
		}
	
	@Override
		protected void onPause() {
			super.onPause();
			mview.onPause();
		}
}
