package trente.asia.welfare.adr.models;

/**
 * LikeModel
 *
 * @author TrungND
 */
public class LikeModel{

	public String		key;
	public String		messageId;
	public String		likeDate;
	public UserModel	likeUser;

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getMessageId(){
		return messageId;
	}

	public void setMessageId(String messageId){
		this.messageId = messageId;
	}

	public String getLikeDate(){
		return likeDate;
	}

	public void setLikeDate(String likeDate){
		this.likeDate = likeDate;
	}

	public UserModel getLikeUser(){
		return likeUser;
	}

	public void setLikeUser(UserModel likeUser){
		this.likeUser = likeUser;
	}
}
