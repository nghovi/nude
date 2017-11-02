package nguyenhoangviet.vpcorp.shiftworking.services.worktime.model;

import java.util.List;

import nguyenhoangviet.vpcorp.shiftworking.common.models.SwUserModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.FileModel;

/**
 * NoticeModel
 *
 * @author TrungND
 */

public class NoticeModel{

	public String				key;
	public String				noticeDate;

	public String				userId;
	public String				userName;

	public String				projectId;
	public String				noticeType;
	public String				reason;

	public String				location;
	public String				gpsLatitude;
	public String				gpsLongitude;

	public String				firstString;
	public String				secondString;

	public String				userAvatar;

	public String				deptName;

	public List<SwUserModel>	checks;
	public FileModel			attachment;
}
