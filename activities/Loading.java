package com.RUFit.android.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.Brain;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.Message;
import com.RUFit.android.objects.Rec_Activity;
import com.RUFit.android.objects.SubActivity;
import com.RUFit.android.objects.User;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/*
 * The Loading activity appears after succesfully logging into the app.
 * This screen will display a loading message as it retrieves necessary data from the server
 * 
 * NOTE: The currentUser is already updated in the Brain from previous activities
 * 
 * Information that is loaded:
 * 		All Rec Activities
 * 		All Sub Activities
 * 		One most recent message from each conversation this user has engaged in (for displaying on messages fragment)
 */
public class Loading extends Activity implements OnTaskCompleted{

	int toDo;	//AsyncTasks that still need to be completed
	int isDone;	//AsyncTasks that are already completed

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);

		APICall api = new APICall(this);
		api.execute("GET","rec_activities","populate_rec_activities");
		toDo++;
		APICall api2 = new APICall(this);
		api2.execute("GET","sub_activities","populate_sub_activities");
		toDo++;
		APICall api3 = new APICall(this);
		api3.execute("GET","messages/" + Brain.getCurrentUser().getId() + "/unique","populate_unique_messages");
		Log.v("DEBUG","messages/" + Brain.getCurrentUser().getId() + "/unique");
		toDo++;
	}


	@Override
	protected void onResume()
	{
		super.onResume();

	}


	@Override
	public void onTaskCompleted(JSONObject j) {

		try {
			String hook = j.get("hook").toString();
			Log.v("Play","AsyncTask Completed with hook: " + hook);

			if (hook.equals("populate_rec_activities"))
			{
				JSONArray rec_activities = (JSONArray) j.get("rec_activities");
				for(int i = 0; i < rec_activities.length(); i++)
				{
					JSONObject recActivity_json = (JSONObject) rec_activities.get(i);
					Rec_Activity recActivity= Brain.convertJSONToRec_Activity(recActivity_json);
					Brain.addRecActivity(recActivity);
				}
			}
			else if (hook.equals("populate_sub_activities"))
			{
				//add Default Any activity to Brain
				SubActivity subActivity = new SubActivity("0","Any","0");
				Brain.addSubActivity(subActivity);

				JSONArray sub_activities = (JSONArray) j.get("sub_activities");
				for(int i = 0; i < sub_activities.length(); i++)
				{
					JSONObject subActivity_json = (JSONObject) sub_activities.get(i);
					subActivity = Brain.convertJSONToSubActivity(subActivity_json);
					Brain.addSubActivity(subActivity);
				}
			}
			else if (hook.equals("populate_unique_messages"))
			{
				JSONArray messages = (JSONArray) j.get("messages");
				for(int i = 0; i < messages.length(); i++)
				{
					JSONObject message_json = (JSONObject) messages.get(i);
					Message message = Brain.convertJSONToMessage(message_json);
					Brain.addMessage(message);

					String otherUserId = message.getOtherUserId();

					if (Brain.getUserById(otherUserId) == null)
					{
						APICall api4 = new APICall(this);
						api4.execute("GET","users/" + otherUserId,"addUserToBrain");
						toDo++;
					}
				}


			}
			else if (hook.equals("addUserToBrain"))
			{
				JSONArray users = (JSONArray) j.get("users");

				for(int i = 0; i < users.length(); i++)
				{
					JSONObject user_json = (JSONObject) users.get(i);
					User user = Brain.convertJSONToUser(user_json);
					Brain.addUser(user);

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		isDone++;

		if (isDone == toDo)
		{
			Intent intent = new Intent(Loading.this, MainActivity.class);
			startActivity(intent);
			finish();

		}
	}
}
