package trente.asia.messenger.services.message.model;

import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * BoardModel
 *
 * @author TrungND
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class BoardModel{

	public int				key;
	public String			boardType;
	public String			boardName;
	public String			boardUnread;
	public String			avatarPath;
	public List<UserModel>	memberList;
	public String			boardNote;
	public String			targetMessageId;

	public BoardModel(){

	}

	public BoardModel(int key, String boardName){
		this.key = key;
		this.boardName = boardName;
	}

	public BoardModel(int key){
		this.key = key;
	}

	public int getKey(){
		return key;
	}

	public void setKey(int key){
		this.key = key;
	}

	public String getBoardType(){
		return boardType;
	}

	public void setBoardType(String boardType){
		this.boardType = boardType;
	}

	public String getBoardName(){
		return boardName;
	}

	public void setBoardName(String boardName){
		this.boardName = boardName;
	}

	public String getBoardUnread(){
		return boardUnread;
	}

	public void setBoardUnread(String boardUnread){
		this.boardUnread = boardUnread;
	}

	public String getAvatarPath(){
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath){
		this.avatarPath = avatarPath;
	}

	public List<UserModel> getMemberList(){
		return memberList;
	}

	public void setMemberList(List<UserModel> memberList){
		this.memberList = memberList;
	}

	public String getBoardNote(){
		return boardNote;
	}

	public void setBoardNote(String boardNote){
		this.boardNote = boardNote;
	}
}
