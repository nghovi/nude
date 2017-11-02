package nguyenhoangviet.vpcorp.shiftworking.common.utils;

import java.io.File;
import java.util.List;

import android.os.Environment;

import asia.chiase.core.model.KeyValueModel;
import asia.chiase.core.util.CCCollectionUtil;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.common.models.SwUserModel;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.ProjectModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * Created by PC-Trente on 10/3/2016.
 */

public class SwUtil{

	/**
	 * get root file of application
	 *
	 * @return string
	 */
	public static String getFilesFolderPath(){
		File folder = new File(Environment.getExternalStorageDirectory(), SwConst.APP_FOLDER);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}

	public static String getNoticeTypeName(List<KeyValueModel> lstNoticeType, String key){
		if(!CCCollectionUtil.isEmpty(lstNoticeType)){
			for(KeyValueModel model : lstNoticeType){
				if(model.getKey().equals(key)){
					return model.getValue();
				}
			}
		}
		return "";
	}

	public static String getProjectName(List<ProjectModel> lstProject, String projectId){
		if(!CCCollectionUtil.isEmpty(lstProject)){
			for(ProjectModel model : lstProject){
				if(model.key.equals(projectId)){
					return model.projectName;
				}
			}
		}
		return "";
	}

	public static String getDeptName(List<DeptModel> lstDept, String deptId){
		if(!CCCollectionUtil.isEmpty(lstDept)){
			for(DeptModel model : lstDept){
				if(model.key.equals(deptId)){
					return model.deptName;
				}
			}
		}
		return "";
	}

	public static boolean isChecked(List<SwUserModel> lstCheck, String userId){
		if(!CCCollectionUtil.isEmpty(lstCheck)){
			for(UserModel model : lstCheck){
				if(model.key.equals(userId)){
					return true;
				}
			}
		}
		return false;
	}
}
