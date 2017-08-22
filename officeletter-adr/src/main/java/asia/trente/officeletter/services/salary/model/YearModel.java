package asia.trente.officeletter.services.salary.model;

import java.util.List;

/**
 * Created by tien on 8/21/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class YearModel{

	public int				year;
	public List<MonthModel>	months;

	public YearModel(){
	}
}
