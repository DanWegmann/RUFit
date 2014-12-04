package com.RUFit.android.objects;
import java.util.Date;

import com.RUFit.android.Brain;

//Class: Message
//@author: Dan Wegmann
//Describes a single chat message that is sent from one user
//to another and displayed inside a chat window.
//Basic getters and setters 
public class Message{
	private String id; //id of message in DB
	private String fromUserId; //user sending the message	
	private String toUserId; //user receiving the message
	private String message; //the message 
	private String timestamp_String; //date and time of when the message was sent
	
	/**
	*
	* @param id, fromUserId, toUserId, message, timestamp_String
	*/
	public Message(String id, String fromUserId, String toUserId, String message, String timestamp_String){
		this.id = id;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.message = message;
		this.timestamp_String = timestamp_String;
	}
	/**
	*
	* @param msg
	*/
	public void setMessage(String msg){
		message = msg;
	}
	/**
	*
	* @return message
	*/	
	public String getMessage(){
		return message;
	}
	/**
	*
	* @param toUserId
	*/	
	public void setToUserId(String toUserId){
		this.toUserId = toUserId;
	}
	/**
	*
	* @return toUserId
	*/	
	public String getToUserId(){
		return toUserId;
	}
	/**
	*
	* @return fromUserId, toUserId
	*/	
	public String getOtherUserId(){
		
		if (toUserId.equals(Brain.getCurrentUser().getId()))
		{
			return fromUserId;
		}
		else
			return toUserId;
		
	}
	/**
	*
	* @param fromUserId
	*/	
	public void setFromUserId(String fromUserId){
		this.fromUserId = fromUserId;
	}
	/**
	*
	* @return fromUserId
	*/	
	public String getFromUserId(){
		return fromUserId;
	}
	/**
	*
	* @return timestamp_String
	*/	
	public String getTimeStamp(){
		return timestamp_String;
	}	
	/**
	*
	* @param timestamp_String
	*/	
	public void setTimeStamp(String timestamp_String){
		this.timestamp_String = timestamp_String;
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
}