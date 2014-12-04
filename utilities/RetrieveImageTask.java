package com.RUFit.android.utilities;

import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/**
*
*
*/
public class RetrieveImageTask extends AsyncTask<String, Void, Bitmap>{
	/**
	*
	*
	*/
	@Override
	protected Bitmap doInBackground(String... params) {


		try {
			URL url = new URL("http://icons.iconarchive.com/icons/fasticon/freestyle/128/flower-icon.png");
			//try this url = "http://icons.iconarchive.com/icons/fasticon/freestyle/128/flower-icon.png"
			HttpGet httpRequest = null;

			httpRequest = new HttpGet(url.toURI());

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);

			HttpEntity entity = response.getEntity();
			BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
			InputStream input = b_entity.getContent();

			Bitmap bitmap = BitmapFactory.decodeStream(input);

			Log.v("image", "retrieved image");
			return bitmap;
		} catch (Exception ex) {
			Log.v("image", ex.toString());
		}
		return null;
	}

}
