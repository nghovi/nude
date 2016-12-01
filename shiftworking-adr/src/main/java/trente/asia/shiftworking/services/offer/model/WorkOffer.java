package trente.asia.shiftworking.services.offer.model;

import java.util.List;

/**
 * Created by viet on 11/25/2016.
 */

public class WorkOffer {
    public String permission;
    public static final Integer OFFER_STATUS_OFFERING = 5;
    public static final Integer OFFER_STATUS_DONE = 4;
    public static final Integer OFFER_STATUS_APPROVING = 1;
    public static final Integer OFFER_STATUS_DISABLED = 7;
    public static final Integer OFFER_STATUS_ALL = 0;

    public static final String OFFER_PERMISSION_EDITABLE = "ME";
    public static final String OFFER_PERMISSION_APPROVEABLE = "AP";
    public static final String OFFER_PERMISSION_SHOW= "VW";

    public WorkOffer() {
    }

    public class Approve {

        public String date;
        public String flow;
        public String flowName;
        public String groupId;
        public String historyNo;
        public String historyName;
        public String result;
        public String note;
        public List<String> memberIds;
    }

    public String serviceCd;
    public String viewMode;
    public String viewType;
    public String execType;
    public String viewScreen;
    public String attention;
    public String key;
    public String keyHash;
    public String companyId;
    public String loginUserId;
    public String attachmentFiles;
    public String deleteFileListString;
    public String attachedType;
    public String token;
    public String language;
    public String timezone;
    public String text;
    public int status;
    public String oauthProviderCode;
    public String oauthProviderId;
    public String targetYear;
    public String targetMonth;
    public String targetUserName;
    public String lastUpdateName;
    public String lastUpdateDate;
    public String lastApproveName;
    public String lastApproveDate;
    public String offerUserId;
    public String offerType;
    public String subType;
    public String offerDate;
    public String startDate;
    public String startTime;
    public String endDate;
    public String endTime;
    public String content = "";
    public String note = "";
    public String approveNote;
    public String flow;
    public Approve approve1;
    public Approve approve2;
    public String providerCode;
    public String viewControlCode;
    public String offerDateString;
    public String startDateString;
    public String endDateString;
    public List<ApproveHistory> historyList;
}
