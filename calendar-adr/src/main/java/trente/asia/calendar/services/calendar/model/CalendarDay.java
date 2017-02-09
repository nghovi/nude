package trente.asia.calendar.services.calendar.model;

import java.util.List;

/**
 * Created by viet on 2/6/2017.
 */

public class CalendarDay{

	public String			date;
	public List<Schedule>	schedules;

	public static class Schedule{

		public String	name;
		public String	note;
		public String	url;
		public String	color;
		public String	startDate;
		public String	endDate;
		public String	startTime;
		public String	endTime;
	}
}
