package nguyenhoangviet.vpcorp.dailyreport.services.activities.model;

import nguyenhoangviet.vpcorp.dailyreport.services.report.model.ReportModel;

/**
 * Created by viet on 8/1/2016.
 */
public class ActivityModel{

	public String		activityUser;
	public String		activityUserName;
	public String		activityUserAvatarPath;
	public String		activityDate;
	public String		activityType;
	public String		activityMessage;

	public String		commentId;
	public ReportModel	report;
	public String		loginUserId;
}
