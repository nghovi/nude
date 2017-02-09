package trente.asia.calendar.services.calendar.model;

import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/6/2017.
 */

public class CalendarDay{

	public String			date;
	public List<Schedule>	schedules;

	public static class Schedule{

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
		public List<Integer>	joinUsers;
	}
}
