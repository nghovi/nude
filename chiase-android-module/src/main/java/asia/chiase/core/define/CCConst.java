package asia.chiase.core.define;

/**
 * <strong>CCConst</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCConst{

	/**
	 * Screen
	 */
	public static final String	SCREEN_MODE					= "mode";
	public static final String	SCREEN_MODE_SEARCH			= "search";
	public static final String	SCREEN_MODE_LIST			= "list";
	public static final String	SCREEN_MODE_FORM			= "form";

	/**
	 * View mode
	 */
	public static final String	VIEW_MODE					= "view_mode";
	public static final String	VIEW_MODE_NEW				= "new";
	public static final String	VIEW_MODE_SHOW				= "show";
	public static final String	VIEW_MODE_EDIT				= "edit";

	/**
	 * Exce type
	 */
	public static final String	EXEC_TYPE					= "exec_type";
	public static final String	EXEC_TYPE_NEW				= "new";
	public static final String	EXEC_TYPE_UPD				= "upd";
	public static final String	EXEC_TYPE_DEL				= "del";
	public static final String	EXEC_TYPE_SEND				= "send";
	public static final String	VIEW_TYPE_BAK				= "bak";

	public static final String	YES							= "Y";
	public static final String	NO							= "N";
	public static final String	NONE						= "0";
	public static final Integer	ZERO						= 0;

	public static final String	ENABLED						= "enabled";
	public static final String	DISABLED					= "disabled";

	public static final String	RESULT_OK					= "OK";
	public static final String	RESULT_NG					= "NG";
	public static final String	RESULT_ZERO					= "0";
	public static final String	RESULT_AUTH					= "AUTH";
	public static final String	RESULT_OUT					= "OUT";

	public static final String	STATUS_YET					= "YT";
	public static final String	STATUS_DONE					= "A";

	public static final String	LOCATION_SERVER				= "SV";
	public static final String	LOCATION_LOCAL				= "LC";

	// // language file name
	// // Each browser has key for a language. EX English: en-us,en;q=0.5
	// (Mozilla) en-US,en;q=0.9 (Opera) en-us (Internet Explorer) en (Lynx)
	// // So i need this to auto-convert all to "en" in chiase.
	// public static final String LANGUAGE_RESOURCE_NAME = "ReportMessages";

	// ### Database
	/**
	 * Database - field and property name for use in Database utility and
	 * Duplication annotation.
	 */
	public static final String	COMPANY_ID_PROPERTIES		= "companyId";
	public static final String	COMPANY_ID_FIELD			= "company_id";
	public static final String	USER_ID_PROPERTIES			= "userId";
	public static final String	USER_ID_FIELD				= "user_id";

	/**
	 * base time zone
	 */
	public static final String	SERVER_TIMEZONE_GMT			= "GMT";

	public static final String	SESSION_KEY_USER_ID			= "user.user_id";
	public static final String	SESSION_KEY_USER_NAME		= "user.username";
	public static final String	SESSION_KEY_EMAIL			= "user.email";
	public static final String	SESSION_KEY_USER_SHORTNAME	= "user.shortname";
	public static final String	SESSION_KEY_USER_ADMIN		= "user.admin";

	public static final String	SESSION_KEY_COMPANY_ID		= "user.company_id";
	public static final String	SESSION_KEY_COMPANY_CD		= "user.company_cd";

	public static final String	SESSION_KEY_COUNTRY			= "user.country";
	public static final String	SESSION_KEY_LANGUAGE		= "user.language";
	public static final String	SESSION_KEY_TIMEZONE		= "user.timezone";
	public static final String	SESSION_KEY_CURRENCY		= "user.currency";
	public static final String	SESSION_KEY_OFFSET			= "user.offset";

	public static final String	SESSION_KEY_IS_PASS			= "user.ispassword";

	public static final String	SESSION_KEY_SERVICE_CD		= "service.code";
	public static final String	SESSION_KEY_SERVICE_TYPE	= "service.type";

	public static final String	SESSION_KEY_DATA_TOKEN		= "data.token";
	public static final String	SESSION_KEY_DATA_MESSAGE	= "data.message";

	public static final String	SESSION_KEY_TIMEOUT			= "session.timeout";

	public static final String	SESSION_KEY_MENU			= "proceeding.session.key.menu";
	public static final String	SESSION_KEY_USER_ROLE		= "proceeding.session.key.user.role";

	public static final String	SESSION_KEY_IS_TERM			= "session.isterm";
	public static final String	SESSION_KEY_USER_PHOTO		= "user.photo";

	public static final String	ACTIVE_CODE_TYPE_SIGNUP		= "C";
	public static final String	ACTIVE_CODE_TYPE_RESET_PASS	= "U";
	public static final String	ACTIVE_CODE_TYPE_AUTH		= "P";

	// public static final String ANDROID_DEVICE_TYPE = "A";
	// public static final String IOS_DEVICE_TYPE = "I";

	public static final String	DEVICE_TYPE					= "device_type";
	public static final String	DEVICE_TYPE_ANDROID			= "A";
	public static final String	DEVICE_TYPE_IOS				= "I";
	public static final String	DEVICE_TYPE_WP				= "W";
}