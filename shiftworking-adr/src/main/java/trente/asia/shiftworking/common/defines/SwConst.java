package trente.asia.shiftworking.common.defines;

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
    public static final String ACTION_TRANSIT_UPDATE = "ACTION_TRANSIT_UPDATE";
    public static final String ACTION_OFFER_DELETE = "ACTION_OFFER_DELETE";
    public static final String ACTION_OFFER_UPDATE = "ACTION_OFFER_UPDATE";

    public static final String KEY_HISTORY_LIST = "KEY_HISTORY_LIST";
    public static final String KEY_HISTORY_NAME = "KEY_HISTORY_NAME";

    public static final String OFFER_CAN_EDIT_DELETE = "ME";
    public static final String OFFER_CAN_APPROVE = "AP";
    public static final String OFFER_ONLY_SHOW = "VW";
    public static final String OFFER_CAN_ONLY_EDIT = "OM";
    public static final String OFFER_ONLY_DELETE = "OD";

    public static final String SW_OFFER_EXEC_TYPE_VIEW = "SW_EXEC_TYPE_VIEW";
    public static final String SW_OFFER_EXEC_TYPE_APR = "SW_EXEC_TYPE_APR";

    public static final String SW_TRANSIT_STATUS_DRAFT = "DRFT";
    public static final String SW_TRANSIT_STATUS_DONE = "DONE";

    public static final String SW_SHIFTWORKING_TYPE_SHIFT = "SI";
    public static final String SW_SHIFTWORKING_TYPE_SUMMARY = "SU";
    public static final String SW_SHIFTWORKING_TYPE_TITLE = "TI";

    public static final String SW_TRANSIT_TYPE_BUS = "BU";
    public static final String SW_TRANSIT_TYPE_TRAIN = "TR";
    public static final String SW_TRANSIT_TYPE_TAXI = "TX";
    public static final String SW_TRANSIT_TYPE_OTHER = "OT";

    public static final String SW_TRANSIT_WAY_TYPE_ONE_WAY = "1W";
    public static final String SW_TRANSIT_WAY_TYPE_TWO_WAY = "2W";

    public static final String SW_WORK_TIME_TYPE_START_TIME = "ST";
    public static final String SW_WORK_TIME_TYPE_END_TIME = "ET";
    public static final String SW_WORK_TIME_TYPE_UNDEFINED = "UN";
    /**
     * shift working API
     */
    public static final String API_PROJECT_LIST = "/api/sw/project/list";
    public static final String API_CHECKIN = "/api/sw/checkin";
    public static final String API_CHECKIN_LIST = "/api/sw/checkin/list";
    public static final String API_CHECKIN_DEL = "/api/sw/checkin/delete";
    public static final String API_NOTICE_LIST = "/api/sw/notice/list";
    public static final String API_NOTICE_UPDATE = "/api/sw/notice/update";
    public static final String API_NOTICE_FORM = "/api/sw/notice/form";
    public static final String API_NOTICE_CHECK = "/api/sw/notice/check";
    public static final String API_CHECK = "/api/sw/check";
    public static final String API_TRANSIT_LIST = "/api/sw/transit/list";
    public static final String API_TRANSIT_DETAIL = "/api/sw/transit/detail";
    public static final String API_TRANSIT_UPDATE = "/api/sw/transit/update";
    public static final String API_TRANSIT_DELETE = "/api/sw/transit/delete";
    public static final String API_WORK_USERS = "/api/sw/work/users";
    public static final String API_WORK_HISTORY = "/api/sw/work/history";
    public static final String API_VACATION_LIST = "/api/sw/vacation/list";
    public static final String API_VACATION_DETAIL = "/api/sw/vacation/detail";
    public static final String API_OVERTIME_LIST = "/api/sw/overtime/list";
    public static final String API_OVERTIME_DETAIL = "/api/sw/overtime/detail";
    public static final String API_OVERTIME_UPDATE = "/api/sw/overtime/update";
    public static final String API_OVERTIME_DELETE = "/api/sw/overtime/delete";
    public static final String API_HOLIDAY_WORKING_LIST = "/api/sw/holiday/list";
    public static final String API_HOLIDAY_WORKING_DETAIL = "/api/sw/holiday/detail";
    public static final String API_OFFER_UPDATE = "/api/sw/offer/update";
    public static final String API_OFFER_APPROVE = "/api/sw/offer/approve";
    public static final String API_OFFER_DELETE = "/api/sw/offer/delete";
    public static final String API_UPDATE_CHECKIN_TYPE = "api/sw/checkintype/update";

}
