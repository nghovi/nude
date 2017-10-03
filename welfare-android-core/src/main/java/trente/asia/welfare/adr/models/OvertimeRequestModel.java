package trente.asia.welfare.adr.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import trente.asia.welfare.adr.models.ApproveHistory;
import trente.asia.welfare.adr.models.VacationRequestModel;

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
