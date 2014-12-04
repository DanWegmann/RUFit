package com.RUFit.android;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.RUFit.android.objects.Conversation;
import com.RUFit.android.objects.Image;
import com.RUFit.android.objects.Message;
import com.RUFit.android.objects.Post;
import com.RUFit.android.objects.Rec_Activity;
import com.RUFit.android.objects.SubActivity;
import com.RUFit.android.objects.User;
import com.RUFit.android.utilities.MessageComparator;



import android.text.format.DateFormat;
import android.util.Log;

//Class: Braaaaaaains
//@author: Dan Wegmann
//Brain of the app
public class Brain {
	static ArrayList<User> allUsers = new ArrayList<User>(); //Every User that was loaded from the database so far
	static ArrayList<Post> allPosts = new ArrayList<Post>(); //Every Post that was loaded from the database so far
	private static ArrayList<Image> allImages = new ArrayList<Image>();	//Every Image that was loaded from the database so far
	static public ArrayList<Message> allMessages = new ArrayList<Message>();	//Every Message that was loaded from the database so far
	static public ArrayList<Conversation> allConversations = new ArrayList<Conversation>();	//Whenever a conversation with another user is opened, they are added to this list to let us know not to load the original messages again.
	static ArrayList<Rec_Activity> allActivities = new ArrayList<Rec_Activity>();	//Every Activity that was loaded from the database so far
	static ArrayList<SubActivity> allSubActivities = new ArrayList<SubActivity>();	//Every SubActivity that was loaded from the database so far
	static String topPostId;		//The ID of the post at the top of the Feed screen
	static String bottomPostId;		//The ID of the post at the bottom of the Feed screen
	static public User currentUser; //The current User of this app

	
	static public String IMAGE_DIR = "http://wasiwrong.com/SeniorProject/RUFit/profile_pictures/"; //Directory where images are stored online
	static public String DEFAULT_IMAGE = "notset.png"; //default profile picture location

	/*
	 * Method Name: findUserById
	 * 
	 * Returns a User object that has the id specified by the parameter
	 * Returns null if no User has this id
	 * 
	 * @param id
	 * @retutn foundUser
	 */
	public static User findUserById(String id){
		User foundUser = null; 
		for (User u: allUsers){
			if (u.getId().equals(id)){
				foundUser = u;
			}
		}
		return foundUser;
	}

	/*
	 * Method Name: getImageById
	 * 
	 * Returns an Image object that has the id specified by the parameter
	 * Returns null if no User has this id
	 * 
	 * @param id
	 * @return img
	 */
	public static Image getImageById(String id)
	{
		for(Image img : allImages)
		{
			if (img.getId().equals(id))
			{
				return img;
			}
		}
		return null;
	}

	/*
	 * Method Name: addImage
	 * 
	 * Adds the Image object to allImages
	 * 
	 * @return img
	 */
	public static void addImage(Image img)
	{
		allImages.add(img);
	}

	/*
	 * Method Name: findPostById
	 * 
	 * Returns the Post that has the id specified in the parameter
	 * 
	 * @param id
	 * @return foundPost
	 */
	public static Post findPostById(String id){
		Post foundPost = null; 
		for (Post p: allPosts){
			if(p.getId().equals(id)){
				foundPost = p;
			}
		}
		return foundPost;
	}

	/*
	 * Method Name: getUniqueMessages
	 * 
	 * Returns the most recent Message object for every User that has had a conversation
	 * with the 'currentUser'
	 * 
	 * @return uniqueMessages
	 */
	public static ArrayList<Message> getUniqueMessages(){

		ArrayList<Message> uniqueMessages = new ArrayList<Message>();

		
		for(Message m : allMessages)
		{
			boolean addToUnique = true;
			for(Message um : uniqueMessages)
			{
				if (m.getOtherUserId().equals(um.getOtherUserId()))
				{
					addToUnique = false;
					break;
				}
			}
			if (addToUnique == true)
			{
				uniqueMessages.add(m);
			}

		}

		return uniqueMessages;
	}

	/*
	 * Method Name: addUser
	 * 
	 * Adds the User to allUsers
	 * @param u
	 */
	static public void addUser(User u)
	{
		allUsers.add(u);
	}

	/*
	 * Method Name: addPost
	 * 
	 * Adds the Post to allPosts
	 *
	 * @return p
	 */
	static public void addPost(Post p)
	{
		Log.v("DEBUG","Adding post id " + p.getId());
		allPosts.add(p);
	}

	/*
	 * Method Name: addMessage
	 * 
	 * Adds the Message to allMessages
	 * @param m
	 */
	public static void addMessage(Message m) {
		allMessages.add(m);

	}

