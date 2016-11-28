package trente.asia.shiftworking.services.offer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOffer;

/**
 * Created by viet on 11/25/2016.
 */

public class WorkOfferEditFragment extends AbstractSwFragment {
    public static final String CMD_RELOAD_OFFER_LIST = "CMD_RELOAD_OFFER_LIST";

    private static final int TIME_PICKER_INTERVAL = 30;
    private WorkOffer offer;
    private String selectedUserId;
    private Map<String, String> currenSubTypes;
    private String offerTypeCode;
    // 1, 2, 3
    private List<String> offerTypes = new ArrayList<>(SwConst.offerTypes.values());


    public void setGroupInfo(Map<String, List<Double>> groupInfo) {
        this.groupInfo = groupInfo;
    }

    private Map<String, List<Double>> groupInfo;

    private String subTypeCode;
    // 1, 2, 9
//    private CSWDateTimePickerDialog datePickerDialogStart;
//    private CSWDateTimePickerDialog datePickerDialogEnd;
    private EditText edtReason;
    private EditText edtMemo;
    private TextView txtStartDate;
    private TextView txtEndDate;
//    private List<User> users = new ArrayList<User>();
    private Map<String, String> targetUsers = new HashMap<String, String>();

    public void setTargetUsers(Map<String, String> targetUsers) {
        this.targetUsers = targetUsers;
    }

