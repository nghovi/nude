package trente.asia.calendar.services.calendar.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by viet on 2/10/2017.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CalendarBirthdayModel {

	public String	birthDay;
	public String	message;
}
