package asia.chiase.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.exception.CCException;

/**
 * <strong>CCDateUtil</strong><br>
 * <br>
 * <ul>
 * <li>isXXX : return boolean
 * <li>makeXXX : return Date
 * <li>getXXX : return int
 * <li>convertXXX : return String
 * </ul>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCDateUtil{

	/**
	 * <strong>isDate</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isDate(String data){
		if(!CCStringUtil.isEmpty(data)){
			if(isDate(data, CCFormatUtil.SDF_DATE_SLS)){
				return true;
			}
			if(isDate(data, CCFormatUtil.SDF_DATE_HYP)){
				return true;
			}
		}
		return false;
	}

	/**
	 * <strong>isDate</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @param fmt
	 * @return
	 */
	public static boolean isDate(String data, SimpleDateFormat fmt){
		try{
			if(CCStringUtil.isEmpty(data) || fmt == null){
				return false;
			}else{
				fmt.parse(data);
				return true;
			}
		}catch(Exception ex){
			new CCException(ex);
			return false;
		}
	}

	/**
	 * <strong>isTime</strong><br>
	 * <br>
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isTime(String target){
		try{
			if(CCStringUtil.isEmpty(target)){
				return false;
			}else{
				String[] time = target.split(":");
				if(time.length != 2){
					return false;
				}
				Integer.parseInt(time[0]);
				Integer.parseInt(time[1]);
				return true;
			}
		}catch(Exception ex){
			new CCException(ex);
			return false;
		}
	}

	/**
	 * <strong>isToday</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date){
		Calendar today = Calendar.getInstance();
		return compareDate(date, today.getTime(), false) == 0;
	}

	/**
	 * <strong>isWeekend</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWeekend(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		switch(cal.get(Calendar.DAY_OF_WEEK)){
		case Calendar.SUNDAY:
			return true;
		case Calendar.SATURDAY:
			return true;
		default:
			return false;
		}
	}

	/**
	 * <strong>isPast</strong><br>
	 * <br>
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static boolean isPast(Integer year, Integer month){

		Calendar now = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);

		int diff = now.getTime().compareTo(cal.getTime());
		if(diff > 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * <strong>isSametime</strong><br>
	 * <br>
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSametime(Date date1, Date date2){

		String d1 = null;
		String d2 = null;
		if(date1 != null){
			d1 = CCFormatUtil.SDF_TIME_CLN.format(date1);
		}
		if(date2 != null){
			d2 = CCFormatUtil.SDF_TIME_CLN.format(date2);
		}
		if(d1 == null ? d2 == null : d1.equals(d2)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * <strong>isRange</strong><br>
	 * <br>
	 * 
	 * @param target
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean isRange(Date target, Date start, Date end){

		if(target == null) return false;
		if(start == null) return false;
		if(end == null) return false;

		if(target.compareTo(start) >= 0 && target.compareTo(end) <= 0){
			return true;
		}else{
			return false;
		}
	}

	// public static boolean isRangeMonth(Integer year, Integer month, Date start, Date end){
	//
	// Boolean startCheck = true;
	// Boolean endCheck = true;
	//
	// if(start != null){
	// Integer startYear = getYear(start);
	// Integer startMonth = getMonth(start);
	//
	// if(year <= startYear && month < startMonth){
	// startCheck = false;
	// }
	// }
	//
	// if(end != null){
	// Integer endYear = getYear(end);
	// Integer endMonth = getMonth(end);
	// if(year > endYear || month > endMonth){
	// endCheck = false;
	// }
	// }
	//
	// if(startCheck && endCheck){
	// return true;
	// }else{
	// return false;
	// }
	// }

	/**
	 * <strong>makeCalendar</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar makeCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(date);
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
		return cal;
	}

	/**
	 * <strong>makeCalendar</strong><br>
	 * <br>
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Calendar makeCalendar(Integer year, Integer month, Integer day){
		Calendar cal = Calendar.getInstance();
		try{
			cal.set(year, month - 1, day);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
		return cal;
	}

	public static Calendar makeCalendarToday(){
		Calendar cal = Calendar.getInstance();
		try{
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
		return cal;
	}

	/**
	 * <strong>makeDate</strong><br>
	 * <br>
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date makeDate(Date dateTime){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateTime);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>makeDate</strong><br>
	 * <br>
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date makeDate(Integer year, Integer month, Integer day){
		try{
			Calendar cal = makeCalendar(year, month, day);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>makeDate</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static Date makeDate(String date){
		return makeDateCustom(date, "yyyy/MM/dd");
	}

	public static Date makeDateWithFirstday(Date date){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DATE, 1);
			return cal.getTime();
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}

	}

	/**
	 * <strong>makeDateWithLastday</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static Date makeDateWithLastday(Date date){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DATE, getLastDay(date));
			return cal.getTime();
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>makeDateWithLastday</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static Date makeDateWithLastday(String date){
		try{
			Date tmp = makeDateCustom(date, "yyyy/MM/dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(tmp);
			cal.set(Calendar.DATE, getLastDay(tmp));
			return cal.getTime();
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>makeDateWithoutDay</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static Date makeDateWithoutDay(String date){
		return makeDateCustom(date + "/01", "yyyy/MM/dd");
	}

	/**
	 * <strong>makeDateTime</strong><br>
	 * <br>
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param time
	 * @return
	 */
	public static Date makeDateTime(Integer year, Integer month, Integer day, String time){

		Calendar cal = Calendar.getInstance();
		try{
			if(CCStringUtil.isEmpty(time)){
				return null;
			}

			String[] hm = time.split(":");

			cal.set(year, month - 1, day, Integer.parseInt(hm[0]), Integer.parseInt(hm[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}

		return cal.getTime();
	}

	/**
	 * <strong>makeDateTime</strong><br>
	 * <br>
	 * 
	 * @param day
	 * @param time
	 * @return
	 */
	public static Date makeDateTime(Date day, String time){

		Calendar cal = Calendar.getInstance();
		try{
			if(CCStringUtil.isEmpty(time)){
				// return null;
				time = "00:00";
			}

			String[] hm = time.split(":");

			if(day != null){
				cal.setTime(day);
			}

			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hm[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(hm[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}

		return cal.getTime();
	}

	/**
	 * <strong>makeDateCustom</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date makeDateCustom(String date, String format){
		try{
			if(CCStringUtil.isEmpty(date)){
				return null;
			}

			SimpleDateFormat sdf1 = new SimpleDateFormat(format);
			return sdf1.parse(date);
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>makeDateCustom</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param sdf
	 * @return
	 */
	public static Date makeDateCustom(String date, SimpleDateFormat sdf){
		try{
			if(CCStringUtil.isEmpty(date)){
				return null;
			}

			return sdf.parse(date);
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>makeDateList</strong><br>
	 * <br>
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> makeDateList(Date start, Date end){

		List<Date> list = new ArrayList<Date>();

		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(start);
		endCal.setTime(end);

		while(startCal.before(endCal) || startCal.equals(endCal)){
			list.add(startCal.getTime());
			startCal.add(Calendar.DATE, 1);
		}
		return list;
	}

	/**
	 * <strong>makeMonthList</strong><br>
	 * <br>
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> makeMonthList(Date start, Date end){

		List<Date> list = new ArrayList<Date>();

		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(start);
		endCal.setTime(end);

		while(startCal.before(endCal) || startCal.equals(endCal)){
			list.add(startCal.getTime());
			startCal.add(Calendar.MONTH, 1);
		}
		return list;
	}

	public static Map<String, String> makeDateMap(Date start, Date end){

		Map<String, String> result = new LinkedHashMap<String, String>();

		for(Date date : makeDateList(start, end)){
			result.put(CCStringUtil.toString(getDay(date)), CCStringUtil.toString(getDay(date)));
		}

		return result;

	}

	/**
	 * <strong>getYear</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(date);
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
		return cal.get(Calendar.YEAR);
	}

	/**
	 * <strong>getMonth</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(date);
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * <strong>getYear</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(String date){
		try{
			if(isDate(date)){
				return Integer.valueOf(date.substring(0, 4));
			}else{
				return 0;
			}
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
	}

	/**
	 * <strong>getYearWithoutDay</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static int getYearWithoutDay(String data){
		return getYear(data + "/01");
	}

	/**
	 * <strong>getMonth</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(String date){
		try{
			if(isDate(date)){
				return Integer.valueOf(date.substring(5, 7));
			}else{
				return 0;
			}
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
	}

	/**
	 * <strong>getMonthWithoutDay</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static int getMonthWithoutDay(String data){
		return getMonth(data + "/01");
	}

	/**
	 * <strong>getDay</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(String date){
		try{
			if(isDate(date)){
				return Integer.valueOf(date.substring(8, 10));
			}else{
				return 0;
			}
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
	}

	/**
	 * <strong>getDay</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(date);
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
		return cal.get(Calendar.DATE);
	}

	/**
	 * <strong>getLastDay</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getLastDay(Date date){
		int last;
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(date);
			last = cal.getActualMaximum(Calendar.DATE);
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
		return last;
	}

	/**
	 * <strong>getHour</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getHour(Date date){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(date);
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * <strong>getMin</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getMin(Date date){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(date);
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * <strong>getSec</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int getSec(Date date){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(date);
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
		return cal.get(Calendar.SECOND);
	}

	/**
	 * <strong>convertDate2Min</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @return
	 */
	public static int convertDate2Min(Date date){
		try{
			if(date != null){
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				return c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
			}else
				return 0;
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}

	}

	/**
	 * <strong>convertMin2Time</strong><br>
	 * <br>
	 * 
	 * @param minuts
	 * @return
	 */
	public static String convertMin2Time(Integer minuts){
		try{
			if(minuts == null || minuts == 0){
				return "";
			}else{

				boolean minus = false;
				if(minuts < 0){
					minus = true;
				}

				int hour = Math.abs(minuts) / 60;
				int min = Math.abs(minuts) % 60;

				if(minus){
					return "-" + String.format("%1$02d", hour) + ":" + String.format("%1$02d", min);
				}else{
					return String.format("%1$02d", hour) + ":" + String.format("%1$02d", min);
				}
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>convertMin2Time2</strong><br>
	 * <br>
	 * 
	 * @param minuts
	 * @return
	 */
	public static float convertMin2Time2(Integer minuts){
		try{
			if(minuts == null || minuts == 0){
				return 0;
			}else{
				return (float)(minuts / 60.0);
			}
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
	}

	/**
	 * <strong>convertMin2Time</strong><br>
	 * <br>
	 * 
	 * @param object
	 * @return
	 */
	public static String convertMin2Time(Object object){
		try{
			if(object == null){
				return "";
			}else{
				return convertMin2Time(Integer.parseInt(object.toString()));
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>convertTime2Min</strong><br>
	 * <br>
	 * 
	 * @param time
	 * @return
	 */
	public static Integer convertTime2Min(String time){
		try{
			if(CCStringUtil.isEmpty(time)){
				return 0;
			}else{
				String[] hm = time.trim().split(":");
				Calendar cal = Calendar.getInstance();
				cal.set(1970, 1, 1, Integer.parseInt(hm[0]), Integer.parseInt(hm[1]));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				return convertDate2Min(cal.getTime());
			}
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
	}

	/**
	 * <strong>convertHour2Min</strong><br>
	 * <br>
	 * 
	 * @param time
	 * @return
	 */
	public static Integer convertHour2Min(String time){
		try{
			if(CCStringUtil.isEmpty(time)){
				return 0;
			}else{
				return Integer.valueOf(time) * 60;
			}
		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
	}

	/**
	 * <strong>convertTimeZone</strong><br>
	 * <br>
	 * 
	 * @param dateTime
	 * @param timeZone
	 * @return
	 */
	public static Calendar convertTimeZone(Date dateTime, String timeZone){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);

		TimeZone tz2 = TimeZone.getTimeZone(timeZone);
		calendar.setTimeZone(tz2);
		calendar.set(Calendar.ZONE_OFFSET, tz2.getRawOffset());

		return calendar;
	}

	/**
	 * <strong>convertTimeZoneDate</strong><br>
	 * <br>
	 * 
	 * @param target
	 * @param timezone
	 * @return
	 */
	public static Date convertTimeZoneDate(Calendar target, String timezone){

		if(target == null){
			return null;
		}
		// instantiates a calendar using the current time in the specified timezone
		Calendar sourceCal = Calendar.getInstance(target.getTimeZone());
		sourceCal.setTime(target.getTime());
		// change the timezone
		sourceCal.setTimeZone(TimeZone.getTimeZone(timezone));
		// get the current hour of the day in the new timezone
		DateFormat format = DateFormat.getDateTimeInstance();
		format.setTimeZone(TimeZone.getTimeZone(timezone));
		String targetString = format.format(sourceCal.getTime());

		return convertStringToDate(targetString, CCFormatUtil.COMMON_DATE_TIME_FORMAT_12H);
	}

	public static Date convertStringToDate(String date, String fomatDate){
		SimpleDateFormat formatter = new SimpleDateFormat(fomatDate);
		try{
			return formatter.parse(date);
		}catch(ParseException e){
			return null;
		}
	}

	/**
	 * <strong>offsetDate</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param offsetDay - number of offset day
	 * @return
	 */
	public static Date offsetDate(Date date, int offsetDay){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, offsetDay);
		return cal.getTime();
	}

	/**
	 * <strong>offsetMonth</strong><br>
	 * <br>
	 * Get offset month from the given date
	 * 
	 * @param date
	 * @param offsetMonth
	 * @return
	 */
	public static Date offsetMonth(Date date, int offsetMonth){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, offsetMonth);
		return cal.getTime();
	}

	/**
	 * <strong>offsetHour</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param offsetHour
	 * @return
	 */
	public static Date offsetHour(Date date, int offsetHour){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, offsetHour);
		return cal.getTime();
	}

	/**
	 * <strong>equalsToDate</strong><br>
	 * <br>
	 * 
	 * @param date1
	 * @param date2
	 * @param checkTime
	 * @return
	 */
	public static boolean equalsToDate(Date date1, Date date2, boolean checkTime){
		return compareDate(date1, date2, checkTime) == 0;
	}

	/**
	 * <strong>compareDate</strong><br>
	 * <br>
	 * 
	 * @param date1
	 * @param date2
	 * @param checkTime
	 * @return
	 */
	public static int compareDate(Date date1, Date date2, boolean checkTime){
		if(!checkTime){
			date1 = CCDateUtil.makeDate(date1);
			date2 = CCDateUtil.makeDate(date2);
		}
		return date1.compareTo(date2);
	}

	/**
	 * <strong>diffDate</strong><br>
	 * <br>
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffDate(Date date1, Date date2){
		// TODO This is not correct when date1 = date2, when date1=date2-> return 1-> not correct
		int dayDiff = (int)((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
		return dayDiff + 1;
	}

	/**
	 * <strong>diffMinutes</strong><br>
	 * <br>
	 * 
	 * @param date1
	 * @param date2
	 * @param timeUnit
	 * @return
	 */
	public static long diffMinutes(Date date1, Date date2, TimeUnit timeUnit){
		long diffInMillies = date1.getTime() - date2.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	/**
	 * <strong>makeDateTime</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Date makeDateTime(Date date, int hour, int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);

		return calendar.getTime();
	}

	/**
	 * <strong>makeDateTime</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date makeDateTime(Date date, int hour, int minute, int second){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);

		return calendar.getTime();
	}

	/**
	 * <strong>getHourFromTimeString</strong><br>
	 * <br>
	 * Extract hour part from time string with format HH:MM
	 * 
	 * @param timeString
	 * @return
	 */
	public static Integer getHourFromTimeString(String timeString){
		if(!CCDateUtil.timeStringIsValid(timeString)){
			return null;
		}

		try{
			String hourStr;
			Integer hour = null;

			StringTokenizer tokenizer = new StringTokenizer(timeString, ":");
			if(tokenizer.hasMoreTokens()){
				hourStr = tokenizer.nextToken();
				hour = Integer.parseInt(hourStr);
			}

			return hour;
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>getMinuteFromTimeString</strong><br>
	 * <br>
	 * Extracts minute part from time string with format HH:MM
	 * 
	 * @param timeString
	 * @return
	 */
	public static Integer getMinuteFromTimeString(String timeString){
		if(!CCDateUtil.timeStringIsValid(timeString)){
			return null;
		}

		try{
			String minuteStr;
			Integer minute = null;

			StringTokenizer tokenizer = new StringTokenizer(timeString, ":");
			tokenizer.nextToken();
			minuteStr = tokenizer.nextToken();

			minute = Integer.parseInt(minuteStr);
			return minute;
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>timeIsOverlap</strong><br>
	 * <br>
	 * Check if two time intervals overlaps
	 * 
	 * @param startDate1
	 * @param endDate1
	 * @param startDate2
	 * @param endDate2
	 * @return
	 */
	public static boolean timeIsOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2){
		Date endOfMin, maxStart;
		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();

		cal.setTime(startDate1);
		cal.set(0, 0, 0);
		cal1.setTime(startDate2);
		cal1.set(0, 0, 0);
		int res = compareDate(cal.getTime(), cal1.getTime(), true);
		if(res == 0){
			return true;
		}else if(res < 0){
			// minStart = startDate1;
			endOfMin = endDate1;
			maxStart = startDate2;
		}else{
			// minStart = startDate2;
			endOfMin = endDate2;
			maxStart = startDate1;
		}
		cal.setTime(endOfMin);
		cal.set(0, 0, 0);
		cal1.setTime(maxStart);
		cal1.set(0, 0, 0);
		if(compareDate(cal.getTime(), cal1.getTime(), true) > 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * <strong>timeStringIsValid</strong><br>
	 * <br>
	 * Check if time string format is valid. The valid form is HH:MM, or H:MM, or HH:M
	 * 
	 * @param timeString
	 * @return
	 */
	public static boolean timeStringIsValid(String timeString){
		String timeFormat = "^([01]?\\d|2[0-3]):([0-5]?\\d)$";
		return timeString.matches(timeFormat);
	}

	/**
	 * <strong>makeCalendar</strong><br>
	 * <br>
	 * 
	 * @param dateValue
	 * @return
	 */
	public static Calendar makeCalendar(long dateValue){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateValue);

		return calendar;
	}

	/**
	 * <strong>isTime4Interval</strong><br>
	 * <br>
	 * 
	 * @param time
	 * @param interval
	 * @return
	 */
	public static boolean isTime4Interval(String time, Integer interval){
		if(interval == null || CCConst.NONE.equals(CCStringUtil.toString(interval))){
			return false;
		}
		if(CCDateUtil.convertTime2Min(time) % interval == 0){
			return true;
		}
		return false;
	}

}