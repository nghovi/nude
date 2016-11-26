package trente.asia.dailyreport.services.report.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import asia.chiase.core.util.CCFormatUtil;

/**
 * Created by viet on 8/1/2016.
 */
public class GoalEntry{

	public String	key;
	public String	goalName;
	public String	goalUnit;
	public String	goalDescription;
	public String	goalMonth;
	public String	goalUser;
	public String	goalValue;
	public String	goalPlan;
	public String	goalActual;
	public String	actualSum	= "0";
	public String	actualRate	= "0";

	public static String getAchievementString(GoalEntry goalEntry){
		return new DecimalFormat("#0.00").format(BigDecimal.valueOf(Double.valueOf(goalEntry.actualRate) * 100)) + CCFormatUtil.getPercentSymbol();
	}
}
