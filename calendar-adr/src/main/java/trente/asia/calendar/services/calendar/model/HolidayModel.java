package trente.asia.calendar.services.calendar.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import android.graphics.Color;

import asia.chiase.core.util.CCDateUtil;
import trente.asia.calendar.commons.activites.MainClActivity;

/**
 * HolidayModel
 *
 * @author TrungND
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class HolidayModel{

	public static final int	HOLIDAY_DAY_COLOR	= Color.RED;

	public String			holidayName;
	@JsonField(typeConverter = MainClActivity.WelfareTimeConverter.class)
	public Date				startDate;
	@JsonField(typeConverter = MainClActivity.WelfareTimeConverter.class)
	public Date				endDate;
	public String			imgPath;

	public HolidayModel(){

	}

	public static List<HolidayModel> getHolidayModels(Date date, List<HolidayModel> holidayModels){
		List<HolidayModel> results = new ArrayList<>();
		for(HolidayModel holidayModel : holidayModels){
			if(CCDateUtil.compareDate(holidayModel.startDate, date, false) <= 0 && CCDateUtil.compareDate(date, holidayModel.endDate, false) <= 0){
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
