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


public class viewProfileImage extends Activity {

	private User otherUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_profile_image);
		final Context context = getBaseContext();

		//GET Intent
				Intent intent = getIntent();
				final String userId = intent.getStringExtra("otherUser_id");
		otherUser = Brain.findUserById(userId);
		
		ImageView largeProfilePic = (ImageView) findViewById(R.id.largeProfilePic);
		
		Image img = Brain.getImageById(userId);
		largeProfilePic.setImageBitmap(img.getBitmap());
		largeProfilePic.setScaleType(ScaleType.FIT_CENTER);

	}
}
