package asia.chiase.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import asia.chiase.core.exception.CCException;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

/**
 * <strong>CCTimezoneUtil</strong><br>
 * <br>
 *
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCTimezoneUtil{

	static Map<Integer, String> TIMEZONES = new HashMap<Integer, String>();

	static{
		int MINUTES_IN_MILISECONDS = 1000 * 60;
		for(String zone : TimeZone.getAvailableIDs()){
			TimeZone timezone = TimeZone.getTimeZone(zone);
			String tzName = timezone.getID();
			if(tzName.contains("GMT")){
				TIMEZONES.put(timezone.getRawOffset() / MINUTES_IN_MILISECONDS, tzName);
			}
		}
	}

	/**
	 * <strong>offsetToTimezoneName</strong><br>
	 * <br>
	 *
	 * @param offset
	 * @return
	 */
	public static String offsetToTimezoneName(Integer offset){
		if(TIMEZONES.containsKey(offset)){
			return TIMEZONES.get(offset);
		}else{
			return TimeZone.getDefault().getDisplayName();
		}
	}

	/**
	 * <strong>getUtc</strong><br>
	 * <br>
	 *
	 * @return
	 */
	public static Date getUtc(){

		LocalDateTime local = new LocalDateTime(DateTimeZone.UTC);
		Date now = local.toDate();
		return now;
	}

	/**
	 * <strong>formatDate</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	public static String formatDate(Date date, TimeZone from, TimeZone to){

		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			format.setTimeZone(from);

			String tmp = CCFormatUtil.formatDateTimeSec(date);
			Date baseDate = format.parse(tmp);

			SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
			format2.setTimeZone(to);

			return format2.format(baseDate);
		}catch(ParseException ex){
			new CCException(ex);
			return "";
		}

	}

	/**
	 * <strong>formatTime</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	public static String formatTime(Date date, TimeZone from, TimeZone to){

		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			format.setTimeZone(from);

			String tmp = CCFormatUtil.formatDateTimeSec(date);
			Date baseDate = format.parse(tmp);

			SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
			format2.setTimeZone(to);

			return format2.format(baseDate);
		}catch(ParseException ex){
			new CCException(ex);
			return "";
		}

	}

	/**
	 * <strong>formatDateTime</strong><br>
	 * <br>
	 *
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	public static String formatDateTime(Date date, TimeZone from, TimeZone to){

		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			format.setTimeZone(from);

			String tmp = CCFormatUtil.formatDateTimeSec(date);
			Date baseDate = format.parse(tmp);

			SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			format2.setTimeZone(to);

			return format2.format(baseDate);
		}catch(ParseException ex){
			new CCException(ex);
			return "";
		}

	}

	/**
	 * <strong>convertDate</strong><br>
	 * <br>
	 * 
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	public static Date convertDate(Date date, TimeZone from, TimeZone to){

		try{

			if(date == null) return null;

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			format.setTimeZone(from);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			// int dbOffset = TimeZone.getDefault().getOffset(cal.getTimeInMillis());
			// int screenOffset = to.getRawOffset();

			// logger.info("[Timezone convert] Session timezone : " + cal);

			// if(dbOffset == screenOffset){
			// logger.info("[Timezone convert](NO) date : " + date);
			// return date;
			// }else{
			String tmp = CCFormatUtil.formatDateTimeSec(date);
			Date baseDate = format.parse(tmp);

			SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			format2.setTimeZone(to);

			String retString = format2.format(baseDate);

			Date retDate = CCDateUtil.makeDateCustom(retString, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
			// logger.info("[Timezone convert](YES) from : " + date + " to :" + retDate);
			return retDate;
			// }

		}catch(ParseException ex){
			new CCException(ex);
			return null;
		}
	}

	/**
	 * <strong>convertDate</strong><br>
	 * <br>
	 *
	 * @param date
	 * @param to
	 * @return
	 */
	public static Date convertDate(Date date, TimeZone to){

		TimeZone utc = TimeZone.getTimeZone("UTC");
		return convertDate(date, utc, to);

	}

}
