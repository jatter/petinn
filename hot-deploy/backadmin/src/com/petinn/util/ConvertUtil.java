package com.petinn.util;


import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.ofbiz.base.util.UtilValidate;

public class ConvertUtil {
	
	/**
	 * 转换字符串->timestamp
	 * @param dateStr
	 * @return Timestamp
	 */
	public static Timestamp StringToTimeStamp(String dateStr) {
        Date  date = null;
        if(UtilValidate.isEmpty(dateStr)){
        	dateStr = "2000-01-01 00:00:00";
        }
        if (dateStr.length() == 10) {
        	dateStr = dateStr +" 00:00:00";
		}
        if (dateStr.length() == 16) {
        	dateStr = dateStr +":00";
		}
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        format.setLenient(false);  
        //要转换字符串 
        try {
        	date = new Date(format.parse(dateStr).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return new Timestamp(date.getTime());
	}

	/**
	 * 转换字符串->timestamp
	 * @param dateStr
	 * @return Timestamp
	 */
	public static Timestamp convertStringToTimeStamp(String dateStr) {
        Date  date = null;
        if(dateStr.equals("")){
        	dateStr = "1975-1-1 00:00:00";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        format.setLenient(false);  
        //要转换字符串 
        try {
        	date = new Date(format.parse(dateStr).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return new Timestamp(date.getTime());
	}
	public static Timestamp convertSubDateStrToTimeStamp(String dateStr) {
        Date  date = null;
        if(dateStr.equals("")){
        	dateStr = "1975-1-1 00:00:00";
        }else{
        	dateStr = dateStr +":00";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setLenient(false);
        //要转换字符串
        try {
        	date = new Date(format.parse(dateStr).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Timestamp(date.getTime());
	}

	public static Timestamp convertStringToTimeStampDay(String dateStr) {
        Date  date = null;
        if(dateStr.equals("")){
        	dateStr = "2000-01-01";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        //要转换字符串
        try {
        	date = new Date(format.parse(dateStr).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Timestamp(date.getTime());
	}

	public static String convertDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        format.setLenient(false);  
        //转换字符串 
		return format.format(date);
	}
	public static String convertTimestampToString(Timestamp date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        format.setLenient(false);  
        //转换字符串 
		return format.format(date);
	}
	public static String convertTimestampToStrdd(Timestamp t) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        format.setLenient(false);  
        //转换字符串 
		return format.format(t);
	}
	/**
	 * return format yyyy-MM-dd HH:mm
	 * @param date
	 * @return
	 */
	public static String convertTimestampToString1(Timestamp t) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
        format.setLenient(false);  
        //转换字符串 
		return format.format(t);
	}
	public static String convertDateToStringM(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");  
        format.setLenient(false);  
        //转换字符串 
		return format.format(date);
	}
	public static String convertDateToStringD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        format.setLenient(false);  
        //转换字符串 
		return format.format(date);
	}
	public static void main(String[] args) {
		System.out.println(StringToTimeStamp("2010-01-01"));
	}

}
