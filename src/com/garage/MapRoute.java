package com.garage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class MapRoute extends MapActivity {

	Uri uri;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		SharedPreferences myData = getSharedPreferences("myData", MODE_PRIVATE);

		String longitude1 = myData.getString("lng", "");
		String latitude1 = myData.getString("lat", "");

		Intent mIntent = getIntent();
		String latitude2 = mIntent.getStringExtra("garage_lat");
		String longitude2 = mIntent.getStringExtra("garage_lng");

		uri = Uri.parse("http://maps.google.com/maps?&saddr=" + latitude1 + ","
				+ longitude1 + "&daddr=" + latitude2 + "," + longitude2);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		finish();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
