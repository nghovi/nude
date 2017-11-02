package nguyenhoangviet.vpcorp.calendar.services.calendar.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by viet on 2/10/2017.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CalendarColorModel {

	public String	key;
	public String	value;
}
