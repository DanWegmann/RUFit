package com.RUFit.android.objects;
import java.util.Date;

import com.RUFit.android.Brain;

//Class: Post
//@author: Dan Wegmann
//Describes an activity post. Contains all relavent info 
//for a post
public class Post{
	private String id;
	private String timestamp;
	private String userId;
	private String prefGender;
	private String recActivityId;
	private String subActivityId;
	private String eventTimestamp;
	private boolean active;
	private String prefFitLvl;
	private String myFitLvl;
	
	/**
	*
	* @param id, timestamp, userId, prefGender, recActivityId, subActivityId, eventTimestamp, active, prefFitLvl, myFitLvl
	*/
	public Post(String id, String timestamp, String userId, String prefGender,
			String recActivityId, String subActivityId, String eventTimestamp,
			boolean active, String prefFitLvl, String myFitLvl) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.userId = userId;
		this.prefGender = prefGender;
		this.recActivityId = recActivityId;
		this.subActivityId = subActivityId;
		this.eventTimestamp = eventTimestamp;
		this.active = active;
		this.prefFitLvl = prefFitLvl;
		this.myFitLvl = myFitLvl;
	}
	/**
	*
	* @return id
	*/	
	public String getId() {
		return id;
	}
	/**
	*
	* @param id
	*/	
	public void setId(String id) {
		this.id = id;
	}
	/**
	*
	* @return timestamp
	*/	
	public String getTimestamp() {
		return timestamp;
	}
	/**
	*
	* @param timestamp
	*/	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	*
	* @return userId
	*/	
	public String getUserId() {
		return userId;
	}
	/**
	*
	* @param userId
	*/	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	*
	* @return prefGender
	*/	
	public String getPrefGender() {
		return prefGender;
	}
	/**
	*
	* @param prefGender
	*/	
	public void setPrefGender(String prefGender) {
		this.prefGender = prefGender;
	}
	/**
	*
	* @return recActivityId
	*/	
	public String getActivityId() {
		return recActivityId;
	}
	/**
	*
	* @param activityId
	*/	
	public void setActivityId(String activityId) {
		this.recActivityId = activityId;
	}
	/**
	*
	* @return subActivityId
	*/	
	public String getSubActivityId() {
		return subActivityId;
	}
	/**
	*
	* @param subActivityId
	*/	
	public void setSubActivityId(String subActivityId) {
		this.subActivityId = subActivityId;
	}
	/**
	*
	* @return eventTimestamp
	*/	
	public String getEventTimestamp() {
		return eventTimestamp;
	}
	/**
	*
	* @param eventTimestamp
	*/	
	public void setEventTimestamp(String eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}
	/**
	*
	* @return active
	*/	
	public boolean isActive() {
		return active;
	}
	/**
	*
	* @param active
	*/	
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	*
	* @return prefFitLvl
	*/	
	public String getPrefFitLvl() {
		return prefFitLvl;
	}
	/**
	*
	* @param prefFitLvl
	*/	
	public void setPrefFitLvl(String prefFitLvl) {
		this.prefFitLvl = prefFitLvl;
	}
	/**
	*
	* @return myFitLvl
	*/	
	public String getMyFitLvl() {
		return myFitLvl;
	}
	/**
	*
	* @param myFitLvl
	*/	
	public void setMyFitLvl(String myFitLvl) {
		this.myFitLvl = myFitLvl;
	}
	/**
	*
	* @return recActivityId
	*/	
	public Rec_Activity getRecActivity() {
		return Brain.getRecActivityById(recActivityId);
	}
	/**
	*
	* @return Brain.getSubActivityById(subActivityId)
	*/	
	public SubActivity getSubActivity() {
		return Brain.getSubActivityById(subActivityId);
	}
}