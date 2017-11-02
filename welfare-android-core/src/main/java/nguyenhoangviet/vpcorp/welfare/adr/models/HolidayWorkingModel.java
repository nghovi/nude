package nguyenhoangviet.vpcorp.welfare.adr.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by viet on 11/25/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class HolidayWorkingModel extends VacationRequestModel{

	public String	approveResult;
	public String	permission;
}
