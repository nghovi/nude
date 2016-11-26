package trente.asia.dailyreport.services.report.model;

import java.util.List;

/**
 * Created by viet on 8/1/2016.
 */
public class Kpi{

	public static final String	KPI_UNIT_TIME	= "TIME";
	public static final String	KPI_UNIT_COUNT	= "COUNT";
	public static final String	KPI_UNIT_SALE	= "SALE";

	public String				itemUnit;
	public String				itemDescription;
	public String				itemName;
	public List<KpiMap>			listKpiMaps;
	public String				key;

	public class KpiMap{

	}
}
