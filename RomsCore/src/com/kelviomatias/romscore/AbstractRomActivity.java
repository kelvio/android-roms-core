package com.kelviomatias.romscore;

import java.io.File;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kelviomatias.romscore.util.ActivityUtil;
import com.kelviomatias.romscore.util.BitmapUtil;
import com.kelviomatias.romscore.util.ColorUtil;

public abstract class AbstractRomActivity extends Activity {

	// private String currentDownloadName = "";

	// private ProgressDialog pDialog;

	private static final String COOL_ROM_DOWNLOAD_PAGE_URL_TEMPLATE = "http://coolrom.com/dlpop.php?id=";

	private AlertDialog alertDialog;

	private Rom rom;

	private Console console;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
		setContentView(R.layout.activity_rom);

		console = (Console) this.getIntent().getSerializableExtra(
				Console.class.getName());
		rom = (Rom) this.getIntent().getSerializableExtra(Rom.class.getName());

		this.setTitle(rom.getName());

		char c = rom.getName().toLowerCase().charAt(0);

		int baseColor = ColorUtil.getLetterColor(c);
		// int backgroundColor = ColorUtil.getBrigtherLetterColor(baseColor);
		int iconColor = ColorUtil.getBrigtherLetterColor(baseColor, 150);

		((ImageView) findViewById(R.id.romIconImageView))
				.setImageBitmap(BitmapUtil.getBitmapForCharacter(this, c,
						iconColor, true));

		TextView name = (TextView) findViewById(R.id.romName);
		name.setText(rom.getName());

		TextView description = (TextView) findViewById(R.id.romDescriptionTextField);
		if (rom.getDescription() != null && !rom.getDescription().isEmpty()) {
			description.setText(rom.getDescription());
		}

		Button downloadButton = (Button) findViewById(R.id.romDownloadButton);
		if (rom.getSize() != 0) {
			downloadButton.setText("Download (" + (rom.getSize() / 1024)
					+ " MB)");
		}

		Button optionsButton = (Button) findViewById(R.id.optionsButton);
		optionsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String[] items = new String[] {
						getString(R.string.open_default_emulator),
						getString(R.string.emulators),
						getString(R.string.more_apps),
						getString(R.string.rate),
						getString(R.string.share)};

