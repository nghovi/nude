package trente.asia.calendar.services.calendar.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/6/2017.
 */

public class CalendarDayModel{

	public Date					date;
	public List<ScheduleModel>	schedules		= new ArrayList<>();
	public List<HolidayModel>	holidayModels	= new ArrayList<>();
	public List<WorkOffer>		workOffers		= new ArrayList<>();
	public List<UserModel>		birthdayUsers	= new ArrayList<>();
}
