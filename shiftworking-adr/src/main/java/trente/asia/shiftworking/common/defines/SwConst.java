package trente.asia.shiftworking.common.defines;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import trente.asia.shiftworking.services.offer.WorkOfferDetailFragment;
import trente.asia.shiftworking.services.offer.model.WorkOffer;

/**
 * SwConst
 *
 * @author TrungND
 */

public class SwConst{

	public static final String			APP_FOLDER					= "Welfare/ShiftWorking";
	public static final String			SW_WORK_NOTICE_TYPE_MODIFY	= "MR";

	public static final int				MAX_CHECK_USER				= 4;
	public static final String			ACTION_TRANSIT_DELETE		= "ACTION_TRANSIT_DELETE";

	public static final String			KEY_HISTORY_LIST			= "KEY_HISTORY_LIST";
    public static final String			KEY_HISTORY_NAME			= "KEY_HISTORY_NAME";

	public static Map<String, String>	offerTypes					= new LinkedHashMap<>();
	public static final String			OFFER_TYPE_ABSENT			= "1";
	public static final String			OFFER_TYPE_HOLIDAY			= "2";
	public static final String			OFFER_TYPE_OVERTIME			= "3";

	static{
		offerTypes.put(OFFER_TYPE_ABSENT, "Absent");
		offerTypes.put(OFFER_TYPE_HOLIDAY, "Holiday");
		offerTypes.put(OFFER_TYPE_OVERTIME, "Overtime");
	}

	public static Map<String, String>	subTypesAbsent					= new LinkedHashMap<>();
	public static final String			SUB_TYPE_ABSENT_ALL				= "All";
	public static final String			SUB_TYPE_ABSENT_MORNING			= "Morning";
	public static final String			SUB_TYPE_ABSENT_AFTERNOON		= "Afternoon";
	public static final String			SUB_TYPE_ABSENT_COMPENSATORY	= "Compensatory";

	public static final String			SUB_TYPE_ABSENT_SPECIAL			= "Special";
	public static final String			SUB_TYPE_ABSENT_OTHER			= "Other";

	static{
		subTypesAbsent.put("1", SUB_TYPE_ABSENT_ALL);
		subTypesAbsent.put("2", SUB_TYPE_ABSENT_MORNING);
		subTypesAbsent.put("3", SUB_TYPE_ABSENT_AFTERNOON);
		subTypesAbsent.put("4", SUB_TYPE_ABSENT_COMPENSATORY);
		subTypesAbsent.put("5", SUB_TYPE_ABSENT_SPECIAL);
		subTypesAbsent.put("9", SUB_TYPE_ABSENT_OTHER);

	}

	public static Map<String, String>	subTypesHoliday				= new LinkedHashMap<>();
	public static final String			SUB_TYPE_HOLIDAY_WORK		= "Work";
	public static final String			SUB_TYPE_HOLIDAY_NO_WORK	= "No Work";
	public static final String			SUB_TYPE_HOLIDAY_OTHER		= "Other";

	static{
		subTypesHoliday.put("1", SUB_TYPE_HOLIDAY_WORK);
		subTypesHoliday.put("2", SUB_TYPE_HOLIDAY_NO_WORK);
		subTypesHoliday.put("3", SUB_TYPE_HOLIDAY_OTHER);
	}

	public static Map<String, String>	subTypesOverTime			= new LinkedHashMap<>();
	public static final String			SUB_TYPE_OVER_TIME_WORK		= "Work";
	public static final String			SUB_TYPE_OVER_TIME_NO_WORK	= "No Work";
	public static final String			SUB_TYPE_OVER_TIME_OTHER	= "Other";

	static{
		subTypesOverTime.put("1", SUB_TYPE_OVER_TIME_WORK);
		subTypesOverTime.put("2", SUB_TYPE_OVER_TIME_NO_WORK);
		subTypesOverTime.put("3", SUB_TYPE_OVER_TIME_OTHER);
	}

	public static Map<String, Map<String, String>> subTypes = new LinkedHashMap<>();

	static{
		subTypes.put(OFFER_TYPE_ABSENT, subTypesAbsent);
		subTypes.put(OFFER_TYPE_HOLIDAY, subTypesHoliday);
		subTypes.put(OFFER_TYPE_OVERTIME, subTypesOverTime);
	}

