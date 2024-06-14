package com.myapp.utils;

import java.util.Calendar;

public class DateUtil {

	public static long currentTimeStamp() {
		return Calendar.getInstance().getTimeInMillis();
	}

}
