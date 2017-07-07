package trente.asia.messenger.services.message.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by tien on 6/30/2017.
 */

public class RealmBoardModel extends RealmObject{

	@PrimaryKey
	public int							key;
	public String						boardType;
	public String						boardName;
	public String						boardUnread;
	public String						avatarPath;
	public RealmList<RealmUserModel>	memberList;
	public String						boardNote;
	public String						targetMessageId;
	public String						lastMessageUpdateDate;

	public RealmBoardModel(){}

	public RealmBoardModel(BoardModel board){
		this.key = board.key;
		this.boardType = board.boardType;
		this.boardName = board.boardName;
		this.boardUnread = board.boardUnread;
		this.avatarPath = board.avatarPath;
		this.memberList = new RealmList<>();
		for(UserModel user : board.memberList){
			this.memberList.add(new RealmUserModel(user));
		}
		this.boardNote = board.boardNote;
		this.targetMessageId = board.targetMessageId;
	}

	public void update(BoardModel board){
		this.boardType = board.boardType;
		this.boardName = board.boardName;
		this.boardUnread = board.boardUnread;
		this.avatarPath = board.avatarPath;
		this.memberList = new RealmList<>();
		for(UserModel user : board.memberList){
			this.memberList.add(new RealmUserModel(user));
		}
		this.boardNote = board.boardNote;
		this.targetMessageId = board.targetMessageId;
	}
}
