package nguyenhoangviet.vpcorp.shiftworking.services.worktime;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.shiftworking.BuildConfig;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.common.models.SwApiHolder;
import nguyenhoangviet.vpcorp.shiftworking.common.models.SwUserModel;
import nguyenhoangviet.vpcorp.shiftworking.common.utils.SwUtil;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.NoticeModel;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

public class WorknoticeFormFragment extends AbstractSwFragment {

    private TextView txtNoticeUser;
    private TextView txtNoticeDate;
    //	private TextView		txtProjectName;
    private TextView txtNoticeType;
    private TextView txtTargetDept;

    private TextView txtReason;
    private TextView txtLocation;
    private ImageView imgMap;
    private ImageView imgPhoto;

    private Button btnCheck;
    private LinearLayout lnrCheckUser;

    private NoticeModel noticeModel;

    public void setNoticeModel(NoticeModel noticeModel) {
        this.noticeModel = noticeModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_work_notice_form, container, false);
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        super.initHeader(R.drawable.sw_back_white, myself.userName, null);

        txtNoticeUser = (TextView) getView().findViewById(R.id.txt_id_notice_user);
        txtNoticeDate = (TextView) getView().findViewById(R.id.txt_id_notice_date);
//		txtProjectName = (TextView)getView().findViewById(R.id.txt_id_project_name);
        txtNoticeType = (TextView) getView().findViewById(R.id.txt_id_notice_type);
        txtTargetDept = (TextView) getView().findViewById(R.id.txt_id_target_dept);
        txtTargetDept = (TextView) getView().findViewById(R.id.txt_id_target_dept);

        txtReason = (TextView) getView().findViewById(R.id.txt_id_reason);
        txtLocation = (TextView) getView().findViewById(R.id.txt_id_location);
        imgMap = (ImageView) getView().findViewById(R.id.img_id_map);
        imgPhoto = (ImageView) getView().findViewById(R.id.img_id_photo);

        lnrCheckUser = (LinearLayout) getView().findViewById(R.id.lnr_id_check_user);
        btnCheck = (Button) getView().findViewById(R.id.btn_id_check);
        btnCheck.setOnClickListener(this);
    }

    @Override
    public void initData() {
        loadNoticeForm();
    }

    private void loadNoticeForm() {

        JSONObject jsonObject = new JSONObject();
        try {
            Date noticeDate = CCDateUtil.makeDateCustom(noticeModel.noticeDate, WelfareConst.WF_DATE_TIME);
            jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, noticeDate));
            jsonObject.put("userId", prefAccUtil.getUserPref().key);
            jsonObject.put("key", noticeModel.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(SwConst.API_NOTICE_FORM, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (SwConst.API_NOTICE_FORM.equals(url)) {
            holder = new SwApiHolder(response);
            noticeModel = CCJsonUtil.convertToModel(response.optString("notice"), NoticeModel.class);
            loadNoticeInfo(noticeModel);
            showChecks(noticeModel);
        } else {
            super.successLoad(response, url);
        }
    }

    private void loadNoticeInfo(NoticeModel notice) {
        canCheck(notice);
        txtNoticeUser.setText(notice.userName);
        txtNoticeDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_HH_MM, WelfareUtil.makeDate(notice.noticeDate)));

        txtNoticeType.setText(SwUtil.getNoticeTypeName(holder.noticeTypes, notice.noticeType));
        txtTargetDept.setText(notice.deptName);
//		txtProjectName.setText(SwUtil.getProjectName(lstProject, notice.projectId));

        txtReason.setText(notice.reason);
        if (!CCStringUtil.isEmpty(notice.location)) {
            txtLocation.setText(notice.location);
            if (!CCStringUtil.isEmpty(notice.gpsLatitude) && !CCStringUtil.isEmpty(notice.gpsLongitude)) {
                int imageWidth = AndroidUtil.getWidthScreen(activity);
                int imageHeight = imageWidth * 6 / 9;
                String mapUrl = WelfareUtil.getGoogleUrl(notice.gpsLatitude, notice.gpsLongitude, imageWidth, imageHeight);
                WfPicassoHelper.loadImage(activity, mapUrl, imgMap, null);
            }
        } else {
            LinearLayout lnrLocation = (LinearLayout) getView().findViewById(R.id.lnr_id_location);
            lnrLocation.setVisibility(View.GONE);
        }

        if (notice.attachment != null && !CCStringUtil.isEmpty(notice.attachment.fileUrl)) {
            WfPicassoHelper.loadImage(activity, BuildConfig.HOST + notice.attachment.fileUrl, imgPhoto, null);
        } else {
            LinearLayout lnrPhoto = (LinearLayout) getView().findViewById(R.id.lnr_id_photo);
            lnrPhoto.setVisibility(View.GONE);
        }
    }

    /**
     * valid user can check for notice
     */
    private void canCheck(NoticeModel notice) {
        boolean isMyself = notice.userId.equals(prefAccUtil.getUserPref().key);
        boolean isChecked = SwUtil.isChecked(notice.checks, prefAccUtil.getUserPref().key);
        if (!isMyself && !isChecked) {
            btnCheck.setVisibility(View.VISIBLE);
        }
    }

    private void showChecks(NoticeModel notice) {
        if (!CCCollectionUtil.isEmpty(notice.checks)) {
            LinearLayout lnrCheck = (LinearLayout) getView().findViewById(R.id.lnr_id_check);
            lnrCheck.setVisibility(View.VISIBLE);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            for (SwUserModel model : notice.checks) {
                View checkerView = inflater.inflate(R.layout.item_notice_detail_checker, null);
                ImageView imgAvatar = (ImageView) checkerView.findViewById(R.id.img_id_avatar);
                TextView txtUserName = (TextView) checkerView.findViewById(R.id.txt_id_user_name);
                TextView txtCheckTime = (TextView) checkerView.findViewById(R.id.txt_id_check_time);
                if (!CCStringUtil.isEmpty(model.avatarPath)) {
                    WfPicassoHelper.loadImage(activity, BuildConfig.HOST + model.avatarPath, imgAvatar, null);
                }
                txtUserName.setText(model.userName);
                Date checkDate = WelfareUtil.makeDate(model.checkDate);
                txtCheckTime.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_HH_MM, checkDate));
                lnrCheckUser.addView(checkerView);
            }
        }
    }

    private void checkNotice() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", noticeModel.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(SwConst.API_NOTICE_CHECK, jsonObject, true);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        if (SwConst.API_NOTICE_CHECK.equals(url)) {
            onClickBackBtn();
        } else {
            super.successUpdate(response, url);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_id_check:
                checkNotice();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onClickBackBtn() {
        if (isClickNotification) {
            emptyBackStack();
            WorkStatusFragment workStatusFragment = new WorkStatusFragment();
            workStatusFragment.setIsShowNoticeListFirst(1);
            gotoFragment(workStatusFragment);
        } else {
            super.onClickBackBtn();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
