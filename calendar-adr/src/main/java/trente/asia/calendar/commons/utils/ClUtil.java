package trente.asia.calendar.commons.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Environment;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import trente.asia.calendar.commons.defines.ClConst;
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
	public static List<MonthlyCalendarDayView> findView4Day(List<MonthlyCalendarDayView> lstView, String startDateString, String endDateString){
		List<MonthlyCalendarDayView> lstCalendarDay = new ArrayList<>();
		if(CCCollectionUtil.isEmpty(lstView)){
			return lstCalendarDay;
		}

//		Date startDate = CCDateUtil.makeDate(WelfareFormatUtil.makeDate(startDateString));
//		Date endDate = CCDateUtil.makeDate(WelfareFormatUtil.makeDate(endDateString));
		for(MonthlyCalendarDayView dayView : lstView){
			Date dateView = WelfareFormatUtil.makeDate(dayView.day);
			if(belongPeriod(dateView, startDateString, endDateString)){
				lstCalendarDay.add(dayView);
			}
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
}
