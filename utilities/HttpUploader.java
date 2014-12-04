package com.RUFit.android.utilities;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.RUFit.android.listeners.OnProfileImageUpdate;
 
 
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
 
//Uploader class
   public class HttpUploader extends AsyncTask<Bitmap, Void, String> {
	   
	   private OnProfileImageUpdate listener;
	   	/**
		*
		* @param listener
		*/
	   public HttpUploader(OnProfileImageUpdate listener)
	   {
		   this.listener = listener;
	   }
	   
	   @Override
		/**
		*
		* @param path
		* @return outPut
		*/   
	   protected String doInBackground(Bitmap... path) {
             
            String outPut = null;
             
            for (Bitmap sdPath : path) {
             
                Bitmap bitmapOrg = sdPath;
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                 
                //Resize the image
                double width = bitmapOrg.getWidth();
                double height = bitmapOrg.getHeight();
                double ratio = 400/width;
                int newheight = (int)(ratio*height);
                 
                System.out.println("———-width" + width);
                System.out.println("———-height" + height);
                 
                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 400, newheight, true);
                 
                //Here you can define .PNG as well
                bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 95, bao);
                byte[] ba = bao.toByteArray();
                //String ba1 = Base64.encodeBytes(ba);
                String ba1 = Base64.encodeToString(ba, 0);
                 
                System.out.println("uploading image now ——–" + ba1);
                 
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("image", ba1));
                 
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://www.wasiwrong.com/SeniorProject/RUFit/api.upload.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                     
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();                
 
                    // print responce
                    outPut = EntityUtils.toString(entity);
                    Log.v("GET RESPONSE—-", outPut);
                     
                    //is = entity.getContent();
                    Log.v("log_tag ******", "good connection");
                     
                    listener.onProfileImageUpdate(outPut);
                    bitmapOrg.recycle();
                     
                } catch (Exception e) {
                    Log.e("log_tag ******", "Error in http connection " + e.toString());
                }
            }
            return outPut;
        }      
    }
