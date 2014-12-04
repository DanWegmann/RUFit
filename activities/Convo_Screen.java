package com.RUFit.android.activities;




import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.Brain;
import com.RUFit.android.listeners.OnTaskCompleted;
import com.RUFit.android.objects.Conversation;
import com.RUFit.android.objects.Message;
import com.RUFit.android.objects.User;
import com.RUFit.android.utilities.APICall;
import com.RUFit.android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Convo_Screen extends Activity implements OnTaskCompleted {
	private Button sendMessage_button; //The button to send
	private EditText myMessage_editText; //The box where you can type your message
	private ScrollView myScrollView; //The scrollbox holding all messages
	private LinearLayout linearLayout_Messages; 
	private String otherUser_id;
	private LayoutInflater inflater;
	private Context context;
	private User otherUser;

	private int topMessageId = -1;
	private int bottomMessageId = -1;
	private int mostRecentMessageId = 0; //The newest Message that was displayed on this screen.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convo_screen);
		context = getBaseContext();

		//GET Intent
		Intent intent = getIntent();
		otherUser_id = intent.getStringExtra("otherUser_id");


		//GET layout objects
		sendMessage_button = ((Button)findViewById(R.id.sendMessage));
		myMessage_editText = ((EditText)findViewById(R.id.textInput));
		linearLayout_Messages = ((LinearLayout)findViewById(R.id.linearLayout_Messages));
		myScrollView = ((ScrollView)findViewById(R.id.scrollView1));
		inflater = (LayoutInflater)context.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		createListeners();
		loadMessages();
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("DEBUG","onReceive!");
			APICall api = new APICall(Convo_Screen.this);
			api.execute("GET","messages/" + otherUser_id + "/" + Brain.getCurrentUser().getId() + "/" + topMessageId + "/" + bottomMessageId,"retrieveMessages");
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		Log.v("Play","onPause unregister reeceiver");
		unregisterReceiver(mMessageReceiver);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v("Play","onResume re-register receiver");
		registerReceiver(mMessageReceiver, new IntentFilter("CUSTOM_BROADCAST_INTENT_FILTER"));

		APICall api2 = new APICall(Convo_Screen.this);
		api2.execute("GET","messages/" + otherUser_id + "/" + Brain.getCurrentUser().getId() + "/" + topMessageId + "/" + bottomMessageId,"retrieveMessages");


	}
	/*Loads all messages for this chat session
	 * This method will first check Brain to see if messages are already loaded.
	 * If not it will load them from the database.
	 */
	public void loadMessages()
	{

		otherUser = Brain.findUserById(otherUser_id);
		if (otherUser == null)
		{
			APICall api = new APICall(Convo_Screen.this);
			api.execute("GET","users/" + otherUser_id ,"addUserToBrain");
		}
		else
		{
			Conversation conversation = Brain.findConversationById(otherUser_id);
			if (conversation == null)
			{
				conversation = new Conversation(otherUser_id);
				Brain.addConversation(conversation);

				APICall api = new APICall(Convo_Screen.this);
				api.execute("GET","messages/" + otherUser_id + "/" + Brain.getCurrentUser().getId(),"retrieveMessages");
			}
			else
			{
				displayMessages();

				APICall api = new APICall(Convo_Screen.this);
				api.execute("GET","messages/" + otherUser_id + "/" + Brain.getCurrentUser().getId() + "/" + topMessageId + "/" + bottomMessageId,"retrieveMessages");
			}
		}
	}

	/*
	 * This method will display all of the messages with otherUser_id onto the screen.
	 */
	public void displayMessages()
	{
		Brain.sortMessages();
		Log.v("DEBUG","displayMessages()");
		ArrayList<Message> messages = Brain.getMessagesWithUser(otherUser_id);
		Log.v("DEBUG","messages.size() " + messages.size());
		for(Message msg : messages)
		{
			if (  (Integer.parseInt(msg.getId()) > bottomMessageId)  || (Integer.parseInt(msg.getId()) < topMessageId) ) // it has not been displayed on screen yet
			{
				boolean above = false;
				if (Integer.parseInt(msg.getId()) > bottomMessageId) 
				{
					above = false;
					bottomMessageId = Integer.parseInt(msg.getId());
					if (topMessageId == -1)
						{
						topMessageId = bottomMessageId;
						}
				}
				else if (Integer.parseInt(msg.getId()) < topMessageId)
				{
					above = true;
					topMessageId = Integer.parseInt(msg.getId());
				}
				
				if (msg.getFromUserId().equals(Brain.getCurrentUser().getId())) //This is from currentUser
				{
					View v = inflater.inflate(R.layout.chat_conversation_to,linearLayout_Messages, false);
					RelativeLayout rl = (RelativeLayout) v;
					TextView tv = (TextView) rl.findViewById(R.id.textOutput);
					tv.setText(msg.getMessage());
					if (above)
						{linearLayout_Messages.addView(v,0); }
					else
						{linearLayout_Messages.addView(v);}
					
				}
				else //This is from otherUser
				{
					View v = inflater.inflate(R.layout.chat_conversation_from,linearLayout_Messages, false);
					RelativeLayout rl = (RelativeLayout) v;
					TextView tv = (TextView) rl.findViewById(R.id.textOutput);
					tv.setText(msg.getMessage());
					if (above)
					{linearLayout_Messages.addView(v,0); }
					else
					{linearLayout_Messages.addView(v);}
				}
				

				
			}
		}

		myScrollView.postDelayed(new Runnable() {
			@Override
			public void run() {
				// This method works but animates the scrolling 
				// which looks weird on first load
				// scroll_view.fullScroll(View.FOCUS_DOWN);

				// This method works even better because there are no animations.
				myScrollView.scrollTo(0, linearLayout_Messages.getBottom());
				Log.v("Play","SV Height: " + myScrollView.getHeight());
				Log.v("Play","SV Bottom: " + myScrollView.getBottom());
				Log.v("Play","LL Height: " + linearLayout_Messages.getHeight());
				Log.v("Play","LL Bottom: " + linearLayout_Messages.getBottom());

			}
		}, 200L);

	}

	public void createListeners(){


		sendMessage_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v){

				APICall api = new APICall(Convo_Screen.this);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("message", "" + myMessage_editText.getText() ));
				api.setNameValuePairs(nameValuePairs);
				api.execute("POST", "messages/" + Brain.getCurrentUser().getId() + "/" + otherUser_id + "/" ,"sendMessage");

				myMessage_editText.setText(" ");
			}
		});

	}

	@Override
	public void onTaskCompleted(JSONObject j) {
		Log.v("Play","OnTaskCompleted Convo Screen");
		if ( j.has("hook") ) //a hook was sent with the request
		{
			try {
				String hook = j.get("hook").toString();
				Log.v("Play","AsyncTask Completed with hook: " + hook);

				if (hook.equals("addUserToBrain"))
				{
					Log.v("DEBUG","Hook is adduserToBrain");
					JSONArray users = (JSONArray) j.get("users");

					for(int i = 0; i < users.length(); i++)
					{
						JSONObject user_json = (JSONObject) users.get(i);
						User user = Brain.convertJSONToUser(user_json);
						Brain.addUser(user);
					}
					loadMessages();
				}
				else if (hook.equals("retrieveMessages"))
				{
					Log.v("DEBUG","Hook is retrieveMessages");
					JSONArray messages = (JSONArray) j.get("messages");
					for(int i = 0; i < messages.length(); i++)
					{
						JSONObject message_json = (JSONObject) messages.get(i);
						Message message = Brain.convertJSONToMessage(message_json);
						Brain.addMessage(message);

					}
					displayMessages();
				}
				else if (hook.equals("sendMessage"))
				{
					Log.v("DEBUG","Hook is sendMessage");
					//Message was succesfully added to the server
					//Log.v("DEBUG","current user id is " + Brain.getCurrentUser().getId() + "and mostRecentId is " + mostRecentMessageId);
					APICall api2 = new APICall(Convo_Screen.this);
					api2.execute("GET","messages/" + otherUser_id + "/" + Brain.getCurrentUser().getId()  + "/" + topMessageId + "/" + bottomMessageId,"retrieveMessages");
				}



			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public  void updateChat() {
		// TODO Auto-generated method stub

	}

}
