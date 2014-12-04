package com.RUFit.android.objects;


import android.graphics.Bitmap;

/**
*
*/
public class Image {
	private String id;
	private Bitmap img;
	
	/**
	*
	* @param id, img
	*/
	public Image(String id, Bitmap img)
	{
		this.id = id;
		this.img = img;
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
	* @return img
	*/
	public Bitmap getBitmap(){
		return img;
	}
	/**
	*
	* @param id
	*/
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	*
	* @param img
	*/	
	public void setBitmap(Bitmap img)
	{
		this.img = img;
	}

}
