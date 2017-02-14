package trente.asia.calendar.services.calendar.model;

import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 11/25/2016.
 */

public class CalendarModel{

	public String			calendarName;
	public String			ownerId;
	public Boolean			isMyself;
	public List<UserModel>	calendarUsers;
	public List<Integer>	calendarUserList;
	public String			key;
	public String			companyId;
	public String calendarImagePath;

	public CalendarModel(){

	}
}
