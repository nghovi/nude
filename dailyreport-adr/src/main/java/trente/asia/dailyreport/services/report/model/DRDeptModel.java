package nguyenhoangviet.vpcorp.dailyreport.services.report.model;

import java.util.List;

import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;

/**
 * Created by viet on 7/14/2016.
 */
public class DRDeptModel extends DeptModel{

	public DRDeptModel(String key, String deptName){
		super(key, deptName);
	}

	public List<DRUserModel> users;
}
