package nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.model;

import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.ProjectModel;

/**
 * WorkHistoryModel
 *
 * @author TrungND
 */

public class WorkHistoryModel{

	public String		itemType	= SwConst.SW_SHIFTWORKING_TYPE_SHIFT;

	public String		title;

	public String		value;

	public String		workDate;

	public ProjectModel	project;

	public String		startCheckin;
	public String		endCheckin;

	public String		startShift;
	public String		endShift;

    public String		startWorking;
    public String		endWorking;

	public WorkHistoryModel(){

	}

	public WorkHistoryModel(String itemType, String title){
		this.itemType = itemType;
		this.title = title;
	}

	public WorkHistoryModel(String itemType, String title, String value){
		this.itemType = itemType;
		this.title = title;
		this.value = value;
	}
}
