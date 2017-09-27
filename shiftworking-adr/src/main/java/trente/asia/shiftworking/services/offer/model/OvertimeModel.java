package trente.asia.shiftworking.services.offer.model;

import java.util.List;

import trente.asia.welfare.adr.models.ApiObjectModel;

/**
 * Created by viet on 11/25/2016.
 */

public class OvertimeModel {

	public String				userId;
	public String				userName;
	public String				offerType;
	public String				offerTypeName;
	public String				overtimeType;
	public String				startDateString;
	public String				startTimeString;
	public String				endDateString;
	public String				endTimeString;
	public String				approveUser1;
	public String				approveResult1;
	public String				approveUser2;
	public String				approveResult2;

	public String				requestDateString;
	public String				approveResult;
	public String				userAvatarPath;
	public String				offerStatus;
	public String				offerStatusName;
	public String				key;
	public String				note;
	public String				companyId;
	public List<ApproveHistory>	listHistories;

	public List<ApiObjectModel>	overtimeTypeList;
	public String				permission;
}
