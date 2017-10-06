package trente.asia.calendar.services.calendar.model;

import java.util.Date;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.HolidayWorkingModel;
import trente.asia.welfare.adr.models.OvertimeRequestModel;
import trente.asia.welfare.adr.models.VacationRequestModel;

/**
 * HolidayModel
 *
 * @author VietNH
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class WorkRequest{

	public static final String	REQUEST_TYPE_OVERTIME					= "OTW";
	public static final String	REQUEST_TYPE_PAID_VACATION_ALL			= "PVAL";
	public static final String	REQUEST_TYPE_PAID_VACATION_MORNING		= "PVMO";
	public static final String	REQUEST_TYPE_PAID_VACATION_AFTERNOON	= "PVAF";
	public static final String	REQUEST_TYPE_SPECIAL_HOLIDAY			= "SPH";
	public static final String	REQUEST_TYPE_COMPENSATORY_HOLIDAY		= "CPH";
	public static final String	REQUEST_TYPE_ABSENT						= "ABS";
	public static final String	REQUEST_TYPE_OVERTIME_WORKING			= "OTW";
	public static final String	REQUEST_TYPE_HOLIDAY_WORKING			= "HDW";
	public static final String	REQUEST_TYPE_SHORT_TIME					= "STO";

	public WorkRequest(){

	}

	public WorkRequest(VacationRequestModel vacationRequestModel){
		offerTypeName = vacationRequestModel.vacationName;
		offerType = REQUEST_TYPE_PAID_VACATION_ALL;

		userId = vacationRequestModel.userId;
		userName = vacationRequestModel.userName;
		startDateString = vacationRequestModel.startDateString;
		startTimeString = "00:00";
		endDateString = vacationRequestModel.endDateString;
		endTimeString = "00:00";
		approveUser1 = vacationRequestModel.approveUser1;
		approveResult1 = vacationRequestModel.approveResult1;
		approveUser2 = vacationRequestModel.approveUser2;
		approveResult2 = vacationRequestModel.approveResult2;

		requestDateString = vacationRequestModel.requestDateString;
		approveResult = vacationRequestModel.approveResult1;
		userAvatarPath = vacationRequestModel.userAvatarPath;
		offerStatus = vacationRequestModel.offerStatus;
		offerStatusName = vacationRequestModel.offerStatusName;
		key = vacationRequestModel.key;
		note = vacationRequestModel.note;
		companyId = vacationRequestModel.companyId;

		startDate = vacationRequestModel.startDate;
		endDate = vacationRequestModel.endDate;
		userColor = vacationRequestModel.userColor;
	}

	public WorkRequest(OvertimeRequestModel overtimeRequestModel, String offerTypeName){
		offerType = REQUEST_TYPE_PAID_VACATION_ALL;
		this.offerTypeName = offerTypeName;

		userId = overtimeRequestModel.userId;
		userName = overtimeRequestModel.userName;
		startDateString = overtimeRequestModel.startDateString;
		startTimeString = "00:00";
		endDateString = overtimeRequestModel.endDateString;
		endTimeString = "00:00";
		approveUser1 = overtimeRequestModel.approveUser1;
		approveResult1 = overtimeRequestModel.approveResult1;
		approveUser2 = overtimeRequestModel.approveUser2;
		approveResult2 = overtimeRequestModel.approveResult2;
		requestDateString = overtimeRequestModel.requestDateString;
		approveResult = overtimeRequestModel.approveResult1;
		userAvatarPath = overtimeRequestModel.userAvatarPath;
		offerStatus = overtimeRequestModel.offerStatus;
		offerStatusName = overtimeRequestModel.offerStatusName;
		key = overtimeRequestModel.key;
		note = overtimeRequestModel.note;
		companyId = overtimeRequestModel.companyId;
		startDate = overtimeRequestModel.startDate;
		endDate = overtimeRequestModel.endDate;
		userColor = overtimeRequestModel.userColor;
	}

	public WorkRequest(HolidayWorkingModel holidayWorkingModel, String offerTypeName){
		startDate = holidayWorkingModel.startDate;
		endDate = holidayWorkingModel.startDate;

		userId = holidayWorkingModel.userId;
		userName = holidayWorkingModel.userName;
		this.offerTypeName = offerTypeName;
		startDateString = holidayWorkingModel.startDateString;
		startTimeString = "00:00";
		endDateString = holidayWorkingModel.startDateString;
		endTimeString = "00:00";
		approveUser1 = holidayWorkingModel.approveUser1;
		approveResult1 = holidayWorkingModel.approveResult1;
		approveUser2 = holidayWorkingModel.approveUser2;
		approveResult2 = holidayWorkingModel.approveResult2;
		requestDateString = holidayWorkingModel.requestDateString;
		approveResult = holidayWorkingModel.approveResult1;
		userAvatarPath = holidayWorkingModel.userAvatarPath;
		offerStatus = holidayWorkingModel.offerStatus;
		offerStatusName = holidayWorkingModel.offerStatusName;
		key = holidayWorkingModel.key;
		note = holidayWorkingModel.note;
		companyId = holidayWorkingModel.companyId;
		userColor = holidayWorkingModel.userColor;
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

	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date		startDate;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date		endDate;
}
