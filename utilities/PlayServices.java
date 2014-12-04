package com.RUFit.android.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class PlayServices {
	public static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
	public static final int REQUEST_CODE_PICK_ACCOUNT = 1002;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String TAG = "Play";
	public static GoogleCloudMessaging gcm;
	public static String regid;
	private static final String API_KEY = "AIzaSyD4cnMIiV0ExCuMKYbNBan-7cpVB-68NMY";

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	static String SENDER_ID = "689408351041";
	/**
	*
	* @param context, a
	* @return false, true
	*/
	public static boolean checkPlayServices(Context context_, Activity a){
		Context context = context_;
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (status != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
				Log.v("Play","status");
				showErrorDialog(status, a);
			} else {
				Log.v("Play","This device is not supported.");
				a.finish();
			}
			return false;
		}
		return true;
	}
	/**
	*
	* @param context, a
	* @return false, true
	*/
	public static boolean checkUserAccount(Context context, Activity a) {
		String accountName = AccountUtils.getAccountName(context);
		if (accountName == null) {
			// Then the user was not found in the SharedPreferences. Either the
			// application deliberately removed the account, or the application's
			// data has been forcefully erased.
			showAccountPicker(a);
			return false;
		}
		else
		{
			Log.v("Play",accountName);
		}
		Account account = AccountUtils.getGoogleAccountByName(context, accountName);
		if (account == null) {
			// Then the account has since been removed.
			AccountUtils.removeAccount(context);
			showAccountPicker(a);
			return false;
		}

		return true;
	}
	/**
	*
	* @param a
	*/
	static void showAccountPicker(Activity a) {
		Intent pickAccountIntent = AccountPicker.newChooseAccountIntent(
				null, null, new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, 
				true, null, null, null, null);
		a.startActivityForResult(pickAccountIntent, REQUEST_CODE_PICK_ACCOUNT);
	}
	/**
	*
	* @param code, a
	*/
	static void showErrorDialog(int code, Activity a) {
		GooglePlayServicesUtil.getErrorDialog(code, a, 
				REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
	}


	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	public static String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private static SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.

		return context.getSharedPreferences("MyPref", 0);

	}


	/**
	 * @param context
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}


	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 * @param context, userId
	 */
	public static void registerInBackground(Context context, String userId) {
		Log.v(TAG,"registerInBackground");
		Register task = new Register(context, userId);
		task.execute();

	}


	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 * @param userId
	 */
	public static void sendRegistrationIdToBackend(String userId) {
		// Your implementation here.
		Log.v(PlayServices.TAG,"sendRegistrationIdToBackend");

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.wasiwrong.com/SeniorProject/RUFit/gcm_server_php/register.php");

		JSONObject data = new JSONObject();
		try{

			data.putOpt("userId", userId);
			data.putOpt("regId", regid);

			Log.e(TAG,"Json data="+data.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringEntity se;
		try {
			se = new StringEntity(data.toString());

			post.setEntity(se);
			/*post.addHeader("Authorization", "key="+API_KEY); */
			post.addHeader("Content-Type", "application/json");
			HttpResponse response = client.execute(post);
			Log.e(TAG ,
					"response code ="+Integer.toString(response.getStatusLine().getStatusCode()));
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null)
			{
				result.append(line);
			}


			Log.e(TAG,"response is"+result.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v(TAG,"Finished html stuff...");
	}

	/*
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("name", "value1"));
		pairs.add(new BasicNameValuePair("email", "value2"));
		pairs.add(new BasicNameValuePair("regId", "5465464"));
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String s = convertStreamToString(is);
			Log.v(TAG,"Before");
			Log.v(TAG,s);
			Log.v(TAG,"After");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 */




	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	public static void storeRegistrationId(Context context, String regId) {
		Log.v(PlayServices.TAG,"storeRegistrationId");
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}


}
