package com.garage;

import static com.garage.GarageActivity.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;

import com.garage.xmlparser.ReadXMLFile;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GarageList extends ListActivity {
	ProgressBar pb;
	ReadXMLFile xp = new ReadXMLFile();
	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> cashless = new ArrayList<String>();
	ArrayList<String> manufacturer = new ArrayList<String>();
	ArrayList<String> street = new ArrayList<String>();
	ArrayList<String> city = new ArrayList<String>();
	ArrayList<String> pincode = new ArrayList<String>();
	ArrayList<String> state = new ArrayList<String>();
	ArrayList<String> contact = new ArrayList<String>();
	ArrayList<String> landline = new ArrayList<String>();
	ArrayList<String> mobile = new ArrayList<String>();
	ArrayList<String> email = new ArrayList<String>();
	String xml;
	int listSize = 0;
	ArrayList<String> nameList = new ArrayList<String>();
	ArrayList<Integer> n = new ArrayList<Integer>();
	TextView noResult;
	View dialogLayout;
	TextView manufText;
	TextView streetText;
	TextView cityText;
	TextView pincodeText;
	TextView stateText;
	TextView contactText;
	TextView landlineText;
	TextView mobileText;
	TextView emailText;
	int Id;
	String title;
	String phoneNo;
	String garage_lat = "18.922028";
	String garage_lng = "72.833358";
	ArrayAdapter<String> infoList;
	Button backButton;
	String url = "http://www.internfair.internshala.com/internFiles/AppDesign/GarageList.xml";
	TextView rowText;
	Typeface font;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		font = Typeface.createFromAsset(this.getAssets(), "fonts/majalla.ttf");
		pb = (ProgressBar) findViewById(R.id.pbar);
		noResult = (TextView) findViewById(R.id.empty);
		noResult.setTypeface(font);
		noResult.setVisibility(View.GONE);
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setTypeface(font);
		new DownloadActivityTask().execute();
		backActivity();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Id = n.get((int) id);
		title = name.get(Id);
		phoneNo = mobile.get(Id);
		customDialog();
		setViews();
		manufText.setText(manufacturer.get(Id));
		streetText.setText(street.get(Id));
		cityText.setText(city.get(Id));
		pincodeText.setText(pincode.get(Id));
		stateText.setText(state.get(Id));
		contactText.setText(contact.get(Id));
		landlineText.setText(landline.get(Id));
		mobileText.setText(mobile.get(Id));
		emailText.setText(email.get(Id));
	}

	private class DownloadActivityTask extends AsyncTask<String, Integer, Long> {
		@Override
		protected Long doInBackground(String... params) {
			try {
				xmlParser();
			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			try {
				setListView();
			} catch (Exception e) {
				e.printStackTrace();
			}

			pb.setVisibility(View.GONE);

		}
	}

	private void setViews() {
		manufText = (TextView) dialogLayout.findViewById(R.id.manufText);
		streetText = (TextView) dialogLayout.findViewById(R.id.streetText);
		cityText = (TextView) dialogLayout.findViewById(R.id.cityText);
		pincodeText = (TextView) dialogLayout.findViewById(R.id.pincodeText);
		stateText = (TextView) dialogLayout.findViewById(R.id.stateText);
		contactText = (TextView) dialogLayout.findViewById(R.id.contactText);
		landlineText = (TextView) dialogLayout.findViewById(R.id.landlineText);
		mobileText = (TextView) dialogLayout.findViewById(R.id.mobileText);
		emailText = (TextView) dialogLayout.findViewById(R.id.emailText);

	}

	private void setListView() {
		for (int i = 0; i < name.size(); i++) {
			{
				if (cashless.get(i).equals("No")) {
					continue;
				} else if (state.get(i).equals(stateName)) {
					nameList.add(name.get(i) + "\n" + city.get(i));
					n.add(i);
				}
			}
		}

		if (nameList.size() == 0) {
			noResult.setVisibility(View.VISIBLE);
		}

		

		infoList = new ArrayAdapter<String>(GarageList.this, R.layout.row,
				nameList);
		setListAdapter(infoList);
	}

	private void customDialog() {
		LayoutInflater inflater = getLayoutInflater();
		dialogLayout = inflater.inflate(R.layout.dialog,
				(ViewGroup) findViewById(R.id.dialog_layout_root));

		AlertDialog.Builder builder = new AlertDialog.Builder(GarageList.this);
		builder.setTitle(title);
		builder.setView(dialogLayout);
		builder.setPositiveButton("Call",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						callActivity();
					}
				});
		builder.setNegativeButton("Map", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mapActivity();
			}
		});
		builder.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void mapActivity() {
		/*
		 * Here garage_lat and garage_lng can be changed if available in
		 * database.
		 */
		Intent mIntent = new Intent(getApplicationContext(), MapRoute.class);
		mIntent.putExtra("garage_lat", garage_lat);
		mIntent.putExtra("garage_lng", garage_lng);
		startActivity(mIntent);
	}

	private void callActivity() {
		try {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel: " + phoneNo));
			startActivity(intent);
		} catch (Exception e) {
			Log.e("SampleApp", "Failed to invoke call", e);
		}
	}

	private void backActivity() {
		backButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	private void xmlParser() {
		/*
		 * Url can be changed from here for diffrent states Eg: for current
		 * state, url can be
		 * http://www.internfair.internshala.com/internFiles/AppDesign
		 * /GarageList.xml+"statename"
		 */
		xml = ReadXMLFile.getXML(url);
		Document doc = ReadXMLFile.XMLfromString(xml);
		xp.parseXml(name, cashless, manufacturer, street, city, pincode, state,
				contact, landline, mobile, email, doc);
	}
}
