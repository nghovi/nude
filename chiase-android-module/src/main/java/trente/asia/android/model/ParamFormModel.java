package trente.asia.android.model;

public class ParamFormModel{

	public Integer	key;

	public String	token;

	public String	updateDateString;

	public String	deleteFlag;

	public Integer getKey(){
		return key;
	}

	public void setKey(Integer key){
		this.key = key;
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getUpdateDateString(){
		return updateDateString;
	}

	public void setUpdateDateString(String updateDateString){
		this.updateDateString = updateDateString;
	}

	public String getDeleteFlag(){
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag){
		this.deleteFlag = deleteFlag;
	}
}
