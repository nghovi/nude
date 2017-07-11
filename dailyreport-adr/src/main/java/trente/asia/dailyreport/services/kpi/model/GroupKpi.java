package trente.asia.dailyreport.services.kpi.model;

import java.util.ArrayList;
import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by hviet on 6/22/17.
 */

public class GroupKpi{

	public String			name;
	public String			todayActual		= "0";
	public String			groupPeriod;
	public String			goal			= "0";
	public String			unit			= "VND";
	public String			condition;
	public String			note;
	public String			startDate;
	public String			endDate;
	public String			ownerId;
	public String			ownerName;
	public String			ownerDeptId;
	public String			key;
	public List<UserModel>	kpiMembers;
	public String			achievement		= "0";
	public String			achievementRate	= "0";
	public String			companyId;
	public String			toGoal			= "0";
}
