package nguyenhoangviet.vpcorp.dailyreport.services.report.model;

/**
 * Created by viet on 7/11/2016.
 */
public class ReportTemplate{

	public static final String	TEMPLATE_NONE_KEY				= "-1";
	public static final String	REPORT_TEMPLATE_TYPE_NORMAL		= "1";
	public static final String	REPORT_TEMPLATE_TYPE_SPECIAL	= "2";

	public String				templateName;
	public String				templateContent;
	public boolean				templateStatus;
	public String				userId;
	public String				key;

	public static ReportTemplate getTemplateNone(String templateName){
		ReportTemplate reportTemplateNone = new ReportTemplate();
		reportTemplateNone.key = TEMPLATE_NONE_KEY;
		reportTemplateNone.templateName = templateName;
		reportTemplateNone.templateStatus = true;
		reportTemplateNone.templateContent = "";
		return reportTemplateNone;
	}
}
