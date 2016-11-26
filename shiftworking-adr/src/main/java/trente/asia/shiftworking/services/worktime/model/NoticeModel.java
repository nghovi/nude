package trente.asia.shiftworking.services.worktime.model;

import java.util.List;

import trente.asia.shiftworking.common.models.SwUserModel;
import trente.asia.welfare.adr.models.FileModel;

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
