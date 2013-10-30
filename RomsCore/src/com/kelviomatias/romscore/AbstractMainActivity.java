package com.kelviomatias.romscore;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kelviomatias.romscore.util.BitmapUtil;
import com.kelviomatias.romscore.util.ColorUtil;

public abstract class AbstractMainActivity extends Activity {

	private boolean dialogDisplayed;

	private AlertDialog alertDialog;

	private String warningText = "Before downloading any rom, check the laws of your region. "
			+ "Note that you will need of an emulator to play any game.";

	private List<Console> listaConsoles = new ArrayList<Console>();

	public abstract Class<? extends Activity> getRomsActivityClass();

	private void initListaConsoles() {

		AsyncTask<Void, Void, Void> a = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				try {
					listaConsoles.clear();
					while (listaConsoles.isEmpty()) {
						try {
							listaConsoles.addAll(new Persister().read(
									Data.class,
									new URL(DataSource.CONSOLE_INDEX_URL)
											.openStream()).getConsoles());
						} catch (Exception e) {

						}

					}

					Collections.sort(listaConsoles, new Comparator<Console>() {

						@Override
						public int compare(Console lhs, Console rhs) {
							return lhs.getName().compareTo(rhs.getName());
						}

					});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							Console[] consolesArray = new Console[listaConsoles
									.size()];

							for (int i = 0; i < consolesArray.length; i++) {
								consolesArray[i] = listaConsoles.get(i);
							}

							ConsoleItemAdapter itemAdapter = new ConsoleItemAdapter(
									AbstractMainActivity.this,
									R.layout.console_list_item, consolesArray);
							ListView listView = (ListView) findViewById(R.id.mainListView);
							listView.setOnItemClickListener(new OnItemClickListener() {

								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long id) {

									Intent i = new Intent();
									i.setClass(AbstractMainActivity.this,
											getRomsActivityClass());
									i.putExtra(Console.class.getName(),
											listaConsoles.get(position));
									// i.putExtra("index", position);

									startActivity(i);
								}
							});
							listView.setAdapter(itemAdapter);
							itemAdapter.notifyDataSetChanged();

							ProgressBar pb = (ProgressBar) findViewById(R.id.activityMainProgressDialog);
							pb.setVisibility(View.INVISIBLE);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

		};

		a.execute();

	}

	private void showLawDialog() {

		if (dialogDisplayed) {
			return;
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Warning");

		// set dialog message
		alertDialogBuilder
				.setMessage(warningText)
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						alertDialog.cancel();

					}
				})
				.setNegativeButton("Close app",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								finish();
							}
						});

		// create alert dialog
		alertDialog = alertDialogBuilder.create();

		alertDialog.show();

		dialogDisplayed = true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		showLawDialog();
		initListaConsoles();

	}

	class ConsoleItemAdapter extends ArrayAdapter<Console> {

		private Activity myContext;

		private Console[] datas;

		public ConsoleItemAdapter(Context context, int textViewResourceId,
				Console[] objects) {
			super(context, textViewResourceId, objects);

			myContext = (Activity) context;
			datas = objects;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = myContext.getLayoutInflater();

			convertView = inflater.inflate(R.layout.console_list_item, null);

			char c = datas[position].getName().toLowerCase().charAt(0);
			int baseColor = ColorUtil.getLetterColor(c);
			int iconColor = ColorUtil.getBrigtherLetterColor(baseColor, 150);
			int rowColor = ColorUtil.getBrigtherLetterColor(baseColor);

			((ImageView) convertView
					.findViewById(R.id.consoleListItemConsoleIcon))
					.setImageBitmap(BitmapUtil.getBitmapForCharacter(
							AbstractMainActivity.this, c, iconColor, true));

			TextView nomeConsole = (TextView) convertView
					.findViewById(R.id.consoleListItemTextView);

			nomeConsole.setText(datas[position].getName());
			nomeConsole.setBackgroundColor(rowColor);
			
			return convertView;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
}
