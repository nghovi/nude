package trente.asia.welfare.adr.define;

/**
 * WfUrlConst
 *
 * @author TrungND
 */
public class WfUrlConst {

    public static final String WF_ACC_0001 = "/api/account/company";
    public static final String WF_ACC_0002 = "/api/account/signup";
    public static final String WF_ACC_0003 = "/api/account/signin";
    public static final String WF_ACC_0004 = "/api/account/signout";
    public static final String WF_ACC_0005 = "/api/accoUnt/password";

    public static final String WF_ACC_INFO_UPDATE = "/api/account/info/update";
    public static final String WF_ACC_INFO_UPDATEINFO = "/api/account/info/updateInfo";
    public static final String WF_ACC_INFO_UPDATE_AVATAR = "/api/account/info/updateAvatar";
    public static final String WF_ACC_INFO_DETAIL = "/api/account/info/detail";
    public static final String WF_ACC_INFO_MEMBER = "/api/account/info/member";
    public static final String WF_ACC_INFO_SETTING = "/api/account/info/setting";

    public static final String WF_ACC_INFO_REQUEST_FORM = "/api/account/info/request/form";
    public static final String WF_ACC_INFO_REQUEST_UPDATE = "/api/account/info/request/update";


    public static final String WF_DAI_0005 = "/api/report/like";
    public static final String WF_DAI_0006 = "/api/report/comment";

    public static final String WF_MSG_0001 = "/api/message/list";
    public static final String WF_MSG_0002 = "/api/message/board";
    public static final String WF_MSG_0003 = "/api/message/detail";
    public static final String WF_MSG_0004 = "/api/message/update";
    public static final String WF_MSG_0005 = "/api/message/like";
    public static final String WF_MSG_0007 = "/api/message/latest";
    public static final String WF_MSG_0008 = "/api/message/delete";

    public static final String WF_NOT_0001 = "/api/message/note/update";
    public static final String WF_NOT_0002 = "/api/message/note/copy";
    public static final String WF_NOT_0003 = "/api/message/note/detail";

    public static final String WF_CMT_0001 = "/api/message/comment";
    public static final String WF_CMT_0002 = "/api/message/comment/lastest";

    public static final String WF_MSG_CTA_0001 = "/api/message/contact/list";
    public static final String WF_MSG_CTA_0002 = "/api/message/contact/update";

    public static final String WF_DVC_0001 = "/api/device/update";
    public static final String WF_DVC_0002 = "/api/device/clear";

    public static final String WF_REPORT_LIST_MYSELF = "/api/report/list/myself";
    public static final String WF_REPORT_LIST_OTHERS = "/api/report/list/others";
    public static final String WF_REPORT_UPDATE = "/api/report/update";
    public static final String WF_REPORT_LIKE = "/api/report/like";
    public static final String WF_REPORT_COMMENT = "/api/report/comment";
    public static final String WF_REPORT_DETAIL = "/api/report/detail";
    public static final String WF_REPORT_ACTIVITY = "/api/report/activity";
    public static final String WF_REPORT_KPI_GRAPH = "/api/report-graph/rate";

    /**
     * API
     */
    public static final String API_THANKSCARD_VERSION_CHECK = "/api/thanks/versionCheck";
    public static final String API_THANKSCARD_GET_MYPAGE_INFO = "/api/thanks/mypage";
    public static final String API_THANKSCARD_GET_RANK_STAGE_INFO = "/api/thanks/ranks";
    public static final String API_THANKSCARD_GET_POST_CARD_HISTORY = "/api/thanks/history";
    public static final String API_THANKSCARD_GET_RECEIVE_CARD_HISTORY = "/api/thanks/history";
    public static final String API_THANKSCARD_GET_TEMPLATE = "/api/thanks/template";
    public static final String API_THANKSCARD_GET_RANKING = "/api/thanks/ranking";
    public static final String API_THANKSCARD_GET_CATEGORY = "/api/thanks/getAllCategory";

    public static final String API_THANKSCARD_POST_NEW_CARD = "/api/thanks/send";
    public static final String API_THANKSCARD_POST_GET_RECEIVER_DEPARTMENT_INFO = "/api/thanks/getAllDeptNameForPost";
    public static final String API_THANKSCARD_SAVE_AVATAR = "/api/thanks/setAvatar";
    public static final String API_THANKSCARD_PASS_CHANGE = "/api/thanks/passChange";
    public static final String API_THANKSCARD_GET_RANK_INFO = "/api/thanks/ranking/info";
    public static final String API_THANKSCARD_POST_LIKE = "/api/thanks/like";
    public static final String API_THANKSCARD_POST_REGISTER_ID = "/api/thanks/setDeviceId";
    public static final String API_THANKSCARD_SET_NOTICE_FLAG = "/api/thanks/setNoticeFlag";

    public static final String FORGET_PASSWORD = "/forgotPass";

    /**
     * shift working API
     */
    public static final String WF_PRO_0001 = "/api/sw/project/list";
    public static final String WF_CKI_0001 = "/api/sw/checkin";
    public static final String WF_CKI_0002 = "/api/sw/checkin/list";
    public static final String WF_CKI_0003 = "/api/sw/checkin/delete";

    public static final String WF_NOTICE_0001 = "/api/sw/notice/list";
    public static final String WF_NOTICE_0002 = "/api/sw/notice/update";
    public static final String WF_NOTICE_0003 = "/api/sw/notice/form";
    public static final String WF_NOTICE_0004 = "/api/sw/notice/check";
    public static final String WF_NOTICE_0005 = "/api/sw/check";

    public static final String WF_TRANS_0001 = "/api/sw/transit/list";
    public static final String WF_TRANS_0002 = "/api/sw/transit/detail";
    public static final String WF_TRANS_0003 = "/api/sw/transit/update";
    public static final String WF_TRANS_0004 = "/api/sw/transit/delete";

    public static final String WF_API_WOKER_LIST = "/api/sw/work/users";
    public static final String WF_API_WORK_HISTORY = "/api/sw/work/history";

    public static final String WF_SW_OFFER_LIST = "/api/sw/offer/list";
    public static final String WF_SW_OFFER_DETAIL = "/api/sw/offer/detail";
    public static final String WF_SW_OFFER_UPDATE = "/api/sw/offer/update";
    public static final String WF_SW_OFFER_APPROVE = "/api/sw/offer/approve";
    public static final String WF_SW_OFFER_DELETE = "/api/sw/offer/delete";

    /**
     * team360 API
     */
    public static final String API_TEAM_STAMP_LIST = "/api/team/stamp/list";


}
