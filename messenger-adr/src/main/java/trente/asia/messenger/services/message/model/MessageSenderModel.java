package trente.asia.messenger.services.message.model;

/**
 * Created by Huy-nq on 7/12/2016.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class MessageSenderModel{

	public String	key;
	public String	userName;
	public String	userUrl;

	public MessageSenderModel(){
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserUrl(){
		return userUrl;
	}

	public void setUserUrl(String userUrl){
		this.userUrl = userUrl;
	}
}
