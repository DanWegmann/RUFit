//Class: Activity
//@author: Dan Wegmann
//Describes a 'class' of activities that will be 
//assigned inside the Brain class. An activity
//is broken up into smaller sub activities. 
package com.RUFit.android.objects;
public class Rec_Activity{
	private String id;
	private String name;
	private String pic_url;
	private String description;
	/**
	*
	* @param id, n, pic, desc
	*/	
	public Rec_Activity(String id, String n, String pic, String desc){
		this.id = id;
		name = n;
		pic_url = pic;
		description = desc;
	}
	/**
	*
	* @return id
	*/	
	public String getId(){
		return id;
	}
	/**
	*
	* @return name
	*/	
	public String getName(){
		return name;
	}
	/**
	*
	* @return pic_url
	*/	
	public String getPicURL(){
		return pic_url;
	}
	/**
	*
	* @return description
	*/	
	public String getDescription(){
		return description;
	}
}