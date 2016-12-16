package trente.asia.shiftworking.common.defines;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SwConst
 *
 * @author TrungND
 */

public class SwConst{

	public static final String			APP_FOLDER						= "Welfare/ShiftWorking";
	public static final String			SW_WORK_NOTICE_TYPE_MODIFY		= "MR";

	public static final int				MAX_CHECK_USER					= 4;
	public static final String			ACTION_TRANSIT_DELETE			= "ACTION_TRANSIT_DELETE";
	public static final String			ACTION_TRANSIT_UPDATE			= "ACTION_TRANSIT_UPDATE";
	public static final String			ACTION_OFFER_DELETE				= "ACTION_OFFER_DELETE";
    public static final String			ACTION_OFFER_UPDATE				= "ACTION_OFFER_UPDATE";

	public static final String			KEY_HISTORY_LIST				= "KEY_HISTORY_LIST";
	public static final String			KEY_HISTORY_NAME				= "KEY_HISTORY_NAME";

	public static final String			OFFER_CAN_EDIT_DELETE			= "ME";
	public static final String			OFFER_CAN_APPROVE				= "AP";
	public static final String			OFFER_ONLY_SHOW					= "VW";
    public static final String			OFFER_CAN_ONLY_EDIT			= "ME";

	public static final String			SW_TRANSIT_STATUS_DRAFT			= "DRFT";
	public static final String			SW_TRANSIT_STATUS_DONE			= "DONE";

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
		workTypeMap.put(WORK_TYPE_STATUS_PAID_VACT_HALF, "Paid vacation " + "" + "(half)");
		workTypeMap.put(WORK_TYPE_STATUS_ABSENT, "Absent");
		workTypeMap.put(WORK_TYPE_STATUS_BE_LATE, "Be late");
		workTypeMap.put(WORK_TYPE_STATUS_LEAVE_EARLY, "Leave early");
		workTypeMap.put(WORK_TYPE_STATUS_SPCIAL_VACT, "Special vacation");
		workTypeMap.put(WORK_TYPE_STATUS_COMP_HOLIDAY, "Compensatory holiday");
		workTypeMap.put(WORK_TYPE_STATUS_NORMAL_HOLIDAY, "Normal holiday");
		workTypeMap.put(WORK_TYPE_STATUS_HOLIDAY_WORK, "Holiday work");
		workTypeMap.put(WORK_TYPE_STATUS_NO_WORK, "Not working");
	}

	public static final String	WORK_SUMMARY_STATUS_UPDATE	= "U";
	public static final String	WORK_SUMMARY_STATUS_APPROVE	= "A";
	public static final String	WORK_SUMMARY_STATUS_YET		= "YET";
}
