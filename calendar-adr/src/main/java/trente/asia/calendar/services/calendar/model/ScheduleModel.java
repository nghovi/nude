package trente.asia.calendar.services.calendar.model;

/**
 * ScheduleModel
 *
 * @author TrungND
 */

public class ScheduleModel{

	public String			scheduleDate;
	public String			scheduleName;
	public String			scheduleNote;
	public String			url;
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

	public String			scheduleType;
	public String			categoryId;
	public String			categoryName;

	public ScheduleModel(){

	}

	public ScheduleModel(String scheduleName, String scheduleDate, String scheduleColor, String scheduleType){
		this.scheduleName = scheduleName;
		this.scheduleDate = scheduleDate;
		this.scheduleColor = scheduleColor;
		this.scheduleType = scheduleType;
	}
}
