package trente.asia.welfare.adr.models;

import java.util.List;

/**
 * GroupModel
 *
 * @author TrungND
 */
public class GroupModel{

	public String			key;
	public String			groupName;
	public List<UserModel>	joinUsers;

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

	public List<UserModel> getJoinUsers(){
		return joinUsers;
	}

	public void setJoinUsers(List<UserModel> joinUsers){
		this.joinUsers = joinUsers;
	}
}
