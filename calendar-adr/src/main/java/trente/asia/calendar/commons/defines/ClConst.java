package trente.asia.calendar.commons.defines;

import android.content.res.Resources;

/**
 * SwConst
 *
 * @author TrungND
 */

public class ClConst{

	public static final String	APP_FOLDER									= "Welfare/Calendar";

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

	public static final int		TEXT_VIEW_HEIGHT							= (int)(Resources.getSystem().getDisplayMetrics().density * 15);

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
}
