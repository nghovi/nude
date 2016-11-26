package trente.asia.dailyreport.services.report.model;

import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 7/14/2016.
 */
public class DRUserModel extends UserModel{

	public List<ReportModel> reports;

	public DRUserModel(String key, String userName){
		super(key, userName);
	}
}
