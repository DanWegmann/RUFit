package com.RUFit.android.activities;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.Brain;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.User;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.utilities.AccountUtils;
import com.RUFit.android.utilities.PlayServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.RUFit.android.R;
import android.os.Bundle;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/*
 * This Activity allows the user to log-in with an existing account or register a new account
 * Once the activity is complete it will save the users profile to memory & redirect them to
 * the Feed Fragment Activity
 */

public class LoginActivity extends Activity implements OnTaskCompleted {

	private Context context;
	private EditText emailText; //Where you type your e-mail address in
	private EditText passwordText;
	private Button loginButton; //The login button on screen
	private Button signUp_button;
	private APICall api;
	
	private SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getBaseContext();

		//Get the layout
		LinearLayout ll = ((LinearLayout) findViewById(R.id.contentArea));
		loginButton = ((Button) findViewById(R.id.loginButton));
		emailText = ((EditText) findViewById(R.id.emailField));
		passwordText = ((EditText) findViewById(R.id.passwordText));
		signUp_button = ((Button) findViewById(R.id.signUp_button));
		createListeners();
		
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.v("Play","OnResume");
		if (PlayServices.checkPlayServices(getBaseContext(), this) && PlayServices.checkUserAccount(getBaseContext(), this)){
			Log.v("Play","GOOD");
		}
		else{
			Log.v("Play","BAD");
		}
	}
	public void createListeners()
	{
		 signUp_button.setOnClickListener(new OnClickListener() {
			 
				public void onClick(View v) {
					
					Log.v("DEBUG","Sign Up Button");
					Intent intent = new Intent(LoginActivity.this, Register_Form.class);
					startActivity(intent);
					
				}});
		 loginButton.setOnClickListener(new OnClickListener() {
		 
			public void onClick(View v) {
				
				String email = emailText.getText().toString();
				String password = passwordText.getText().toString();
				
				api = new APICall(LoginActivity.this);
				api.execute("GET","users/login/" + email + "/" + password,"checkLogin");
				/*
				if (emailText.getText().toString().equals("admin"))
				{
					//Set values//
					Editor editor = pref.edit();
					editor.putString("name",emailText.getText().toString());
					editor.commit();
					//////////
					startLoadingActivity();
					finish();
				}
				else
				{
					
				} 
				*/
			}
		}
				);
	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PlayServices.REQUEST_CODE_RECOVER_PLAY_SERVICES:
			if (resultCode == RESULT_CANCELED) {
				Log.v("Play","Google Play Services must be installed.");
				finish();
			}

		case PlayServices.REQUEST_CODE_PICK_ACCOUNT:
			if (resultCode == RESULT_OK) {
				String accountName = data.getStringExtra(
						AccountManager.KEY_ACCOUNT_NAME);
				AccountUtils.setAccountName(this, accountName);
			} else if (resultCode == RESULT_CANCELED) {
				Log.v("Play","This application requires a Google account.");
				finish();
			}

			return;
		}


		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startLoadingActivity()
	{

		Log.v("login","startLoadingActivity");
		Intent intent = new Intent(this, Loading.class);
		intent.putExtra("Email", emailText.getText().toString());
		startActivity(intent);
		finish();


	}

	@Override
	public void onTaskCompleted(JSONObject j) {
			try {
				String hook = j.get("hook").toString();
				Log.v("Play","AsyncTask Completed with hook: " + hook);

				if (hook.equals("checkLogin"))
				{
					JSONArray users = (JSONArray) j.get("users");
					Log.v("JSON","users size: " + users.length());
					
					
					if (users.length() == 0) //Incorrect log-in info. No user returned.
					{
						AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
						alertDialog.setTitle("Error");
						alertDialog.setMessage("Incorrect Log-in");
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Add your code for the button here.
							}
						});
						// Set the Icon for the Dialog
						alertDialog.show();
					}
					else //Correct log-in information. User returned.
					{
						User currentUser = Brain.convertJSONToUser((JSONObject) users.get(0));
						
						//Set currentUser in Brain
						Brain.setCurrentUser(currentUser);
						Brain.addUser(currentUser);
						
						PlayServices.registerInBackground(context, currentUser.getId());
						
						//Go to loading screen
						Intent intent = new Intent(LoginActivity.this, Loading.class);
						startActivity(intent);
						finish();
					}
				}


			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
}
