package trente.asia.messenger.services.message.model;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.BitmapModel;
import trente.asia.welfare.adr.models.CommentModel;
import trente.asia.welfare.adr.models.FileModel;
import trente.asia.welfare.adr.models.LikeModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by Huy-nq on 7/12/2016.
 */
public class MessageContentModel extends BitmapModel{

	public String				key;
	public String				boardId;
	public UserModel			messageSender;
	public String				messageType;
	public String				messageDate;

	public String				messageContent;
	public String				messageFileName;
	public String				messageUrlThumbnail;
	public String				messageOriginal;

	public FileModel			attachment;
	public FileModel			thumbnailAttachment;
	public List<UserModel>		checks;
	public String				gpsLongtitude;
	public String				gpsLatitude;

	public List<LikeModel>		likes;
	public List<CommentModel>	comments;
	public List<UserModel>		targets;
	// public boolean isEmotion = false;
	// public int sequence;

	public Integer				checkCount;
	public List<UserModel>		checkList;
	public boolean				isChanged	= false;

	public MessageContentModel(){
	}

	public MessageContentModel(String key, String messageType, String messageContent){
		this.key = key;
		this.messageType = messageType;
		this.messageContent = messageContent;
	}

	public MessageContentModel(String messageDate){
		this.messageType = WelfareConst.ITEM_TEXT_TYPE_DATE;
		this.messageDate = messageDate;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getMessageType(){
		return messageType;
	}

	public void setMessageType(String messageType){
		this.messageType = messageType;
	}

	public String getMessageDate(){
		return messageDate;
	}

	public void setMessageDate(String messageDate){
		this.messageDate = messageDate;
	}

	public String getMessageContent(){
		return messageContent;
	}

	public void setMessageContent(String messageContent){
		this.messageContent = messageContent;
	}

	public String getMessageFileName(){
		return messageFileName;
	}

	public void setMessageFileName(String messageFileName){
		this.messageFileName = messageFileName;
	}

	public String getMessageUrlThumbnail(){
		return messageUrlThumbnail;
	}

	public void setMessageUrlThumbnail(String messageUrlThumbnail){
		this.messageUrlThumbnail = messageUrlThumbnail;
	}

	public String getMessageOriginal(){
		return messageOriginal;
	}

	public void setMessageOriginal(String messageOriginal){
		this.messageOriginal = messageOriginal;
	}

	public List<LikeModel> getLikes(){
		return likes;
	}

	public void setLikes(List<LikeModel> likes){
		this.likes = likes;
	}

	public List<CommentModel> getComments(){
		return comments;
	}

	public void setComments(List<CommentModel> comments){
		this.comments = comments;
	}

	public UserModel getMessageSender(){
		return messageSender;
	}

	public void setMessageSender(UserModel messageSender){
		this.messageSender = messageSender;
	}

	public FileModel getAttachment(){
		return attachment;
	}

	public void setAttachment(FileModel attachment){
		this.attachment = attachment;
	}

	public FileModel getThumbnailAttachment(){
		return thumbnailAttachment;
	}

	public void setThumbnailAttachment(FileModel thumbnailAttachment){
		this.thumbnailAttachment = thumbnailAttachment;
	}

	public String getGpsLongtitude(){
		return gpsLongtitude;
	}

	public void setGpsLongtitude(String gpsLongtitude){
		this.gpsLongtitude = gpsLongtitude;
	}

	public String getGpsLatitude(){
		return gpsLatitude;
	}

	public void setGpsLatitude(String gpsLatitude){
		this.gpsLatitude = gpsLatitude;
	}

	public void setTargets(List<UserModel> targets){
		this.targets = targets;
	}

	public String getBoardId(){
		return boardId;
	}

	public void setBoardId(String boardId){
		this.boardId = boardId;
	}

	public List<UserModel> getChecks(){
		return checks;
	}

	public void setChecks(List<UserModel> checks){
		this.checks = checks;
	}

	public List<UserModel> getTargets(){
		return targets;
	}

	public Integer getCheckCount(){
		if(checkCount != null && !isChanged){
			return checkCount;
		}

		int index = 0;
		checkList = new ArrayList<>();
		if(messageSender != null && !CCCollectionUtil.isEmpty(checks)){
			for(UserModel userModel : checks){
				if(messageSender.key == null || !messageSender.key.equals(userModel.key)){
					checkList.add(userModel);
					index++;
				}
			}
		}
		checkCount = index;
		isChanged = false;
		return checkCount;
	}

	public List<UserModel> getCheckList(){
		if(checkList != null){
			return checkList;
		}

		List<UserModel> lstUser = new ArrayList<>();
		if(messageSender != null && !CCCollectionUtil.isEmpty(checks)){
			for(UserModel userModel : checks){
				if(!messageSender.key.equals(userModel.key)){
					lstUser.add(userModel);
				}
			}
		}
		checkList = lstUser;
		return checkList;
	}
}
