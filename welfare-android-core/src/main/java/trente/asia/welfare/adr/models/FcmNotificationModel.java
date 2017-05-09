package trente.asia.welfare.adr.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class FcmNotificationModel{

	public String	title;

	public String	body;

	public String	body_loc_key;
	public String[]	body_loc_args;

}
