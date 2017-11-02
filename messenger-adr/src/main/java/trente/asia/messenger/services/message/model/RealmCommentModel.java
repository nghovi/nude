package nguyenhoangviet.vpcorp.messenger.services.message.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import nguyenhoangviet.vpcorp.welfare.adr.models.CommentModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * Created by tien on 6/29/2017.
 */

public class RealmCommentModel extends RealmObject{

	@PrimaryKey
	public int				key;
	public String			loginUserId;
	public String			messageId;
	public String			commentDate;
	public String			commentContent;
	public String			itemType;
	public String			commentType;
	public RealmUserModel	commentUser;

	public RealmCommentModel(){
	}

	public RealmCommentModel(CommentModel comment){
		this.key = Integer.parseInt(comment.key);
		this.loginUserId = comment.loginUserId;
		this.messageId = comment.messageId;
		this.commentDate = comment.commentDate;
		this.commentContent = comment.commentContent;
		this.itemType = comment.itemType;
		this.commentType = comment.commentType;
		this.commentUser = new RealmUserModel(comment.commentUser);
	}
}
