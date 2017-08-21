package trente.asia.calendar.services.calendar.model;

/**
 * Created by hviet on 7/26/17.
 */

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RoomModel{

	public String	key;
	public String	roomName;
	public String	roomNote;
	public String	color		= "#FFFFFF";
	public String	textColor	= "#000000";

}
