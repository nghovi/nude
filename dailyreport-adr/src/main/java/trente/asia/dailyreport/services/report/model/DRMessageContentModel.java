package trente.asia.dailyreport.services.report.model;

import java.util.List;

import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.CommentModel;
import trente.asia.welfare.adr.models.FileModel;
import trente.asia.welfare.adr.models.LikeModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by Huy-nq on 7/12/2016.
 */
public class DRMessageContentModel{

	public String				key;
	public UserModel			messageSender;
	public String				messageType;
	public String				messageDate;

	public String				messageContent;
	public String				messageFileName;
	public String				messageUrlThumbnail;
	public String				messageOriginal;

	public FileModel			attachment;
	public FileModel			thumbnailAttachment;
	public List<LikeModel>		likes;
	public List<CommentModel>	comments;
	public List<UserModel>		userList;
	public String				gpsLongtitude;
	public String				gpsLatitude;
	public List<UserModel>		targets;

	public DRMessageContentModel(){
	}

	public DRMessageContentModel(String key, String messageType, String messageContent){
		this.key = key;
		this.messageType = messageType;
		this.messageContent = messageContent;
	}

	public DRMessageContentModel(String messageDate){
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

	public List<UserModel> getUserList(){
		return userList;
	}

	public List<UserModel> getTargets(){
		return targets;
	}

	public void setUserList(List<UserModel> userList){
		this.userList = userList;
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
}
