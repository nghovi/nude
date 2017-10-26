package trente.asia.calendar.commons.defines;

import trente.asia.welfare.adr.utils.WelfareUtil;

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
	public static final String	PREF_SELECT_USER_LIST						= "PREF_SELECT_USER_LIST";
	public static final String	PREF_FILTER_TYPE							= "PREF_FILTER_TYPE";
	public static final String	PREF_FILTER_TYPE_ROOM						= "ROOM";
	public static final String	PREF_FILTER_TYPE_USER						= "USER";

	public static final String	SCHEDULE_REPEAT_TYPE_WEEKLY					= "WL";
	public static final String	SCHEDULE_REPEAT_TYPE_MONTHLY				= "ML";
	public static final String	SCHEDULE_REPEAT_TYPE_NONE					= "NN";

	public static final String	SCHEDULE_REPEAT_LIMIT_FOREVER				= "F";
	public static final String	SCHEDULE_REPEAT_LIMIT_UNTIL					= "U";

	public static final String	SCHEDULE_MODIFY_TYPE_ALL					= "AL";
	public static final String	SCHEDULE_MODIFY_TYPE_ONLY_THIS				= "OT";
	public static final String	SCHEDULE_MODIFY_TYPE_ONLY_FUTURE			= "OF";

	public static final int		TEXT_VIEW_HEIGHT							= WelfareUtil.dpToPx(18);

	public static final String	ACTION_SCHEDULE_DELETE						= "ACTION_SCHEDULE_DELETE";
	public static final String	ACTION_SCHEDULE_UPDATE						= "ACTION_SCHEDULE_UPDATE";
	public static final String	ACTION_SCHEDULE_UPDATE_NEW_KEY				= "ACTION_SCHEDULE_UPDATE_NEW_KEY";


	public static final String	API_CALENDAR_LIST							= "/api/cl/calendars";

	public static final String	API_SCHEDULE_LIST							= "/api/cl/schedule/list";
	public static final String	API_SCHEDULE_DETAIL							= "/api/cl/schedule/detail";
	public static final String	API_SCHEDULE_CHECK_UPDATE					= "/api/cl/schedule/checkupd";
	public static final String	API_SCHEDULE_UPDATE							= "/api/cl/schedule/upd";
	public static final String	API_SCHEDULE_DEL							= "/api/cl/schedule/del";

	public static final String	API_TODO_LIST								= "/api/cl/todo/list";
	public static final String	API_TODO_DETAIL								= "/api/cl/todo/detail";
	public static final String	API_TODO_UPDATE								= "/api/cl/todo/update";
	public static final String	API_TODO_DELETE								= "/api/cl/todo/delete";

	public static final String	API_FILTER									= "/api/cl/filter";
	public static final String	PREF_ACTIVE_ROOM							= "PREF_ACTIVE_ROOM";
}
