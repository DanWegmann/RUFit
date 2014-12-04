package com.RUFit.android.utilities;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
*
*
*/
public class Register extends AsyncTask<Object, String, String> {

	private Context context;
	String userId;
	/**
	*
	* @param context, userId
	*/
	public Register(Context context, String userId) {
		Log.v(PlayServices.TAG,"Created Register");
		this.context = context;
		this.userId = userId;
	}

	/**
	*
	* @param params
	* @return msg
	*/
	@Override
	protected String doInBackground(Object... params) {
		Log.v(PlayServices.TAG,"Register.doInBackground");
		String msg = "";
		try {
			if (PlayServices.gcm == null) {
				PlayServices.gcm = GoogleCloudMessaging.getInstance(context);
			}
			PlayServices.regid = PlayServices.gcm.register(PlayServices.SENDER_ID);
			msg = "Device registered, registration ID=" + PlayServices.regid;
			Log.v(PlayServices.TAG,msg);
			// You should send the registration ID to your server over HTTP,
			// so it can use GCM/HTTP or CCS to send messages to your app.
			// The request to your server should be authenticated if your app
			// is using accounts.
			PlayServices.sendRegistrationIdToBackend(userId);

			// For this demo: we don't need to send it because the device
			// will send upstream messages to a server that echo back the
			// message using the 'from' address in the message.

			// Persist the regID - no need to register again.
			PlayServices.storeRegistrationId(context, PlayServices.regid);
		} catch (IOException ex) {
			msg = "Error :" + ex.getMessage();
			// If there is an error, don't just keep trying to register.
			// Require the user to click a button again, or perform
			// exponential back-off.
		}
		return msg;
	}

} 
