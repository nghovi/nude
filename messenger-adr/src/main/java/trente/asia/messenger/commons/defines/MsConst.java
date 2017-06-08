package trente.asia.messenger.commons.defines;

/**
 * MsConst
 *
 * @author TrungND
 */
public class MsConst{

	public static final String	API_MESSAGE_LIST				= "/api/message/list";
	public static final String	API_MESSAGE_BOARD				= "/api/message/board";
	public static final String	API_MESSAGE_DETAIL				= "/api/message/detail";
	public static final String	API_MESSAGE_UPDATE				= "/api/message/update";
	public static final String	API_MESSAGE_LIKE				= "/api/message/like";
	public static final String	API_MESSAGE_LATEST				= "/api/message/latest";
	public static final String	API_MESSAGE_DELETE				= "/api/message/delete";

	public static final String	API_MESSAGE_NOTE_UPDATE			= "/api/message/note/update";
	public static final String	API_MESSAGE_NOTE_COPY			= "/api/message/note/copy";
	public static final String	API_MESSAGE_NOTE_DETAIL			= "/api/message/note/detail";

	public static final String	API_MESSAGE_COMMENT				= "/api/message/comment";
	public static final String	API_MESSAGE_COMMENT_LATEST		= "/api/message/comment/lastest";

	public static final String	API_MESSAGE_CONTACT_LIST		= "/api/message/contact/list";
	public static final String	API_MESSAGE_CONTACT_UPDATE		= "/api/message/contact/update";

	public static final String	API_MESSAGE_STAMP_CATEGORY_LIST	= "/api/system/stamp/category/list";

	public static final class EmitKeyWord{

		public static final String	LOGIN			= "login";
		public static final String	SEND_MESSAGE	= "sendMessage";
		public static final String	NEW_USER		= "newUser";
		public static final String	NEW_MESSAGE		= "newMessage";
		public static final String	SEND_TYPING		= "sendTyping";
		public static final String	OPEN_MESSAGE	= "openMessage";
		public static final String	MESSAGE_UPDATED	= "messageUpdated";
		public static final String	DELETE_MESSAGE	= "deleteMessage";
		public static final String	USER_LEFT		= "userLeft";
		public static final String	SOCKET_ERROR	= "socketerror";
	}

	public static final String	APP_FOLDER					= "Welfare/Messenger";

	public static final int		REQUEST_CODE_ADD_CONTACT	= 21;

	public static final class BoardViewType{

		public static final String	MESSAGE	= "1";
		public static final String	NOTE	= "2";
		public static final String	MEMBER	= "3";
	}

	public static final String	MESSAGE_ACTION_COPY				= "1";
	public static final String	MESSAGE_ACTION_EDIT				= "2";
	public static final String	MESSAGE_ACTION_DELETE			= "3";

	public static final String	MESSAGE_STAMP_LAST_UPDATE_DATE	= "stamp_last_update_date";
}
