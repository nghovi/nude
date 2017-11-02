package nguyenhoangviet.vpcorp.shiftworking.common.models;

import java.util.List;

import org.json.JSONObject;

import asia.chiase.core.model.KeyValueModel;
import asia.chiase.core.util.CCJsonUtil;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.ProjectModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;

/**
 * SwApiHolder
 *
 * @author TrungND
 */

public class SwApiHolder{

	public List<KeyValueModel>	noticeTypes;
	public List<DeptModel>		depts;
	public List<ProjectModel>	projects;

	public SwApiHolder(){

	}

	public SwApiHolder(JSONObject response){
		noticeTypes = CCJsonUtil.convertToModelList(response.optString("noticeTypes"), KeyValueModel.class);
		depts = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
		projects = CCJsonUtil.convertToModelList(response.optString("projects"), ProjectModel.class);
	}

}
