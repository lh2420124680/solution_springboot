package com.zlb.ecp.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DateCenterHelper {

	/**
	 * <p>
	 * Description: 获取前几天的日期
	 * <p>
	 * 
	 * @param predays
	 * @return String
	 * @date 2017年8月8日 下午2:56:05
	 */
	public static String preDate(Integer predays) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, predays);
		Date time = cal.getTime();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(time);
		return date;
	}
	
	/**
	 * <p>Description: 获取某一天的前几天或后几天<p>
	 * @param predays
	 * @return String
	 * @date 2017年8月16日 上午9:45:46
	 */
	public static String queryDatePreNext(String date,Integer days) {
		Calendar cal = Calendar.getInstance();
		Calendar cal1 = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date parse;
		String dates = "";
		try {
			parse = df.parse(date);
		cal1.setTime(parse);
		cal.add(cal1.DATE, days);
		Date time = cal.getTime();
		dates = new SimpleDateFormat("yyyy-MM-dd").format(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dates;
	}

	/**
	 * <p>
	 * Description: 取每隔多少小时的时间
	 * <p>
	 * 
	 * @param preHours
	 * @return String
	 * @date 2017年8月10日 下午2:10:23
	 */
	public static String preDateHour(Integer preHours) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, preHours);
		Date time = cal.getTime();
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time);
		return date;
	}

	/**
	 * <p>
	 * Description: 获取一天的时刻
	 * <p>
	 * 
	 * @param preHours
	 * @return String
	 * @date 2017年8月10日 下午2:19:16
	 */
	public static Map<Object, String> dateHourList() {
		Map<Object, String> map = new TreeMap<Object, String>(new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				return Integer.valueOf(arg0.toString()).compareTo(Integer.valueOf(arg1.toString()));
			}
		});
		String currDate = currDate("yyyy-MM-dd");
		for (int i = 0; i < 24; i++) {
			String pre = "";
			if (i < 10) {
				pre = "0" + i;
			} else {
				pre = i + "";
			}
			String date = currDate + " " + pre + ":" + "00" + "-" + pre + ":" + "59";
			map.put(i, date);
		}
		return map;
	}

	/**
	 * <p>
	 * Description: 获取前一周的日期
	 * <p>
	 * 
	 * @return Map<String,String>
	 * @date 2017年8月10日 下午3:37:47
	 */
	public static Map<Object, String> dateWeekList() {
		Map<Object, String> map = new TreeMap<Object, String>(new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				return Integer.valueOf(arg0.toString()).compareTo(Integer.valueOf(arg1.toString()));
			}
		});
		int j = 0;
		for (int i = 7; i > 0; i--) {
			j += 1;
			String preDate = preDate(-i);
			map.put(j , preDate);
		}
		return map;
	}

	/**
	 * <p>
	 * Description: 获取前一月的日期
	 * <p>
	 * 
	 * @return Map<String,String>
	 * @date 2017年8月10日 下午3:37:47
	 */
	public static Map<Object, String> dateMonthList() {
		Map<Object, String> map = new TreeMap<Object, String>(new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				return Integer.valueOf(arg0.toString()).compareTo(Integer.valueOf(arg1.toString()));
			}
		});
		int j = 0;
		for (int i = 30; i > 0; i--) {
			j += 1;
			String preDate = preDate(-i);
			map.put(j, preDate);
		}
		// 遍历集合
		return map;
	}
	
	/**
	 * <p>
	 * Description: 获取前半年的日期，以周为刻度
	 * <p>
	 * 
	 * @return Map<String,String>
	 * @date 2017年8月10日 下午3:37:47
	 */
	public static Map<Object, String> dateHalfYearList() {
		Map<Object, String> map = new TreeMap<Object, String>(new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				return Integer.valueOf(arg1.toString()).compareTo(Integer.valueOf(arg0.toString()));
			}
		});
		int j = 0;
		for (int i = 7; i < 183; i+=7) {
			j += 1;
			String preDate = preDate(-i);
			map.put(j, preDate);
		}
		// 遍历集合
		return map;
	}
	
	/**
	 * <p>
	 * Description: 获取前一年的日期，以周为刻度
	 * <p>
	 * 
	 * @return Map<String,String>
	 * @date 2017年8月10日 下午3:37:47
	 */
	public static Map<Object, String> dateYearList() { 
		Map<Object, String> map = new TreeMap<Object, String>(new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				return Integer.valueOf(arg1.toString()).compareTo(Integer.valueOf(arg0.toString()));
			}
		});
		int j = 0;
		for (int i = 7; i < 366; i+=7) {
			j += 1;
			String preDate = preDate(-i);
			map.put(j, preDate);
		}
		// 遍历集合
		return map;
	}

	public static void main(String[] args) { // 52 26
		System.out.println(dateMonthList());
		System.out.println(queryDatePreNext("2017-08-09",8));
	}

	/**
	 * <p>
	 * Description: 获取当前的日期
	 * <p>
	 * 
	 * @param simpleDateFormat
	 * @return String
	 * @date 2017年8月8日 下午2:55:56
	 */
	public static String currDate(String simpleDateFormat) {
		Calendar cd = Calendar.getInstance();
		Date currdate = cd.getTime();
		String currDataTable = new SimpleDateFormat(simpleDateFormat).format(currdate);
		return currDataTable;
	}

}
