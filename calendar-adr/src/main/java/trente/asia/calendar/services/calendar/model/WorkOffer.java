package trente.asia.calendar.services.calendar.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Date;

import trente.asia.calendar.commons.activites.MainClActivity;

/**
 * HolidayModel
 *
 * @author VietNH
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
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

	public String	userColor	= "#FFFFFF";
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

	@JsonField(typeConverter = MainClActivity.WelfareTimeConverter.class)
	public Date		startDate;
	@JsonField(typeConverter = MainClActivity.WelfareTimeConverter.class)
	public Date		endDate;
	public Date		startDateObj;
	public Date		endDateObj;

}
