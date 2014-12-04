package com.RUFit.android.utilities;

import java.util.Comparator;

import com.RUFit.android.objects.Message;

public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message m1, Message m2) {
    	int m1_id = Integer.parseInt(m1.getId());
    	int m2_id = Integer.parseInt(m2.getId());
    	
    	if (m1_id > m2_id)
    		return 1;
    	return 0;
    }
}