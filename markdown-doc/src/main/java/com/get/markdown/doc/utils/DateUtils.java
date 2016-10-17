package com.get.markdown.doc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关于时间的处理公共类
 * 
 * @author wangrui
 */
public class DateUtils {

	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
	
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Date转换成String
	 * 
	 * @param format
	 *            日期模板
	 * @param date
	 *            需转换的时间
	 * @return
	 */
	public static String getDateTimeString(String format, Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(date);
	}

	/**
	 * 获取当天日期（传入显示格式 ）
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateTimeString(String format) {
		Date date = new Date();
		return getDateTimeString(format, date);
	}

	/**
	 * 获取日期的默认时间格式String
	 * 
	 * @param date
	 *            需要转换的日期
	 * @return
	 */
	public static String getDateTimeString(Date date) {
		return getDateTimeString(DEFAULT_FORMAT, date);
	}
	
	/**
	 * 获取当前日期的默认时间格式String
	 * @return
	 */
	public static String getDateTimeString() {
		Date date = new Date();
		return getDateTimeString(DEFAULT_FORMAT, date);
	}
	
	/**
	 * 修改时间
	 * @param targetDate
	 * @param field Calendar.HOUR_OF_DAY、Calendar.SECOND等
	 * @param amount 修改的值
	 * @return
	 */
	public static Date operateDate(Date targetDate, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(targetDate);
		cal.add(field, amount);
		return cal.getTime();
	}
	
	/**
	 * 比较时间
	 * @param targetDate
	 * @param field Calendar.MILLISECOND、Calendar.SECOND、Calendar.MINUTE、Calendar.HOUR、Calendar.HOUR_OF_DAY
	 * @return
	 */
	public static long compareDate(Date date1, Date date2, int field) {
		long compareValue = date1.getTime() - date2.getTime();
		switch (field) {
			case Calendar.MILLISECOND: break;
			case Calendar.SECOND: compareValue = compareValue/1000;break;
			case Calendar.MINUTE: compareValue = compareValue/(1000*60);break;
			case Calendar.HOUR: compareValue = compareValue/(1000*60*60);break;
			case Calendar.HOUR_OF_DAY: compareValue = compareValue/(1000*60*60);break;
			default:break;
		}
		return compareValue;
	}

	/**
	 * 字符串转日期
	 * @param dateStr   日期字符串
	 * @param format    转换格式
	 * @return  日期时间
	 * @throws java.text.ParseException
	 */
	public static Date parse(String dateStr, String format) {
        String formatTemp = format;
        if(formatTemp == null || "".equals(formatTemp)){
        	formatTemp = DEFAULT_FORMAT;
        }
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatTemp);
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			logger.error("", e);
			return null;
		}
	}

    /**
     * 字符串转日期，自动适配字符串格式
     * yyyy-MM-dd HH:mm:ss
     * @param dateStr 日期字符串
     * @return
     * @throws java.text.ParseException
     */
    public static Date parse (String dateStr) throws ParseException {
        return parse(dateStr, DEFAULT_FORMAT);
    }
    
    /**
     * 时间后移
     * <p>
     * unit可取值<code>Calendar.YEAR</code>或<code>Calendar.DAT_OF_YEAR</code>
     * @param opDate 操作时间
     * @param unit 单位
     * @param off 偏移量
     * @return 操作后的日期
     */
    public static Date add(Date opDate, int unit, int off){
    	
    	DateTime dateTime = new DateTime(opDate);
    	Date result = null;
    	if (unit == Calendar.YEAR){
    		dateTime = dateTime.plusYears(off);
    	}else if (unit == Calendar.DAY_OF_YEAR) {
			dateTime = dateTime.plusDays(off);
		}
    	
    	result = dateTime.toDate();
    	return result;
    }
    
    /**
     * 一天中的第一秒.
     * @param opDate
     * @return
     */
    public static Date getDayOfFirstS(Date opDate){
    	
    	DateTime dateTime = new DateTime(opDate);
    	Date resultDate = null;
    	dateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 0, 0);
    	resultDate = dateTime.toDate();
    	
    	return resultDate;
    }
    
    /**
     * 一天中的最后一秒.
     * @param opDate
     * @return
     */
    public static Date getDayOfLastS(Date opDate){
    	
    	DateTime dateTime = new DateTime(opDate);
    	Date resultDate = null;
    	dateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 23, 59, 59);
    	resultDate = dateTime.toDate();
    	
    	return resultDate;
    }

    /**
     * 获取时间差
     * @param format
     * @param startTime
     * @param endTime
     * @return 分钟时间差
     */
	public static long getTimeDifferencce(String format, Date startTime,
			Date endTime) {
		long min = 0;
		try {
			long diff = endTime.getTime() - startTime.getTime();
			min = diff / (1000 * 60);
		} catch (Exception e) {
			logger.warn("getTimeDifference ");
		}
		return min;
	}
	
	/**
	 * 获取当前日期是星期几
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static int getWeekOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week < 0)
			week = 0;
		if(week == 0)
			week = 7;
		return week;
	}

	public static void main(String[] args) {
		Date nowTime = new Date();
		System.err.println(getDateTimeString(nowTime));
		System.err.println(getDateTimeString(add(nowTime, Calendar.DAY_OF_YEAR, 2)));
		
		System.err.println(getDateTimeString(getDayOfFirstS(nowTime)));
		System.err.println(getDateTimeString(getDayOfLastS(nowTime)));
		
		try {
			System.err.println(getWeekOfDate(parse("2015-12-27 00:00:00")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		int f = 0;
//		System.out.println(DateUtils.getDateTimeString(new Date()));
//		for(int i =0;i<100;i++){
//			f++;
//			for(int j = 0; j<100;j++){
//				f++;
//				for(int z =0; z<100;z++){
//					f++;
//				}
//			}
//		}
//		System.out.println(f);
//		System.out.println(DateUtils.getDateTimeString(new Date()));
//		System.out.println("==========================");
//		
//		f=0;
//		System.out.println(DateUtils.getDateTimeString(new Date()));
//		for(int i = 0; i< 100 ;i++){
//			f++;
//			for(int j =0;j<100;j++){
//				f++;
//			}
//		}
//		for(int j=0;j<100;j++){
//			f++;
//			for(int z=0;z<100;z++){
//				f++;
//			}
//		}
//		System.out.println(f);
//		System.out.println(DateUtils.getDateTimeString(new Date()));
		
	}
}
