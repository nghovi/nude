package trente.asia.welfare.adr.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * DeptModel
 *
 * @author TrungND
 */

@JsonObject
public class DeptModel{

	@JsonField
	public String			key;
	@JsonField
	public String			deptName;
	@JsonField
	public List<UserModel>	managers;
	@JsonField
	public List<UserModel>	members;

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getDeptName(){
		return deptName;
	}

	public void setDeptName(String deptName){
		this.deptName = deptName;
	}

	public List<UserModel> getManagers(){
		return managers;
	}

	public void setManagers(List<UserModel> managers){
		this.managers = managers;
	}

	public static String KEY_ALL = "-1";

	public DeptModel(String key, String deptName){
		this.key = key;
		this.deptName = deptName;
	}

	public DeptModel(){

	}
}
