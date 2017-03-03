package trente.asia.calendar.commons.model;

import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.model.ScheduleModel;

/**
 * ScheduleRepeatModel
 *
 * @author TrungND
 */

public class ScheduleRepeatModel{

	public String	repeatType;
	public String	repeatData;
	public String	repeatLimitType;

	public String	repeatEnd;
	public String	repeatInterval;

	public ScheduleRepeatModel(){
		repeatType = ClConst.SCHEDULE_REPEAT_TYPE_NONE;
	}

	public ScheduleRepeatModel(ScheduleModel scheduleModel){
		repeatType = scheduleModel.repeatType;
		repeatData = scheduleModel.repeatData;
		repeatLimitType = scheduleModel.repeatLimitType;
		repeatEnd = scheduleModel.repeatEnd;
		repeatInterval = scheduleModel.repeatInterval;
	}
}
