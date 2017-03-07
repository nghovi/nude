package trente.asia.calendar.services.calendar.model;

import java.util.Calendar;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * ScheduleModel
 *
 * @author TrungND
 */

public class ScheduleModel{

	public String			scheduleName;
	public String			scheduleNote;
	public String			scheduleUrl;
	// public String scheduleColor;
	public String			startDate;
	public String			endDate;
	public String			startTime;
	public String			endTime;

	public String			key;
	public String			calendarId;
	public String			roomId;
	public String			joinUsers;
	public CalendarModel	calendar;

	public String			scheduleType;
	public String			categoryId;
	public CategoryModel	categoryModel;
	public Boolean			isAllDay;
	public Boolean			isRepeat;
	public List<UserModel>	scheduleJoinUsers;
	// public Boolean isHoliday;

	public String			repeatType;
	public String			repeatLimitType;
	public String			repeatData;
	public String			repeatEnd;
	public String			repeatInterval;
	public Boolean			isWarning;
	public String			ownerId;
	public UserModel		owner;

	public ScheduleModel(){

	}

	public ScheduleModel(HolidayModel holidayModel){
		this.scheduleName = holidayModel.holidayName;
		this.startDate = holidayModel.startDate;
		this.endDate = holidayModel.endDate;
		this.isAllDay = true;
		// pink color
		// this.scheduleColor = "D22DB6";
		this.scheduleType = ClConst.SCHEDULE_TYPE_HOLIDAY;
		// this.isHoliday = true;
	}

	public ScheduleModel(UserModel userModel){
		this.scheduleName = userModel.userName;
		Calendar calendar = Calendar.getInstance();
		Calendar birthdayCalendar = CCDateUtil.makeCalendar(WelfareUtil.makeDate(userModel.dateBirth));
		Calendar scheduleCalendar = CCDateUtil.makeCalendar(calendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH) + 1, birthdayCalendar.get(Calendar.DAY_OF_MONTH));
		this.startDate = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_1, scheduleCalendar.getTime());
		this.endDate = this.startDate;
		this.isAllDay = true;
		this.scheduleType = ClConst.SCHEDULE_TYPE_BIRTHDAY;
	}

	public boolean isPeriodSchedule(){
		if(startDate == null || endDate == null){
			return false;
		}

		String startDateFormat = WelfareFormatUtil.formatDate(WelfareUtil.makeDate(startDate));
		String endDateFormat = WelfareFormatUtil.formatDate(WelfareUtil.makeDate(endDate));
		return !startDateFormat.equals(endDateFormat);
	}

	public String getScheduleColor(List<CategoryModel> lstCategory){
		if(!CCStringUtil.isEmpty(this.calendarId)){
			CategoryModel categoryModel = ClUtil.findCategory4Id(lstCategory, this.categoryId);
			return WelfareFormatUtil.formatColor(categoryModel.categoryColor);
		}
		if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleType)){
			return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_HOLIDAY);
		}else if(ClConst.SCHEDULE_TYPE_BIRTHDAY.equals(scheduleType)){
			return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_BIRTHDAY);
		}

		return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_NORMAL);
	}
}
