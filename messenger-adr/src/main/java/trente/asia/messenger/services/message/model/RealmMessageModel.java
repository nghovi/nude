package trente.asia.messenger.services.message.model;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.BitmapModel;
import trente.asia.welfare.adr.models.CommentModel;
import trente.asia.welfare.adr.models.FileModel;
import trente.asia.welfare.adr.models.LikeModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by tien on 6/29/2017.
 */

public class RealmMessageModel extends RealmObject{

	@PrimaryKey
	public int							key;
	public int							boardId;
	public RealmUserModel				messageSender;
	public String						messageType;
	public String						messageDate;
	public String						messageContent;
	public String						messageFileName;
	public String						messageUrlThumbnail;
	public String						messageOriginal;


	public RealmFileModel				attachment;
	public RealmFileModel				thumbnailAttachment;
	public RealmList<RealmUserModel>	checks;
	public String						gpsLongtitude;
	public String						gpsLatitude;

	public RealmList<RealmCommentModel>	comments;
	public RealmList<RealmUserModel>	targets;

	public Integer						checkCount;
	public RealmList<RealmUserModel>	checkList;
	public boolean						isChanged	= false;
	@Ignore
	public BitmapModel					bitmapModel	= new BitmapModel();

	public RealmMessageModel(){
	}

	public RealmMessageModel(MessageContentModel message){
		this.key = Integer.parseInt(message.key);
		this.boardId = Integer.parseInt(message.boardId);
		this.messageSender = new RealmUserModel(message.messageSender);
		this.messageType = message.messageType;
		this.messageDate = message.messageDate;
		this.messageContent = message.messageContent;
		this.messageFileName = message.messageFileName;
		this.messageUrlThumbnail = message.messageUrlThumbnail;
		this.messageOriginal = message.messageOriginal;

		this.attachment = new RealmFileModel(message.attachment);
		this.thumbnailAttachment = new RealmFileModel(message.thumbnailAttachment);
		this.checks = new RealmList<>();
		for(UserModel check : message.checks){
			this.checks.add(new RealmUserModel(check));
		}

		this.gpsLongtitude = message.gpsLongtitude;
		this.gpsLatitude = message.gpsLatitude;

		this.comments = new RealmList<>();
		for(CommentModel comment : message.comments){
			this.comments.add(new RealmCommentModel(comment));
		}

		this.targets = new RealmList<>();
		for(UserModel target : message.targets){
			this.targets.add(new RealmUserModel(target));
		}

		this.checkCount = message.checkCount;

		this.checkList = new RealmList<>();
		for(UserModel user : message.checkList){
			this.checkList.add(new RealmUserModel(user));
		}

		this.isChanged = message.isChanged;
	}

	public RealmMessageModel(String messageDate){
		this.messageType = WelfareConst.ITEM_TEXT_TYPE_DATE;
		this.messageDate = messageDate;
	}

	public Integer getCheckCount(){
		if(checkCount != null && !isChanged){
			return checkCount;
		}

		int index = 0;
		checkList = new RealmList<>();
		if(messageSender != null && !CCCollectionUtil.isEmpty(checks)){
			for(RealmUserModel userModel : checks){
				if(messageSender.key != userModel.key){
					checkList.add(userModel);
					index++;
				}
			}
		}
		checkCount = index;
		isChanged = false;
		return checkCount;
	}

	public List<RealmUserModel> getCheckList(){
		if(checkList != null){
			return checkList;
		}

		RealmList<RealmUserModel> lstUser = new RealmList<>();
		if(messageSender != null && !CCCollectionUtil.isEmpty(checks)){
			for(RealmUserModel userModel : checks){
				if(messageSender.key != userModel.key){
					lstUser.add(userModel);
				}
			}
		}
		checkList = lstUser;
		return checkList;
	}

}
