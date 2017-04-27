package trente.asia.welfare.adr.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;

/**
 * UserModel
 *
 * @author TrungND
 */

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class UserModel extends BitmapModel implements Serializable{

	public String		key;
	public String		userName;
	public String		userAccount;
	public String		userMail;
	public String		userNameKana;

	public String		adminFlag;
	public String		loginUserId;
	public String		avatarPath;

	public DeptModel	dept;
	public String		companyId;
	public String		deptBoardId;
	public String		pathProfile;
	public String		token;
	public String		language	= "en";
	public String		timezone	= "Asia/Ho_Chi_Minh";
	public String		dateBirth;

	public UserModel(){

	}

	public UserModel(String key){
		this.key = key;
	}

	public UserModel(String key, String userName){
		this.key = key;
		this.userName = userName;
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

	public String getUserAccount(){
		return userAccount;
	}

	public void setUserAccount(String userAccount){
		this.userAccount = userAccount;
	}

	public String getUserMail(){
		return userMail;
	}

	public void setUserMail(String userMail){
		this.userMail = userMail;
	}

	public String getAdminFlag(){
		return adminFlag;
	}

	public void setAdminFlag(String adminFlag){
		this.adminFlag = adminFlag;
	}

	public String getLoginUserId(){
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId){
		this.loginUserId = loginUserId;
	}

	public DeptModel getDept(){
		return dept;
	}

	public void setDept(DeptModel dept){
		this.dept = dept;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getDeptBoardId(){
		return deptBoardId;
	}

	public void setDeptBoardId(String deptBoardId){
		this.deptBoardId = deptBoardId;
	}

	public String getPathProfile(){
		return pathProfile;
	}

	public void setPathProfile(String pathProfile){
		this.pathProfile = pathProfile;
	}

	public String getAvatarPath(){
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath){
		this.avatarPath = avatarPath;
	}

	public String getUserNameKana(){
		return userNameKana;
	}

	public void setUserNameKana(String userNameKana){
		this.userNameKana = userNameKana;
	}

	public static String	KEY_ALL	= "-1";

	public static UserModel getUserModel(String key, List<UserModel> userModels){
		for(UserModel userModel : userModels){
			if(userModel.key.equals(key)){
				return userModel;
			}
		}
		return null;
	}

	public static List<String> getSelectedUserIds(List<UserModel> selectedUsers){
		List<String> selectedUserIds = new ArrayList<>();
		for(UserModel userModel : selectedUsers){
			selectedUserIds.add(userModel.key);
		}
		return selectedUserIds;
	}

	public static boolean contain(List<UserModel> lstUser, UserModel userModel){
		if(CCCollectionUtil.isEmpty(lstUser) || userModel == null){
			return false;
		}

		for(UserModel user : lstUser){
			if(user.key.equals(userModel.key)){
				return true;
			}
		}
		return false;
	}
}
