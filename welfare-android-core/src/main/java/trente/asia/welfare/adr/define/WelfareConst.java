package trente.asia.welfare.adr.define;

import java.util.HashSet;
import java.util.Set;

/**
 * WelfareConst
 * 
 * @author TrungND on 7/18/16.
 */
public class WelfareConst{

	public static final String		SERVICE_CD_TC					= "TC";
	public static final String		SERVICE_CD_DR					= "DR";
	public static final String		SERVICE_CD_MS					= "MS";
	public static final String		SERVICE_CD_SW					= "SW";
	public static final String		SERVICE_CD_TM					= "TM";
	public static final String		SERVICE_CD_CL					= "CL";
	public static final String		SERVICE_CD_FUKURI				= "FKR";
	public static final String		SERVICE_CD_AC					= "AC";
	public static final String		SERVICE_CD_OL					= "OL";

	public static final String		MYSELF_PARAM					= "myself";
	public static final String		REGISTRATION_ID_PARAM			= "deviceRegistrationId";
	public static final String		ANDROID_DEVICE_CODE				= "A";

	public static final String		ITEM_TEXT_TYPE_TEXT				= "TEXT";
	public static final String		ITEM_TEXT_TYPE_LOC				= "LOC";
	public static final String		ITEM_TEXT_TYPE_DATE				= "DAT";
	public static final String		ITEM_TEXT_TYPE_LIKE				= "ICON";
	public static final String		ITEM_TEXT_TYPE_STAMP			= "STAMP";

	public static final String		ITEM_FILE_TYPE_PHOTO			= "PHOTO";
	public static final String		ITEM_FILE_TYPE_MOVIE			= "MOVIE";
	public static final String		ITEM_FILE_TYPE_FILE				= "FILE";

	public static final int			MENU_BUTTON_ANIMATION_DURATION	= 150;
	public static final int			MENU_LAYOUT_ANIMATION_DURATION	= 400;
	public static final int			RECORDING_ANIMATION_DURATION	= 1000;

	// Welfare date time format
	public static final String		WF_DATE_TIME					= "yyyy/MM/dd HH:mm:ss";
	public static final String		WF_DATE_TIME_DATE_HH_MM			= "yyyy/MM/dd HH:mm";
	public static final String		WF_DATE_TIME_YYYY_MM			= "yyyy/MM";
	public static final String		WF_DATE_TIME_MM_DD				= "MM/dd";
	public static final String		WF_DATE_TIME_DATE				= "yyyy/MM/dd";
	public static final String		WF_DATE_TIME_YYYY_HYPHEN_MM		= "yyyy-MM";
	public static final String		WF_DATE_TIME_HH_MM				= "HH:mm";
	public static final String		WF_DATE_TIME_DATE_WEEKDAY		= "yyyy/MM/dd (EEE)";
	public static final String		WF_DATE_TIME_DD					= "dd";
	public static final String		WF_DATE_TIME_MMMM_YY			= "MMMM, yyyy";
	public static final String		WF_DATE_TIME_WEEK_DAY			= "EEEE";
	public static final String		WF_DATE_TIME_CL_FULL			= "EEEE, MMMM dd, yyyy";
	public static final String		WF_DATE_TIME_CODE				= "yyyyMMdd";

	public static final String		WF_CONTACT_PROBLEM				= "B";
	public static final String		WF_CONTACT_IMPROVE				= "I";

	public static final String		IMG_EXT_PNG						= "png";
	public static final String		IMG_EXT_JPG						= "jpg";
	public static final String		IMG_EXT_JPEG					= "jpeg";
	public static final String		IMG_EXT_GIF						= "gif";

	public static final String		NONE							= "0";
	public static final String		WF_FILE_SIZE_NG					= "WF_FILE_SIZE_NG";

	public static final Set<String>	IMG_EXT_SET						= new HashSet<String>();

	static{
		IMG_EXT_SET.add(IMG_EXT_PNG);
		IMG_EXT_SET.add(IMG_EXT_JPG);
		IMG_EXT_SET.add(IMG_EXT_JPEG);
		IMG_EXT_SET.add(IMG_EXT_GIF);
		IMG_EXT_SET.add(IMG_EXT_PNG.toUpperCase());
		IMG_EXT_SET.add(IMG_EXT_JPG.toUpperCase());
		IMG_EXT_SET.add(IMG_EXT_JPEG.toUpperCase());
		IMG_EXT_SET.add(IMG_EXT_GIF.toUpperCase());
	}

	public static final Set<String> MOVIE_EXT_SET = new HashSet<String>();

	static{
		MOVIE_EXT_SET.add("mp4");
		MOVIE_EXT_SET.add("mov");
		MOVIE_EXT_SET.add("avi");
		MOVIE_EXT_SET.add("mp4".toUpperCase());
		MOVIE_EXT_SET.add("mov".toUpperCase());
		MOVIE_EXT_SET.add("avi".toUpperCase());
	}

	public static final String	BOARD_TYPE_DEPT		= "D";
	public static final String	BOARD_TYPE_USER		= "U";
	public static final String	BOARD_TYPE_GROUP	= "G";

	public static final class Extras{

