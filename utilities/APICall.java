package com.RUFit.android.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.listeners.OnTaskCompleted;

import android.os.AsyncTask;
import android.util.Log;

public class APICall extends AsyncTask<String, Void, JSONObject> {

	private final static String apiURL = "http://wasiwrong.com/SeniorProject/RUFit/my-rest-api/index.php/";
	private HttpClient client;
	private OnTaskCompleted listener;
	private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
	public APICall(OnTaskCompleted listener){
		this.listener = listener;
	}

	public void setNameValuePairs(List<NameValuePair> nameValuePairs)
	{
		this.nameValuePairs = nameValuePairs;
	};

	@Override
	protected JSONObject doInBackground(String... params) {
		String requestMethod = params[0];
		String methodAndParams = params[1];
		String hook = params[2];


		if(hook.isEmpty())
		{
			hook = "null";
		}

		try {
			client = new DefaultHttpClient();

			if (requestMethod.equals("GET"))
			{
				Log.v("Play","GET METHOD ENTERED");
				HttpGet http = new HttpGet(apiURL + hook + "/" + methodAndParams);
				http.addHeader("Content-Type", "application/json");
				HttpResponse response;

				response = client.execute(http);

				Log.e(PlayServices.TAG , "HTTP GET response code ="+Integer.toString(response.getStatusLine().getStatusCode()));
				BufferedReader rd = new BufferedReader(	new InputStreamReader(response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null)
				{
					result.append(line);
				}
				Log.e(PlayServices.TAG,"response is"+result.toString());
				JSONObject data = new JSONObject(result.toString());
				return data;

			}
			else if (requestMethod.equals("POST"))
			{
				Log.v("Play","POST METHOD ENTERED");
				HttpPost httppost = new HttpPost(apiURL + hook + "/" + methodAndParams);

				
				httppost.setEntity(new UrlEncodedFormEntity(this.nameValuePairs));

				HttpResponse response = client.execute(httppost);

				Log.e(PlayServices.TAG , "HTTP POST response code ="+Integer.toString(response.getStatusLine().getStatusCode()));
				BufferedReader rd = new BufferedReader(	new InputStreamReader(response.getEntity().getContent()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null)
				{
					result.append(line);
				}
				Log.e(PlayServices.TAG,"Response is: "+result.toString());

				JSONObject data = new JSONObject(result.toString());
				return data;

			}


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;

	}
	protected void onPostExecute(JSONObject j)
	{
		listener.onTaskCompleted(j);
	}
}
