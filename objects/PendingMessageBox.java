package com.RUFit.android.objects;

import com.RUFit.android.Brain;
import com.RUFit.android.activities.Convo_Screen;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
*
*
*/
public class PendingMessageBox {

	private ImageView image_profilepic;
	private TextView text_name;
	private String otherUser_id;
	private LinearLayout wrapper;
	private ProgressBar progress;
	/**
	*
	*
	*/
	public PendingMessageBox(ProgressBar progress, LinearLayout wrapper, String otherUser_id, ImageView image_profilepic, TextView text_name) {
		this.progress = progress;
		this.wrapper = wrapper;
		this.otherUser_id = otherUser_id;
		this.image_profilepic = image_profilepic;
		this.text_name = text_name;
	}
	/**
	*
	* 
	*/	
	public void update()
	{

	}
	/**
	*
	* @return image_profilepic
	*/
	public ImageView getImage_profilepic() {
		return image_profilepic;
	}
	/**
	*
	* @param image_profilepic
	*/
	public void setImage_profilepic(ImageView image_profilepic) {
		this.image_profilepic = image_profilepic;
	}
	/**
	*
	* @return text_name
	*/
	public TextView getText_name() {
		return text_name;
	}
	/**
	*
	* @param text_name
	*/
	public void setText_name(TextView text_name) {
		this.text_name = text_name;
	}
	/**
	*
	* @return otherUser_id
	*/
	public String getOtherUser_id() {
		return otherUser_id;
	}
	/**
	*
	* @param otherUser_id
	*/
	public void setOtherUser_id(String otherUser_id) {
		this.otherUser_id = otherUser_id;
	}
	/**
	*
	* @return Wrapper
	*/
	public LinearLayout getWrapper() {
		return wrapper;
	}
	/**
	*
	* @return progress
	*/
	public ProgressBar getProgress() {
		return progress;
	}
	/**
	*
	* @param wrapper
	*/
	public void setWrapper(LinearLayout wrapper) {
		this.wrapper = wrapper;
	}
}
