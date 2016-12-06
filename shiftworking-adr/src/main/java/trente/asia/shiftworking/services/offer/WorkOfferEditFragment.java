package trente.asia.shiftworking.services.offer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOffer;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * Created by viet on 11/25/2016.
 */

public class WorkOfferEditFragment extends AbstractSwFragment{

	private WorkOffer			offer;
	private ChiaseListDialog	spnType;
	private DatePickerDialog	datePickerDialogStart;
	private DatePickerDialog	datePickerDialogEnd;
	private TimePickerDialog	timePickerDialogStart;
	private TimePickerDialog	timePickerDialogEnd;
	private TextView			txtStartTime;
	private TextView			txtEndTime;
	private EditText			edtNote;
	private TextView			txtStartDate;
	private TextView			txtEndDate;
	private int					startYear, startMonthOfYear, startDay, startHour, startMinute, endYear, endMonthOfYear, endDay, endHour, endMinute;
	private String				selectedType;
	private Map<String, String>	offerTypesMaster;
	private Map<String, String>	offerStatusMaster;
	private ChiaseTextView		txtOfferType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_offer_edit, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		buildHeaderWithBackBtn();
		buildOfferTypeSpinner();
		buildDatePickerDialogs();
		buildEditText();
	}

	private void buildHeaderWithBackBtn(){
		super.initHeader(R.drawable.wf_back_white, myself.userName, R.drawable.ic_action_remove);
		ImageView btnDone = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		btnDone.setOnClickListener(this);
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	private void buildOfferTypeSpinner(){
		txtOfferType = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_offer_type);
		int selectedPosition = 0;
		if(offer != null){
			selectedPosition = new ArrayList<String>(offerTypesMaster.keySet()).indexOf(offer.offerType);
		}
		String offerTypeCode = (String)offerTypesMaster.keySet().toArray()[selectedPosition];
		txtOfferType.setText(offerTypesMaster.get(offerTypeCode));
		txtOfferType.setValue(offerTypeCode);
		txtOfferType.setOnClickListener(this);
		spnType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), offerTypesMaster, txtOfferType, new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				selectedType = (String)offerTypesMaster.keySet().toArray()[position];
				OnOfferTypeChangedUpdateLayout();
			}
		});
	}

	private void OnOfferTypeChangedUpdateLayout(){
		if(WorkOffer.OFFER_TYPE_HOLIDAY_WORKING.equals(selectedType) || WorkOffer.OFFER_TYPE_OVERTIME.equals(selectedType)){
			getView().findViewById(R.id.lnr_start_time).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.lnr_end_time).setVisibility(View.VISIBLE);
		}else{
			getView().findViewById(R.id.lnr_start_time).setVisibility(View.GONE);
			getView().findViewById(R.id.lnr_end_time).setVisibility(View.GONE);
		}
	}

	private void buildEditText(){
		edtNote = (EditText)getView().findViewById(R.id.edt_fragment_offer_edit_note);
		if(offer != null){
			edtNote.setText(offer.note);
		}
	}

	private void buildDatePickerDialogs(){
		txtStartDate = (TextView)getView().findViewById(R.id.txt_fragment_offer_edit_start_date);
		txtEndDate = (TextView)getView().findViewById(R.id.txt_fragment_offer_edit_end_date);
		txtStartTime = (TextView)getView().findViewById(R.id.txt_fragment_offer_edit_start_time);
		txtEndTime = (TextView)getView().findViewById(R.id.txt_fragment_offer_edit_end_time);

		Calendar c = Calendar.getInstance();
		if(offer != null){
			txtStartDate.setText(offer.startDateString);
			txtEndDate.setText(offer.endDateString);
			txtStartTime.setText(offer.startTimeString);
			txtEndTime.setText(offer.endTimeString);

			Date starDate = CCDateUtil.makeDate(offer.startDateString);
			c.setTime(starDate);
			startYear = c.get(Calendar.YEAR);
			startMonthOfYear = c.get(Calendar.MONTH);
			startDay = c.get(Calendar.DAY_OF_MONTH);
			startHour = CCStringUtil.isEmpty(offer.startTimeString) ? 0 : Integer.parseInt(offer.startTimeString.split(":")[0]);
			startMinute = CCStringUtil.isEmpty(offer.startTimeString) ? 0 : Integer.parseInt(offer.startTimeString.split(":")[1]);

			Date endDate = CCDateUtil.makeDate(offer.endDateString);
			c.setTime(endDate);
			endYear = c.get(Calendar.YEAR);
			endMonthOfYear = c.get(Calendar.MONTH);
			endDay = c.get(Calendar.DAY_OF_MONTH);
			endHour = CCStringUtil.isEmpty(offer.endTimeString) ? 0 : Integer.parseInt(offer.endTimeString.split(":")[0]);
			endMinute = CCStringUtil.isEmpty(offer.endTimeString) ? 0 : Integer.parseInt(offer.endTimeString.split(":")[1]);
		}else{
			startYear = c.get(Calendar.YEAR);
			startMonthOfYear = c.get(Calendar.MONTH);
			startDay = c.get(Calendar.DAY_OF_MONTH);
			startHour = 0;
			startMinute = 0;// c.get(Calendar.MINUTE);
			endYear = c.get(Calendar.YEAR);
			endMonthOfYear = c.get(Calendar.MONTH);
			endDay = c.get(Calendar.DAY_OF_MONTH);
			endHour = 0;
			endMinute = 0;// c.get(Calendar.MINUTE);
			// txtStartDate.setText(startYear + "/" + getDisplayNum(startMonthOfYear + 1) + "/" + getDisplayNum(startDay));
			// txtEndDate.setText(endYear + "/" + getDisplayNum(endMonthOfYear + 1) + "/" + getDisplayNum(endDay));
			// txtStartTime.setText(getDisplayNum(startHour) + ":" + getDisplayNum(startMinute));
			// txtEndTime.setText(getDisplayNum(endHour) + ":" + getDisplayNum(endMinute));
		}
		getView().findViewById(R.id.lnr_start_date).setOnClickListener(this);
		getView().findViewById(R.id.lnr_start_time).setOnClickListener(this);
		getView().findViewById(R.id.lnr_end_date).setOnClickListener(this);
		getView().findViewById(R.id.lnr_end_time).setOnClickListener(this);
	}

	private void showEndTimePickerDialog(){
		if(timePickerDialogEnd == null){
			timePickerDialogEnd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute){
					endHour = hourOfDay;
					endMinute = minute;
					txtEndTime.setText(getDisplayNum(endHour) + ":" + getDisplayNum(endMinute));
				}
			}, endHour, endMinute, true);
		}
		timePickerDialogEnd.show();
	}

	private void showStartTimePickerDialog(){
		if(timePickerDialogStart == null){
			timePickerDialogStart = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute){
					startHour = hourOfDay;
					startMinute = minute;
					txtStartTime.setText(getDisplayNum(startHour) + ":" + getDisplayNum(startMinute));
				}
			}, startHour, startMinute, true);
		}
		timePickerDialogStart.show();
	}

	private void showStartDatePickerDialog(){
		if(datePickerDialogStart == null){
			datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
					startYear = year;
					startMonthOfYear = month;
					startDay = dayOfMonth;
					String startDate = startYear + "/" + getDisplayNum(startMonthOfYear + 1) + "/" + getDisplayNum(startDay);
					txtStartDate.setText(startDate);
				}
			}, startYear, startMonthOfYear, startDay);

		}
		datePickerDialogStart.show();
	}

	private void showEndDatePickerDialog(){
		if(datePickerDialogEnd == null){
			datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
					endYear = year;
					endMonthOfYear = month;
					endDay = dayOfMonth;
					String endDate = endYear + "/" + getDisplayNum(endMonthOfYear + 1) + "/" + getDisplayNum(endDay);
					txtEndDate.setText(endDate);
				}
			}, endYear, endMonthOfYear, endDay);

		}
		datePickerDialogEnd.show();
	}

	public String getDisplayNum(int num){
		if(num <= 9){
			return "0" + num;
		}
		return String.valueOf(num);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		((ChiaseActivity)activity).isInitData = true;
		onClickBackBtn();
	}

	public void setOfferTypeStatusMaster(Map<String, String> offerTypesMaster, Map<String, String> offerStatusMaster){
		this.offerTypesMaster = new LinkedHashMap<>();
		for(String key : offerTypesMaster.keySet()){
			if(!key.equals("0")){
				this.offerTypesMaster.put(key, offerTypesMaster.get(key));
			}
		}
		this.offerStatusMaster = offerStatusMaster;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		edtNote = null;
		txtEndDate = null;
		txtStartDate = null;
	}

	private void onClickBtnDone(){
		JSONObject jsonObject = new JSONObject();
		try{
			if(offer != null){
				jsonObject.put("key", offer.key);
			}
			jsonObject.put("userId", myself.key);
			jsonObject.put("offerType", selectedType);
			jsonObject.put("startDateString", txtStartDate.getText().toString());
			jsonObject.put("endDateString", txtEndDate.getText().toString());
			jsonObject.put("startTimeString", txtStartTime.getText().toString());
			jsonObject.put("endTimeString", txtEndTime.getText().toString());
			jsonObject.put("note", edtNote.getText().toString());

		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_SW_OFFER_UPDATE, jsonObject, true);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			onClickBtnDone();
			break;
		case R.id.lnr_start_date:
			showStartDatePickerDialog();
			break;
		case R.id.lnr_end_date:
			showEndDatePickerDialog();
			break;
		case R.id.lnr_start_time:
			showStartTimePickerDialog();
			break;
		case R.id.lnr_end_time:
			showEndTimePickerDialog();
			break;
		case R.id.txt_fragment_offer_edit_offer_type:
			spnType.show();
			break;
		}
	}

	public void setOffer(WorkOffer offer){
		this.offer = offer;
	}
}
