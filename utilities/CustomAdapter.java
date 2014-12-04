package com.RUFit.android.utilities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final int resourceID;

	public CustomAdapter(Context context, int resource, ArrayList<String> bah) {
		super(context, resource, bah);

		this.context = context;
		this.resourceID = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(resourceID, parent, false);

		return rowView;
	}
}