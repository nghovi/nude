package trente.asia.shiftworking.services.offer.edit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseEditText;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.databinding.FragmentOvertimeEditBinding;
import trente.asia.shiftworking.services.offer.list.OvertimeListFragment;
import trente.asia.shiftworking.services.offer.model.OvertimeModel;
import trente.asia.shiftworking.services.offer.model.WorkOfferModelHolder;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * Created by chi on 9/22/2017.
 */

public class OvertimeEditFragment extends AbstractSwFragment{
        private OvertimeModel offer;
    private ChiaseListDialog			spnType;
        private Map<String, String> targetUserModels	= new HashMap<String, String>();
        private Map<String, List<Double>>	groupInfo;
        private DatePickerDialog datePickerDialogStart;
        private TimePickerDialog timePickerDialogStart;
        private TimePickerDialog			timePickerDialogEnd;
        private ChiaseTextView txtUserName;
        private ChiaseTextView				txtStartTime;
        private ChiaseTextView				txtEndTime;
        private ChiaseTextView				txtStartDate;
        private ChiaseTextView				txtOfferType;
    private ChiaseEditText txtReason;
        private String						activeOfferId;
        private FragmentOvertimeEditBinding binding;

        public void setActiveOfferId(String activeOfferId){
            this.activeOfferId = activeOfferId;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            if(mRootView == null){
                binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overtime_edit, container, false);
                mRootView = binding.getRoot();
            }
            return mRootView;
        }

        @Override
        protected void initView(){
            super.initView();
            super.initHeader(R.drawable.sw_back_white, myself.userName, R.drawable.sw_action_save);
            ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
            imgRightIcon.setOnClickListener(this);

            txtUserName = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_offer_user);
            txtOfferType = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_offer_type);
            txtStartDate = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_start_date);
            txtStartTime = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_start_time);
            txtEndTime = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_end_time);
            txtReason = (ChiaseEditText) getView().findViewById((R.id.txt_fragment_overtime_detail_reason));

            setOnClickListener();
        }

        private void setOnClickListener(){
            getView().findViewById(R.id.lnr_id_type).setOnClickListener(this);
            getView().findViewById(R.id.lnr_start_date).setOnClickListener(this);
            getView().findViewById(R.id.lnr_start_time).setOnClickListener(this);
            getView().findViewById(R.id.lnr_end_time).setOnClickListener(this);
        }

        @Override
        public int getFooterItemId(){
            return 0;
        }

        @Override
        protected void initData(){
            loadWorkOfferForm();
        }

        private void loadWorkOfferForm() {
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("key", activeOfferId);
            }catch(JSONException e){
                e.printStackTrace();
            }

            requestLoad(SwConst.API_OVERTIME_DETAIL, jsonObject, true);
        }

        @Override
        protected void successLoad(JSONObject response, String url){
            if(SwConst.API_OVERTIME_DETAIL.equals(url)){
                offer = CCJsonUtil.convertToModel(response.optString("overtime"), OvertimeModel.class);
                buildDatePickerDialogs(offer);
                if(!CCStringUtil.isEmpty(activeOfferId)){
                    loadWorkOffer(offer);
                }else{
                    txtUserName.setText(myself.userName);
                }
                initDialog(offer);
            }else{
                super.successLoad(response, url);
            }
        }

        private void loadWorkOffer(OvertimeModel offer){
            LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
            try {
                Gson gson = new Gson();
                CAObjectSerializeUtil.deserializeObject(lnrContent, new JSONObject(gson.toJson(offer)));
                txtUserName.setText(offer.userName);
                txtOfferType.setText(offer.overtimeType);
                txtStartDate.setText(offer.startDateString);
                txtStartTime.setText(offer.startTimeString);
                txtEndTime.setText(CCStringUtil.toString(offer.endTimeString));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Button btnDelete = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_delete);
            if(SwConst.OFFER_CAN_EDIT_DELETE.equals(offer.permission) || SwConst.OFFER_ONLY_DELETE.equals(offer.permission)){
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(this);
            }else{
                btnDelete.setVisibility(View.GONE);
            }
        }


        private void initDialog(OvertimeModel offer){
            if(CCStringUtil.isEmpty(activeOfferId)){
                txtOfferType.setText(offer.overtimeTypeList.get(0).value);
                txtOfferType.setValue(offer.overtimeTypeList.get(0).key);
            }

            spnType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), WelfareFormatUtil.convertList2Map(offer.overtimeTypeList), txtOfferType, new ChiaseListDialog.OnItemClicked(){

                @Override
                public void onClicked(String selectedKey, boolean isSelected){
                    dismissLoad();
                }
            });
//            OnOfferTypeChangedUpdateLayout();
        }

