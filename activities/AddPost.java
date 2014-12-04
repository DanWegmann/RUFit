package com.RUFit.android.activities;




import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.RUFit.android.Brain;
import com.RUFit.android.R;
import com.RUFit.android.listeners.OnDatePicked;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.listeners.OnTimePicked;
import com.RUFit.android.objects.Rec_Activity;
import com.RUFit.android.objects.SubActivity;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.utilities.AccountUtils;
import com.RUFit.android.utilities.PlayServices;

import android.os.Bundle;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AddPost extends Activity implements OnTimePicked, OnDatePicked, OnTaskCompleted, OnItemSelectedListener {

private RadioGroup radioGroup_gender_pref;
private RadioButton radio_male;
private RadioButton radio_female;
private RadioButton radio_either;
private Button datePicker_button;
private Button timePicker_button;

private int activity_id;
private String subActivity_id;
private int fitnessLevel = 1;
private String genderPref = "E";
private int theirFitnessLevel = 0;
private String date;
private String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_post);


		//Get radio buttons for gender preference
		radioGroup_gender_pref = ((RadioGroup)findViewById(R.id.radioGroup_gender_pref));
		radio_male = ((RadioButton)findViewById(R.id.radio_male));
		radio_female = ((RadioButton)findViewById(R.id.radio_female));
		radio_either = ((RadioButton)findViewById(R.id.radio_either));
		
		Spinner dropdown = (Spinner)findViewById(R.id.spinner_activity);
		ArrayList<String> items = new ArrayList<String>();
		ArrayList<Rec_Activity> activities = Brain.getRecActivities();
		for (Rec_Activity a : activities)
		{
			items.add(a.getName());
		}
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items); //selected item will look like a spinner set from XML
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(spinnerArrayAdapter);
		dropdown.setOnItemSelectedListener(this);
		
		dropdown = (Spinner)findViewById(R.id.spinner_subActivity);
		items = new ArrayList<String>();
		ArrayList<SubActivity> subactivities = Brain.getSubActivities();
		for (SubActivity a : subactivities)
		{
			items.add(a.getName());
		}
		spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items); //selected item will look like a spinner set from XML
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(spinnerArrayAdapter);
		dropdown.setOnItemSelectedListener(this);
		
		dropdown = (Spinner)findViewById(R.id.spinner_fitLevel);
		items = new ArrayList<String>();
		
		items.add("Beginner (0 - 3 months)");
		items.add( "Intermediate (3 - 24 months)");
		items.add("Expert ( > 24 months)");
		spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items); //selected item will look like a spinner set from XML
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(spinnerArrayAdapter);
		dropdown.setOnItemSelectedListener(this);
		
		items = new ArrayList<String>();
		items.add("Any");
		items.add("Beginner (0 - 3 months)");
		items.add( "Intermediate (3 - 24 months)");
		items.add("Expert ( > 24 months)");
		dropdown = (Spinner)findViewById(R.id.spinner_theirFitLevel);
		spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items); //selected item will look like a spinner set from XML
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(spinnerArrayAdapter);
		dropdown.setOnItemSelectedListener(this);
		
		timePicker_button = (Button)findViewById(R.id.timePicker_button);
		datePicker_button = (Button)findViewById(R.id.datePicker_button);
		
		


	}

	public void submitPost(View view){
		Log.v("Play","Submit Post");
		
		
		APICall api = new APICall(AddPost.this);

		//Add to users database
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("activity_id", "" + activity_id));
		nameValuePairs.add(new BasicNameValuePair("subActivity_id", "" + subActivity_id));
		nameValuePairs.add(new BasicNameValuePair("fitnessLevel", "" + fitnessLevel));
		nameValuePairs.add(new BasicNameValuePair("genderPref", genderPref));
		nameValuePairs.add(new BasicNameValuePair("theirFitnessLevel", "" + theirFitnessLevel));
		nameValuePairs.add(new BasicNameValuePair("date", date));
		nameValuePairs.add(new BasicNameValuePair("time", time));
		nameValuePairs.add(new BasicNameValuePair("user_id", Brain.getCurrentUser().getId()));
		
		api.setNameValuePairs(nameValuePairs);
		api.execute("POST","posts","hook1");
	}
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment(this);
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment(this);
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    ((RadioButton) view).setChecked(true);
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_male:
	            radio_male.setChecked(true);
	        	if (checked)
	                Log.v("Play","radio male");
	        		genderPref = "M";
	            break;
	        case R.id.radio_female:
	        	radio_female.setChecked(true);
	            if (checked)
	            	Log.v("Play","radio female");
	            genderPref = "F";
	            break;
	        case R.id.radio_either:
	        	radio_either.setChecked(true);
	            if (checked)
	            	Log.v("Play","radio either");
	            genderPref = "E";
	            break;
	    }
	}
	

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.v("Play","OnResume");
		if (PlayServices.checkPlayServices(getBaseContext(), this) && PlayServices.checkUserAccount(getBaseContext(), this)){
			Log.v("Play","GOOD");
		}
		else{
			Log.v("Play","BAD");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PlayServices.REQUEST_CODE_RECOVER_PLAY_SERVICES:
			if (resultCode == RESULT_CANCELED) {
				Log.v("Play","Google Play Services must be installed.");
				finish();
			}

		case PlayServices.REQUEST_CODE_PICK_ACCOUNT:
			if (resultCode == RESULT_OK) {
				String accountName = data.getStringExtra(
						AccountManager.KEY_ACCOUNT_NAME);
				AccountUtils.setAccountName(this, accountName);
			} else if (resultCode == RESULT_CANCELED) {
				Log.v("Play","This application requires a Google account.");
				finish();
			}

			return;
		}


		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTaskCompleted(JSONObject j) {
		Log.v("Play","Task completed.");
		finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		Log.v("Play","Spinner item " + position);
		switch(parent.getId()){
			case R.id.spinner_activity:
				Log.v("Play","Spinner - " + parent.getSelectedItem().toString());
				Log.v("Play","The ID is - " + Brain.getRecActivityByName(parent.getSelectedItem().toString()).getId());
				activity_id = Integer.parseInt(Brain.getRecActivityByName(parent.getSelectedItem().toString()).getId());
				Spinner spinner = (Spinner)findViewById(R.id.spinner_subActivity);
				

				
				ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
				spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(spinnerAdapter);
				
				ArrayList<SubActivity> subActivities = Brain.getSubActivitiesByActivityId("" + activity_id);
				spinnerAdapter.add("Any");
				Log.v("Play","subActivities size: " + subActivities.size());
				for (SubActivity a : subActivities)
				{
					Log.v("Play","adding: " + a.getName());
					spinnerAdapter.add(a.getName());
				}
				
				spinnerAdapter.notifyDataSetChanged();
				break;
			case R.id.spinner_fitLevel:
				fitnessLevel = position+1;
				Log.v("Play","Your fitness level=" + (position+1));
				break;
			case R.id.spinner_subActivity:
				SubActivity sa = Brain.getSubActivityByName(parent.getSelectedItem().toString());
				subActivity_id = sa.getId();
				Log.v("Play","Sub Activity id set to " + subActivity_id);
				break;
			case R.id.spinner_theirFitLevel:
				theirFitnessLevel = position;
				Log.v("Play","Their fitness level=" + position);
				break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
	
	public static class TimePickerFragment extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener {
		
		private OnTimePicked listener;
		
		public TimePickerFragment(OnTimePicked listener)
		{
			this.listener = listener;
		}
		
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current time as the default values for the picker
final Calendar c = Calendar.getInstance();
int hour = c.get(Calendar.HOUR_OF_DAY);
int minute = c.get(Calendar.MINUTE);

// Create a new instance of TimePickerDialog and return it
return new TimePickerDialog(getActivity(), this, hour, minute,
DateFormat.is24HourFormat(getActivity()));
}

public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
// Do something with the time chosen by the user
	listener.onTimePicked(hourOfDay, minute);
}
}
	
	
	
	public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {
		private OnDatePicked listener;
		
		public DatePickerFragment(OnDatePicked listener)
		{
			this.listener = listener;
		}
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
final Calendar c = Calendar.getInstance();
int year = c.get(Calendar.YEAR);
int month = c.get(Calendar.MONTH);
int day = c.get(Calendar.DAY_OF_MONTH);

// Create a new instance of DatePickerDialog and return it
return new DatePickerDialog(getActivity(), this, year, month, day);
}

public void onDateSet(DatePicker view, int year, int month, int day) {
listener.onDatePicked(year, month, day);
}
}



	@Override
	public void onDatePicked(int year, int month, int day) {
		Log.v("Play","Date picked ");
		datePicker_button.setText("" + (month+1) + "/" + day + "/" + year);
		date = "" + year + "-" + (month+1) + "-" + day;
		Log.v("Play","Date set to " + date);
	}

	@Override
	public void onTimePicked(int hourOfDay, int minute) {
		Log.v("Play","Time picked ");
		String amPm = "AM";
		int normalizedHour = hourOfDay;
		if (hourOfDay > 12)
		{
			normalizedHour = normalizedHour - 12;
			amPm = "PM";
		}
		timePicker_button.setText("" + normalizedHour + ":" + minute + " " + amPm);
		time = "" + hourOfDay + ":" + minute + ":00";
		Log.v("Play","Time set to " + time);
	}
	
}
