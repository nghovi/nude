package trente.asia.welfare.adr.models;

import java.io.Serializable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Huy-nq on 8/25/2016.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class SettingModel implements Serializable{

	public String	DR_PUBLIC_LEVEL;
	public String	WF_PUSH_SETTING;
	public String	WF_MAX_FILE_SIZE;
	public String	SW_APPOINTMENT_ENABLED;

	public String	CL_START_DAY_IN_WEEK;
}
