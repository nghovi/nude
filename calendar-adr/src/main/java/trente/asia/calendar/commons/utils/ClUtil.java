package trente.asia.calendar.commons.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.Environment;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfDateUtil;

/**
 * Created by PC-Trente on 10/3/2016.
 */

public class ClUtil{

	/**
	 * get root file of application
	 *
	 * @return string
	 */
	public static String getFilesFolderPath(){
		File folder = new File(Environment.getExternalStorageDirectory(), ClConst.APP_FOLDER);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}

	/**
	 * find calendar dayStr
	 *
	 * @return MonthlyCalendarDayView
	 */
	public static List<MonthlyCalendarDayView> findView4Day(List<MonthlyCalendarDayView> lstView, String startDateString, String endDateString){
		List<MonthlyCalendarDayView> lstCalendarDay = new ArrayList<>();
		if(CCCollectionUtil.isEmpty(lstView)){
			return lstCalendarDay;
		}

		for(MonthlyCalendarDayView dayView : lstView){
			Date dateView = WelfareFormatUtil.makeDate(dayView.day);
			if(belongPeriod(dateView, startDateString, endDateString)){
				lstCalendarDay.add(dayView);
			}
		}
		return lstCalendarDay;
	}

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

		Date dateStart = WelfareFormatUtil.makeDate(scheduleModel.startDate);
		switch(CCStringUtil.checkNull(scheduleModel.repeatType)){
		case ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY:
			List<String> lstRepeatData = Arrays.asList(scheduleModel.repeatData.split(","));
			if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(WfDateUtil.diffDate(dateView, dateStart) >= 0){
						if(lstRepeatData.contains(String.valueOf(CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_WEEK)))){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(belongPeriod(dateView, scheduleModel.startDate, scheduleModel.repeatEnd)){
						if(lstRepeatData.contains(String.valueOf(CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_WEEK)))){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}else if(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER.equals(scheduleModel.repeatLimitType)){
				List<String> lstActiveDate = ClUtil.getDateList4RepeatTimes(scheduleModel);
				for(MonthlyCalendarDayView dayView : lstView){
					if(lstActiveDate.contains(dayView.day)){
						lstCalendarDay.add(dayView);
					}
				}
			}

			break;
		case ClConst.SCHEDULE_REPEAT_TYPE_MONTHLY:
			if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(WfDateUtil.diffDate(dateView, dateStart) >= 0){
						int dayOfMonth = CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_MONTH);
						int dayOfMonthStart = CCDateUtil.makeCalendar(dateStart).get(Calendar.DAY_OF_MONTH);
						if(dayOfMonth == dayOfMonthStart){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(belongPeriod(dateView, scheduleModel.startDate, scheduleModel.repeatEnd)){
						int dayOfMonth = CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_MONTH);
						int dayOfMonthStart = CCDateUtil.makeCalendar(dateStart).get(Calendar.DAY_OF_MONTH);
						if(dayOfMonth == dayOfMonthStart){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}else if(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER.equals(scheduleModel.repeatLimitType)){
				List<String> lstActiveDate = ClUtil.getDateList4RepeatTimes(scheduleModel);
				for(MonthlyCalendarDayView dayView : lstView){
					if(lstActiveDate.contains(dayView.day)){
						lstCalendarDay.add(dayView);
					}
				}
			}
			break;
		case ClConst.SCHEDULE_REPEAT_TYPE_YEARLY:
			if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(WfDateUtil.diffDate(dateView, dateStart) >= 0){
						int dayOfYear = CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_YEAR);
						int dayOfYearStart = CCDateUtil.makeCalendar(dateStart).get(Calendar.DAY_OF_YEAR);
						if(dayOfYear == dayOfYearStart){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(scheduleModel.repeatLimitType)){
				for(MonthlyCalendarDayView dayView : lstView){
					Date dateView = WelfareFormatUtil.makeDate(dayView.day);
					if(belongPeriod(dateView, scheduleModel.startDate, scheduleModel.repeatEnd)){
						int dayOfYear = CCDateUtil.makeCalendar(dateView).get(Calendar.DAY_OF_YEAR);
						int dayOfYearStart = CCDateUtil.makeCalendar(dateStart).get(Calendar.DAY_OF_YEAR);
						if(dayOfYear == dayOfYearStart){
							lstCalendarDay.add(dayView);
						}
					}
				}
			}else if(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER.equals(scheduleModel.repeatLimitType)){
				List<String> lstActiveDate = ClUtil.getDateList4RepeatTimes(scheduleModel);
				for(MonthlyCalendarDayView dayView : lstView){
					if(lstActiveDate.contains(dayView.day)){
						lstCalendarDay.add(dayView);
					}
				}
			}

			break;
		default:
			break;
		}

		return lstCalendarDay;
	}

	/**
	 * date belong min, max period
	 */
	public static boolean belongPeriod(Date date, String min, String max){
		if(date == null || min == null || max == null){
			return false;
		}

		Date minDate = CCDateUtil.makeDate(WelfareFormatUtil.makeDate(min));
		Date maxDate = CCDateUtil.makeDate(WelfareFormatUtil.makeDate(max));
		Date activeDate = CCDateUtil.makeDate(date);

		if(activeDate.compareTo(minDate) >= 0 && activeDate.compareTo(maxDate) <= 0){
			return true;
		}
		return false;
	}

	/**
	 * convertUserList2String
	 */
	public static String convertUserList2String(List<UserModel> lstUser){
		StringBuilder builder = new StringBuilder();
		if(!CCCollectionUtil.isEmpty(lstUser)){
			for(UserModel userModel : lstUser){
				builder.append(userModel.key + ",");
			}
		}
		return builder.toString();
	}

	public static List<UserModel> getJoinedUserModels(ScheduleModel schedule, List<UserModel> userModels){
		List<UserModel> joinUsers = new ArrayList<>();
		if(userModels != null && !CCStringUtil.isEmpty(schedule.joinUsers)){
			for(String userId : schedule.joinUsers.split(",")){
				UserModel userModel = UserModel.getUserModel(userId, userModels);
				if(userModel != null){
					joinUsers.add(userModel);
				}
			}
		}
		return joinUsers;
	}

	/**
	 * getMaxInList
	 */
	public static int getMaxInList(List<MonthlyCalendarDayView> lstDay){
		int maxSchedule = 0;
		for(MonthlyCalendarDayView dayView : lstDay){
			if(maxSchedule < dayView.getNumberOfSchedule()){
				maxSchedule = dayView.getNumberOfSchedule();
			}
		}
		return maxSchedule;
	}

	/**
	 * getTargetUserList
	 */
	public static List<UserModel> getTargetUserList(List<UserModel> lstUser, String targetUserData){
		if(CCStringUtil.isEmpty(targetUserData)){
			return lstUser;
		}

		List<String> targetUserListId = Arrays.asList(targetUserData.split(","));
		List<UserModel> lstTargetUser = new ArrayList<>();
		for(UserModel userModel : lstUser){
			if(targetUserListId.contains(userModel.key)){
				lstTargetUser.add(userModel);
			}
		}
		return lstTargetUser;
	}

	/**
	 * get date list for repeat schedule follow
	 * getDateList4RepeatTimes
	 */
	public static List<String> getDateList4RepeatTimes(ScheduleModel scheduleModel){
		List<String> lstDate = new ArrayList<>();
		int indexSchedule = 0;

		Date startDate = WelfareUtil.makeDate(scheduleModel.startDate);
		Calendar startCalendar = CCDateUtil.makeCalendar(startDate);
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
		}else if(ClConst.SCHEDULE_REPEAT_TYPE_YEARLY.equals(scheduleModel.repeatType)){
			while(indexSchedule <= Integer.valueOf(scheduleModel.repeatInterval)){
				startCalendar.add(Calendar.YEAR, 1);
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

	public static CategoryModel findCategory4Id(List<CategoryModel> lstCategory, String key){
		CategoryModel categoryModel = null;
		if(!CCCollectionUtil.isEmpty(lstCategory)){
			for(CategoryModel model : lstCategory){
				if(model.key.equals(key)){
					categoryModel = model;
					break;
				}
			}
		}
		return categoryModel;
	}

	/**
	 * <strong>convert category list to Map</strong><br>
	 * 
	 * @return
	 */
    public static Map<String, String> convertCategoryList2Map(List<CategoryModel> list){
        if(CCCollectionUtil.isEmpty(list)){
            return null;
        }
        Map<String, String> map = new LinkedHashMap<>();
        for(CategoryModel model : list){
            map.put(model.key, model.categoryName);
        }
        return map;
    }
}
