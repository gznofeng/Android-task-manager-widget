package com.sftask.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	/**
	 * 取得当月的第一天0时0分0移
	 * 
	 * @param current
	 * @return
	 */
	public static Date getStartDayOfMonth(Date current) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(current);
		Calendar startCalendar = (Calendar) calendar.clone();
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		Date start = DateUtils.getStartOfDay(startCalendar.getTime());
		return start;
	}

	/**
	 * 取得当月的最后一天的23时59分59移
	 * 
	 * @param current
	 * @return
	 */
	public static Date getEndDayOfMonth(Date current) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(current);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Date end = DateUtils.getEndOfDay(calendar.getTime());
		return end;
	}

	/**
	 * 取得当天的最早一个0时0分0移
	 * 
	 * @param start
	 * @return
	 */
	public static Date getStartOfHour(Date start) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(start);
		int year = startCal.get(Calendar.YEAR);
		int month = startCal.get(Calendar.MONTH);
		int day = startCal.get(Calendar.DAY_OF_MONTH);
		int hour = startCal.get(Calendar.HOUR_OF_DAY);
		return new GregorianCalendar(year, month, day, hour, 0, 0).getTime();
	}

	/**
	 * 取得当天的最早一个0时0分0移
	 * 
	 * @param start
	 * @return
	 */
	public static Date getEndOfHour(Date end) {
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);
		int year = endCal.get(Calendar.YEAR);
		int month = endCal.get(Calendar.MONTH);
		int day = endCal.get(Calendar.DAY_OF_MONTH);
		int hour = endCal.get(Calendar.HOUR_OF_DAY);
		return new GregorianCalendar(year, month, day, hour, 59, 59).getTime();
	}

	/**
	 * 取得当天的最早一个0时0分0移
	 * 
	 * @param start
	 * @return
	 */
	public static Date getStartOfDay(Date start) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(start);
		int year = startCal.get(Calendar.YEAR);
		int month = startCal.get(Calendar.MONTH);
		int day = startCal.get(Calendar.DAY_OF_MONTH);
		return new GregorianCalendar(year, month, day, 0, 0, 0).getTime();
	}

	/**
	 * 取得当天的最后一个时间23时59分59移
	 * 
	 * @param end
	 * @return
	 */
	public static Date getEndOfDay(Date end) {
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);
		int year = endCal.get(Calendar.YEAR);
		int month = endCal.get(Calendar.MONTH);
		int day = endCal.get(Calendar.DAY_OF_MONTH);
		return new GregorianCalendar(year, month, day, 23, 59, 59).getTime();
	}

	public static boolean isTheSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
	}

	public static String SIMPLE_DEFAULT_FORMAT = "yyyy-MM-dd";
	public static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String format(Date date) {
		return format(date, DEFAULT_FORMAT);
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	public static Date toDate(String date){
		return toDate(date, SIMPLE_DEFAULT_FORMAT);
	}
	
	public static Date toDate(String date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date result=dateFormat.parse(date);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

	public static String secondFormat(int second) {
		String str = "";
		int hour = 0;
		int minute = 0;
		if (second >= 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		DecimalFormat df  = new DecimalFormat("00");
		return (df.format(hour) + ":" + df.format(minute)+ ":" +df.format(second));
	}
}
