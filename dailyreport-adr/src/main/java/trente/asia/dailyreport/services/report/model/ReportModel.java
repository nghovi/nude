package nguyenhoangviet.vpcorp.dailyreport.services.report.model;

import java.util.List;

import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * Created by viet on 7/8/2016.
 */
public class ReportModel{

	public static final String	REPORT_STATUS_DONE	= "DONE";
	public static final String	REPORT_STATUS_DRTT	= "DRTT";

	public String				reportDate;
	public String				reportContent;
	public String				reportStatus;
	public UserModel			reportUser;
	public String				key;
	public String				loginUserId;
	public List<ReportComment>	comments;
	public List<DRLikeModel>	likes;
	public List<DRCheckModel>	checks;
	public String				reportTemplate;
	public Holiday				holiday;
	public String				reportCustom;
	public String				reportCustomTitle;
	public String				workingSymbol;

	public ReportModel(){

	}

	public ReportModel(String key){
		this.key = key;
	}
}
