package trente.asia.android.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;

/**
 * <strong>CsDateUtil</strong><br>
 * <br>
 * 
 * @author HoanLV
 * @version $Id$
 */
public class CsDateUtil{

	public static Date makeMonthWithFirstDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date addMonth(Date date, int add){
		Calendar calendar = CCDateUtil.makeCalendar(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + add);
		return calendar.getTime();
	}

	public static Date addDate(Date date, int add){
		Calendar calendar = CCDateUtil.makeCalendar(date);
		calendar.add(Calendar.DATE, add);
		return calendar.getTime();
	}

	/**
	 * <strong>getAllDate4Month</strong><br>
	 * <br> get all date in month with start: Sunday and end: Saturday
	 *
	 * @param calendar
	 * @return
	 */
	public static List<Date> getAllDate4Month(Calendar calendar){
		if(calendar == null){
			return null;
		}else{
			List<Date> lstDate = new ArrayList<>();

			// int start = calendar.get(Calendar.WEEK_OF_YEAR);
			Calendar firstDay = (Calendar)calendar.clone();
			firstDay.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
			Date firstDate = firstDay.getTime();

			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, -1);
			Calendar lastDay = (Calendar)calendar.clone();
			lastDay.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
			lastDay.add(Calendar.DATE, 6);
			Date lastDate = lastDay.getTime();

			lstDate = CCDateUtil.makeDateList(firstDate, lastDate);

			return lstDate;
		}
	}

	public static Integer getAge(String birthday){

		if(CCStringUtil.isEmpty(birthday)){
			return null;
		}
		Date birthdayDate = CCDateUtil.makeDate(birthday);

		Integer age = getAge(birthdayDate);
		return age;
	}

	/**
	 * <strong>getAge</strong><br>
	 * <br>
	 * 
	 * @param birthday
	 * @return
	 */
	public static Integer getAge(Date birthday){
		// Reference:
		// http://stackoverflow.com/questions/15128851/java-how-to-calculate-age-with-day-month-year

		if(birthday == null){
			return 0;
		}else{
			Calendar calNow = Calendar.getInstance();

			Calendar calBirthday = Calendar.getInstance();
			calBirthday.setTime(birthday);

			Integer factor = 0;
			if(calNow.get(Calendar.DAY_OF_YEAR) < calBirthday.get(Calendar.DAY_OF_YEAR)){
				factor = -1;
			}

			Integer age = calNow.get(Calendar.YEAR) - calBirthday.get(Calendar.YEAR) + factor;
			return age;
		}

	}
}