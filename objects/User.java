package com.RUFit.android.objects;

//Class:User
//@author: Dan Wegmann
//Describes a User of the app. Holds basic info about user
//as well as an arraylist of all the message that user is involved in
public class User{
	private String id;
	private String username;
	private String pic_url = "notset.png";
	private String bio;
	private double rating;
	/**
	*
	*
	*/	
	public User()
		{
		this.id = "0";
		this.username = "NotSet";
		this.bio = "NotSet";
		this.rating = 0.0;
		}
	/**
	*
	* @param id
	*/	
	public User(String id)
	{
	this.id = id;
	this.username = "NotSet";
	this.bio = "NotSet";
	this.rating = 0.0;
	}
	/**
	*
	* @param id, username, pic_url, bio, rating
	*/	
	public User(String id, String username, String pic_url, String bio,
				double rating){
		
		this.id = id;
		this.username= username;
		this.pic_url = pic_url;
		this.bio = bio;
		this.rating = rating;
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
	* @return username
	*/
	public String getUsername() {
		return username;
	}
	/**
	*
	* @return pic_url
	*/
	public String getPic_url() {
		return pic_url;
	}
	/**
	*
	* @return bio
	*/
	public String getBio() {
		return bio;
	}
	/**
	*
	* @return rating
	*/
	public double getRating() {
		return rating;
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
	* param username
	*/	
	public void setUsername(String username)
	{
		this.username = username;
	}
	/**
	*
	* @param pic_url
	*/	
	public void setPicUrl(String pic_url)
	{
		this.pic_url = pic_url;
	}
	/**
	*
	* @param bio
	*/	
	public void setBio(String bio)
	{
		this.bio = bio;
	}
}