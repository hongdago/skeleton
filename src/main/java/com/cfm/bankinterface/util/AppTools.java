package com.cfm.bankinterface.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppTools {
	
	
	
	public static String getDataTimeStr(String pattern) {
		return getDataTimeStr(pattern, new Date());
	}

	public static String getDataTimeStr(String pattern, Date date) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

}
