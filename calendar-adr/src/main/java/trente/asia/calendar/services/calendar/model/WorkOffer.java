package trente.asia.calendar.services.calendar.model;

/**
 * HolidayModel
 *
 * @author VietNH
 */

public class WorkOffer{

	public static final String	OFFER_TYPE_OVERTIME					= "OTW";
	public static final String	OFFER_TYPE_PAID_VACATION_ALL		= "PVAL";
	public static final String	OFFER_TYPE_PAID_VACATION_MORNING	= "PVMO";
	public static final String	OFFER_TYPE_PAID_VACATION_AFTERNOON	= "PVAF";
	public static final String	OFFER_TYPE_SPECIAL_HOLIDAY			= "SPH";
	public static final String	OFFER_TYPE_COMPENSATORY_HOLIDAY		= "CPH";
	public static final String	OFFER_TYPE_ABSENT					= "ABS";
	public static final String	OFFER_TYPE_OVERTIME_WORKING			= "OTW";
	public static final String	OFFER_TYPE_HOLIDAY_WORKING			= "HDW";
	public static final String	OFFER_TYPE_SHORT_TIME				= "STO";

	public WorkOffer(){
	}

	public String	userId;
	public String	userName;
	public String	offerType;
	public String	offerTypeName;
	public String	startDateString;
	public String	startTimeString;
	public String	endDateString;
	public String	endTimeString;
	public String	approveUser1;
	public String	approveResult1;
	public String	approveUser2;
	public String	approveResult2;

	public String	requestDateString;
	public String	approveResult;
	public String	userAvatarPath;
	public String	offerStatus;
	public String	offerStatusName;
	public String	key;
	public String	note;
	public String	companyId;

	public String	startDate;
	public String	endDate;

}
