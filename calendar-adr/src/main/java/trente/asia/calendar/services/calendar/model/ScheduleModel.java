package trente.asia.calendar.services.calendar.model;

/**
 * ScheduleModel
 *
 * @author TrungND
 */

public class ScheduleModel{

	public String	scheduleName;
	public String	scheduleNote;

	public String	scheduleColor;
	public String	scheduleDate;

	public ScheduleModel(){

	}

	public ScheduleModel(String scheduleName, String scheduleDate, String scheduleColor){
		this.scheduleName = scheduleName;
		this.scheduleDate = scheduleDate;
		this.scheduleColor = scheduleColor;
	}
}
