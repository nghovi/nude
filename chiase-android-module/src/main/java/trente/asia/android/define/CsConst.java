package trente.asia.android.define;

import android.Manifest;

/**
 * Created by takyas on 27/11/14.
 */
public class CsConst{

	public static String			ROOT						= "CA-ROOT";

	public static final String		ACCOUNT_PROPERTY			= "asia.chiase.account";

	public static final String		SERIES_PROPERTY				= "asia.chiase.series";

	public static final String		TOKEN_PROPERTY				= "asia.chiase.token";
	// Cookies const
	public static String[]			COOKIES_NAME				= {ACCOUNT_PROPERTY, SERIES_PROPERTY, TOKEN_PROPERTY};

	public static String			RETURN_LINE					= "\n";

	public static final String		ARG_KEY						= "key";

	public static final String		ARG_KEY_PARENT				= "parentKey";

	public static final String		ARG_VIEW_MODE				= "viewMode";

	public static final String		SYNC_TIME_PROPERTY			= "syncTimeString";

	public static final String		ARG_ACCOUNT					= "account";

	public static final String		ARG_TOKEN					= "token";

	public static final String		ARG_LANG					= "lang";

	public static final String		ARG_TIMEZONE				= "timezone";

	public static final String		ARG_DEVICE					= "deviceType";

	public static final String		ARG_VERSION					= "version";

	public static final String		GPS_LATITUDE				= "gpsLatitude";

	public static final String		GPS_LONGITUDE				= "gpsLongitude";

	public static final String		ARG_EXEC_TYPE				= "execType";

	// Static string
	public static final String		STATUS						= "status";
	public static final String		MESSAGES					= "messages";

	public static final int			DAY_NUMBER_A_WEEK			= 7;

	public static final String		STATUS_NG					= "NG";
	public static final String		STATUS_OK					= "OK";

	public static final int			REQUEST_EXTERNAL_STORAGE	= 1;
	public static final String[]	PERMISSIONS_STORAGE			= {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

	public static final String		ONE							= "1";
}
