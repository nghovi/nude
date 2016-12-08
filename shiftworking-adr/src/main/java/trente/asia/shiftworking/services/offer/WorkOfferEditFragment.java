package trente.asia.shiftworking.services.offer;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOfferModel;
import trente.asia.shiftworking.services.offer.model.WorkOfferModelHolder;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * Created by viet on 11/25/2016.
 */

public class WorkOfferEditFragment extends AbstractSwFragment{

	private ChiaseListDialog	spnType;
	private DatePickerDialog	datePickerDialogStart;
	private DatePickerDialog	datePickerDialogEnd;
	private TimePickerDialog	timePickerDialogStart;
	private TimePickerDialog	timePickerDialogEnd;
	private ChiaseTextView		txtStartTime;
	private ChiaseTextView		txtEndTime;
//	private EditText			edtNote;
	private ChiaseTextView		txtStartDate;
	private ChiaseTextView		txtEndDate;

	private ChiaseTextView		txtOfferType;
	private String				activeOfferId;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_offer_edit, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_offer;
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.wf_back_white, myself.userName, R.drawable.sw_action_save);
		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setOnClickListener(this);

		txtOfferType = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_offer_type);
		txtStartDate = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_start_date);
		txtEndDate = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_end_date);
		txtStartTime = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_start_time);
		txtEndTime = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_end_time);
//		edtNote = (EditText)getView().findViewById(R.id.edt_fragment_offer_edit_note);

		setOnClickListener();
	}

	@Override
	protected void initData(){
		loadOfferForm();
	}

	private void loadOfferForm(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeOfferId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_SW_OFFER_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_SW_OFFER_DETAIL.equals(url)){
			WorkOfferModelHolder holder = CCJsonUtil.convertToModel(CCStringUtil.toString(response), WorkOfferModelHolder.class);
			initDialog(holder);
            buildDatePickerDialogs(holder.offer);
			if(!CCStringUtil.isEmpty(activeOfferId)){
				loadWorkOffer(holder.offer);
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void setOnClickListener(){
		getView().findViewById(R.id.lnr_id_type).setOnClickListener(this);
		getView().findViewById(R.id.lnr_start_date).setOnClickListener(this);
		getView().findViewById(R.id.lnr_start_time).setOnClickListener(this);
		getView().findViewById(R.id.lnr_end_date).setOnClickListener(this);
		getView().findViewById(R.id.lnr_end_time).setOnClickListener(this);
	}

	private void initDialog(WorkOfferModelHolder holder){
		if(CCStringUtil.isEmpty(activeOfferId)){
			txtOfferType.setText(holder.offerTypeList.get(0).value);
			txtOfferType.setValue(holder.offerTypeList.get(0).key);
		}

		spnType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), WelfareFormatUtil.convertList2Map(holder.offerTypeList), txtOfferType, new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				OnOfferTypeChangedUpdateLayout();
			}
		});
		OnOfferTypeChangedUpdateLayout();
	}

	private void loadWorkOffer(WorkOfferModel offerModel){
		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject(lnrContent, new JSONObject(gson.toJson(offerModel)));
			txtOfferType.setText(offerModel.approveResult);
			txtStartDate.setText(offerModel.startDateString);
            txtStartDate.setValue(offerModel.startDateString);
			txtStartTime.setText(offerModel.startTimeString);
            txtStartTime.setValue(offerModel.startTimeString);
			txtEndDate.setText(offerModel.endDateString);
            txtEndDate.setValue(offerModel.endDateString);
			txtEndTime.setText(offerModel.endTimeString);
            txtEndTime.setValue(offerModel.endTimeString);
		}catch(JSONException e){
			e.printStackTrace();
		}

        Button btnDelete = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_delete);
        if(WorkOfferModel.OFFER_STATUS_OFFER.equals(offerModel.approveResult) && offerModel.userId.equals(myself.key)){
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(this);
        }else{
            btnDelete.setVisibility(View.GONE);
        }
	}

	private void OnOfferTypeChangedUpdateLayout(){
		String selectedType = txtOfferType.getValue();
		if(WorkOfferModel.OFFER_TYPE_HOLIDAY_WORKING.equals(selectedType) || WorkOfferModel.OFFER_TYPE_OVERTIME.equals(selectedType)){
			getView().findViewById(R.id.lnr_start_time).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.lnr_end_time).setVisibility(View.VISIBLE);
		}else{
			getView().findViewById(R.id.lnr_start_time).setVisibility(View.GONE);
			getView().findViewById(R.id.lnr_end_time).setVisibility(View.GONE);
		}
	}

	private void buildDatePickerDialogs(WorkOfferModel offerModel){
		Calendar calendar = Calendar.getInstance();
        Date starDate = new Date();
        Date endDate = new Date();
        int startHour = 0, startMinute = 0;
        int endHour = 0, endMinute = 0;

		if(!CCStringUtil.isEmpty(activeOfferId)){
			starDate = CCDateUtil.makeDate(offerModel.startDateString);
			endDate = CCDateUtil.makeDate(offerModel.endDateString);
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

        calendar.setTime(endDate);
        datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                String startDate = year + "/" + getDisplayNum(month + 1) + "/" + getDisplayNum(dayOfMonth);
                txtEndDate.setText(startDate);
                txtEndDate.setValue(startDate);
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
		txtEndDate = null;
		txtStartDate = null;
	}

	private void onClickBtnDone(){
        LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(lnrContent, null);
		try{
			jsonObject.put("key", activeOfferId);
			jsonObject.put("userId", myself.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_SW_OFFER_UPDATE, jsonObject, true);
	}

    @Override
    protected void successUpdate(JSONObject response, String url){
        if(WfUrlConst.WF_SW_OFFER_UPDATE.equals(url)){
            ((ChiaseActivity)activity).isInitData = true;
            onClickBackBtn();
        }else{
            super.successLoad(response, url);
        }
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
		case R.id.lnr_end_date:
            datePickerDialogEnd.show();
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
		}
	}
}
