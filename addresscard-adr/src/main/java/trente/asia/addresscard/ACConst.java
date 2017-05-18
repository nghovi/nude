package trente.asia.addresscard;

import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * SwConst
 *
 * @author TrungND
 */

public class ACConst {

	public static final String	APP_FOLDER									= "Welfare/AddressCard";

	public static final String	SCHEDULE_TYPE_ALL							= "1";
	public static final String	SCHEDULE_TYPE_PERIOD						= "2";

	public static final String	SELECTED_CALENDAR_STRING					= "SELECTED_CALENDAR_STRING";
	public static final String	PREF_ACTIVE_DATE							= "PREF_ACTIVE_DATE";
	public static final String	PREF_ACTIVE_USER_LIST						= "PREF_ACTIVE_USER_LIST";

	public static final String	SCHEDULE_REPEAT_TYPE_WEEKLY					= "WL";
	public static final String	SCHEDULE_REPEAT_TYPE_MONTHLY				= "ML";
	public static final String	SCHEDULE_REPEAT_TYPE_YEARLY					= "YL";
	public static final String	SCHEDULE_REPEAT_TYPE_NONE					= "NN";

	public static final String	SCHEDULE_REPEAT_LIMIT_FOREVER				= "F";
	public static final String	SCHEDULE_REPEAT_LIMIT_UNTIL					= "U";
	// public static final String SCHEDULE_REPEAT_LIMIT_AFTER = "A";

	public static final String	SCHEDULE_MODIFY_TYPE_ALL					= "AL";
	public static final String	SCHEDULE_MODIFY_TYPE_ONLY_THIS				= "OT";
	public static final String	SCHEDULE_MODIFY_TYPE_ONLY_FUTURE			= "OF";

	public static final int		TEXT_VIEW_HEIGHT							= WelfareUtil.dpToPx(15);

	public static final String	SCHEDULE_TYPE_HOLIDAY						= "H";
	public static final String	SCHEDULE_TYPE_WORK_OFFER					= "O";
	public static final String	SCHEDULE_TYPE_BIRTHDAY						= "B";

	public static final String	SCHEDULE_COLOR_NORMAL						= "444444";
	public static final String	SCHEDULE_COLOR_HOLIDAY						= "D22DB6";
	public static final String	SCHEDULE_COLOR_OFFER						= "359CF3";
	public static final String	SCHEDULE_COLOR_BIRTHDAY						= "24C772";

	public static final String	ACTION_SCHEDULE_DELETE						= "ACTION_SCHEDULE_DELETE";
	public static final String	ACTION_SCHEDULE_UPDATE						= "ACTION_SCHEDULE_UPDATE";
	public static final String	ACTION_SCHEDULE_UPDATE_NEW_KEY				= "ACTION_SCHEDULE_UPDATE_NEW_KEY";

	public static final String	WORKING_OFFER_TYPE_PAID_VACATION_ALL		= "PVAL";
	public static final String	WORKING_OFFER_TYPE_PAID_VACATION_MORNING	= "PVMO";
	public static final String	WORKING_OFFER_TYPE_PAID_VACATION_AFTERNOON	= "PVAF";
	public static final String	WORKING_OFFER_TYPE_SPECIAL_HOLIDAY			= "SPH";
	public static final String	WORKING_OFFER_TYPE_COMPENSATORY_HOLIDAY		= "CPH";
	public static final String	WORKING_OFFER_TYPE_ABSENT					= "ABS";
	public static final String	WORKING_OFFER_TYPE_OVERTIME_WORKING			= "OTW";
	public static final String	WORKING_OFFER_TYPE_HOLIDAY_WORKING			= "HDW";
	public static final String	WORKING_OFFER_TYPE_SHORT_TIME				= "STO";

	/**
	 * API URL
	 */
	public static final String AC_BUSINESS_CATEGORY_LIST					= "/api/ac/business/category/list";
	public static final String AC_BUSINESS_CATEGORY_DETAIL					= "/api/ac/business/category/detail";
	public static final String AC_BUSINESS_CATEGORY_UPDATE					= "/api/ac/business/category/update";

	public static final String AC_BUSINESS_CUSTOMER_LIST					= "/api/ac/business/customer/list";
	public static final String AC_BUSINESS_CUSTOMER_DETAIL					= "/api/ac/business/customer/detail";
	public static final String AC_BUSINESS_CUSTOMER_UPDATE					= "/api/ac/business/customer/update";
	public static final String AC_BUSINESS_CUSTOMER_DELETE					= "/api/ac/business/customer/delete";
	public static final String AC_BUSINESS_CUSTOMER_CREATE					= "/api/ac/business/customer/create";

	public static final String AC_BUSINESS_CARD_LIST 						= "/api/ac/business/card/list";
	public static final String AC_BUSINESS_CARD_DETAIL 						= "/api/ac/business/card/detail";
	public static final String AC_BUSINESS_CARD_NEW 						= "/api/ac/business/card/new";
	public static final String AC_BUSINESS_CARD_UPDATE 						= "/api/ac/business/card/update";
	public static final String AC_BUSINESS_CARD_DELETE 						= "/api/ac/business/card/delete";
	public static final String AC_BUSINESS_CARD_UNGROUP 					= "/api/ac/business/card/ungroup";

	public static final String AC_BUSINESS_COMMENT_LIST 					= "/api/ac/business/comment/list";
	public static final String AC_BUSINESS_COMMENT_UPDATE 					= "/api/ac/business/comment/update";
	public static final String AC_BUSINESS_COMMENT_DELETE 					= "/api/ac/business/comment/delete";

	/**
	 * AC Constants
	 */
	public static final int AC_REQUEST_CODE_TAKE_CAPTURE 					= 1;
}
