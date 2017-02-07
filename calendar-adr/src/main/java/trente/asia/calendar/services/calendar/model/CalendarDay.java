package trente.asia.calendar.services.calendar.model;

import java.util.List;

/**
 * Created by viet on 2/6/2017.
 */

public class CalendarDay{

	public String		date;
	public List<Event>	events;

	public static class Event{

		public String	startTime;
		public String	endTime;
	}
}
