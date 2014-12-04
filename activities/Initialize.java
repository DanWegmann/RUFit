package com.RUFit.android.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.Brain;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.User;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.utilities.PlayServices;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.RUFit.android.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/*
 * This Activity is run before anything else on the app.
 * It checks to see if the user has already run this app before.
 * If there is already a saved profile it will skip the Login screen and go directly to the Loading screen
 * If there is not a saved profile it will send the user to the Login screen
 */
public class Initialize extends Activity implements OnTaskCompleted {

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);

		context = getBaseContext();

		if (PlayServices.checkPlayServices(context, this)) {
			PlayServices.gcm = GoogleCloudMessaging.getInstance(context);
			PlayServices.regid = PlayServices.getRegistrationId(context);

			if (PlayServices.regid.isEmpty()) {
				Log.v(PlayServices.TAG,"No registration found in shared prefrences.");
				//Go to Log-in screen
				Intent intent = new Intent(Initialize.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				Log.i(PlayServices.TAG, "Registration found!." + PlayServices.getRegistrationId(context));
				Log.v(PlayServices.TAG,"Updating Current User...");
				
				//Update the currentUser in the Brain with the database info & go to Loading screen
				APICall api = new APICall(Initialize.this);
				api.execute("GET","usersByRegId/" + PlayServices.regid,"updateCurrentUser");
			}
		} else {
			Log.i(PlayServices.TAG, "No valid Google Play Services APK found.");
		}
	}

	@Override
	public void onTaskCompleted(JSONObject j) {

		try {
			String hook = j.get("hook").toString();
			Log.v(PlayServices.TAG,"AsyncTask Completed with hook: " + hook);

			/*
			 * Async hook: updateCurrentUser
			 * 	This will update the currentUser in the Brain
			 */

			if (hook.equals("updateCurrentUser"))
			{
				JSONArray users = (JSONArray) j.get("users");

				Log.v(PlayServices.TAG,"Updating current user in Brain");
				Log.v(PlayServices.TAG, users.get(0).toString() );

				User currentUser = Brain.convertJSONToUser((JSONObject) users.get(0));
				
				//Set currentUser in Brain
				Brain.setCurrentUser(currentUser);
				Brain.addUser(currentUser);

				Log.v("Play","Set current user as " + currentUser.getUsername() + " in Brain.");

				//Go to loading screen
				Intent intent = new Intent(Initialize.this, Loading.class);
				startActivity(intent);
				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}