	public static Map<Integer, String> offerStatus = new LinkedHashMap<>();

	static{
		offerStatus.put(WorkOffer.OFFER_STATUS_APPROVING, "APPROVING");
		offerStatus.put(WorkOffer.OFFER_STATUS_OFFERING, "OFFERING");
		offerStatus.put(WorkOffer.OFFER_STATUS_DISABLED, "DISABLED");
		offerStatus.put(WorkOffer.OFFER_STATUS_DONE, "DONE");
	}

	public static final String			APPROVE_STATUS_YET		= "0";					// 0
	public static final String			APPROVE_STATUS_REVERSE	= "2";					// 2
	public static final String			APPROVE_STATUS_STOP		= "3";					// 3
	public static final String			APPROVE_STATUS_ACCEPT	= "1";					// 1
	public static final String			APPROVE_STATUS_DISABLED	= "8";					// 8
	public static final String			APPROVE_STATUS_NA		= "9";					// 8

	public static Map<String, String>	approveTypes			= new LinkedHashMap<>();

	static{
		approveTypes.put(APPROVE_STATUS_YET, "YET");
		approveTypes.put(APPROVE_STATUS_REVERSE, "REVERSE");
		approveTypes.put(APPROVE_STATUS_STOP, "STOP");
		approveTypes.put(APPROVE_STATUS_ACCEPT, "ACCEPT");
		approveTypes.put(APPROVE_STATUS_DISABLED, "DISABLED");
		approveTypes.put(APPROVE_STATUS_NA, "N/A");
	}

