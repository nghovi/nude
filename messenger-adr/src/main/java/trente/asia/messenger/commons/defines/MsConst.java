package trente.asia.messenger.commons.defines;

/**
 * MsConst
 *
 * @author TrungND
 */
public class MsConst{

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

	public static final String	MESSAGE_ACTION_COPY		= "1";
	public static final String	MESSAGE_ACTION_EDIT		= "2";
	public static final String	MESSAGE_ACTION_DELETE	= "3";
}
