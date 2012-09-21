package com.garage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GarageActivity extends Activity {
	TextView locationText;
	ProgressBar pb;
	ImageView nearbyImage;
	ImageView byCityImage;
	ImageView claimImage;
	ImageView locImage;
	String address = null;
	private Typeface font;
	public static String stateName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setViews();
		nearby();
		registerClaim();
		search();
		checkProvider();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.menu, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refreshLocation:
			pb.setVisibility(View.VISIBLE);
			new DownloadActivityTask().execute();
			return (true);

		case R.id.aboutMenu:
			about();
			return (true);
		}

		return super.onOptionsItemSelected(item);
	}

	private class DownloadActivityTask extends AsyncTask<String, Integer, Long> {
		@Override
		protected Long doInBackground(String... params) {
			try {
				setUpLocation();
				geocoding(locationText);
			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			try {
			} catch (Exception e) {
				e.printStackTrace();
			}

			pb.setVisibility(View.GONE);
		}
	}

	private void nearby() {
		nearbyImage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(GarageActivity.this,
						GarageList.class);
				startActivity(intent);
			}
		});
	}

	private void registerClaim() {

		claimImage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(GarageActivity.this,
						RegisterClaim.class);
				startActivity(intent);
			}
		});
	}

	private void search() {

		byCityImage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(GarageActivity.this,
						SearchGarages.class);
				startActivity(intent);
			}
		});
	}

	public void setUpLocation() {

		try {
			LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setPowerRequirement(Criteria.POWER_LOW);

			Double latitude = null;
			Double longitude = null;

			if (!mLocationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				String locationprovider = mLocationManager.getBestProvider(
						criteria, true);
				Location mLocation = mLocationManager
						.getLastKnownLocation(locationprovider);
				latitude = mLocation.getLongitude();
				Log.d("Latitudeg", latitude + "");
				longitude = mLocation.getLatitude();
				Log.d("Longitudeg", longitude + "");

			} else {
				Location mLocation = mLocationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				latitude = mLocation.getLongitude();
				Log.d("Latitudei", latitude + "");
				longitude = mLocation.getLatitude();
				Log.d("Longitudei", longitude + "");

			}

			SharedPreferences myData = getSharedPreferences("myData",
					MODE_PRIVATE);
			Editor myEdit = myData.edit();
			myEdit.putString("lng", latitude + "");
			myEdit.putString("lat", longitude + "");
			myEdit.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void geocoding(TextView tv) {
		SharedPreferences myData = getSharedPreferences("myData", MODE_PRIVATE);
		Double latitude = Double.parseDouble(myData.getString("lat", ""));
		Double longitude = Double.parseDouble(myData.getString("lng", ""));
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());

		try {
			List<Address> addresses = geocoder.getFromLocation(latitude,
					longitude, 1);

			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
									"\n");
				}

				stateName = addresses.get(0).getAdminArea().toString();
				address = strReturnedAddress.toString();
				tv.setText(address);

			} else {
				tv.setText("No Address returned!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setViews() {
		nearbyImage = (ImageView) findViewById(R.id.imgButton1);
		byCityImage = (ImageView) findViewById(R.id.imgButton2);
		claimImage = (ImageView) findViewById(R.id.imgButton3);
		locationText = (TextView) findViewById(R.id.locationText);
		pb = (ProgressBar) findViewById(R.id.pb);
		font = Typeface.createFromAsset(getAssets(), "fonts/majalla.ttf");
		locationText.setTypeface(font);
		locationText.setText("Touch to Refresh Location");
		locationText.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				pb.setVisibility(View.VISIBLE);
				new DownloadActivityTask().execute();
			}
		});
	}

	private void checkProvider() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				&& isNetworkAvailable()) {
			new DownloadActivityTask().execute();

		} else if (isNetworkAvailable()) {
			pb.setVisibility(View.GONE);
			showGPSDisabledAlertToUser();
		} else {
			// noConnectionView();
		}
	}

	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	private void about() {
		new AlertDialog.Builder(GarageActivity.this).setTitle("GLocator")
				.setMessage("Version: 0.2").setPositiveButton("OK", null)
				.show();
	}
}
