package com.RUFit.android.activities;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import java.awt.image.*;

import javax.imageio.ImageIO;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.RUFit.android.Brain;
import com.RUFit.android.R;
import com.RUFit.android.listeners.OnProfileImageUpdate;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.User;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.utilities.HttpUploader;
import com.RUFit.android.utilities.PlayServices;

public class Register_Form_2 extends Activity implements OnProfileImageUpdate, OnTaskCompleted {

	private RadioButton radio_male;
	private RadioButton radio_female;
	private EditText editText_bio;
	
	private String gender = "M";
	private String bio = "";


	private static Bitmap Image = null;
	private static Bitmap rotateImage = null;
	private ImageView profilePic;
	private static final int GALLERY = 1;

	String uploadFilePath = "android/";
	final String uploadFileName = "testing.png";
	ProgressDialog dialog = null;
	String upLoadServerUri = null;

	Uri currImageURI;
	
	HttpURLConnection connection = null;
	DataOutputStream outputStream = null;
	DataInputStream inputStream = null;
	String urlServer = "http://www.wasiwrong.com/SeniorProject/RUFit/handle_upload.php";
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary =  "*****";

	
	int serverResponseCode = 0;
	String serverResponseMessage = null;
	
	int bytesRead, bytesAvailable, bufferSize;
	byte[] buffer;
	int maxBufferSize = 1*1024*1024;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_form_2);


		//Get radio buttons for gender preference
		radio_male = ((RadioButton)findViewById(R.id.radio_male));
		radio_female = ((RadioButton)findViewById(R.id.radio_female));
		editText_bio = ((EditText)findViewById(R.id.editText_bio));

		profilePic = (ImageView) findViewById(R.id.profilePic);

	}

	public void choose_image(View v)
	{
		Log.v("DEBUG","Choose image");
		profilePic.setImageBitmap(null);
		if (Image != null)
			Image.recycle();
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("DEBUG","Data: " + data.toString());
		if (requestCode == GALLERY && resultCode != 0) {
			currImageURI = data.getData();  
			Log.v("DEBUG","Current image Path is ----->" + getRealPathFromURI(currImageURI));

			try {
				Image = Media.getBitmap(this.getContentResolver(), currImageURI);
				if (getOrientation(getApplicationContext(), currImageURI) != 0) {
					Matrix matrix = new Matrix();
					matrix.postRotate(getOrientation(getApplicationContext(), currImageURI));
					if (rotateImage != null)
						rotateImage.recycle();
					rotateImage = Bitmap.createBitmap(Image, 0, 0, Image.getWidth(), Image.getHeight(), matrix,true);
					profilePic.setImageBitmap(rotateImage);
				} else
					profilePic.setImageBitmap(Image);        
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//Convert the image URI to the direct file system path of the image file
	public String getRealPathFromURI(Uri contentUri) {
	    String [] proj={MediaStore.Images.Media.DATA};
	    android.database.Cursor cursor = managedQuery( contentUri,
	    proj,     // Which columns to return
	    null,     // WHERE clause; which rows to return (all rows)
	    null,     // WHERE clause selection arguments (none)
	    null);     // Order-by clause (ascending by name)
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	public static int getOrientation(Context context, Uri photoUri) {
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}
		cursor.moveToFirst();
		return cursor.getInt(0);
	}



	public void onRadioButtonClicked(View view) {
		((RadioButton) view).setChecked(true);

		// Check which radio button was clicked
		switch(view.getId()) {
		case R.id.radio_male:
			radio_male.setChecked(true);
			Log.v("DEBUG","Gender male selected");
			gender = "M";
			break;
		case R.id.radio_female:
			radio_female.setChecked(true);
			gender = "F";
			Log.v("DEBUG","Gender female selected");
			break;
		}
	}

	public void onDone()
	{
		Log.v("DEBUG","onDone()");
	}
	public void finish_registration(View v)
	{
		Log.v("DEBUG","Finish registration.");
		
		HttpUploader uploader = new HttpUploader(Register_Form_2.this);
		try {
		 // String image_name = uploader.execute(getRealPathFromURI(currImageURI)).get(); 
			String image_name = uploader.execute(Image).get(); 
			Image.recycle(); 
		  Log.v("DEBUG","Uploading Image...");
		} catch (InterruptedException e) {
		  e.printStackTrace();
		  Log.v("DEBUG","Exception: " + e);
		} catch (ExecutionException e) {
		  e.printStackTrace();
		  Log.v("DEBUG","Exception: " + e);
		}
	}

	@Override
	public void onProfileImageUpdate(String imgUrl) {
		Log.v("DEBUG","onProfileImageUpdate imgUrl: " + imgUrl );
		APICall api = new APICall(Register_Form_2.this);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", Brain.getCurrentUser().getId()));
		nameValuePairs.add(new BasicNameValuePair("pic_url", imgUrl));
		nameValuePairs.add(new BasicNameValuePair("gender", gender));
		nameValuePairs.add(new BasicNameValuePair("bio", editText_bio.getText().toString()));
		
		
		api.setNameValuePairs(nameValuePairs);
		api.execute("POST","usersUpdate","updated_user");
		
	}

	@Override
	public void onTaskCompleted(JSONObject j) {
		try {
			String hook = j.get("hook").toString();
			Log.v("Play","AsyncTask Completed with hook: " + hook);

			if (hook.equals("updated_user"))
			{
				String success = j.get("success").toString();
				String error = j.get("error").toString();
				if (success.equals("false"))
				{
					AlertDialog alertDialog = new AlertDialog.Builder(Register_Form_2.this).create();
					alertDialog.setTitle("Error");
					alertDialog.setMessage(error);
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Add your code for the button here.
						}
					});
					// Set the Icon for the Dialog
					alertDialog.show();
				}
				else
				{
					Log.v("DEBUG","Updated user in database.");
					
					Intent intent = new Intent(Register_Form_2.this, Initialize.class);
					startActivity(intent);
					finish();
				}
			}
			


		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
		
	}

