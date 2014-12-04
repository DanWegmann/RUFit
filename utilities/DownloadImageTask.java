package com.RUFit.android.utilities;

import java.io.InputStream;

import com.RUFit.android.Brain;
import com.RUFit.android.listeners.OnImageDownloaded;
import com.RUFit.android.objects.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;

/**
*
*
*/
public class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
    ImageView bmImage;
    String id;
    ProgressBar progressbar;
    OnImageDownloaded listener;
	/**
	*
	* @param bmImage, progressBar, id, listener
	*/
    public DownloadImageTask(ImageView bmImage, ProgressBar progressbar, String id, OnImageDownloaded listener) {
        this.bmImage = bmImage;
        this.id = id;
        this.progressbar = progressbar;
        this.listener = listener;
    }
	/**
	*
	* @param urls
	* @return mIcon11
	*/
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            //Log.v("COD","doInBackground");
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
    
    /*
     @Override(non-Javadoc)
     @see android.os.AsyncTask#onProgressUpdate(Progress[])
     
    protected void onProgressUpdate(Integer... progress) {    
    	super.onProgressUpdate(progress);
    	Log.v("COD","Trying to enter onProgressUpdate");
        progressbar.setProgress(progress[0]);
        Log.v("COD","Progress: " + progress[0]);
        }
    */
	/**
	*
	* @param result 
	*/
    protected void onPostExecute(Bitmap result) {
    	progressbar.setVisibility(View.GONE);
    	Image img = new Image(id, result);
    	Brain.addImage(img);
        bmImage.setImageBitmap(result);
        bmImage.setScaleType(ScaleType.FIT_CENTER);
        listener.OnImageDownloaded();
    }
}