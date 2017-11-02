package nguyenhoangviet.vpcorp.thankscard.commons.defines;

/**
 * Created by viet on 8/13/2015.
 */
public class TcConst{

	public static final String	APP_FOLDER								= "Welfare/ThanksCard";

	public static final String	TC_DATE_FORMAT							= "yyyy/MM/dd HH:mm:ss";
	public static final String	ACTIVE_FOOTER_ITEM_ID					= "ACTIVE_FOOTER_ITEM_ID";

	public static final String	THANKS_CATEGORY_ID						= "1";
	public static final String	CONGRAT_CATEGORY_ID						= "2";
	public static final String	GOOD_JOB_CATEGORY_ID					= "3";

	public static final String	THANKS_NOTICE_TYPE_MANUAL				= "M";
	public static final String	THANKS_NOTICE_TYPE_BIRTH				= "SB";

	/**
	 * API
	 */
	public static final String	API_GET_MYPAGE_INFO						= "/api/thanks/mypage";
	public static final String	API_GET_RANK_STAGE_INFO					= "/api/thanks/ranks";
	public static final String	API_GET_RECEIVE_CARD_HISTORY			= "/api/thanks/history";
	public static final String	API_GET_POST_CARD_HISTORY				= "/api/thanks/history";
	public static final String	API_GET_TEMPLATE						= "/api/thanks/template";
	public static final String	API_GET_RANKING							= "/api/thanks/ranking";
	public static final String	API_POST_NEW_CARD						= "/api/thanks/send";
	public static final String	API_POST_GET_RECEIVER_DEPARTMENT_INFO	= "/api/thanks/getAllDeptNameForPost";
	public static final String	API_POST_LIKE							= "/api/thanks/like";

	public static final String	PREF_TEMPLATE_ID						= "PREF_TEMPLATE_ID";
	public static final String	PREF_TEMPLATE_PATH						= "PREF_TEMPLATE_PATH";
	public static final String	PREF_POINT_BRONZE						= "PREF_POINT_BRONZE";
	public static final String	PREF_POINT_SILVER						= "PREF_POINT_SILVER";
	public static final String	PREF_POINT_GOLD							= "PREF_POINT_GOLD";
	public static final String	PREF_POINT_TOTAL						= "PREF_POINT_TOTAL";

	public static final String	MESSAGE_STAMP_LAST_UPDATE_DATE			= "MESSAGE_STAMP_LAST_UPDATE_DATE";
	public static final String	API_MESSAGE_STAMP_CATEGORY_LIST			= "/api/system/stamp/category/list";

	public static final float	FRAME_RATIO								= 1.43f;
	public static final String	PREF_FRAME_WIDTH						= "PREF_FRAME_WIDTH";
	public static final String	PREF_FRAME_HEIGHT						= "PREF_FRAME_HEIGHT";
	public static final String	PREF_Y_FROM_TOP							= "PREF_Y_FROM_TOP";
	public static final String	POSITION_TOP							= "TOP";
	public static final String	POSITION_CENTER							= "CENTER";
	public static final String	POSITION_BOTTOM							= "BOTTOM";
	public static final String	POSITION_LEFT							= "LEFT";
	public static final String	PREF_NORMAL_TEXT_SIZE					= "PREF_NORMAL_TEXT_SIZE";
	public static final String	PREF_PHOTO_TEXT_SIZE					= "PREF_PHOTO_TEXT_SIZE";
	public static final String	IS_BIRTHDAY								= "IS_BIRTHDAY";
}
