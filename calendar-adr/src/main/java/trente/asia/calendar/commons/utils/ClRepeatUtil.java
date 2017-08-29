package trente.asia.calendar.commons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.model.ScheduleRepeatModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WfDateUtil;

/**
 * ClRepeatUtil
 *
 * @author TrungND
 */

public class ClRepeatUtil{

	/**
	 * find calendar day or repeat schedule
	 *
	 * @return MonthlyCalendarDayView list
	 */
	public static List<MonthlyCalendarDayView> findView4RepeatSchedule(List<MonthlyCalendarDayView> lstView, ScheduleModel scheduleModel){
		List<MonthlyCalendarDayView> lstCalendarDay = new ArrayList<>();
		if(CCCollectionUtil.isEmpty(lstView) || CCStringUtil.isEmpty(scheduleModel.repeatType)){
			return lstCalendarDay;
		}

		// Date dateStart = WelfareFormatUtil.makeDate(scheduleModel.startDate);
		switch(CCStringUtil.checkNull(scheduleModel.repeatType)){
		case ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY:
			List<String> lstRepeatData = Arrays.asList(scheduleModel.repeatData.split(","));
			if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(WfDateUtil.diffDate(dateView, scheduleModel.startDateObj) >= 0){
						if(lstRepeatData.contains(String.valueOf(CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_WEEK)))){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(ClUtil.belongPeriod(dateView, scheduleModel.startDate, scheduleModel.repeatEnd)){
						if(lstRepeatData.contains(String.valueOf(CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_WEEK)))){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}
			// else if(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER.equals(scheduleModel.repeatLimitType)){
			// List<String> lstActiveDate = ClRepeatUtil.getDateList4RepeatTimes(scheduleModel);
			// for(MonthlyCalendarDayView dayView : lstView){
			// if(lstActiveDate.contains(dayView.day)){
			// lstCalendarDay.add(dayView);
			// }
			// }
			// }

			break;
		case ClConst.SCHEDULE_REPEAT_TYPE_MONTHLY:
			if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(WfDateUtil.diffDate(dateView, scheduleModel.startDateObj) >= 0){
						int dayOfMonth = CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_MONTH);
						int dayOfMonthStart = CCDateUtil.makeCalendar(scheduleModel.startDateObj).get(Calendar.DAY_OF_MONTH);
						if(dayOfMonth == dayOfMonthStart){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(ClUtil.belongPeriod(dateView, scheduleModel.startDate, scheduleModel.repeatEnd)){
						int dayOfMonth = CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_MONTH);
						int dayOfMonthStart = CCDateUtil.makeCalendar(scheduleModel.startDateObj).get(Calendar.DAY_OF_MONTH);
						if(dayOfMonth == dayOfMonthStart){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}
			// else if(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER.equals(scheduleModel.repeatLimitType)){
			// List<String> lstActiveDate = ClRepeatUtil.getDateList4RepeatTimes(scheduleModel);
			// for(MonthlyCalendarDayView dayView : lstView){
			// if(lstActiveDate.contains(dayView.day)){
			// lstCalendarDay.add(dayView);
			// }
			// }
			// }
			break;

		default:
			break;
		}

		return lstCalendarDay;
	}

	/**
	 * get date list for repeat schedule follow
	 * getDateList4RepeatTimes
	 */
	public static List<String> getDateList4RepeatTimes(ScheduleModel scheduleModel){
		List<String> lstDate = new ArrayList<>();
		int indexSchedule = 0;

		// Date startDate = WelfareUtil.makeDate(scheduleModel.startDate);
		Calendar startCalendar = CCDateUtil.makeCalendar(scheduleModel.startDateObj);
		List<String> lstDayOfWeek = Arrays.asList(scheduleModel.repeatData.split(","));

		if(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY.equals(scheduleModel.repeatType)){
			int indexCircle = -1;
			while(indexSchedule <= Integer.valueOf(scheduleModel.repeatInterval)){
				indexCircle = getIndexCircle(lstDayOfWeek.size(), indexCircle);
				setNextDayOfWeek(startCalendar, Integer.valueOf(lstDayOfWeek.get(indexCircle)));
				lstDate.add(WelfareFormatUtil.formatDate(startCalendar.getTime()));
				indexSchedule++;
			}
		}else if(ClConst.SCHEDULE_REPEAT_TYPE_MONTHLY.equals(scheduleModel.repeatType)){
			while(indexSchedule <= Integer.valueOf(scheduleModel.repeatInterval)){
				startCalendar.add(Calendar.MONTH, 1);
				lstDate.add(WelfareFormatUtil.formatDate(startCalendar.getTime()));
				indexSchedule++;
			}
		}

		return lstDate;
	}

	public static void setNextDayOfWeek(Calendar calendar, int dayOfWeek){
		int calendarDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if(calendarDayOfWeek < dayOfWeek){
			calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek + 7 - calendarDayOfWeek);
		}else{
			calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek - calendarDayOfWeek);
		}
	}

	public static int getIndexCircle(int size, int index){
		if(index < (size - 1)){
			return index++;
		}else{
			return 0;
		}
	}

	public static boolean isRepeat(String repeatType){
		return !CCStringUtil.isEmpty(repeatType) && !ClConst.SCHEDULE_REPEAT_TYPE_NONE.equals(repeatType);
	}

	/**
	 * get repeat description
	 */
	public static String getRepeatDescription(ScheduleRepeatModel repeatModel, Context context){
		StringBuilder builder = new StringBuilder();

		if(!isRepeat(repeatModel.repeatType)){
			builder.append(context.getString(R.string.chiase_common_none));
			return builder.toString();
		}

		if(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY.equals(repeatModel.repeatType)){
			if(CCStringUtil.isEmpty(repeatModel.repeatData)){
				builder.append(context.getString(R.string.chiase_common_none));
			}else{
				builder.append(context.getString(R.string.cl_schedule_repeat_weekly_message, getRepeatDays4Data(context, repeatModel.repeatData)));
			}
		}else if(ClConst.SCHEDULE_REPEAT_TYPE_MONTHLY.equals(repeatModel.repeatType)){
			builder.append(context.getString(R.string.cl_schedule_repeat_monthly_message));
		}

		if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(repeatModel.repeatLimitType)){
		}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(repeatModel.repeatLimitType)){
			// Date repeatEndDate = WelfareUtil.makeDate(repeatModel.repeatEnd);
			// builder.append(context.getString(R.string.cl_schedule_repeat_until_message, WelfareFormatUtil.formatDate(repeatEndDate)));
		}else{
			if(CsConst.ONE.equals(repeatModel.repeatInterval)){
				builder.append(context.getString(R.string.cl_schedule_repeat_for_one_message));
			}else{
				builder.append(context.getString(R.string.cl_schedule_repeat_for_message, repeatModel.repeatInterval));
			}
		}

		return builder.toString();
	}

	public static String getRepeatDays4Data(Context context, String repeatData){
		StringBuilder builder = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		if(!CCStringUtil.isEmpty(repeatData)){
			String[] repeatDays = repeatData.split(",");
			for(String repeatDay : repeatDays){
				calendar.set(Calendar.DAY_OF_WEEK, Integer.valueOf(repeatDay));
				builder.append(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_WEEK_DAY_SHORT, calendar.getTime()) + context.getString(R.string.day) + context.getString(R.string.comma) + " ");
			}
		}
		if(builder.length() > 0){
			return builder.substring(0, builder.length() - 2);
		}
		return builder.toString();
	}
}
