package trente.asia.shiftworking.services.offer.model;

import java.util.List;

/**
 * Created by viet on 11/25/2016.
 */

public class WorkOfferModel {

	public static final String	OFFER_TYPE_OVERTIME				= "OTW";
	public static final String	OFFER_TYPE_HOLIDAY_WORKING		= "HDW";

	public static final String	OFFER_STATUS_OFFER				= "OFFER";
	public static final String APPROVE_STATUS_YET = "YET";
	public static final String APPROVE_STATUS_OK = "OK";
	public static final String APPROVE_STATUS_NA = "NA";

	public WorkOfferModel(){
	}

	public class OfferType{

		public String	key;
		public String	value;
	}

	public class OfferDept{

		public String	key;
		public String	value;
	}

	public class Approve{

		public String		date;
		public String		flow;
		public String		flowName;
		public String		groupId;
		public String		historyNo;
		public String		historyName;
		public String		result;
		public String		note;
		public List<String>	memberIds;
	}

	public String				userId;
	public String				userName;
	public String				offerType;
	public String				startDateString;
	public String				startTimeString;
	public String				endDateString;
	public String				endTimeString;
	public String				approveUser1;
	public String				approveResult1;
	public String				approveUser2;
	public String				approveResult2;
	public String				requestDateString;
	public String				approveResult;
	public String				userAvatarPath;
	public String				offerStatus;
	public String				key;
	public String				note;
	public String				companyId;
	public List<ApproveHistory>	listHistories;
}
