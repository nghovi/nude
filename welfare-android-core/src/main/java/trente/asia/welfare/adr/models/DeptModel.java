package trente.asia.welfare.adr.models;

import java.util.List;

/**
 * DeptModel
 *
 * @author TrungND
 */
public class DeptModel{

	public String			key;
	public String			deptName;
	public List<UserModel>	managers;
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
}
