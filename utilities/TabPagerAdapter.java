package com.RUFit.android.utilities;


import com.RUFit.android.activities.Calendar_Fragment;
import com.RUFit.android.activities.Feed_Fragment;
import com.RUFit.android.activities.Messages_Fragment;
import com.RUFit.android.activities.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

	/**
	*
	* @param fm
	*/
	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub

	}
	/**
	*
	* @param email_
	*/
	public void setEmail(String email_)
	{

	}
	/**
	*
	* @param i
	* @return fragment, new Calendar_Fragment(), new Messages_Fragment(), new Settings()
	*/
	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			//Fragement for Android Tab
			Fragment fragment = new Feed_Fragment();
			Bundle bundle = new Bundle();
			fragment.setArguments(bundle);

			return fragment;

		case 1:
			//Fragment for Ios Tab
			return new Calendar_Fragment();
		case 2:
			//Fragment for Windows Tab
			return new Messages_Fragment();
		case 3: 
			return new Settings();
		}
		return null;

	}
	/**
	*
	* return 4
	*/
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4; //No of Tabs
	}


}