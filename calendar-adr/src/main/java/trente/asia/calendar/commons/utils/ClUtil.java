package trente.asia.calendar.commons.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Environment;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import trente.asia.calendar.commons.defines.ClConst;
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
	 * find calendar day
	 *
	 * @return MonthlyCalendarDayView
	 */
	public static List<MonthlyCalendarDayView> findView4Day(List<MonthlyCalendarDayView> lstView, ScheduleModel scheduleModel){
		List<MonthlyCalendarDayView> lstCalendarDay = new ArrayList<>();
		if(CCCollectionUtil.isEmpty(lstView)){
			return lstCalendarDay;
		}

		Date startDate = CCDateUtil.makeDate(WelfareFormatUtil.makeDate(scheduleModel.startDate));
		Date endDate = CCDateUtil.makeDate(WelfareFormatUtil.makeDate(scheduleModel.endDate));
		for(MonthlyCalendarDayView dayView : lstView){
			Date dateView = WelfareFormatUtil.makeDate(dayView.day);
			if(dateView.compareTo(startDate) >= 0 && dateView.compareTo(endDate) <= 0){
				lstCalendarDay.add(dayView);
			}
		}
		return lstCalendarDay;
	}

    /**
     * convertUserList2String
     *
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
