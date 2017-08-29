package asia.trente.officeletter.services.salary.model;

import com.bluelinelabs.logansquare.annotation.JsonIgnore;

/**
 * Created by tien on 8/21/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class MonthModel{

	public int		month;
	public int		salaryId;
	public int		passwordType;
	public String	passwordHint;
	@JsonIgnore
	public int		year	= 0;

	public MonthModel(){
	}

	public MonthModel(int year){
		this.year = year;
	}

	public String getName(){
		if(year != 0){
			return String.valueOf(year);
		}else{
			switch(month){
			case 1:
				return "January";
			case 2:
				return "February";
			case 3:
				return "March";
			case 4:
				return "April";
			case 5:
				return "May";
			case 6:
				return "June";
			case 7:
				return "July";
			case 8:
				return "August";
			case 9:
				return "September";
			case 10:
				return "October";
			case 11:
				return "November";
			default:
				return "December";
			}
		}
	}
}
