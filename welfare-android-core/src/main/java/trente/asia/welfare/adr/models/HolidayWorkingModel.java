package trente.asia.welfare.adr.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import trente.asia.welfare.adr.models.VacationRequestModel;

/**
 * Created by viet on 11/25/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class HolidayWorkingModel extends VacationRequestModel{
	public String				approveResult;
	public String				permission;
}
