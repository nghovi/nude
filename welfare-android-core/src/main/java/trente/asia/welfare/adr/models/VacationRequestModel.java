package trente.asia.welfare.adr.models;

import java.util.Date;
import java.util.List;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import trente.asia.welfare.adr.activity.WelfareActivity;

/**
 * Created by hviet on 9/29/17.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class VacationRequestModel{

	public String				vacationId;
	public String				vacationName;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date					endDate;
	public String				endDateString;
	public String				amount;
	public String				amountString;
	public Boolean				sickAbsent;
	public String				userId;
	public String				userAvatarPath;
	public String				userName;
	public String				offerStatus;
	public String				offerStatusName;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date					startDate;
	public String				startDateString;
	public String				note;
	public String				approveUser1;
	public String				approveResult1;
	public String				approveUser2;
	public String				approveResult2;
	public String				requestDateString;
	public Boolean				isWorktimeRelation;
	public String				key;
	public String				companyId;
	public List<ApproveHistory>	listHistories;
}
