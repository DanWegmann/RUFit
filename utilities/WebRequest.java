package com.RUFit.android.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
*
*
*/
public class WebRequest extends AsyncTask<Object, String, String> {

	private Context context;

	/**
	*
	* @param context
	*/
	public WebRequest(Context context_) {
		Log.v(PlayServices.TAG,"Created Web Request");
		context = context_;
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

		return msg;
	}



} 