	public static WorkOfferDetailFragment.ApproveStatus						YYG1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_YET, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						YYG2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_YET, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						YYA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_YET, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						YYA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_YET, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						YAA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_ACCEPT, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						YAA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_ACCEPT, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						YSG1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_STOP, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						YSG2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_STOP, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						YSA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_STOP, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						YSA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_STOP, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						YNG1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_NA, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						YNA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_YET, APPROVE_STATUS_NA, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						AYA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_YET, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						AYG2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_YET, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						AYA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_YET, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						AAA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_ACCEPT, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						AAA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_ACCEPT, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						ASG2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_STOP, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						ASA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_STOP, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						ASA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_STOP, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						ANA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_ACCEPT, APPROVE_STATUS_NA, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						SYG1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_YET, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						SYG2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_YET, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						SYA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_YET, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						SYA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_YET, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						SAA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_ACCEPT, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						SAA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_ACCEPT, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						SSG1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_STOP, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						SSG2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_STOP, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						SSA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_STOP, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						SSA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_STOP, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						SNG1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_NA, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						SNA1					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_STOP, APPROVE_STATUS_NA, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE1);
	public static WorkOfferDetailFragment.ApproveStatus						NYG2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_NA, APPROVE_STATUS_YET, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						NYA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_NA, APPROVE_STATUS_YET, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						NAA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_NA, APPROVE_STATUS_ACCEPT, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						NSG2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_NA, APPROVE_STATUS_STOP, 0, WorkOfferDetailFragment.ApproveStatus.APPROVE2);
	public static WorkOfferDetailFragment.ApproveStatus						NSA2					= new WorkOfferDetailFragment.ApproveStatus(APPROVE_STATUS_NA, APPROVE_STATUS_STOP, 1, WorkOfferDetailFragment.ApproveStatus.APPROVE2);

	public static Map<WorkOfferDetailFragment.ApproveStatus, List<String>>	approveStatusListMap	= new LinkedHashMap<>();

	static{
		approveStatusListMap.put(YYG1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(YYG2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(YYA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(YYA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(YAA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(YAA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(YSG1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(YSG2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(YSA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(YSA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));

		approveStatusListMap.put(YNG1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(YNA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(AYA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(AYG2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(AYA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(AAA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(AAA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(ASG2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(ASA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(ASA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(ANA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(SYG1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(SYG2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(SYA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(SYA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(SAA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(SAA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(SSG1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(SSG2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(SSA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(SSA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(SNG1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(SNA1, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(NYG2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(NYA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(NAA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_DISABLED)));
		approveStatusListMap.put(NSG2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP)));
		approveStatusListMap.put(NSA2, Arrays.asList(approveTypes.get(APPROVE_STATUS_ACCEPT), approveTypes.get(APPROVE_STATUS_REVERSE), approveTypes.get(APPROVE_STATUS_STOP), approveTypes.get(APPROVE_STATUS_DISABLED)));
	}

	public static final String			WORK_TYPE_STATUS_NORMAL_WORK	= "11";
	public static final String			WORK_TYPE_STATUS_PAID_VACT_ALL	= "12";
	public static final String			WORK_TYPE_STATUS_PAID_VACT_HALF	= "13";
	public static final String			WORK_TYPE_STATUS_ABSENT			= "14";
	public static final String			WORK_TYPE_STATUS_BE_LATE		= "15";
	public static final String			WORK_TYPE_STATUS_LEAVE_EARLY	= "16";
	public static final String			WORK_TYPE_STATUS_SPCIAL_VACT	= "17";
	public static final String			WORK_TYPE_STATUS_COMP_HOLIDAY	= "18";
	public static final String			WORK_TYPE_STATUS_NORMAL_HOLIDAY	= "21";
	public static final String			WORK_TYPE_STATUS_HOLIDAY_WORK	= "22";
	public static final String			WORK_TYPE_STATUS_NO_WORK		= "23";

	public static Map<String, String>	workTypeMap						= new LinkedHashMap<String, String>();

	static{
		workTypeMap.put(WORK_TYPE_STATUS_NORMAL_WORK, "Normal work");
		workTypeMap.put(WORK_TYPE_STATUS_PAID_VACT_ALL, "Paid vacation (all)");
		workTypeMap.put(WORK_TYPE_STATUS_PAID_VACT_HALF, "Paid vacation " + "(half)");
		workTypeMap.put(WORK_TYPE_STATUS_ABSENT, "Absent");
		workTypeMap.put(WORK_TYPE_STATUS_BE_LATE, "Be late");
		workTypeMap.put(WORK_TYPE_STATUS_LEAVE_EARLY, "Leave early");
		workTypeMap.put(WORK_TYPE_STATUS_SPCIAL_VACT, "Special vacation");
		workTypeMap.put(WORK_TYPE_STATUS_COMP_HOLIDAY, "Compensatory holiday");
		workTypeMap.put(WORK_TYPE_STATUS_NORMAL_HOLIDAY, "Normal holiday");
		workTypeMap.put(WORK_TYPE_STATUS_HOLIDAY_WORK, "Holiday work");
		workTypeMap.put(WORK_TYPE_STATUS_NO_WORK, "Not working");
	}

	public static Map<String, String> workTypeHolidayMap = new LinkedHashMap<String, String>();

	static{
		workTypeHolidayMap.put(WORK_TYPE_STATUS_NORMAL_HOLIDAY, "Normal " + "holiday");
		workTypeHolidayMap.put(WORK_TYPE_STATUS_HOLIDAY_WORK, "Holiday work");
	}

	public static Map<String, String> workTypeNormalDayMap = new LinkedHashMap<String, String>();

	static{
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_NORMAL_WORK, "Normal work");
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_PAID_VACT_ALL, "Paid " + "vacation (all)");
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_PAID_VACT_HALF, "Paid " + "vacation (half)");
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_ABSENT, "Absent");
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_BE_LATE, "Be late");
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_LEAVE_EARLY, "Leave early");
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_SPCIAL_VACT, "Special " + "vacation");
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_COMP_HOLIDAY, "Compensatory" + " holiday");
		workTypeNormalDayMap.put(WORK_TYPE_STATUS_NO_WORK, "Not working");
	}

	public static final String			WORK_SUMMARY_STATUS_UPDATE	= "U";
	public static final String			WORK_SUMMARY_STATUS_APPROVE	= "A";
	public static final String			WORK_SUMMARY_STATUS_YET		= "YET";

	public static Map<String, String>	workSummaryStatusMap		= new LinkedHashMap<>();

	static{
		workSummaryStatusMap.put(WORK_SUMMARY_STATUS_UPDATE, "Updated");
		workSummaryStatusMap.put(WORK_SUMMARY_STATUS_APPROVE, "Approved");
		workSummaryStatusMap.put(WORK_SUMMARY_STATUS_YET, "Yet");
	}
}
