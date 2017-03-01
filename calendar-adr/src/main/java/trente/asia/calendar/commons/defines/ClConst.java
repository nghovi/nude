package trente.asia.calendar.commons.defines;

import android.content.res.Resources;

/**
 * SwConst
 *
 * @author TrungND
 */

public class ClConst {

    public static final String APP_FOLDER = "Welfare/Calendar";

//	public static final int		CALENDAR_MAX_PAGE				= 1000;

    public static final String SCHEDULE_TYPE_ALL = "1";
    public static final String SCHEDULE_TYPE_PERIOD = "2";

    public static final String SELECTED_CALENDAR_STRING =
            "SELECTED_CALENDAR_STRING";
    public static final String PREF_ACTIVE_DATE = "PREF_ACTIVE_DATE";
    public static final String PREF_ACTIVE_USER_LIST = "PREF_ACTIVE_USER_LIST";

    public static final String SCHEDULE_REPEAT_TYPE_WEEKLY = "WL";
    public static final String SCHEDULE_REPEAT_TYPE_MONTHLY = "ML";
    public static final String SCHEDULE_REPEAT_TYPE_YEARLY = "YL";
    public static final String SCHEDULE_REPEAT_TYPE_NONE = "NN";

    public static final String SCHEDULE_REPEAT_LIMIT_FOREVER = "F";
    public static final String SCHEDULE_REPEAT_LIMIT_UNTIL = "U";
    public static final String SCHEDULE_REPEAT_LIMIT_AFTER = "A";

    public static final int TEXT_VIEW_HEIGHT = (int) (Resources.getSystem()
            .getDisplayMetrics().density * 15);

    public static final String IS_UPDATE_SCHEDULE = "IS_UPDATE_SCHEDULE";
}
