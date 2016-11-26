package trente.asia.dailyreport.services.report.model;

/**
 * Created by viet on 8/1/2016.
 */
public class ActionEntry{

	public String	key;
	public String	itemId;
	public String	actionName;
	public String	actionPlan;
	public String	actionActual;
	public String	actionAvg;

	public String	kpiItem;		// why we need kpiItem, why don't use itemId

	public int		tmpIdx;

	/*
	 * Copy some value from actionEntry(excluding actionActual)
	 */
	public static ActionEntry copyFrom(ActionEntry actionEntry){
		ActionEntry newActionEntry = new ActionEntry();
		newActionEntry.itemId = actionEntry.itemId;
		newActionEntry.actionName = actionEntry.actionName;
		newActionEntry.actionPlan = actionEntry.actionPlan;
		newActionEntry.actionActual = "";
		newActionEntry.actionAvg = actionEntry.actionAvg;
		newActionEntry.kpiItem = actionEntry.kpiItem;
		return newActionEntry;
	}
}
