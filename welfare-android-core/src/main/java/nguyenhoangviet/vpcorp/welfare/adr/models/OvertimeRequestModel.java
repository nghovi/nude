package nguyenhoangviet.vpcorp.welfare.adr.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by viet on 11/25/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class OvertimeRequestModel extends VacationRequestModel{

	public static final String OVERTIME_EARLY = "E";
	public static final String OVERTIME_LATELY = "O";

	public String				offerType;
	public String				offerTypeName;
	public String				overtimeType;
	public String				startTimeString;
	public String				endTimeString;
	public String				approveResult;
}