	/*
	 * Method Name: getMessagesWithUser
	 * 
	 * Returns an ArrayList<Message> of all messages Brain holds between the 
	 * currentUser and the User specified.
	 *
	 * @param u1
	 * @return messageWithUser
	 */
	public static ArrayList<Message> getMessagesWithUser(String u1)
	{
		String u2 = currentUser.getId(); //The currentUser's id
		ArrayList<Message> messagesWithUser = new ArrayList<Message>();

		for(Message msg : allMessages)
		{
			String msg_u1 = msg.getToUserId();
			String msg_u2 = msg.getFromUserId();

			if ( ( (msg_u1.equals(u1)) && (msg_u2.equals(u2)) ) || ( (msg_u1.equals(u2)) && (msg_u2.equals(u1)) ) )
			{
				messagesWithUser.add(msg);
			}
		}
		return messagesWithUser;
	}

	/*
	*
	* @param c
	*/
	public static void addConversation(Conversation c)
	{
		allConversations.add(c);
	}
	/*
	*
	* @param otherUserId
	* @return true, false
	*/
	public static boolean isConversation(String otherUserId)
	{
		for(Conversation c : allConversations)
		{
			if (c.getOtherUserId().equals(otherUserId))
			{
				return true;
			}
		}
		return false;
	}
	/*
	*
	* @param otherUserId
	* @return c
	*/	
	public static Conversation findConversationById(String otherUserId)
	{
		for(Conversation c : allConversations)
		{
			if (c.getOtherUserId().equals(otherUserId))
			{
				return c;
			}
		}
		return null;
	}
	/*
	*
	* @param u
	*/
	public static void setCurrentUser(User u) {
		currentUser = u;

	}
	/*
	*
	* @return currentUser
	*/
	public static User getCurrentUser() {

		return currentUser;
	}
	/*
	*
	* @param thisActivity
	*/
	public static void addRecActivity(Rec_Activity thisActivity) {

		allActivities.add(thisActivity);
	}
	/*
	*
	* @return allActivities
	*/
	public static ArrayList<Rec_Activity> getRecActivities()
	{
		return allActivities;
	}
	/*
	*
	* @param thisSubActivity
	*/
	public static void addSubActivity(SubActivity thisSubActivity) {

		allSubActivities.add(thisSubActivity);
	}
	/*
	*
	* @return allSubActivities
	*/
	public static ArrayList<SubActivity> getSubActivities()
	{
		return allSubActivities;
	}
	/*
	*
	* @return allPosts
	*/
	public static ArrayList<Post> getPosts() {
		return allPosts;
	}
	/*
	*
	* @return 1,-1
	*/
	public static void sortMessages(){
		Collections.sort(allMessages,new Comparator<Message>() {
            public int compare(Message lhs, Message rhs) {
                    int lhsId = Integer.parseInt(lhs.getId());
                    int rhsId = Integer.parseInt(rhs.getId());
                    if(lhsId > rhsId)
                    {   
                        return -1;
                    }
                    else
                    {
                        return 1;
                    }
            }
        });		
	}
	/*
	*
	*
	*/
	public static void sortPosts(){

		Collections.sort(allPosts,new Comparator<Post>() {

            public int compare(Post lhs, Post rhs) {

                try {
                    SimpleDateFormat dateFormatlhs = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    Date convertedDatelhs = dateFormatlhs.parse(lhs.getEventTimestamp());
                    Calendar calendarlhs = Calendar.getInstance();
                    calendarlhs.setTime(convertedDatelhs);

                    SimpleDateFormat dateFormatrhs = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    Date convertedDaterhs = dateFormatrhs.parse(rhs.getEventTimestamp());
                    Calendar calendarrhs = Calendar.getInstance();
                    calendarrhs.setTime(convertedDaterhs);

                    if(calendarlhs.getTimeInMillis() > calendarrhs.getTimeInMillis())
                    {   
                        return -1;
                    }
                    else
                    {
                        return 1;
                    }
                } catch (ParseException e) {

                    e.printStackTrace();
                }
				
                return 0;
            }
        });
		
	}
	/*
	*
	* @param recActivityId
	* @return ra2, ra
	*/
	public static Rec_Activity getRecActivityById(String recActivityId) {
		Rec_Activity ra = null;
		for(Rec_Activity ra2 : allActivities)
		{
			if (ra2.getId().equals(recActivityId))
			{
				return ra2;
			}
		}
		return ra;
	}
	/*
	*
	* @param name
	* @return ra
	*/
	public static Rec_Activity getRecActivityByName(String name)
	{
		for(Rec_Activity ra : allActivities)
		{
			if (ra.getName().equals(name))
			{
				return ra;
			}
		}
		return null;
	}
	/*
	*
	* @param subactivity_id
	* @return sa2, sa
	*/
	public static SubActivity getSubActivityById(String subactivity_id) {
		SubActivity sa = null;
		for(SubActivity sa2 : allSubActivities)
		{
			if (sa2.getId().equals(subactivity_id))
			{
				return sa2;
			}
		}
		return sa;
	}
	/*
	*
	* @param activity_id
	* @return returnList
	*/
	public static ArrayList<SubActivity> getSubActivitiesByActivityId(
			String activity_id) {
		ArrayList<SubActivity> returnList = new ArrayList<SubActivity>();
		for(SubActivity sa : allSubActivities)
		{
			if (sa.get_rec_Activity_id().equals(activity_id))
			{
				returnList.add(sa);
			}
		}
		return returnList;
	}
	/*
	*
	* @param name 
	* @return sa
	*/
	public static SubActivity getSubActivityByName(String name) {
		for(SubActivity sa : allSubActivities)
		{
			if (sa.getName().equals(name))
			{
				return sa;
			}
		}
		return null;
	}
	/*
	*
	* @param userId
	* @return u
	*/
	public static User getUserById(String userId) {
		for(User u : allUsers)
		{
			if (u.getId().equals(userId))
			{
				return u;
			}
		}
		return null;
	}
	/*
	*
	* @param img
	*/
	public static void removeImage(Image img) {
		allImages.remove(img);

	}
	/*
	*
	* @param user_json
	* @return user, null
	*/
	public static User convertJSONToUser(JSONObject user_json) {
		//Parse the JSON Object
		String id;
		try {
			id = user_json.get("id").toString();
			String username = user_json.get("username").toString();
			String pic_url = user_json.get("pic_url").toString();
			String bio = user_json.get("bio").toString();

			//Set up a User object
			User user = new User();
			user.setId(id);
			user.setUsername(username);
			user.setPicUrl(pic_url);
			user.setBio(bio);

			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	/*
	*
	* @param recActivity_json
	* @return recActivity, null
	*/
	public static Rec_Activity convertJSONToRec_Activity(JSONObject recActivity_json) {
		String a_id;
		try {
			a_id = recActivity_json.get("id").toString();

			String a_name = recActivity_json.get("name").toString();
			String a_description = recActivity_json.get("description").toString();
			String a_icon_url = recActivity_json.get("icon_url").toString();
			Rec_Activity recActivity = new Rec_Activity(a_id,a_name,a_icon_url,a_description);

			return recActivity;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	*
	* @param subActivity_json
	* @return subActivity, null
	*/
	public static SubActivity convertJSONToSubActivity(	JSONObject subActivity_json) {
		try {
		String id = subActivity_json.get("id").toString();
		String name = subActivity_json.get("name").toString();
		String activity_id = subActivity_json.get("activity_id").toString();
		
		SubActivity subActivity = new SubActivity(id,name,activity_id);
		return subActivity;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	*
	*
	*/
	public static Message convertJSONToMessage(JSONObject message_json) {
		try {
		
		String id = message_json.get("id").toString();
		String fromUserId = message_json.get("from_id").toString();
		String toUserId = message_json.get("to_id").toString();
		String msg = message_json.get("message").toString();
		String timestamp_String = message_json.get("timestamp").toString();
		
		Message message = new Message(id, fromUserId, toUserId, msg, timestamp_String);
		return message;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	*
	*
	*/
	public static Post convertJSONToPost(JSONObject post_json) {
		try {
		String id = post_json.get("id").toString();
		String timestamp = post_json.get("timestamp").toString();
		String userId = post_json.get("user_id").toString();
		String prefGender = post_json.get("pref_gender").toString();
		String recActivityId = post_json.get("activity_id").toString();
		String subActivityId = post_json.get("sub_actv_id").toString();
		String eventTimestamp = post_json.get("event_timestamp").toString();
		
		boolean active;
		String active_String = post_json.get("active").toString();
			if (active_String.equals("1"))
			{active = true;} else {active = false;}
		
		String prefFitLvl = post_json.get("pref_fit_lvl").toString();
		String myFitLvl = post_json.get("my_fit_lvl").toString();
		
		Post post = new Post(id, timestamp, userId, prefGender, recActivityId, subActivityId, eventTimestamp, active, prefFitLvl, myFitLvl);
		return post;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	*
	*
	*/
	public static void reset() {
		allUsers = new ArrayList<User>(); //Every User that was loaded from the database so far
		allPosts = new ArrayList<Post>(); //Every Post that was loaded from the database so far
		allImages = new ArrayList<Image>();	//Every Image that was loaded from the database so far
		allMessages = new ArrayList<Message>();	//Every Message that was loaded from the database so far
		allConversations = new ArrayList<Conversation>();	//Whenever a conversation with another user is opened, they are added to this list to let us know not to load the original messages again.
		allActivities = new ArrayList<Rec_Activity>();	//Every Activity that was loaded from the database so far
		allSubActivities = new ArrayList<SubActivity>();	//Every SubActivity that was loaded from the database so far
		topPostId = null;		//The ID of the post at the top of the Feed screen
		bottomPostId = null;		//The ID of the post at the bottom of the Feed screen
		currentUser = null; //The current User of this app
		
	}
	/*
	*
	*
	*/
	public static boolean validate_email(String email) {
		if(email != null && !email.isEmpty())
			{
			return true;
			}
		return false;
	}
	/*
	*
	*
	*/
	public static boolean validate_username(String username) {
		if(username != null && !username.isEmpty())
			{
			return true;
			}
		return false;
	}
	/*
	*
	*
	*/
	public static boolean validate_password(String password) {
		if(password != null && !password.isEmpty())
		{
		return true;
		}
		return false;
	}
	/*
	*
	*
	*/
	public static String resolveDate(Post post) {
		
		//Calendar
		String am_pm = "PM";
		String dayText = "";

		String post_date_string = post.getEventTimestamp();
		
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss"); 
	    Date post_date;
	    try {
	        post_date = df.parse(post_date_string);
	        //String newDateString = df.format(post_date);
	        //System.out.println(newDateString);
		Calendar calendar = GregorianCalendar.getInstance();

		// set the calendar to start of today
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		// and get that as a Date
		Date today_start = calendar.getTime();


		// set the calendar to end of today
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);

		// and get that as a Date
		Date today_end = calendar.getTime();

		calendar.setTime(post_date);

		boolean isBeforeToday = false;
		boolean isAfterToday = false;

		// test your condition
		if (post_date.before(today_start)) {
			Log.v("Play","Date specified [" + post_date + "] is before today [" + today_start + "]");
			isBeforeToday = true;
		} else {
			Log.v("Play","Date specified [" + post_date + "] is NOT before today [" + today_start + "]");
			isBeforeToday = false;
		}

		// test your condition
		if (post_date.after(today_end)) {
			Log.v("Play","Date specified [" + post_date + "] is after today [" + today_end + "]");
			isAfterToday = true;
		} else {
			Log.v("Play","Date specified [" + post_date + "] is NOT after today [" + today_end + "]");
			isAfterToday = false;
		}

		int theHour = calendar.get(Calendar.HOUR);
		int theMinute = calendar.get(Calendar.MINUTE);
		int theMonthInt = calendar.get(Calendar.MONTH);
		int theDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		String theHourString = "";
		if (theHour == 0)
		{
			theHourString = "12";
		}
		else {theHourString = "" + theHour;}
		
		String theMinuteString = "";
		if (theMinute < 10)
			{
			theMinuteString = "0" + theMinute;
			}
		else {theMinuteString = "" + theMinute;}
		
		
		String daySuffix = "";
		
		if ( (theDay == 1)|| (theDay == 21) || (theDay == 31) )
		{
			daySuffix = "st";
			
		}
		else if ( (theDay == 2)|| (theDay == 22) )
		{
			daySuffix = "nd";
		}
		else if ( (theDay == 3) )
		{
			daySuffix = "rd";
		}
		else
		{
			daySuffix = "th";
		}
		
		String theMonthString = "";
		switch (theMonthInt)
		{
		case Calendar.JANUARY:
				theMonthString = "Jan";
				break;
		case Calendar.FEBRUARY:
			theMonthString = "Feb";
			break;
		case Calendar.MARCH:
			theMonthString = "Mar";
			break;
		case Calendar.APRIL:
			theMonthString = "April";
			break;
		case Calendar.MAY:
			theMonthString = "May";
			break;
		case Calendar.JUNE:
			theMonthString = "June";
			break;
		case Calendar.JULY:
			theMonthString = "July";
			break;
		case Calendar.AUGUST:
			theMonthString = "Aug";
			break;
		case Calendar.SEPTEMBER:
			theMonthString = "Sep";
			break;
		case Calendar.OCTOBER:
			theMonthString = "Oct";
			break;
		case Calendar.NOVEMBER:
			theMonthString = "Nov";
			break;
		case Calendar.DECEMBER:
			theMonthString = "Dec";
			break;

		
		}
		

		if (calendar.get(Calendar.AM_PM)== Calendar.PM )
		{
			am_pm = "PM";
		}
		else
		{
			am_pm = "AM";
		}
		Log.v("DATE","Time of event is " + theHourString + ":" + theMinute + " " + am_pm);

		if ((!isBeforeToday) && (!isAfterToday))
		{
			dayText = "Today";
		}
		else
		{
			
			dayText = theMonthString + " " + theDay + daySuffix;
		}

		return dayText + " at " + theHourString + ":" + theMinuteString + am_pm;
		//END calendar
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}

}