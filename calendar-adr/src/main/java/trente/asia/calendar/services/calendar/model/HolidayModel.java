package trente.asia.calendar.services.calendar.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * HolidayModel
 *
 * @author TrungND
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class HolidayModel{

	public String	holidayName;
	public String	startDate;
	public String	endDate;
	public String	imgPath;

	public HolidayModel(){

	}

	public static List<HolidayModel> getHolidayModels(Date date, List<HolidayModel> holidayModels){
		List<HolidayModel> results = new ArrayList<>();
		for(HolidayModel holidayModel : holidayModels){
			Date startDate = CCDateUtil.makeDateCustom(holidayModel.startDate, WelfareConst.WF_DATE_TIME_DATE);
			Date endDate = CCDateUtil.makeDateCustom(holidayModel.endDate, WelfareConst.WF_DATE_TIME_DATE);
			if(CCDateUtil.compareDate(startDate, date, false) <= 0 && CCDateUtil.compareDate(date, endDate, false) <= 0){
				results.add(holidayModel);
			}
		}
		sortHolidayModels(results);
		return results;
	}

	public static void sortHolidayModels(List<HolidayModel> results){
		Collections.sort(results, new Comparator<HolidayModel>() {

			@Override
			public int compare(HolidayModel o1, HolidayModel o2){
				return o1.holidayName.compareTo(o2.holidayName);
			}
		});
	}
}
