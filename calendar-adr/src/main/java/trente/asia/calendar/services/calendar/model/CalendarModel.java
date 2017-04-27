package trente.asia.calendar.services.calendar.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import trente.asia.welfare.adr.models.BitmapModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 11/25/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CalendarModel extends BitmapModel{

	public String			calendarName;
	public String			ownerId;
	public Boolean			isMyself;
	public List<UserModel>	calendarUsers;
	public List<Integer>	calendarUserList;
	public String			key;
	public String			companyId;
	public String			imagePath;

	public CalendarModel(){

	}
}