    private int startYear, startMonthOfYear, startDay, startHour, startMinute, endYear,
            endMonthOfYear, endDay, endHour, endMinute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_offer_edit, container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
//        buildHeaderWithBackBtn(getString(offer != null ? R.string.fragment_offer_edit_title
//                : R.string.fragment_offer_edit_title_new));
        buildOfferTypeSpinner();
        buildDatePickerDialogs();
        buildEditText();
        buildButtons();
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    private void buildOfferTypeSpinner() {
        if (offer != null) {
            offerTypes = new ArrayList<>();
            offerTypes.add(SwConst.offerTypes.get(offer.offerType));
        }
        buildSpinner(R.id.spn_fragment_offer_edit_offer_type, offerTypes,
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                                               long id) {
                        String offerType = offerTypes.get(position); // absent

//                        offerTypeCode =
//                                (String) CSWUtil.getKeyFromValue(SwConst.offerTypes, offerType);
//                        currenSubTypes = SwConst.subTypes.get(offerTypeCode);
//                        buildSubTypeSpinner(new ArrayList<String>(currenSubTypes.values()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }, offer != null ? offerTypes.indexOf(SwConst.offerTypes.get(offer.offerType)) : 0);
    }

    private void buildSubTypeSpinner(final List<String> subTypes) {
        int position = 0;
        if (offer != null) {
            position = subTypes.indexOf(currenSubTypes.get(offer.subType));
        } else {
            if (offerTypeCode == SwConst.OFFER_TYPE_HOLIDAY) {
                position = subTypes.indexOf(SwConst.SUB_TYPE_HOLIDAY_NO_WORK);
            } else if (offerTypeCode == SwConst.OFFER_TYPE_OVERTIME) {
                position = subTypes.indexOf(SwConst.SUB_TYPE_OVER_TIME_OTHER);
            }
        }
        buildSpinner(R.id.spn_fragment_offer_edit_sub_type, subTypes,
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                                               long id) {
//                        subTypeCode =
//                                (String) CSWUtil.getKeyFromValue(currenSubTypes,
//                                        subTypes.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }, position);
    }

    private void buildEditText() {
        edtReason = (EditText) getView().findViewById(R.id.edt_fragment_offer_edit_reason);
        edtMemo = (EditText) getView().findViewById(R.id.edt_fragment_offer_edit_memo);
        if (offer != null) {
            edtReason.setText(offer.content);
            edtMemo.setText(offer.note);
        }
    }

    private void buildDatePickerDialogs() {
        txtStartDate = (TextView) getView().findViewById(R.id.txt_fragment_offer_edit_start_date);
        txtEndDate = (TextView) getView().findViewById(R.id.txt_fragment_offer_edit_end_date);
        Calendar c = Calendar.getInstance();
        if (offer != null) {
            String strStartDate = offer.startDateString;
//            txtStartDate.setText(CSWUtil.isEmpty(offer.startTime) ? strStartDate : strStartDate
//                    + " ~ " + offer.startTime);
//            String strEndDate = offer.startDateString;
//            txtEndDate.setText(CSWUtil.isEmpty(offer.endTime) ? strEndDate : strEndDate + " ~ "
//                    + offer.endTime);
//            Date starDate = CSWUtil.getDateFromString(strStartDate, SwConst.DISPLAY_DATE_FORMAT);
//            c.setTime(starDate);
//            startYear = c.get(Calendar.YEAR);
//            startMonthOfYear = c.get(Calendar.MONTH);
//            startDay = c.get(Calendar.DAY_OF_MONTH);
//            startHour =
//                    CSWUtil.isEmpty(offer.startTime) ? 0 : Integer.parseInt(offer.startTime
//                            .split(":")[0]);
//            startMinute =
//                    CSWUtil.isEmpty(offer.startTime) ? 0 : Integer.parseInt(offer.startTime
//                            .split(":")[1]);
//
//            Date endDate = CSWUtil.getDateFromString(strEndDate, SwConst.DISPLAY_DATE_FORMAT);
//            c.setTime(endDate);
//            // // TODO: 5/18/2016 setHour, set Minute for c from offer
//            // .startTime ?
//            endYear = c.get(Calendar.YEAR);
//            endMonthOfYear = c.get(Calendar.MONTH);
//            endDay = c.get(Calendar.DAY_OF_MONTH);
//            endHour =
//                    CSWUtil.isEmpty(offer.endTime) ? 0 : Integer
//                            .parseInt(offer.endTime.split(":")[0]);
//            endMinute =
//                    CSWUtil.isEmpty(offer.endTime) ? 0 : Integer
//                            .parseInt(offer.endTime.split(":")[1]);
        } else {
            startYear = c.get(Calendar.YEAR);
            startMonthOfYear = c.get(Calendar.MONTH);
            startDay = c.get(Calendar.DAY_OF_MONTH);
            startHour = 0;
            startMinute = 0;// c.get(Calendar.MINUTE);
            endYear = c.get(Calendar.YEAR);
            endMonthOfYear = c.get(Calendar.MONTH);
            endDay = c.get(Calendar.DAY_OF_MONTH);
            endHour = 0;
//            endMinute = 0;// c.get(Calendar.MINUTE);
//            txtStartDate.setText(startYear + "/" + CSWUtil.getDisplayNum(startMonthOfYear + 1)
//                    + "/" + CSWUtil.getDisplayNum(startDay));
//            txtEndDate.setText(endYear + "/" + CSWUtil.getDisplayNum(endMonthOfYear + 1) + "/"
//                    + CSWUtil.getDisplayNum(endDay));
        }
        getView().findViewById(R.id.lnr_start_date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });

        getView().findViewById(R.id.lnr_end_date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

    }

    private void showStartDatePickerDialog() {
//        if (datePickerDialogStart == null) {
//            datePickerDialogStart = new CSWDateTimePickerDialog(activity);
//            datePickerDialogStart.setListener(new CSWDateTimePickerDialog.OnDateTimeSetListener() {
//
//                @Override
//                public void onDateTimeSet(Calendar c) {
//                    startYear = c.get(Calendar.YEAR);
//                    startMonthOfYear = c.get(Calendar.MONTH);
//                    startDay = c.get(Calendar.DAY_OF_MONTH);
//                    startHour = c.get(Calendar.HOUR_OF_DAY);
//                    startMinute = c.get(Calendar.MINUTE);
//                    String startDate =
//                            startYear + "/" + CSWUtil.getDisplayNum(startMonthOfYear + 1) + "/"
//                                    + CSWUtil.getDisplayNum(startDay);
//                    if (!offerTypeCode.equals(SwConst.OFFER_TYPE_ABSENT)) {
//                        startDate +=
//                                " ~ " + CSWUtil.getDisplayNum(startHour) + ":"
//                                        + CSWUtil.getDisplayNum(startMinute);
//                    }
//                    txtStartDate.setText(startDate);
//                }
//            });
//            datePickerDialogStart.setTimePickerInterval(TIME_PICKER_INTERVAL);
//        }
        Calendar c = Calendar.getInstance();
        c.set(startYear, startMonthOfYear, startDay, startHour, startMinute);
//        datePickerDialogStart.setDateTime(c);
//        datePickerDialogStart
//                .setTimePickerVisible(offerTypeCode.equals(SwConst.OFFER_TYPE_ABSENT) ? false : true);
//        datePickerDialogStart.show();
    }

    private void showEndDatePickerDialog() {
//        if (datePickerDialogEnd == null) {
//            datePickerDialogEnd = new CSWDateTimePickerDialog(activity);
//            datePickerDialogEnd.setListener(new CSWDateTimePickerDialog.OnDateTimeSetListener() {
//
//                @Override
//                public void onDateTimeSet(Calendar c) {
//                    endYear = c.get(Calendar.YEAR);
//                    endMonthOfYear = c.get(Calendar.MONTH);
//                    endDay = c.get(Calendar.DAY_OF_MONTH);
//                    endHour = c.get(Calendar.HOUR_OF_DAY);
//                    endMinute = c.get(Calendar.MINUTE);
//                    String endDate =
//                            endYear + "/" + CSWUtil.getDisplayNum(endMonthOfYear + 1) + "/"
//                                    + CSWUtil.getDisplayNum(endDay);
//                    if (!offerTypeCode.equals(SwConst.OFFER_TYPE_ABSENT)) {
//                        endDate +=
//                                " ~ " + CSWUtil.getDisplayNum(endHour) + ":"
//                                        + CSWUtil.getDisplayNum(endMinute);
//                    }
//                    txtEndDate.setText(endDate);
//                }
//            });
//            datePickerDialogEnd.setTimePickerInterval(TIME_PICKER_INTERVAL);
//        }
//        Calendar c = Calendar.getInstance();
//        c.set(endYear, endMonthOfYear, endDay, endHour, endMinute);
//        datePickerDialogEnd.setDateTime(c);
//        datePickerDialogEnd
//                .setTimePickerVisible(offerTypeCode.equals(SwConst.OFFER_TYPE_ABSENT) ? false : true);
//        datePickerDialogEnd.show();
    }

    private void buildButtons() {
//        Button btnDelete = (Button) getView().findViewById(R.id.btn_fragment_offer_edit_delete);
//        User userMe = prefAccUtil.getUserPref();
//        if (offer == null) {
//            btnDelete.setVisibility(View.GONE);
//        } else {
//            int userType = WorkOfferDetailFragment.getUserType(groupInfo, offer, userMe);
//            if (offer.status == Offer.OFFER_STATUS_APPROVING
//                    && ((offer.approve1.result.equals(SwConst.APPROVE_STATUS_YET) && offer.approve2.result
//                    .equals(SwConst.APPROVE_STATUS_YET))
//                    || (offer.approve1.result.equals(SwConst.APPROVE_STATUS_YET) && offer.approve2.result
//                    .equals(SwConst.APPROVE_STATUS_NA)) || (offer.approve1.result
//                    .equals(SwConst.APPROVE_STATUS_NA) && offer.approve2.result
//                    .equals(SwConst.APPROVE_STATUS_YET)))
//                    && (userType == User.ADMIN || WorkOfferDetailFragment
//                    .isUserOffer(userMe, offer))) {
//                btnDelete.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        onClickBtnDelete();
//                    }
//                });
//            } else {
//                btnDelete.setVisibility(View.GONE);
//            }
//        }
//
//        Button btnUpdate = (Button) getView().findViewById(R.id.btn_fragment_offer_edit_update);
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                onClickBtnUpdate();
//            }
//        });
        buildUserNameSpinner();
    }

    private void onClickBtnUpdate() {
//        Calendar c = Calendar.getInstance();
//        JSONObject jsonObject;
//        if (offer != null) {// edit
//            jsonObject =
//                    getJsonBuilder()
//                            .add("startTime",
//                                    offerTypeCode.equals(SwConst.OFFER_TYPE_ABSENT) ? null
//                                            : getDisplayTime(startHour, startMinute))
//                            .add("endTime",
//                                    offerTypeCode.equals(SwConst.OFFER_TYPE_ABSENT) ? null
//                                            : getDisplayTime(endHour, endMinute))
//                            .add("key", offer.key)
//                            .add("keyHash", offer.keyHash)
//                            .add("execType", "offer")
//                            .add("offerUserId", selectedUserId)
//                            .add("offerType", offerTypeCode)
//                            .add("subType", subTypeCode)
//                            .add("offerDate",
//                                    getDisplayDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
//                                            c.get(Calendar.DAY_OF_MONTH)))
//                            .add("startDate", getDisplayDate(startYear, startMonthOfYear, startDay))
//                            .add("endDate", getDisplayDate(endYear, endMonthOfYear, endDay))
//                            .add("content", edtReason.getText().toString())
//                            .add("note", edtMemo.getText().toString()).getJsonObj();
//        } else {// create new offer
//            jsonObject =
//                    getJsonBuilder()
//                            .add("startTime", getDisplayTime(startHour, startMinute))
//                            .add("endTime", getDisplayTime(endHour, endMinute))
//                            .add("key", null)
//                            .add("keyHash", null)
//                            .add("execType", "offer")
//                            .add("offerUserId", selectedUserId)
//                            .add("offerType", offerTypeCode)
//                            .add("subType", subTypeCode)
//                            .add("offerDate",
//                                    getDisplayDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
//                                            c.get(Calendar.DAY_OF_MONTH)))
//                            .add("startDate", getDisplayDate(startYear, startMonthOfYear, startDay))
//                            .add("endDate", getDisplayDate(endYear, endMonthOfYear, endDay))
//                            .add("content", edtReason.getText().toString())
//                            .add("note", edtMemo.getText().toString()).getJsonObj();
//        }
//        requestUpdate(SwConst.API_WORK_OFFER_UPDATE, jsonObject, true);
    }

    private void sendDeleteOfferRequest() {
//        JSONObject jsonObject =
//                getJsonBuilder().add("key", offer.key).add("keyHash", offer.keyHash).getJsonObj();
//        requestUpdate(SwConst.API_WORK_OFFER_DELETE, jsonObject, true);
    }

//    private String getDisplayDate(int year, int monthOfYear, int day) {
//        return year + "/" + CSWUtil.getDisplayNum(monthOfYear + 1) + "/"
//                + CSWUtil.getDisplayNum(day);
//    }
//
//    private String getDisplayTime(int hour, int minute) {
//        return CSWUtil.getDisplayNum(hour) + ":" + CSWUtil.getDisplayNum(minute);
//    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
//        if (SwConst.API_WORK_OFFER_DELETE.equals(url)) {
//            Toast.makeText(activity, getString(R.string.fragment_work_offer_edit_delete_success),
//                    Toast.LENGTH_LONG).show();
//            ((MainActivity) activity).onClickOfferListFooter(endMonthOfYear);
//        } else {
//            ((MainActivity) activity).isInitData = true;
//            ((MainActivity) activity).setCommand(CMD_RELOAD_OFFER_LIST, new Bundle());
//            onClickBackBtn();
//        }
    }

    private void onClickBtnDelete() {
//        final ChiaseWorkDialog dlgConfirmDelete = new ChiaseWorkDialog(activity);
//        dlgConfirmDelete.setDialogTitleButton(
//                getString(R.string.fragment_offer_edit_confirm_delete_msg),
//                getString(android.R.string.ok), getString(android.R.string.cancel),
//                new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        sendDeleteOfferRequest();
//                        dlgConfirmDelete.dismiss();
//                    }
//                }).show();

    }

    private void buildUserNameSpinner() {
//        final List<String> userNames = new ArrayList<>();
//        if (offer == null) { // create new
//            User userMe = prefAccUtil.getUserPref();
//            for (String userId : targetUsers.keySet()) {
//                if (userMe.isBoss || (!userMe.isBoss && userId.equals(userMe.userId))) {
//                    User user = new User();
//                    user.userId = userId;
//                    user.viewName = targetUsers.get(userId);
//                    users.add(user);
//                }
//            }
//        } else {
//            User offerUser = new User();
//            offerUser.userId = offer.offerUserId;
//            offerUser.viewName = offer.targetUserName;
//            users.add(offerUser);
//        }
//        for (User user : users) {
//            userNames.add(CSWUtil.isEmpty(user.viewName) ? user.userId : user.viewName);
//        }
//
//        buildSpinner(R.id.spn_fragment_offer_edit_username, userNames,
//                new AdapterView.OnItemSelectedListener() {
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position,
//                                               long id) {
//                        selectedUserId = users.get(position).userId;
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                }, 0); // // TODO: 5/16/2016
    }

    private void buildSpinner(int spinnerId, List<String> values,
                              AdapterView.OnItemSelectedListener listener, int defaultPosition) {
        Spinner spinner = (Spinner) getView().findViewById(spinnerId);

//        WorkTransportDetailFragment.buildSpinner(activity, spinner, values, listener,
//                defaultPosition);
    }

//    public void setOffer(Offer offer) {
//        this.offer = offer;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        edtReason = null;
        edtMemo = null;
        txtEndDate = null;
        txtStartDate = null;
//        datePickerDialogEnd = null;
//        datePickerDialogStart = null;
    }

    @Override
    public void onClick(View v) {

    }
}
