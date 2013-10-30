package com.kelviomatias.romscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public abstract class AbstractSplashScreenActivity extends Activity {

	public abstract Class<? extends Activity> getNextActivityClass();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash_screen);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500);
					startActivity(new Intent(AbstractSplashScreenActivity.this,
							getNextActivityClass()));
					finish();
				} catch (Exception e) {

				}
			}
		}).start();

		/*
		 * AsyncTask<Void, Void, Void> a = new AsyncTask<Void, Void, Void>() {
		 * 
		 * @Override protected Void doInBackground(Void... params) { try {
		 * 
		 * ConnectivityManager connectivityManager = (ConnectivityManager)
		 * getSystemService(Context.CONNECTIVITY_SERVICE); if
		 * (connectivityManager != null) { NetworkInfo ni =
		 * connectivityManager.getActiveNetworkInfo(); if (ni == null ||
		 * ni.getState() != NetworkInfo.State.CONNECTED) { // record the fact
		 * that there is not connection
		 * 
		 * Toast.makeText(SplashScreenActivity.this,
		 * "You need an internet connection to run this application.",
		 * Toast.LENGTH_LONG).show();
		 * 
		 * 
		 * 
		 * } }
		 * 
		 * while (DataSource.getData().getConsoles().size() == 0) {
		 * DataSource.initData(); }
		 * 
		 * return null;
		 * 
		 * } catch (Exception e) { return null; } }
		 * 
		 * @Override protected void onPostExecute(Void result) {
		 * super.onPostExecute(result);
		 * 
		 * if (canGoToMain) { alertDialog.cancel();
		 * 
		 * } else { canGoToMain = true; }
		 * 
		 * }
		 * 
		 * };
		 * 
		 * a.execute();
		 */

	}

}
