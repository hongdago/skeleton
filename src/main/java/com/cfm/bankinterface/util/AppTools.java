package com.cfm.bankinterface.util;

import java.lang.reflect.Field;
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

	public static String getValue(Object obj , String fileName) {
		if(obj != null){
			Class clazz =obj.getClass();
			while(clazz !=null){
				try{
					Field field = clazz.getDeclaredField(fileName);
					field.setAccessible(true);
					return field.get(obj).toString();
				}catch(Exception e){

				}
				clazz =clazz.getSuperclass();
			}
		}
		return "";
	}
}
