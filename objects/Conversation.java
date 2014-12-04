package com.RUFit.android.objects;

/**
*
*/
public class Conversation {
	String topMessageId;     //Allows us to know how far back messages were loaded
	String bottomMessageId;  //Allows us to know how far forward messages were loaded
	String otherUserId;	  //The other user for this conversation
	
	/**
	*
	* @return topMessageId
	*/
	public String getTopMessageId() {
		return topMessageId;
	}
	/**
	* @param topMessageId
	*/
	public void setTopMessageId(String topMessageId) {
		this.topMessageId = topMessageId;
	}
	/**
	*
	* @return bottomMessageId
	*/
	public String getBottomMessageId() {
		return bottomMessageId;
	}
	/**
	* @param bottomMessageId
	*/
	public void setBottomMessageId(String bottomMessageId) {
		this.bottomMessageId = bottomMessageId;
	}
	/**
	*
	* @return otherUserId
	*/
	public String getOtherUserId() {
		return otherUserId;
	}
	/**
	*
	* @param otherUserId
	*/
	public void setOtherUserId(String otherUserId) {
		this.otherUserId = otherUserId;
	}
	/**
	*
	* @class Conversation
	* @param otherUserId
	*/
	public Conversation(String otherUserId) {
		super();
		this.topMessageId = "0";
		this.bottomMessageId = "0";
		this.otherUserId = otherUserId;
	}
}
