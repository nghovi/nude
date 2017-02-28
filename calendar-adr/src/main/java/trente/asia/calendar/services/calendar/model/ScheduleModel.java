package trente.asia.calendar.services.calendar.model;

import java.util.List;

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
	public String			scheduleColor;
	public String			startDate;
	public String			endDate;
	public String			startTime;
	public String			endTime;

	public String			key;
	public String			calendarId;
	public String			roomId;
	public String			joinUsers;
	public CalendarModel	calendar;

	// public String scheduleType;
	public String			categoryId;
	public String			categoryName;
	public Boolean			isAllDay;
	public Boolean			isRepeat;
	public List<UserModel>	scheduleJoinUsers;
	public Boolean			isHoliday;

	public String			repeatType;
	public String			repeatLimitType;
	public String			repeatData;
	public String			repeatEnd;
	public String			repeatInterval;

	public ScheduleModel(){

	}

	public ScheduleModel(HolidayModel holidayModel){
		this.scheduleName = holidayModel.holidayName;
		this.startDate = holidayModel.startDate;
		this.endDate = holidayModel.endDate;
		this.isAllDay = true;
		// pink color
		this.scheduleColor = "D22DB6";
		this.isHoliday = true;
	}

	public boolean isPeriodSchedule(){
		if(startDate == null || endDate == null){
			return false;
		}

		String startDateFormat = WelfareFormatUtil.formatDate(WelfareUtil.makeDate(startDate));
		String endDateFormat = WelfareFormatUtil.formatDate(WelfareUtil.makeDate(endDate));
		return !startDateFormat.equals(endDateFormat);
	}
}
