package com.RUFit.android.objects;

import com.RUFit.android.Brain;
import com.RUFit.android.listeners.OnImageDownloaded;
import com.RUFit.android.utilities.DownloadImageTask;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;

/*
 * The purpose of this class is because when the Feed Fragment loads all the post, if each post requested to download an Image
 * we would end up downloading the same image multiple times (if there were multiple posts by the same person).
 * This task will hold all the information needed to download an image & place it on the GUI without actually going into the
 * process of downloading it. Once we have all of the Feed Posts done loading we can look through a list of queuedDownloadImageTask Objects
 * and only download one of each Image but still know what ImageView we need to update.
 */
public class queuedDownloadImageTask {

	private ImageView imgView;
	private ProgressBar progress;
	private String userId;
	private String picURL;
	private OnImageDownloaded listener;

	/**
	*
	* @param imgView, progress, userId, picURL, listener 
	*/
	public queuedDownloadImageTask(ImageView imgView, ProgressBar progress, String userId, String picURL, OnImageDownloaded listener) {
		super();
		this.imgView = imgView;
		this.progress = progress;
		this.userId = userId;
		this.picURL = picURL;
		this.listener = listener;
	}
	/**
	*
	* @return userId
	*/	
	public String getUserId()
	{
		return userId;
	}
	
	/*
	 * downloadImage will actually fetch the image from the internet, download it & update the correct
	 * ImageView on the GUI.
	 */
	public void downloadImage()
	{
		Log.v("Notice","Downloading image for user " + userId);
		new DownloadImageTask(imgView, progress, userId, listener)
		.execute(Brain.IMAGE_DIR + picURL);
	}
	
	/*
	 * updateImage can be called when we know for certain this image was already downloaded. It will update the GUI with the image.
	 */
	public void updateImage()
	{
		Image img = Brain.getImageById(userId);
		if (img != null)
		{
    	progress.setVisibility(View.GONE);
        imgView.setImageBitmap(img.getBitmap());
        imgView.setScaleType(ScaleType.CENTER_CROP);
		}
		else
		{
			Log.v("ERROR","You called updateImage() but the image wasn't loaded into the Brain.");
		}
	}
}
