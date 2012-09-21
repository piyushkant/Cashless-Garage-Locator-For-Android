package com.garage;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;

import com.garage.xmlparser.ReadXMLFile;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchGarages extends Activity {

	static Button stateButton;
	static Button cityButton;
	Button searchButton;
	static String defaultState = "Delhi";
	static String stateName = null;
	static String cityName = null;
	static String[] stateList;
	static String[] cityList = { "Not Found" };
	int resId;
	String resName;
	ReadXMLFile xp = new ReadXMLFile();
	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> cashless = new ArrayList<String>();
	ArrayList<String> city = new ArrayList<String>();
	ArrayList<String> state = new ArrayList<String>();
	String xml;
	ProgressDialog progDialog;
	private Typeface font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		setViews();
		search();
	}

	private class DownloadActivityTask extends AsyncTask<String, Integer, Long> {
		@Override
		protected Long doInBackground(String... params) {
			try {
				getCityList();

			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			try {
				cityButton.setText("Select City");
			} catch (Exception e) {
				e.printStackTrace();
			}
			progDialog.dismiss();
		}
	}

	void setViews() {
		font = Typeface.createFromAsset(getAssets(), "fonts/majalla.ttf");
		stateButton = (Button) findViewById(R.id.stateButton);
		stateButton.setTypeface(font);
		stateButton.setText("Select State");
		cityButton = (Button) findViewById(R.id.cityButton);
		cityButton.setTypeface(font);
		cityButton.setText("Select City");
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setTypeface(font);
		stateList = getResources().getStringArray(R.array.states);

		stateButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SearchGarages.this);
				builder.setTitle("Select State");
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				builder.setItems(stateList,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								progDialog = ProgressDialog.show(
										SearchGarages.this, "Please wait!",
										"Loading...", true);
								stateName = stateList[item];
								stateButton.setText(stateName);
								new DownloadActivityTask().execute();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		cityButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (stateButton.getText().toString().equals("Select State")) {
					new AlertDialog.Builder(SearchGarages.this)
							.setTitle("State not selected")
							.setMessage("Please select a State")
							.setPositiveButton("OK", null).show();
				} else {
					if (!cityList[0].equals("NA")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								SearchGarages.this);
						builder.setTitle("Select City");
						builder.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										return;
									}
								});
						builder.setItems(cityList,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										cityName = cityList[item];
										cityButton.setText(cityName);
									}
								});

						AlertDialog alert = builder.create();
						alert.show();
					} else {
						new AlertDialog.Builder(SearchGarages.this)
								.setTitle("No list available for this state.")
								.setMessage("0 results")
								.setPositiveButton("OK", null).show();
					}
				}
			}
		});

	}

	void search() {
		searchButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (stateButton.getText().toString().equals("Select State")) {
					new AlertDialog.Builder(SearchGarages.this)
							.setTitle("State not selected!")
							.setMessage("Please select a State")
							.setPositiveButton("OK", null).show();
				} else {

					Intent intent = new Intent(SearchGarages.this,
							SearchList.class);
					startActivity(intent);
				}
			}
		});
	}

	void getCityList() {
		resName = stateName.replaceAll("\\s+", "");
		resId = SearchGarages.this.getResources().getIdentifier(resName,
				"array", SearchGarages.this.getPackageName());
		cityList = getResources().getStringArray(resId);
	}
}
