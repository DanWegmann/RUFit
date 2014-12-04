package com.RUFit.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.Message;
import com.RUFit.android.objects.User;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.utilities.TabPagerAdapter;
import com.RUFit.android.Brain;
import com.RUFit.android.R;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends FragmentActivity implements OnTaskCompleted  {
	ViewPager Tab;
	TabPagerAdapter TabAdapter;
	ActionBar actionBar;

	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("DEBUG","onReceive!");
			
			Bundle extras = intent.getExtras();
			if (!extras.isEmpty()) {
			
			
			APICall api = new APICall(MainActivity.this);
			api.execute("GET", "messages/" + Brain.getCurrentUser().getId() + "/id/" + extras.getString("last_insert_id") ,"retrieveMessages");
			Log.v("DEBUG","api.execute('GET','messages/" + Brain.getCurrentUser().getId() + "/id/" + extras.getString("last_insert_id") + "/");
			}
			
			

		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_old);


		TabAdapter = new TabPagerAdapter(getSupportFragmentManager());

		Tab = (ViewPager)findViewById(R.id.pager);
		Tab.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {

						actionBar = getActionBar();
						actionBar.setSelectedNavigationItem(position);                    }
				});
		Tab.setAdapter(TabAdapter);

		actionBar = getActionBar();
		//Enable Tabs on Action Bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.TabListener tabListener = new ActionBar.TabListener(){

			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

				Tab.setCurrentItem(tab.getPosition());
			}


			
			@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}};
			//Add New Tab

			actionBar.addTab(actionBar.newTab().setTabListener(tabListener).setIcon(R.drawable.feed_icon));
			actionBar.addTab(actionBar.newTab().setTabListener(tabListener).setIcon(R.drawable.calendar_icon));
			actionBar.addTab(actionBar.newTab().setTabListener(tabListener).setIcon(R.drawable.chat_icon));
			actionBar.addTab(actionBar.newTab().setTabListener(tabListener).setIcon(R.drawable.settings_icon));

	}

	@Override
	public void onPause()
	{
		super.onPause();
		unregisterReceiver(mMessageReceiver);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		registerReceiver(mMessageReceiver, new IntentFilter("CUSTOM_BROADCAST_INTENT_FILTER"));
	}

	@Override
	public void onTaskCompleted(JSONObject j) {
		Log.v("Play","OnTaskCompleted Main Activity Screen");
		if ( j.has("hook") ) //a hook was sent with the request
		{
			try {
				String hook = j.get("hook").toString();
				Log.v("Play","AsyncTask Completed with hook: " + hook);

			if (hook.equals("retrieveMessages"))
				{
					Log.v("DEBUG","Hook is retrieveMessages");
					JSONArray messages = (JSONArray) j.get("messages");
					for(int i = 0; i < messages.length(); i++)
					{
						JSONObject message_json = (JSONObject) messages.get(i);
						Message message = Brain.convertJSONToMessage(message_json);
						Brain.addMessage(message);

					}
					
					try {
					    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
					    r.play();
					} catch (Exception e) {
					    e.printStackTrace();
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}






}
