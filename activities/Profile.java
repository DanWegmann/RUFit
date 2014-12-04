package com.RUFit.android.activities;

import com.RUFit.android.Brain;
import com.RUFit.android.R;
import com.RUFit.android.objects.Image;
import com.RUFit.android.objects.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;


public class Profile extends Activity {

	private User otherUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		final Context context = getBaseContext();

		//GET Intent
				Intent intent = getIntent();
				final String userId = intent.getStringExtra("otherUser_id");
		otherUser = Brain.findUserById(userId);
		

		ImageView gotoChat = (ImageView) findViewById(R.id.gotoChat);
		ImageView largeProfilePic = (ImageView) findViewById(R.id.largeProfilePic);
		
		Image img = Brain.getImageById(userId);
		largeProfilePic.setImageBitmap(img.getBitmap());
		largeProfilePic.setScaleType(ScaleType.CENTER_CROP);
		
		largeProfilePic.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.v("Play","Goto View Profile Pic.");
				Intent intent = new Intent(context, viewProfileImage.class);
				intent.putExtra("otherUser_id", userId);
				startActivity(intent);
				
			}
			
			
		});
		
		gotoChat.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Log.v("Play","Goto Chat.");
				Intent intent = new Intent(context, Convo_Screen.class);
				intent.putExtra("otherUser_id", userId);
				startActivity(intent);
			}
		});

	}
}
