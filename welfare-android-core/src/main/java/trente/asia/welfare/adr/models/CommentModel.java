package trente.asia.welfare.adr.models;

/**
 * CommentModel
 *
 * @author TrungND
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CommentModel{

	// TODO Trung: use common type
	public static final String	COMMENT_TYPE_PHOTO	= "PHOTO";
	public static final String	COMMENT_TYPE_TEXT	= "TEXT";
	public static final String	COMMENT_TYPE_FILE	= "FILE";
	public static final String	COMMENT_TYPE_MOVIE	= "MOVIE";

	public String				messageId;
	public String				commentDate;
	public String				commentContent;
	public String				itemType;
	public String				commentType;
	public UserModel			commentUser;

	public AttachmentModel		attachment;
	public ThumbnailModel		thumbnail;
	public String				key;
	public String				loginUserId;

	public CommentModel(String commentContent, UserModel commentUser){
		this.commentContent = commentContent;
		this.commentUser = commentUser;
	}

	public CommentModel(){
	}

	public String getCommentDate(){
		return commentDate;
	}

	public void setCommentDate(String commentDate){
		this.commentDate = commentDate;
	}

	public String getCommentContent(){
		return commentContent;
	}

	public void setCommentContent(String commentContent){
		this.commentContent = commentContent;
	}

	public String getItemType(){
		return itemType;
	}

	public void setItemType(String itemType){
		this.itemType = itemType;
	}

	public String getCommentType(){
		return commentType;
	}

	public void setCommentType(String commentType){
		this.commentType = commentType;
	}

	public UserModel getCommentUser(){
		return commentUser;
	}

	public void setCommentUser(UserModel commentUser){
		this.commentUser = commentUser;
	}

	public AttachmentModel getAttachment(){
		return attachment;
	}

	public void setAttachment(AttachmentModel attachment){
		this.attachment = attachment;
	}

	public ThumbnailModel getThumbnail(){
		return thumbnail;
	}

	public void setThumbnail(ThumbnailModel thumbnail){
		this.thumbnail = thumbnail;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getLoginUserId(){
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId){
		this.loginUserId = loginUserId;
	}

	public String getMessageId(){
		return messageId;
	}

	public void setMessageId(String messageId){
		this.messageId = messageId;
	}
}
