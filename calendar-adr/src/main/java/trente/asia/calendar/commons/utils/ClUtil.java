package trente.asia.calendar.commons.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

	public static List<MonthlyCalendarDayView> findListView4Day(List<MonthlyCalendarDayView> lstView, Date startDate, Date endDate){
		List<MonthlyCalendarDayView> lstCalendarDay = new ArrayList<>();
		if(CCCollectionUtil.isEmpty(lstView)){
			return lstCalendarDay;
		}

		for(MonthlyCalendarDayView dayView : lstView){
			Date dateView = WelfareFormatUtil.makeDate(dayView.day);
			if(belongPeriod(dateView, startDate, endDate)){
				lstCalendarDay.add(dayView);
			}
		}
		return lstCalendarDay;
	}

    public static MonthlyCalendarDayView findView4Day(List<MonthlyCalendarDayView> lstView, Date startDate, Date endDate){
        if(CCCollectionUtil.isEmpty(lstView)){
            return null;
        }

        for(MonthlyCalendarDayView dayView : lstView){
            Date dateView = WelfareFormatUtil.makeDate(dayView.day);
            if(belongPeriod(dateView, startDate, endDate)){
                return dayView;
            }
        }
        return null;
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
     * date belong min, max period
     */
    public static boolean belongPeriod(Date date, Date minDate, Date maxDate){
        if(date == null || minDate == null || maxDate == null){
            return false;
        }

        if(date.compareTo(minDate) >= 0 && date.compareTo(maxDate) <= 0){
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
			if(maxSchedule < dayView.getActivePeriodNum()){
				maxSchedule = dayView.getActivePeriodNum();
			}
		}
		return maxSchedule;
	}

	/**
	 * getTargetUserList
	 */
	public static List<UserModel> getTargetUserList(List<UserModel> lstUser, String targetUserData){
        List<UserModel> lstTargetUser = new ArrayList<>();
		if(CCStringUtil.isEmpty(targetUserData) || CCCollectionUtil.isEmpty(lstUser)){
			return lstTargetUser;
		}

		List<String> targetUserListId = Arrays.asList(targetUserData.split(","));
		for(UserModel userModel : lstUser){
			if(targetUserListId.contains(userModel.key)){
				lstTargetUser.add(userModel);
			}
		}
		return lstTargetUser;
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

    /**
     * <strong>convert category list to Map</strong><br>
     *
     * @return
     */
    public static Map<String, CategoryModel> convertCategory2Map(List<CategoryModel> lstCategory){
        Map<String, CategoryModel> categoryMap = new HashMap<>();
        for(CategoryModel categoryModel : lstCategory){
            categoryMap.put(categoryModel.key, categoryModel);
        }
        return categoryMap;
    }
}
