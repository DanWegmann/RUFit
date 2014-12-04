package com.RUFit.android.activities;

import java.util.ArrayList;
import java.util.List;

import com.RUFit.android.Brain;
import com.RUFit.android.R;
import com.RUFit.android.utilities.PlayServices;
import com.google.android.gms.wearable.NodeApi.GetConnectedNodesResult;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends Fragment {
	private SharedPreferences prefs;
	private Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View settings = inflater.inflate(R.layout.settings, container, false);
		Button signOut = (Button) settings.findViewById(R.id.signOut);

		signOut.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				signOut();
			}
		});
		//((TextView)windows.findViewById(R.id.textView)).setText("Settings");
		return settings;
	}
	
	public void signOut()
	{
		Log.v("DEBUG","signOut()");
		context = getActivity().getBaseContext();
		prefs = context.getSharedPreferences("MyPref", 0);
		Editor editor = prefs.edit();
		editor.putString(PlayServices.PROPERTY_REG_ID, "");
		editor.commit();
		
		Brain.reset();
		
		Intent intent = new Intent(getActivity(), Initialize.class);
		startActivity(intent);
		getActivity().finish();
		
	}
}
