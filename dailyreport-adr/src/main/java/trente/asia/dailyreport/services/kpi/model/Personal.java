package trente.asia.dailyreport.services.kpi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hviet on 6/23/17.
 */

public class Personal{

	public String			goal;
	public String			achievement;
	public String			achievementRate;
	public String			todayActual;
	public String			startDate;
	public String			endDate;
	public GroupKpi			group;
	public List<Progress>	progressList	= new ArrayList<>();
}