				AlertDialog.Builder ab = new AlertDialog.Builder(
						AbstractRomActivity.this);
				ab.setTitle(AbstractRomActivity.this
						.getString(R.string.app_name));
				ab.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int choice) {

						switch (choice) {
						case 0: // OPEN EMULATOR
							openDefaultEmulator();
							break;
						case 1: // DOWNLOAD EMULATOR APP
							downloadEmulatorApp();
							break;
						case 2: // MORE APPS
							moreApps();
							break;
						case 3: // RATE
							rate();
							break;
						case 4: // SHARE
							share();
							break;
						default:
							break;
						}

					}
				});
				ab.show();
			}

			private void openDefaultEmulator() {

				if (console.getEmulatorUrl() == null
						|| console.getEmulatorUrl().equals("")) {
					downloadEmulatorApp();
					return;
				}

				if (!ActivityUtil.appInstalledOrNot(AbstractRomActivity.this,
						console.getEmulatorPackage())) {

					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse(AbstractRomActivity.this.console
									.getEmulatorUrl())));

				} else {
					startActivity(ActivityUtil.getLaunchIntentForPackage(
							AbstractRomActivity.this,
							console.getEmulatorPackage()));
				}

			}

			private void downloadEmulatorApp() {

				if (ActivityUtil.appInstalledOrNot(AbstractRomActivity.this,
						"com.kelviomatias.emulators")) {

					startActivity(ActivityUtil.getLaunchIntentForPackage(
							AbstractRomActivity.this,
							"com.kelviomatias.emulators"));

				} else {

					Uri uri = Uri
							.parse("https://play.google.com/store/apps/details?id=com.kelviomatias.emulators");
					Intent i = new Intent(Intent.ACTION_VIEW, uri);
					try {
						startActivity(i);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(AbstractRomActivity.this,
								getString(R.string.google_play_not_installed),
								Toast.LENGTH_LONG).show();
					}
				}

			}

			private void moreApps() {
				Uri uri = Uri
						.parse("https://play.google.com/store/apps/developer?id=Kelvio+Matias");
				Intent i = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(i);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(AbstractRomActivity.this,
							getString(R.string.google_play_not_installed),
							Toast.LENGTH_LONG).show();
				}
			}

			private void rate() {
				Uri uri = Uri
						.parse("https://play.google.com/store/apps/details?id="
								+ getPackageName());
				Intent i = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(i);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(AbstractRomActivity.this,
							getString(R.string.google_play_not_installed),
							Toast.LENGTH_LONG).show();
				}
			}

			private void share() {
				Intent share = new Intent(android.content.Intent.ACTION_SEND);
				share.setType("text/plain");
				share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

				// Add data to the intent, the receiving app will
				// decide
				// what to do with it.
				share.putExtra(Intent.EXTRA_SUBJECT,
						getString(R.string.app_name));
				share.putExtra(Intent.EXTRA_TEXT,
						getString(R.string.appShareDescription));

				startActivity(Intent.createChooser(share,
						getString(R.string.select_share_method)));
			}

		});

		((TextView)findViewById(R.id.activityRomDownloadDestination)).setVisibility(View.INVISIBLE);
		
		File romFile = getDestinationFile();
		if (romFile.exists()) {
			refreshDestinationText();
			refreshDownloadButton();

		} else {
			downloadButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AbstractRomActivity.this,
									"Downloading " + rom.getName() + ".",
									Toast.LENGTH_LONG).show();
						}
					});

					startDownloadTask();

				}
			});
		}

		/*
		 * Button downloadEmulatorsAppButton = (Button)
		 * findViewById(R.id.activityRomDownloadEmulatorsAppButton);
		 * 
		 * if (ActivityUtil.appInstalledOrNot(this,
		 * "com.kelviomatias.emulators")) {
		 * downloadEmulatorsAppButton.setText("Open Emulators App");
		 * downloadEmulatorsAppButton .setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { startActivity(ActivityUtil
		 * .getLaunchIntentForPackage( AbstractRomActivity.this,
		 * "com.kelviomatias.emulators")); } });
		 * 
		 * } else {
		 * 
		 * downloadEmulatorsAppButton .setOnClickListener(new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * startActivity(new Intent(Intent.ACTION_VIEW, Uri
		 * .parse(AbstractRomActivity.this.console .getEmulatorUrl())));
		 * 
		 * } });
		 * 
		 * }
		 * 
		 * Button downloadEmulatorButton = (Button)
		 * findViewById(R.id.activityRomDownloadEmulatorButton); if
		 * (console.getEmulatorName() == null ||
		 * "".equals(console.getEmulatorName())) {
		 * downloadEmulatorButton.setEnabled(false); } else {
		 * 
		 * if (!ActivityUtil.appInstalledOrNot(AbstractRomActivity.this,
		 * console.getEmulatorPackage())) {
		 * downloadEmulatorButton.setText("Download \"" +
		 * console.getEmulatorName() + "\" emulator");
		 * downloadEmulatorButton.setEnabled(true); downloadEmulatorButton
		 * .setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * startActivity(new Intent( Intent.ACTION_VIEW,
		 * Uri.parse(AbstractRomActivity.this.console .getEmulatorUrl())));
		 * 
		 * } }); } else { downloadEmulatorButton.setText("Open \"" +
		 * console.getEmulatorName() + "\" emulator");
		 * downloadEmulatorButton.setEnabled(true); downloadEmulatorButton
		 * .setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * startActivity(ActivityUtil .getLaunchIntentForPackage(
		 * AbstractRomActivity.this, console.getEmulatorPackage()));
		 * 
		 * } }); }
		 * 
		 * }
		 */
		

	}

	private String getIdDownloadRom(String objectId) throws Exception {

		Document d = Jsoup.parse(new URL(COOL_ROM_DOWNLOAD_PAGE_URL_TEMPLATE
				+ objectId), 10000);

		String s = d.select("table").first().child(0).child(0).child(0)
				.child(1).html();
		s = s.substring(s.indexOf("action=\""));
		s = s.substring(0, s.indexOf("/\""));
		s = s.substring(s.indexOf("\"") + 1);
		return s + "/";
	}

	public void startDownloadTask() {

		final AsyncTask<Void, Void, Void> downloadTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				if (!validateBeforeDownload()) {
					return null;
				}

				String downloadUrl = null;

				refreshDestinationText();

				try {
					downloadUrl = getIdDownloadRom(rom.getId());
				} catch (Exception e) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									AbstractRomActivity.this);

							// set title
							alertDialogBuilder.setTitle("Sorry");

							// set dialog message
							alertDialogBuilder
									.setMessage(
											"Could not download  "
													+ rom.getName() + ".")
									.setCancelable(false)
									.setPositiveButton(
											"Try again",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {

													startDownloadTask();
													alertDialog.cancel();

												}
											})
									.setNegativeButton(
											"Close",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {

													alertDialog.cancel();
												}
											});

							// create alert dialog
							alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}
					});

					return null;
				}

				if (downloadUrl == null || downloadUrl.isEmpty()) {
					return null;
				}

				Uri downloadUri = Uri.parse(downloadUrl);

				File f = getDestinationFile();
				if (!f.exists()) {
					f.mkdirs();
					f.delete();

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Button downloadButton = (Button) findViewById(R.id.romDownloadButton);
							downloadButton.setVisibility(View.INVISIBLE);
						}
					});

					refreshDownloadButton();
				}

				final DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

				DownloadManager.Request request = new DownloadManager.Request(
						downloadUri);
				request.setDescription(rom.getName());

				String destinationUri = getDestinationUri();

				final long downloadReference = downloadManager
						.enqueue(request
								.setAllowedNetworkTypes(
										DownloadManager.Request.NETWORK_WIFI
												| DownloadManager.Request.NETWORK_MOBILE)
								.setAllowedOverRoaming(false)
								.setTitle("Downloading " + rom.getName())
								.setDestinationUri(Uri.parse(destinationUri))
								.setDescription(rom.getName()));

				IntentFilter filter = new IntentFilter(
						DownloadManager.ACTION_DOWNLOAD_COMPLETE);
				registerReceiver(new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {
						long referenceId = intent.getLongExtra(
								DownloadManager.EXTRA_DOWNLOAD_ID, -1);
						if (downloadReference == referenceId) {

							refreshDownloadButton();

							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(
											AbstractRomActivity.this,
											"Downloading of " + rom.getName()
													+ " finished",
											Toast.LENGTH_LONG).show();
								}
							});

							Intent i = new Intent();
							i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
							startActivity(i);
						}

					}
				}, filter);
				return null;
			}

		};

		downloadTask.execute();

	}

	private void refreshDestinationText() {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				TextView tv = (TextView) findViewById(R.id.activityRomDownloadDestination);
				tv.setText("Download destination (SD Card): " + "/Roms/"
						+ console.getName() + "/" + rom.getName()
						+ console.getDefaultExtension());
				tv.setVisibility(View.VISIBLE);

			}
		});

	}

	private void refreshDownloadButton() {
		if (getDestinationFile().exists()) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Button b = (Button) findViewById(R.id.romDownloadButton);
					b.setVisibility(View.INVISIBLE);

					ImageView i = (ImageView) findViewById(R.id.activityRomHasImage);
					i.setVisibility(View.VISIBLE);

				}
			});
		}
	}

	public String getDestinationUri() {
		return "file://" + Environment.getExternalStorageDirectory().getPath()
				+ "/Roms/" + console.getName() + "/" + rom.getName()
				+ console.getDefaultExtension();
	}

	public File getDestinationFile() {
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ "/Roms/" + console.getName() + "/" + rom.getName()
				+ console.getDefaultExtension());
	}

	public boolean validateBeforeDownload() {
		return true;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
}
