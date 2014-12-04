//Class: SubActivity
//@author: Dan Wegmann
//Describes subactivity. Contains a name and a the category of activity that 
//it is contained in
package com.RUFit.android.objects;
public class SubActivity{
	private String id;
	private String name; //name of subactivity
	private String rec_Activity_id; //category of subactivity
	/**
	*
	* @param id, name, rec_Activity_id
	*/
	public SubActivity(String id, String name, String rec_Activity_id){
		this.id = id;
		this.name = name;
		this.rec_Activity_id = rec_Activity_id;
		
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
	* @return rec_Activity_id
	*/	
	public String get_rec_Activity_id()
	{
		return rec_Activity_id;
	}
	/**
	*
	* @return id
	*/
	public String getId() {
		return id;
	}
}