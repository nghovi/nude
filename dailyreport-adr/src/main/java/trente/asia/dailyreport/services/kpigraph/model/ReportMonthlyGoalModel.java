package trente.asia.dailyreport.services.kpigraph.model;

/**
 * Created by PC-Trente on 9/8/2016.
 */
public class ReportMonthlyGoalModel{

	public String	parentId;
	public String	goalName;
	public String	goalUnit;
	public String	goalDescription;
	public String	goalMonth;
	public String	goalUser;
	public String	goalValue;
	public String	goalActual;
	public Boolean	filterByDeptMode;
	public String	deptId;
	public String	actualSum;
	public String	actualRate;
	public String	key;

	public String getParentId(){
		return parentId;
	}

	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getGoalName(){
		return goalName;
	}

	public void setGoalName(String goalName){
		this.goalName = goalName;
	}

	public String getGoalUnit(){
		return goalUnit;
	}

	public void setGoalUnit(String goalUnit){
		this.goalUnit = goalUnit;
	}

	public String getGoalDescription(){
		return goalDescription;
	}

	public void setGoalDescription(String goalDescription){
		this.goalDescription = goalDescription;
	}

	public String getGoalMonth(){
		return goalMonth;
	}

	public void setGoalMonth(String goalMonth){
		this.goalMonth = goalMonth;
	}

	public String getGoalUser(){
		return goalUser;
	}

	public void setGoalUser(String goalUser){
		this.goalUser = goalUser;
	}

	public String getGoalValue(){
		return goalValue;
	}

	public void setGoalValue(String goalValue){
		this.goalValue = goalValue;
	}

	public String getGoalActual(){
		return goalActual;
	}

	public void setGoalActual(String goalActual){
		this.goalActual = goalActual;
	}

	public Boolean getFilterByDeptMode(){
		return filterByDeptMode;
	}

	public void setFilterByDeptMode(Boolean filterByDeptMode){
		this.filterByDeptMode = filterByDeptMode;
	}

	public String getDeptId(){
		return deptId;
	}

	public void setDeptId(String deptId){
		this.deptId = deptId;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getActualSum(){
		return actualSum;
	}

	public void setActualSum(String actualSum){
		this.actualSum = actualSum;
	}

	public String getActualRate(){
		return actualRate;
	}

	public void setActualRate(String actualRate){
		this.actualRate = actualRate;
	}

}
