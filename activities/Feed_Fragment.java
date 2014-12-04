package com.RUFit.android.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import java.text.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.Brain;
import com.RUFit.android.listeners.OnImageDownloaded;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.Image;
import com.RUFit.android.objects.Post;
import com.RUFit.android.objects.User;
import com.RUFit.android.objects.queuedDownloadImageTask;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.utilities.DownloadImageTask;
import com.RUFit.android.R;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class Feed_Fragment extends Fragment implements OnTaskCompleted, OnRefreshListener, OnImageDownloaded{

	
	private SwipeRefreshLayout swipeLayout;
	private boolean isRefreshing = false;
	private Button addPost_button;
	private LayoutInflater inflater;
	private LinearLayout linearLayout_Posts;
	private int pendingUsers = 0;
	private int mostRecentPostId = 0; //THe most recent post we loaded in
	private int topPostId = 999999999;
	private int bottomPostId = 0;
	
	private ArrayList<String> displayedPostIds = new ArrayList<String>();
	
	private int imagesDownloading = 0;
	private int imagesDownloaded = 0;
	
	private ArrayList<queuedDownloadImageTask> queuedImages = new ArrayList<queuedDownloadImageTask>();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		View feed = inflater.inflate(R.layout.feed_frag, container, false);
		linearLayout_Posts = ((LinearLayout)feed.findViewById(R.id.linearLayout_Posts));
		TextView addPost_button = (TextView)feed.findViewById(R.id.addPost_button);
		addPost_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				//Log.v("Play","AddPost_button pressed.");
				Intent intent = new Intent(getActivity(), AddPost.class);
				startActivity(intent);
			}});

		 swipeLayout = (SwipeRefreshLayout) feed.findViewById(R.id.swipe_container);
		    swipeLayout.setOnRefreshListener(this);
		    swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
		            android.R.color.holo_green_light, 
		            android.R.color.holo_orange_light, 
		            android.R.color.holo_red_light);

		loadMore();
		return feed;
	}
	@Override
	public void onResume()
	{
		super.onResume();
		Log.v("DEBUG","onResume() Feed Frag");
		loadMore();
	}
	public void loadMore()
	{
		displayFeedItems();
		
		APICall api = new APICall(Feed_Fragment.this);
		api.execute("GET","posts/" + mostRecentPostId,"addFeedItems");
		Log.v("DEBUG","API GET posts/" + mostRecentPostId);
	}

	public void setListeners()
	{
		addPost_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v){

				//Log.v("Play","AddPost_button pressed.");
				Intent intent = new Intent(getActivity(), AddPost.class);
				intent.putExtra("Email", "test");
				startActivity(intent);

			}});
	}

	@Override
	public void onTaskCompleted(JSONObject j) {
		Log.v("Play","OnTaskCompleted Convo Screen");
		try {
			String hook = j.get("hook").toString();
			Log.v("Play","AsyncTask Completed with hook: " + hook);

			if (hook.equals("addFeedItems"))
			{
				JSONArray posts = (JSONArray) j.get("posts");
				for(int i = 0; i < posts.length(); i++)
				{
					JSONObject post_json = (JSONObject) posts.get(i);
					Post post = Brain.convertJSONToPost(post_json);


					//java.util.Date date = (java.util.Date) new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",Locale.ENGLISH).parse(p_event_timestamp);

					User userId = Brain.findUserById(post.getUserId());
					if (userId == null)
					{
						pendingUsers ++;
						APICall api_2 = new APICall(Feed_Fragment.this);
						api_2.execute("GET","users/" + post.getUserId(), "addUserToBrain");
					}

					Brain.addPost(post);
				}
				swipeLayout.setRefreshing(false);
				isRefreshing = false;
				if (pendingUsers == 0)
				{
					displayFeedItems();
				}
			}

			else if (hook.equals("addUserToBrain"))
			{
				JSONArray users = (JSONArray) j.get("users");
				//Log.v("Play", users.get(0).toString() ); //prints info for first user
				for(int i = 0; i < users.length(); i++)
				{
					JSONObject user_json = (JSONObject) users.get(i);

					User user = Brain.convertJSONToUser(user_json);
					Brain.addUser(user);

					pendingUsers --;
				}
				if (pendingUsers == 0)
				{
					displayFeedItems();
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void displayFeedItems() {
		Brain.sortPosts();
		for(Post post : Brain.getPosts())
		{
			if (!(displayedPostIds.contains(post.getId())))
			{
				
			
			final String userId = post.getUserId();
			User user = Brain.findUserById(post.getUserId());

			if (user != null)
			{
				View v = inflater.inflate(R.layout.post_layout,linearLayout_Posts, false);
				TextView text_Name = (TextView) v.findViewById(R.id.text_Name);
				text_Name.setText(user.getUsername());

				LinearLayout gotoChat = (LinearLayout) v.findViewById(R.id.gotoChat);
				ImageView image_profilepic = (ImageView) v.findViewById(R.id.image_profilepic);
				image_profilepic.setScaleType(ScaleType.CENTER_CROP);
				
				gotoChat.setOnClickListener(new OnClickListener(){
					public void onClick(View v){
						//Log.v("Play","Goto Chat.");
						Intent intent = new Intent(getActivity(), Convo_Screen.class);
						intent.putExtra("otherUser_id", userId);
						startActivity(intent);
					}
				});

				image_profilepic.setOnClickListener(new OnClickListener(){
					public void onClick(View v){
						//Log.v("Play","Goto Chat.");
						Intent intent = new Intent(getActivity(), Profile.class);
						intent.putExtra("otherUser_id", userId);
						startActivity(intent);
					}
				});
				
				((TextView)v.findViewById(R.id.text_Activity)).setText(post.getRecActivity().getName());
				((TextView)v.findViewById(R.id.text_SubActivity)).setText(post.getSubActivity().getName());
				String readableDate = Brain.resolveDate(post);
				((TextView)v.findViewById(R.id.text_time)).setText(readableDate);
				

				
				//FITNESS LEVEL
				int theirLevel = Integer.parseInt(post.getPrefFitLvl());
				int myLevel = Integer.parseInt(post.getMyFitLvl());
				String displayLevels = "";

				switch (myLevel)
				{
				case 1:
					displayLevels += "Beginner";
					break;
				case 2:
					displayLevels += "Intermediate";
					break;
				case 3:
					displayLevels += "Expert";
					break;
				}

				displayLevels += " looking for ";
				switch (theirLevel)
				{
				case 0:
					displayLevels += "Any";
					break;
				case 1:
					displayLevels += "Beginners";
					break;
				case 2:
					displayLevels += "Intermediates";
					break;
				case 3:
					displayLevels += "Experts";
					break;
				}
				//END FITNESS LEVEL


				//((TextView)v.findViewById(R.id.text_time)).setText(dayText + " at " + theHour + ":" + theMinute + " " + am_pm);
				((TextView)v.findViewById(R.id.text_Level)).setText(displayLevels);



				Image img = Brain.getImageById(user.getId());
				ImageView imgView = (ImageView) v.findViewById(R.id.image_profilepic);
				ProgressBar progress = (ProgressBar) v.findViewById(R.id.progressBar1);
				if (img != null)
				{
					Log.v("Play","Image Exists... retrieving");

					imgView.setImageBitmap(img.getBitmap());
					imgView.setScaleType(ScaleType.CENTER_CROP);

					if (img.getBitmap() == null)
					{
						Log.v("Play","Got image from brain ... but it was null loading new one");
						Brain.removeImage(img);
						queuedImages.add(new queuedDownloadImageTask(imgView, progress, user.getId(), user.getPic_url(), this));
						
					}
					else
					{
						progress.setVisibility(View.GONE);
					}


				}
				else
				{
					Log.v("COD","Image does not exist.Creating. DownloadImageTask userId: " + user.getId() + " URL: " + Brain.IMAGE_DIR + user.getPic_url());
					queuedImages.add(new queuedDownloadImageTask(imgView, progress, user.getId(), user.getPic_url(), this));
					
				}



				
				displayedPostIds.add(post.getId());
				if (mostRecentPostId < Integer.parseInt(post.getId()))
				{
					mostRecentPostId = Integer.parseInt(post.getId());
					linearLayout_Posts.addView(v,0); //add to top
				}
				else
				{
					linearLayout_Posts.addView(v); //add to bottom
				}
				
				Log.v("DEBUG","mostRecentPostId: " + mostRecentPostId);
			}
			}
			
			else
			{
				Log.v("DEBUG","Post id " + post.getId() + " already displayed.");
				
			}

		}
		
		//if there are images that need to be downloaded
		if (queuedImages.size() > 0)
		{
			ArrayList<String> ids = new ArrayList<String>();
			for (queuedDownloadImageTask q : queuedImages)
			{
				boolean downloadMe = true;
				String id = q.getUserId();
				for(String s : ids)
				{
					if (id.equals(s))
						{
						//SKIP we already are in the process of downloading this image.
						downloadMe = false;
						break;
						}
				}
				if (downloadMe == true)
				{
					imagesDownloading++;
					ids.add(id);
					q.downloadImage();
				}
			}
		}

	}

	@Override
	public void OnImageDownloaded() {
		imagesDownloaded ++;
		if (imagesDownloaded == imagesDownloading)
		{
			Log.v("Notice","All images downloaded. Updating all others.");
			for (queuedDownloadImageTask q : queuedImages)
			{
				q.updateImage();
			}
			}
		}

	@Override public void onRefresh() {
		
		if (!(isRefreshing))
			{
			Log.v("DEBUG","It is not currently refreshing.");
			loadMore();
			isRefreshing = true;
			}
			else
			{
				Log.v("DEBUG","It's already refreshing..");
			}
		
	}
}
