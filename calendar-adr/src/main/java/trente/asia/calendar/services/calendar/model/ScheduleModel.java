package trente.asia.calendar.services.calendar.model;

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
	public Boolean			isDayPeriod;
	public Boolean			isRepeat;

	public ScheduleModel(){

	}

	public ScheduleModel(HolidayModel holidayModel){
		this.scheduleName = holidayModel.holidayName;
		this.startDate = holidayModel.startDate;
		this.endDate = holidayModel.endDate;
		// pink color
		this.scheduleColor = "D22DB6";
        this.isDayPeriod = true;
	}
}