		public static final String	USER					= "USER";
		public static final String	CONFIG					= "CONFIG";
		public static final String	ROOM_ID					= "ROOM_ID";
		public static final String	TYPE_OF_PHOTO_INTENT	= "TYPE_OF_PHOTO";
		public static final String	UPLOAD_MODEL			= "UPLOAD_MODEL";
		public static final String	ADDRESS					= "ADDRESS";
		public static final String	LATLNG					= "LATLNG";
		public static final String	STICKERS				= "STICKERS";
		public static final String	ACTIVE_BOARD_ID			= "ACTIVE_BOARD_ID";
		public static final String	ACTIVE_REPORT_ID		= "ACTIVE_REPORT_ID";
		public static final String	COMMENT_CONTENT			= "COMMENT_CONTENT";
		public static final String	UPLOAD_PHOTO_AFTER_CROP	= "UPLOAD_PHOTO_AFTER_CROP";
	}

	public static final class PhotoIntents{

		public static final int	GALLERY	= 1;
		public static final int	CAMERA	= 2;
	}

	public static final class RequestCode{

		public static final int	GALLERY			= 1;
		public static final int	CAMERA			= 2;
		public static final int	PHOTO_CHOOSE	= 3;
		public static final int	PICK_FILE		= 4;
		public static final int	VIDEO_CHOOSE	= 5;
		public static final int	AUDIO_CHOOSE	= 6;
		public static final int	LOCATION_CHOOSE	= 7;
		public static final int	CONTACT_CHOOSE	= 8;
		public static final int	PLAY_MOVIE		= 9;
		public static final int	PHOTO_CROP		= 10;
	}

	public static final class FilesName{

		public static final String	CAMERA_TEMP_FILE_NAME	= "camera";
		public static final String	CAMERA_TEMP_FILE_EXT	= ".jpg";
		public static final String	AUDIO_TEMP_FILE_NAME	= "voice.wav";
		public static final String	VIDEO_TEMP_FILE_NAME	= "video";
		public static final String	VIDEO_TEMP_FILE_EXT		= ".mp4";
		public static final String	SCALED_PREFIX			= "scaled_";
		public static final String	IMAGE_TEMP_FILE_NAME	= "image_spika";
		public static final String	TEMP_FILE_NAME			= "temp.spika";
	}

	public static final class Video{

		public static final int	MAX_RECORDING_VIDEO_TIME	= 60;		// seconds
		public static final int	MAX_RECORDING_AUDIO_TIME	= 300000;	// milisecond
	}

	public static final String	IMAGE_PATH_KEY	= "IMAGE_PATH";
	public static final int		PROFILE_SIZE_TB	= 300;

	public static final class NotificationType{

		public static final String	MS_NOTI_NEW_MESSAGE			= "MS_NOTI_NEW_MESSAGE";
		public static final String	MS_NOTI_NEW_COMMENT			= "MS_NOTI_NEW_COMMENT";

		public static final String	DR_NOTI_NEW_REPORT			= "DR_NOTI_NEW_REPORT";
		public static final String	DR_NOTI_COMMENT_REPORT		= "DR_NOTI_COMMENT_REPORT";
		public static final String	DR_NOTI_LIKE_REPORT			= "DR_NOTI_LIKE_REPORT";
		public static final String	DR_NOTI_CHECKPOINT			= "DR_NOTI_CHECKPOINT";
		public static final String	DR_NOTI_COMPLETE_PROGRESS	= "DR_NOTI_COMPLETE_PROGRESS";
		public static final String	DR_NOTI_UNCOMPLETE_PROGRESS	= "DR_NOTI_UNCOMPLETE_PROGRESS";
		public static final String	DR_NOTI_COMPLETE_GROUP_GOAL	= "DR_NOTI_COMPLETE_GROUP_GOAL";

		public static final String	TC_NOTI_NEW_NOTICE			= "TC_NOTI_NEW_NOTICE";
		public static final String	TC_NOTI_BIRTHDAY			= "TC_NOTI_BIRTHDAY";
		public static final String	TC_NOTI_RECEIVE_CARD		= "TC_NOTI_RECEIVE_CARD";

		public static final String	SW_NOTI_NEW_NOTICE			= "SW_NOTI_NEW_NOTICE";
		public static final String	SW_NOTI_CHECK_IN			= "SW_NOTI_CHECK_IN";
		public static final String	SW_NOTI_OVER_TIME			= "SW_NOTI_OVER_TIME";
		public static final String	SW_NOTI_OFFER				= "SW_NOTI_OFFER";

		public static final String	CL_NOTI_NEW_SCHEDULE		= "CL_NOTI_NEW_SCHEDULE";
		public static final String	OL_DELIVERY_DOC				= "OL_DELIVERY_DOC";
		public static final String	OL_DELIVERY_SALARY			= "OL_DELIVERY_SALARY";
	}

	public static final class NotificationReceived{

		public static final String	USER_INFO_NOTI_TYPE			= "type";
		public static final String	USER_INFO_NOTI_PARENT_KEY	= "parentKey";
		public static final String	USER_INFO_NOTI_KEY			= "key";
	}

	public static final int		MAX_FILE_SIZE_2MB		= 2097152;
	public static final int		NOTIFICATION_ID			= 1;

	public static final String	ACCOUNT_TERM_URL		= "https://tr-fukuri.jp/use_policy";
	public static final String	ACCOUNT_POLICY_URL		= "https://tr-fukuri.jp/privacy_policy";

	public static final int		ONE_MINUTE_TIME_LOAD	= 60 * 1000;
}
