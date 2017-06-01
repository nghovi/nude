package trente.asia.thankscard.commons.defines;

/**
 * Created by viet on 8/13/2015.
 */
public class TcConst{

	public static final String	APP_FOLDER					= "Welfare/ThanksCard";

	public static final String	TC_DATE_FORMAT				= "yyyy/MM/dd HH:mm:ss";
	public static final String	ACTIVE_FOOTER_ITEM_ID		= "ACTIVE_FOOTER_ITEM_ID";

	public static final String	THANKS_CATEGORY_ID			= "1";
	public static final String	CONGRAT_CATEGORY_ID			= "2";
	public static final String	GOOD_JOB_CATEGORY_ID		= "3";

	public static final String	THANKS_NOTICE_TYPE_MANUAL	= "M";
	public static final String	THANKS_NOTICE_TYPE_BIRTH	= "SB";

	/**
	 * API
	 */
	public static final String API_GET_MYPAGE_INFO = "/api/thanks/mypage";
	public static final String API_GET_RANK_STAGE_INFO = "/api/thanks/ranks";
	public static final String API_GET_RECEIVE_CARD_HISTORY = "/api/thanks/history";
	public static final String API_GET_POST_CARD_HISTORY = "/api/thanks/history";
	public static final String API_GET_TEMPLATE = "/api/thanks/template";
	public static final String API_GET_RANKING = "/api/thanks/ranking";
	public static final String API_POST_NEW_CARD = "/api/thanks/send";
	public static final String API_POST_GET_RECEIVER_DEPARTMENT_INFO = "/api/thanks/getAllDeptNameForPost";
	public static final String API_POST_LIKE = "/api/thanks/like";
}
