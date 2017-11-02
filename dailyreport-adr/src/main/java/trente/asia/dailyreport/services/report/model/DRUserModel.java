package nguyenhoangviet.vpcorp.dailyreport.services.report.model;

import java.util.List;

import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * Created by viet on 7/14/2016.
 */
public class DRUserModel extends UserModel{

	public List<ReportModel> reports;

	public DRUserModel(String key, String userName){
		super(key, userName);
	}
}
