package trente.asia.android.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;

/**
 * <strong>CsDateUtil</strong><br>
 * <br>
 * 
 * @author HoanLV
 * @version $Id$
 */
public class CsDateUtil{

	public static final String CS_DATE_TIME_1 = "EEE";

	public static Date getFirstDateOfCurrentMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date addMonth(Date date, int add){
		Calendar calendar = CCDateUtil.makeCalendar(date);
		calendar.add(Calendar.MONTH, add);
		return calendar.getTime();
	}

	public static Date addWeek(Date date, int add){
		Calendar calendar = CCDateUtil.makeCalendar(date);
		calendar.add(Calendar.WEEK_OF_YEAR, add);
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
	public static List<Date> getAllDate4Month(Calendar calendar, int startDay){
		if(calendar == null){
			return null;
		}else{
			calendar.setFirstDayOfWeek(startDay);
			List<Date> lstDate = new ArrayList<>();

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

	/**
	 * <strong>getAllDate4Week</strong><br>
	 * <br> get all date in week with start: Sunday and end: Saturday
	 *
	 * @param calendar
	 * @return
	 */
	public static List<Date> getAllDate4Week(Calendar calendar, int startDay){
		if(calendar == null){
			return null;
		}else{
			calendar.setFirstDayOfWeek(startDay);
			List<Date> lstDate = new ArrayList<>();

			calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
			Date firstDate = calendar.getTime();
			calendar.add(Calendar.DATE, 6);
			Date lastDate = calendar.getTime();

			lstDate = CCDateUtil.makeDateList(firstDate, lastDate);

			return lstDate;
		}
	}

	/**
	 * <strong>getAllDay4Week</strong><br>
	 * <br> get all day in week: MON, TUE, WED, THU, FRI, SAT, SUN
	 *
	 * @param firstDay
	 * @return
	 */
	public static List<DayModel> getAllDay4Week(int firstDay){
		List<DayModel> lstDay = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, firstDay);
		for(int index = 0; index < CsConst.DAY_NUMBER_A_WEEK; index++){
			if(index > 0){
				calendar.add(Calendar.DATE, 1);
			}
			DayModel dayModel = new DayModel();
			dayModel.date = calendar.getTime();
			dayModel.day = CCFormatUtil.formatDateCustom(CS_DATE_TIME_1, calendar.getTime()).toUpperCase();
			dayModel.dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			lstDay.add(dayModel);
		}

		return lstDay;
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

	public static int diffDate(Date date1, Date date2){
		int dayDiff = (int)((date1.getTime() - date2.getTime()) / 86400000L);
		return dayDiff;
	}

	public static boolean isDiffMonth(Date date1, Date date2){
		if(date1 == null || date2 == null){
			return false;
		}
		Calendar calendar1 = CCDateUtil.makeCalendar(date1);
		Calendar calendar2 = CCDateUtil.makeCalendar(date2);
		return !(calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH));
	}
}