//    private void OnOfferTypeChangedUpdateLayout() {
//        String selectedType = txtOfferType.getValue();
//    }

    private void buildDatePickerDialogs(OvertimeModel offerModel){
            Calendar calendar = Calendar.getInstance();
            Date starDate = new Date();
            int startHour = 0, startMinute = 0;
            int endHour = 0, endMinute = 0;

            if(!CCStringUtil.isEmpty(activeOfferId)){
                starDate = CCDateUtil.makeDate(offerModel.startDateString);
                startHour = CCStringUtil.isEmpty(offerModel.startTimeString) ? 0 : CCNumberUtil.toInteger(offerModel.startTimeString.split(":")[0]);
                startMinute = CCStringUtil.isEmpty(offerModel.startTimeString) ? 0 : CCNumberUtil.toInteger(offerModel.startTimeString.split(":")[1]);
                endHour = CCStringUtil.isEmpty(offerModel.endTimeString) ? 0 : CCNumberUtil.toInteger(offerModel.endTimeString.split(":")[0]);
                endMinute = CCStringUtil.isEmpty(offerModel.endTimeString) ? 0 : CCNumberUtil.toInteger(offerModel.endTimeString.split(":")[1]);
            }else{
            }

            calendar.setTime(starDate);
            datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                    String startDate = year + "/" + getDisplayNum(month + 1) + "/" + getDisplayNum(dayOfMonth);
                    txtStartDate.setText(startDate);
                    txtStartDate.setValue(startDate);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            timePickerDialogEnd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    txtEndTime.setText(getDisplayNum(hourOfDay) + ":" + getDisplayNum(minute));
                    txtEndTime.setValue(getDisplayNum(hourOfDay) + ":" + getDisplayNum(minute));
                }
            }, endHour, endMinute, true);

            timePickerDialogStart = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    txtStartTime.setText(getDisplayNum(hourOfDay) + ":" + getDisplayNum(minute));
                    txtStartTime.setValue(getDisplayNum(hourOfDay) + ":" + getDisplayNum(minute));
                }
            }, startHour, startMinute, true);
        }

    public String getDisplayNum(int num){
        if(num <= 9){
            return "0" + num;
        }
        return String.valueOf(num);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        txtStartDate = null;
    }

    private void onClickBtnDone(){
        LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
        JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(lnrContent, null);
        try{
            jsonObject.put("userId", myself.key);
            jsonObject.put("startDateString", txtStartDate);
            jsonObject.put("startTimeString", txtStartTime);
            jsonObject.put("endTimeString", txtEndTime);
            jsonObject.put("overTimeType", txtOfferType);
            jsonObject.put("note", txtReason);
        }catch(JSONException e){
            e.printStackTrace();
        }
        requestUpdate(SwConst.API_OVERTIME_UPDATE, jsonObject, true);
    }

        @Override
        protected void successUpdate(JSONObject response, String url){
            if(SwConst.API_OVERTIME_UPDATE.equals(url)){
                ((ChiaseActivity)activity).isInitData = true;
                ((WelfareActivity)activity).dataMap.put(SwConst.ACTION_OFFER_UPDATE, CCConst.YES);
                getFragmentManager().popBackStack();
            }else if(SwConst.API_OVERTIME_DELETE.equals(url)){
                getFragmentManager().popBackStack();
                ((WelfareActivity)activity).dataMap.put(SwConst.ACTION_OFFER_DELETE, CCConst.YES);
            }else{
                super.successUpdate(response, url);
            }
        }

    private void onClickBtnDelete(){
        final WfDialog dlgConfirmDelete = new WfDialog(activity);
        dlgConfirmDelete.setDialogTitleButton(getString(R.string.fragment_offer_edit_confirm_delete_msg), getString(android.R.string.ok), getString(android.R.string.cancel), new View.OnClickListener() {

            @Override
            public void onClick(View v){
                sendDeleteOfferRequest();
                dlgConfirmDelete.dismiss();
            }
        }).show();
    }

    private void sendDeleteOfferRequest(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("key", activeOfferId);
        }catch(JSONException e){
            e.printStackTrace();
        }
        requestUpdate(SwConst.API_OVERTIME_DELETE, jsonObject, true);
    }

        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.img_id_header_right_icon:
                    onClickBtnDone();
                    break;
                case R.id.lnr_start_date:
                    datePickerDialogStart.show();
                    break;
                case R.id.lnr_start_time:
                    timePickerDialogStart.show();
                    break;
                case R.id.lnr_end_time:
                    timePickerDialogEnd.show();
                    break;
                case R.id.lnr_id_type:
                    spnType.show();
                    break;
                case R.id.btn_fragment_offer_detail_delete:
                    onClickBtnDelete();
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onClickBackBtn(){
            if(isClickNotification){
                emptyBackStack();
                gotoFragment(new OvertimeListFragment());
            }else{
                super.onClickBackBtn();
            }
        }
    }

