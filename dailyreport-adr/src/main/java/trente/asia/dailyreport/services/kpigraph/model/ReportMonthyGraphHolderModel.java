package trente.asia.dailyreport.services.kpigraph.model;

/**
 * Created by PC-Trente on 9/12/2016.
 */

public class ReportMonthyGraphHolderModel{

	public String					userId;
	public String					userName;
	public String					monthString;
	public ReportMonthlyGoalModel	goal;

	public String getUserId(){
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public ReportMonthlyGoalModel getGoal(){
		return goal;
	}

	public void setGoal(ReportMonthlyGoalModel goal){
		this.goal = goal;
	}

	public String getMonthString(){
		return monthString;
	}

	public void setMonthString(String monthString){
		this.monthString = monthString;
	}
}
