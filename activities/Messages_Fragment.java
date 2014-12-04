package com.RUFit.android.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.Brain;
import com.RUFit.android.listeners.OnImageDownloaded;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.Image;
import com.RUFit.android.objects.Message;
import com.RUFit.android.objects.PendingMessageBox;
import com.RUFit.android.objects.Post;
import com.RUFit.android.objects.User;
import com.RUFit.android.objects.queuedDownloadImageTask;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class Messages_Fragment extends Fragment implements OnImageDownloaded, OnTaskCompleted {
	private LayoutInflater inflater;
	private int imagesDownloading = 0;
	private int imagesDownloaded = 0;
	
	private View messages;

	private int pendingUsers = 0;
	private ArrayList<PendingMessageBox> pendingMessageBoxes = new ArrayList<PendingMessageBox>();
	
	private ArrayList<queuedDownloadImageTask> queuedImages = new ArrayList<queuedDownloadImageTask>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		messages = inflater.inflate(R.layout.messages_frag, container, false);
		this.inflater = inflater;
		return messages;
	}

	private void displayMessages() {
		Log.v("DEBUG","displayMessages()");
		LinearLayout message_LL = ((LinearLayout)messages.findViewById(R.id.container));

		Brain.sortMessages();
		ArrayList<Message> uniqueMessages = Brain.getUniqueMessages();

		if (uniqueMessages != null)
		{
			message_LL.removeAllViews();
		for(Message m : uniqueMessages)
		{
			Log.v("DEBUG","msg frag 1");
			LinearLayout messageBox = (LinearLayout) inflater.inflate(R.layout.top_message,message_LL, false);
			ProgressBar progress = (ProgressBar) messageBox.findViewById(R.id.progress);
			ImageView image_profilepic = (ImageView) messageBox.findViewById(R.id.image_profilepic);
			image_profilepic.setScaleType(ScaleType.CENTER_CROP);
			TextView text_name = (TextView) messageBox.findViewById(R.id.text_name);
			TextView text_msg = (TextView) messageBox.findViewById(R.id.text_msg);
			LinearLayout wrapper = (LinearLayout) messageBox.findViewById(R.id.wrapper);
			Log.v("DEBUG","msg frag 2");
			text_msg.setText(m.getMessage());
			
			final User otherUser = Brain.getUserById(m.getOtherUserId());
			
			if (otherUser == null)
			{
				Log.v("DEBUG","msg frag 3 null");
				PendingMessageBox pending = new PendingMessageBox(progress, wrapper, m.getOtherUserId(), image_profilepic, text_name);
				pendingMessageBoxes.add(pending);
				pendingUsers++;
				APICall api = new APICall(Messages_Fragment.this);
				api.execute("GET","users/" + m.getOtherUserId(), "addUserToBrain");
			}
			else
			{
				Log.v("DEBUG","msg frag 4 not null");
				Image img = Brain.getImageById(otherUser.getId());
				
				if (img == null)
					{
					//User picture has not been downloaded yet
					queuedImages.add(new queuedDownloadImageTask(image_profilepic, progress, otherUser.getId(), otherUser.getPic_url(), this));
					}
				else
				{
					Log.v("DEBUG","Image already downloaded!");
					image_profilepic.setImageBitmap(img.getBitmap());

					progress.setVisibility(View.GONE);
					
				}
				text_name.setText(otherUser.getUsername());
				
				wrapper.setOnClickListener(new OnClickListener(){
					public void onClick(View v){
						Log.v("Play","Goto Chat.");
						Intent intent = new Intent(getActivity(), Convo_Screen.class);
						intent.putExtra("otherUser_id", otherUser.getId());
						startActivity(intent);
					}
				});
			}
			
			message_LL.addView(messageBox);	
			
			

		}
		
		for(queuedDownloadImageTask q : queuedImages)
			{
			q.downloadImage();
			}
		queuedImages = new ArrayList<queuedDownloadImageTask>();
		}
		else
		{
			Log.v("DEBUG","UniqueMessages is null");
		}
		Log.v("DEBUG","msg frag 5");
		
	}

	@Override
	public void onResume() {
		super.onResume();
		displayMessages();
	}
	@Override
	public void OnImageDownloaded() {
		Log.v("DEBUG","Image downloaded!");
		
	}

	@Override
	public void onTaskCompleted(JSONObject j) {
		Log.v("Play","OnTaskCompleted Messages Frag");
		try {
			String hook = j.get("hook").toString();
			Log.v("Play","AsyncTask Completed with hook: " + hook);
			if (hook.equals("addUserToBrain"))
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
					updatePending();
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void updatePending() {

		for(PendingMessageBox thisBox : pendingMessageBoxes)
		{
			final User otherUser = Brain.getUserById(thisBox.getOtherUser_id());

			ImageView image_profilepic = thisBox.getImage_profilepic();
			ProgressBar progress = thisBox.getProgress();
			TextView text_name = thisBox.getText_name();
			LinearLayout wrapper = thisBox.getWrapper();
			
			Image img = Brain.getImageById(otherUser.getId());
			if (img == null)
				{
				//User picture has not been downloaded yet
				queuedImages.add(new queuedDownloadImageTask(image_profilepic, progress, otherUser.getId(), otherUser.getPic_url(), this));
				}
			else
			{
				Log.v("DEBUG","Image already downloaded!");
				image_profilepic.setImageBitmap(img.getBitmap());
				progress.setVisibility(View.GONE);
				
			}
			text_name.setText(otherUser.getUsername());
			
			wrapper.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					Log.v("Play","Goto Chat.");
					Intent intent = new Intent(getActivity(), Convo_Screen.class);
					intent.putExtra("otherUser_id", otherUser.getId());
					startActivity(intent);
				}
			});
		}
		
		for(queuedDownloadImageTask q : queuedImages)
		{
		q.downloadImage();
		}
		
		queuedImages = new ArrayList<queuedDownloadImageTask>();

		
		
	}
}

