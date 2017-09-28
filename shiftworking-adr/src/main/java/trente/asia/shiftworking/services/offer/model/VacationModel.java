package trente.asia.shiftworking.services.offer.model;

import java.util.List;

/**
 * Created by tien on 9/22/2017.
 */

public class VacationModel{

	public String				key;
	public String				vacationId;
	public String				vacationName;
	public String				endDate;
	public String				endDateString;
	public String				startDate;
	public String				startDateString;
	public String				userAvatarPath;
	public String				userId;
	public String				userName;
	public String				note;
	public String				approveUser1;
	public String				approveResult1;
	public String				approveResult2;
	public String				approveUser2;
	public String				requestDateString;
	public String				execType;
	public String				offerStatus;
	public String				offerStatusName;
	public String				amount;
	public String				amountString;
	public boolean				sickAbsent;
	public List<ApproveHistory>	listHistories;
}
