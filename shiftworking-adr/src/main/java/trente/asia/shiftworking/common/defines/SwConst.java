package trente.asia.shiftworking.common.defines;

import java.util.LinkedHashMap;
import java.util.Map;

import trente.asia.shiftworking.services.offer.model.WorkOffer;

/**
 * SwConst
 *
 * @author TrungND
 */

public class SwConst {

    public static final String APP_FOLDER = "Welfare/ShiftWorking";
    public static final String SW_WORK_NOTICE_TYPE_MODIFY = "MR";

    public static final int MAX_CHECK_USER = 4;
    public static final String ACTION_TRANSIT_DELETE = "ACTION_TRANSIT_DELETE";

    public static final String KEY_HISTORY_LIST = "KEY_HISTORY_LIST";
    public static final String KEY_HISTORY_NAME = "KEY_HISTORY_NAME";

    public static Map<String, String> offerTypes = new LinkedHashMap<>();
    public static final String OFFER_TYPE_PAID_VACATION_ALL = "PVAL";
    public static final String OFFER_TYPE_PAID_VACATION_MORNING = "PVMO";
    public static final String OFFER_TYPE_PAID_VACATION_AFTERNOON = "PVAF";
    public static final String OFFER_TYPE_HOLIDAY_SPECIAL = "SPH";
    public static final String OFFER_TYPE_HOLIDAY_COMPENSATORY = "CPH";

    public static final String OFFER_TYPE_ABSENT = "ABS";
    public static final String OFFER_TYPE_OVERTIME = "OTW";
    public static final String OFFER_TYPE_HOLIDAY_WORKING = "HDW";

    static {
        offerTypes.put(OFFER_TYPE_PAID_VACATION_ALL, "vacation_all");
        offerTypes.put(OFFER_TYPE_PAID_VACATION_MORNING, "vacation_morning");
        offerTypes.put(OFFER_TYPE_PAID_VACATION_AFTERNOON, "vacation_afternoon");
        offerTypes.put(OFFER_TYPE_HOLIDAY_SPECIAL, "vacation_holiday_special");
        offerTypes.put(OFFER_TYPE_HOLIDAY_COMPENSATORY, "holiday_compensatory");
        offerTypes.put(OFFER_TYPE_ABSENT, "absent");
        offerTypes.put(OFFER_TYPE_OVERTIME, "overtime");
        offerTypes.put(OFFER_TYPE_HOLIDAY_WORKING, "holiday_working");
    }

    public static Map<Integer, String> offerStatus = new LinkedHashMap<>();

    static {
        offerStatus.put(WorkOffer.OFFER_STATUS_APPROVING, "APPROVING");
        offerStatus.put(WorkOffer.OFFER_STATUS_OFFERING, "OFFERING");
        offerStatus.put(WorkOffer.OFFER_STATUS_DISABLED, "DISABLED");
        offerStatus.put(WorkOffer.OFFER_STATUS_DONE, "DONE");
    }

    public static final String APPROVE_STATUS_YET = "0";                    // 0
    public static final String APPROVE_STATUS_REVERSE = "2";
    // 2
    public static final String APPROVE_STATUS_STOP = "3";
    // 3
    public static final String APPROVE_STATUS_ACCEPT = "1";
    // 1
    public static final String APPROVE_STATUS_DISABLED = "8";
    // 8
    public static final String APPROVE_STATUS_NA = "9";                    // 8

    public static Map<String, String> approveTypes = new LinkedHashMap<>();

    static {
        approveTypes.put(APPROVE_STATUS_YET, "YET");
        approveTypes.put(APPROVE_STATUS_REVERSE, "REVERSE");
        approveTypes.put(APPROVE_STATUS_STOP, "STOP");
        approveTypes.put(APPROVE_STATUS_ACCEPT, "ACCEPT");
        approveTypes.put(APPROVE_STATUS_DISABLED, "DISABLED");
        approveTypes.put(APPROVE_STATUS_NA, "N/A");
    }

