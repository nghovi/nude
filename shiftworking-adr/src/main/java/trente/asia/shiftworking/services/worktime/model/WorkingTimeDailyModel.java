package trente.asia.shiftworking.services.worktime.model;

import java.util.Date;

import trente.asia.welfare.adr.models.BitmapModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by takyas on 10/28/16.
 */

public class WorkingTimeDailyModel extends BitmapModel{

	public Integer		userId;

	public UserModel	user;

	public Date			workDate;

	public ProjectModel	project;

	public String		startShift;

	public String		endShift;

    public String		startCheckin;

    public String		endCheckin;

	public String		startWorking;

	public String		endWorking;

	public String		totalWorktime;

	public String		totalBreaktime;

	public String		totalOvertime;

	public String		totalNighttime;

}
