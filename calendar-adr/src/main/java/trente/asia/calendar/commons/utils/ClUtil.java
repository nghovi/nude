package trente.asia.calendar.commons.utils;

import java.io.File;
import java.util.List;

import android.os.Environment;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;

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
	public static MonthlyCalendarDayView findView4Day(List<MonthlyCalendarDayView> lstView, String day){
		if(CCCollectionUtil.isEmpty(lstView)){
			return null;
		}

		MonthlyCalendarDayView view = null;
		for(MonthlyCalendarDayView dayView : lstView){
			if(dayView.day.equals(day)){
				view = dayView;
				break;
			}
		}
		return view;
	}
}
