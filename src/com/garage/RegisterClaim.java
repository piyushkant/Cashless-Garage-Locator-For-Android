package com.garage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterClaim extends Activity {

	Button sendButton;
	static Button attachButton;
	private Typeface font;
	// matches 10-digit numbers only
	String regexStr = "^[0-9]{10}$";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claim);
		setupViews();
	}

	private void claimForm() {
		EditText policyArea = (EditText) findViewById(R.id.policyArea);
		EditText vehicleArea = (EditText) findViewById(R.id.vehicleArea);
		EditText mobileArea = (EditText) findViewById(R.id.mobileArea);
		EditText emailArea = (EditText) findViewById(R.id.emailArea);

		if ((policyArea.getText().toString().trim().equals(""))
				|| (vehicleArea.getText().toString().trim().equals(""))
				|| (mobileArea.getText().toString().trim().equals(""))
				|| (emailArea.getText().toString().trim().equals(""))) {
			new AlertDialog.Builder(RegisterClaim.this).setTitle("")
					.setMessage("Please fill all the fields.")
					.setPositiveButton("OK", null).show();

		} else if ((checkemail(emailArea.getText().toString()) == true)
				&& mobileArea.getText().toString().matches(regexStr)) {
			Toast.makeText(
					getApplicationContext(),
					"The form can be submitted once the server "
							+ "end point is specified by the host.",
					Toast.LENGTH_LONG).show();
		} else if ((checkemail(emailArea.getText().toString()) == false)
				&& mobileArea.getText().toString().matches(regexStr)) {
			new AlertDialog.Builder(RegisterClaim.this).setTitle("")
					.setMessage("Please enter valid Email address.")
					.setPositiveButton("OK", null).show();
		} else if ((!mobileArea.getText().toString().matches(regexStr))
				&& (checkemail(emailArea.getText().toString()) == true)) {
			new AlertDialog.Builder(RegisterClaim.this).setTitle("")
					.setMessage("Please enter valid 10 digit Mobile Number.")
					.setPositiveButton("OK", null).show();
		} else if ((checkemail(emailArea.getText().toString()) == false)
				&& (!mobileArea.getText().toString().matches(regexStr))) {
			new AlertDialog.Builder(RegisterClaim.this).setTitle("")
					.setMessage("Please enter valid Email address.")
					.setPositiveButton("OK", null).show();
		}
	}

	public boolean checkemail(String email) {

		Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}

	void setupViews() {
		font = Typeface.createFromAsset(this.getAssets(), "fonts/majalla.ttf");
		sendButton = (Button) findViewById(R.id.sendButton);
		sendButton.setTypeface(font);
		attachButton = (Button) findViewById(R.id.attachButton);
		attachButton.setTypeface(font);

		sendButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				claimForm();
			}
		});

		attachButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(RegisterClaim.this,
						FileExplorer.class);
				startActivity(intent);
			}
		});
	}
}