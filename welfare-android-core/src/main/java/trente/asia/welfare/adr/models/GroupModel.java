package trente.asia.welfare.adr.models;

import java.util.List;

/**
 * GroupModel
 *
 * @author TrungND
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class GroupModel{

	public String			key;
	public String			groupName;
	public List<UserModel>	listUsers;

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public List<UserModel> getListUsers(){
		return listUsers;
	}

	public void setListUsers(List<UserModel> listUsers){
		this.listUsers = listUsers;
	}
}
