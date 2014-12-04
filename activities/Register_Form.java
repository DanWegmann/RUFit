package com.RUFit.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.Brain;
import com.RUFit.android.R;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.Post;
import com.RUFit.android.objects.User;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.utilities.PlayServices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Register_Form extends Activity implements OnTaskCompleted {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_form);
		
		//GET layout
		final EditText editText_email = (EditText) findViewById(R.id.editText_email);
		final EditText editText_username = (EditText) findViewById(R.id.editText_username);
		final EditText editText_password = (EditText) findViewById(R.id.editText_password);
		final EditText editText_confirmPassword = (EditText) findViewById(R.id.editText_confirmPassword);
		Button button_next = (Button) findViewById(R.id.button_next);
		
		button_next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String email = editText_email.getText().toString();
				String username = editText_username.getText().toString();
				String password = editText_password.getText().toString();
				String confirmPassword = editText_confirmPassword.getText().toString();
				
				
				if (!(Brain.validate_email(email)))
						{
					AlertDialog alertDialog = new AlertDialog.Builder(Register_Form.this).create();
					alertDialog.setTitle("Error");
					alertDialog.setMessage("Email must be set.");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Add your code for the button here.
						}
					});
					// Set the Icon for the Dialog
					alertDialog.show();
						}
				else if (!(Brain.validate_username(username)))
				{
					AlertDialog alertDialog = new AlertDialog.Builder(Register_Form.this).create();
					alertDialog.setTitle("Error");
					alertDialog.setMessage("Username must be set.");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Add your code for the button here.
						}
					});
					// Set the Icon for the Dialog
					alertDialog.show();
				}
				else if ((!(Brain.validate_password(password))) || (!(Brain.validate_password(confirmPassword))))
						{
					AlertDialog alertDialog = new AlertDialog.Builder(Register_Form.this).create();
					alertDialog.setTitle("Error");
					alertDialog.setMessage("Password must be set.");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Add your code for the button here.
						}
					});
					// Set the Icon for the Dialog
					alertDialog.show();
						}
				else if (!(password.equals(confirmPassword)))
				{
					Log.v("DEBUG","password: '" + password + "' and confirm: '" + confirmPassword + "'");
					AlertDialog alertDialog = new AlertDialog.Builder(Register_Form.this).create();
					alertDialog.setTitle("Error");
					alertDialog.setMessage("Password & Confirmation do not match!");
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
					//Everything is good - connect to server & test if we can add user
					APICall api = new APICall(Register_Form.this);
					//Add to users database
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("email", "" + email));
					nameValuePairs.add(new BasicNameValuePair("password", "" + password));
					nameValuePairs.add(new BasicNameValuePair("gender", "M"));
					nameValuePairs.add(new BasicNameValuePair("username", username));
					nameValuePairs.add(new BasicNameValuePair("pic_url", Brain.DEFAULT_IMAGE));
					
					api.setNameValuePairs(nameValuePairs);
					api.execute("POST","users","register_user");
				}
			}
			
		});
		
	}

	@Override
	public void onTaskCompleted(JSONObject j) {
		try {
			String hook = j.get("hook").toString();
			Log.v("Play","AsyncTask Completed with hook: " + hook);

			if (hook.equals("register_user"))
			{
				String success = j.get("success").toString();
				String error = j.get("error").toString();
				if (success.equals("false"))
				{
					AlertDialog alertDialog = new AlertDialog.Builder(Register_Form.this).create();
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
					Log.v("DEBUG","Added user to database successfully.");
					
					JSONArray users = (JSONArray) j.get("users");

					Log.v(PlayServices.TAG,"Updating current user in Brain");
					Log.v(PlayServices.TAG, users.get(0).toString() );

					User currentUser = Brain.convertJSONToUser((JSONObject) users.get(0));
					
					//Set currentUser in Brain
					Brain.setCurrentUser(currentUser);
					Brain.addUser(currentUser);

					Log.v("Play","Set current user as " + currentUser.getUsername() + " in Brain.");

					
					Intent intent = new Intent(Register_Form.this, Register_Form_2.class);
					startActivity(intent);
					finish();
				}
			}
			


		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
