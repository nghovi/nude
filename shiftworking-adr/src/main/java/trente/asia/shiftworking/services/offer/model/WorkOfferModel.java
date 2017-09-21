package trente.asia.shiftworking.services.offer.model;

import java.util.List;

/**
 * Created by viet on 11/25/2016.
 */

public class WorkOfferModel{

	public static final String	OFFER_TYPE_OVERTIME					= "OTW";
	public static final String	OFFER_TYPE_HOLIDAY_WORKING			= "HDW";
	public static final String	OFFER_TYPE_SHORT_TIME				= "STO";
	public static final String	OFFER_TYPE_PAID_VACATION_ALL		= "PVAL";
	public static final String	OFFER_TYPE_PAID_VACATION_MORNING	= "PVMO";
	public static final String	OFFER_TYPE_PAID_VACATION_AFTERNOON	= "PVAF";
	public static final String	OFFER_TYPE_SPECIAL_HOLIDAY			= "SPH";
	public static final String	OFFER_TYPE_COMPENSATORY_HOLIDAY		= "CPH";
	public static final String	OFFER_TYPE_ABSENT					= "ABS";

	public WorkOfferModel(){
	}

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
	public boolean				sickAbsent;
}
