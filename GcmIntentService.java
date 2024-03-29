package com.RUFit.android;

import java.util.List;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.RUFit.android.activities.Convo_Screen;
import com.RUFit.android.activities.LoginActivity;
import com.RUFit.android.activities.MainActivity;
import com.RUFit.android.utilities.PlayServices;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.RUFit.android.R;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
		Log.v("DEBUG","GCMIntentService constructor");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		Log.v("DEBUG","onHandleIntent ");
		
		 ActivityManager am = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
		    // Get info from the currently active task
		    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks( 1 );
		    String activityName = taskInfo.get( 0 ).topActivity.getClassName();
		    if(activityName.equals( Convo_Screen.class.getName() ))
		    {
		    	Log.v("Play","Convo screen is active. Load new messages from database.");
		    	
		    	sendBroadcast(new Intent("CUSTOM_BROADCAST_INTENT_FILTER"));
		    }
		    else if (activityName.equals("com.RUFit.android.activities.MainActivity"))
		    {
		    	Log.v("DEBUG","activity is MainActivity");
		    	Intent customIntent = new Intent("CUSTOM_BROADCAST_INTENT_FILTER");
		    	customIntent.putExtra("last_insert_id", extras.getString("last_insert_id"));
		    	sendBroadcast(customIntent);
		    }
		    else
		    {
		    	

		    	
		    Log.v("DEBUG",activityName);
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.
					MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " +
						extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// This loop represents the service doing some work.
				for (int i=0; i<5; i++) {
					Log.i(PlayServices.TAG, "Working... " + (i+1)
							+ "/5 @ " + SystemClock.elapsedRealtime());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}
				Log.i(PlayServices.TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				// Post notification of received message.
				//sendNotification("Received: id: " + extras.getString("last_insert_id") + extras.getString("price"));
				sendNotification(extras.getString("price"));
				Log.i(PlayServices.TAG, "Received: " + extras.getString("price"));
				
		    	
			}
		}		    
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		

		
		mNotificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, LoginActivity.class), 0);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("GCM Notification")
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(msg))
		.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}