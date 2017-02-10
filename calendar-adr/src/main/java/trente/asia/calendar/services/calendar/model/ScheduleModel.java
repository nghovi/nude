package trente.asia.calendar.services.calendar.model;

import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * ScheduleModel
 *
 * @author TrungND
 */

public class ScheduleModel{

	public String	scheduleDate;

	public ScheduleModel(){

	}

	public ScheduleModel(String scheduleName, String scheduleDate, String scheduleColor){
		this.scheduleName = scheduleName;
		this.scheduleDate = scheduleDate;
		this.scheduleColor = scheduleColor;
	}

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
}
