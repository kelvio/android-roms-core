package com.kelviomatias.romscore;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.core.Persister;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kelviomatias.romscore.DataSource.Container;
import com.kelviomatias.romscore.util.ActivityUtil;
import com.kelviomatias.romscore.util.BitmapUtil;
import com.kelviomatias.romscore.util.ColorUtil;
import com.kelviomatias.romscore.util.ConsoleAppEngineUtil;

public abstract class AbstractRomsActivity extends Activity {

	private Console console;

	private Map<Character, Bitmap> bitmapMap = new HashMap<Character, Bitmap>();

	public abstract Class<? extends Activity> getRomActivityClass();

	private void initListaRoms() {

		AsyncTask<Void, Void, Void> a = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				try {

					console.getRoms().clear();
					int attempt = 0;
					while (console.getRoms().isEmpty() && attempt < 3) {
						attempt++;
						try {
							console.getRoms()
									.addAll(new Persister()
											.read(Container.class,
													new URL(
															ConsoleAppEngineUtil
																	.getUrlForConsoleRoms(console))
															.openStream())
											.getRoms());
						} catch (Exception e) {

						}

					}

					fillRomListView();

				} catch (Exception e) {

				}

				return null;

			}

		};

		a.execute();
	}

	private void fillRomListView() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				Rom[] romsArray = new Rom[console.getRoms().size()];
				for (int i = 0; i < romsArray.length; i++) {
					romsArray[i] = console.getRoms().get(i);
				}
				RomItemAdapter itemAdapter = new RomItemAdapter(
						AbstractRomsActivity.this, R.layout.rom_list_item,
						romsArray);

				ListView listView = (ListView) findViewById(R.id.romsListView);
				listView.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						Intent i = new Intent();
						i.setClass(AbstractRomsActivity.this,
								getRomActivityClass());
						i.putExtra(Console.class.getName(),
								AbstractRomsActivity.this.console);
						i.putExtra(Rom.class.getName(),
								AbstractRomsActivity.this.console.getRoms()
										.get(position));
						startActivity(i);
					}
				});

				listView.setAdapter(itemAdapter);
				itemAdapter.notifyDataSetChanged();

				ProgressBar pb = (ProgressBar) findViewById(R.id.activityRomsProgressBar);
				pb.setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
		setContentView(R.layout.activity_roms);

		console = (Console) this.getIntent().getSerializableExtra(
				Console.class.getName());
		if (console != null) {
			this.setTitle(console.getName());
		}

		this.initListaRoms();

	}

	@Override
	protected void onResume() {
		super.onResume();

		ListView listView = (ListView) this.findViewById(R.id.romsListView);

		if (listView.getAdapter() != null
				&& listView.getAdapter() instanceof RomItemAdapter) {
			RomItemAdapter adapter = (RomItemAdapter) listView.getAdapter();
			adapter.notifyDataSetChanged();
		}

	}

	class RomItemAdapter extends ArrayAdapter<Rom> {

		private Activity myContext;

		private Rom[] datas;

		public RomItemAdapter(Context context, int textViewResourceId,
				Rom[] objects) {
			super(context, textViewResourceId, objects);

			myContext = (Activity) context;
			datas = objects;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = myContext.getLayoutInflater();

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.rom_list_item, null);
			}

			// ImageView im = (ImageView) convertView
			// .findViewById(R.id.rowListItemOkImage);

			TextView infoView = (TextView) convertView
					.findViewById(R.id.rowListItemInfoText);

			char c = datas[position].getName().toLowerCase().charAt(0);

			int baseColor = ColorUtil.getLetterColor(c);
			int rowColor = ColorUtil.getBrigtherLetterColor(baseColor);
			int iconColor = ColorUtil.getBrigtherLetterColor(baseColor, 150);

			((ImageView) convertView.findViewById(R.id.romListItemImageView))
					.setImageBitmap(BitmapUtil.getBitmapForCharacter(
							AbstractRomsActivity.this, c, iconColor, true));

			if (getDestinationFile(datas[position]).exists()) {
				// im.setVisibility(View.VISIBLE);

				infoView.setText("INSTALLED");

			} else {
				// im.setVisibility(View.INVISIBLE);
				infoView.setText("DOWNLOAD NOW!");
			}

			TextView nomeCanalView = (TextView) convertView
					.findViewById(R.id.listItemTextView);

			nomeCanalView.setText(datas[position].getName());
			nomeCanalView.setBackgroundColor(rowColor);

			return convertView;
		}

		public File getDestinationFile(Rom rom) {
			return new File(Environment.getExternalStorageDirectory().getPath()
					+ "/Roms/" + console.getName() + "/" + rom.getName()
					+ console.getDefaultExtension());
		}

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
