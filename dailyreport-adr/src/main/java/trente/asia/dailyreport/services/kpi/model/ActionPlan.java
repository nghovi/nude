package trente.asia.dailyreport.services.kpi.model;

/**
 * Created by hviet on 6/22/17.
 */

public class ActionPlan{

	public static final String	STATUS_NG	= "N";
	public static final String	STATUS_YET	= "YET";
	public static final String	STATUS_OK	= "Y";
	public String				planName;
	public String				planValue;
	public String				key			= "2";
	public String				date		= "2017/06/30 00:00:00";
	public String				startDate	= "2017/06/30 00:00:00";
	public String				endDate		= "2017/06/30 00:00:00";
	public String				actionStatus;
	public String				actual;
	public String				achievement;
	public String				achievementRate;
	public String				unit;
	public String				actionDate;
	public String				groupId;
	public String				achievementMonth;
}
