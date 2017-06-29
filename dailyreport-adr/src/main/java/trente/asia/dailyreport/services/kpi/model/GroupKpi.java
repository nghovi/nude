package trente.asia.dailyreport.services.kpi.model;

import java.util.List;

/**
 * Created by hviet on 6/22/17.
 */

public class GroupKpi{

	public static class CheckPoint{

		public String	date;
		public String	achievement;
	}

	public String			name;
	public String			goal;
	public String			startDate;
	public String			endDate;
	public String			kpiMembers;
	public String			achievement;
	public String			achievementRate;
	public String			key;
	public List<CheckPoint>	checkPointList;
}
