package trente.asia.shiftworking.services.requests.model;

import java.util.List;

import trente.asia.welfare.adr.models.ApproveHistory;
import trente.asia.welfare.adr.models.VacationRequestModel;

/**
 * Created by viet on 11/25/2016.
 */

public class OvertimeRequestModel extends VacationRequestModel{

	public String				offerType;
	public String				offerTypeName;
	public String				overtimeType;
	public String				startTimeString;
	public String				endTimeString;
	public String				approveResult;

}