    public static final String WORK_TYPE_STATUS_NORMAL_WORK = "11";
    public static final String WORK_TYPE_STATUS_PAID_VACT_ALL = "12";
    public static final String WORK_TYPE_STATUS_PAID_VACT_HALF = "13";
    public static final String WORK_TYPE_STATUS_ABSENT = "14";
    public static final String WORK_TYPE_STATUS_BE_LATE = "15";
    public static final String WORK_TYPE_STATUS_LEAVE_EARLY = "16";
    public static final String WORK_TYPE_STATUS_SPCIAL_VACT = "17";
    public static final String WORK_TYPE_STATUS_COMP_HOLIDAY = "18";
    public static final String WORK_TYPE_STATUS_NORMAL_HOLIDAY = "21";
    public static final String WORK_TYPE_STATUS_HOLIDAY_WORK = "22";
    public static final String WORK_TYPE_STATUS_NO_WORK = "23";

    public static Map<String, String> workTypeMap = new LinkedHashMap<String,
            String>();

    static {
        workTypeMap.put(WORK_TYPE_STATUS_NORMAL_WORK, "Normal work");
        workTypeMap.put(WORK_TYPE_STATUS_PAID_VACT_ALL, "Paid vacation (all)");
        workTypeMap.put(WORK_TYPE_STATUS_PAID_VACT_HALF, "Paid vacation " + "" +
                "(half)");
        workTypeMap.put(WORK_TYPE_STATUS_ABSENT, "Absent");
        workTypeMap.put(WORK_TYPE_STATUS_BE_LATE, "Be late");
        workTypeMap.put(WORK_TYPE_STATUS_LEAVE_EARLY, "Leave early");
        workTypeMap.put(WORK_TYPE_STATUS_SPCIAL_VACT, "Special vacation");
        workTypeMap.put(WORK_TYPE_STATUS_COMP_HOLIDAY, "Compensatory holiday");
        workTypeMap.put(WORK_TYPE_STATUS_NORMAL_HOLIDAY, "Normal holiday");
        workTypeMap.put(WORK_TYPE_STATUS_HOLIDAY_WORK, "Holiday work");
        workTypeMap.put(WORK_TYPE_STATUS_NO_WORK, "Not working");
    }

    public static Map<String, String> workTypeHolidayMap = new
            LinkedHashMap<String, String>();

    static {
        workTypeHolidayMap.put(WORK_TYPE_STATUS_NORMAL_HOLIDAY, "Normal " +
                "holiday");
        workTypeHolidayMap.put(WORK_TYPE_STATUS_HOLIDAY_WORK, "Holiday work");
    }

    public static Map<String, String> workTypeNormalDayMap = new
            LinkedHashMap<String, String>();

    static {
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_NORMAL_WORK, "Normal work");
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_PAID_VACT_ALL, "Paid " +
                "vacation (all)");
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_PAID_VACT_HALF, "Paid " +
                "vacation (half)");
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_ABSENT, "Absent");
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_BE_LATE, "Be late");
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_LEAVE_EARLY, "Leave early");
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_SPCIAL_VACT, "Special " +
                "vacation");
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_COMP_HOLIDAY,
                "Compensatory" + " holiday");
        workTypeNormalDayMap.put(WORK_TYPE_STATUS_NO_WORK, "Not working");
    }

    public static final String WORK_SUMMARY_STATUS_UPDATE = "U";
    public static final String WORK_SUMMARY_STATUS_APPROVE = "A";
    public static final String WORK_SUMMARY_STATUS_YET = "YET";

    public static Map<String, String> workSummaryStatusMap = new
            LinkedHashMap<>();

    static {
        workSummaryStatusMap.put(WORK_SUMMARY_STATUS_UPDATE, "Updated");
        workSummaryStatusMap.put(WORK_SUMMARY_STATUS_APPROVE, "Approved");
        workSummaryStatusMap.put(WORK_SUMMARY_STATUS_YET, "Yet");
    }
}
