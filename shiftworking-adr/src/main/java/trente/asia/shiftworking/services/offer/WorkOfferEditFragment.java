package trente.asia.shiftworking.services.offer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOffer;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * Created by viet on 11/25/2016.
 */

public class WorkOfferEditFragment extends AbstractSwFragment{

	private WorkOffer			offer;
	private List<String>		offerTypes	= new ArrayList<>(SwConst.offerTypes.values());

	private WfSpinner			spnType;
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
		if(offer != null){
			offerTypes = new ArrayList<>();
			offerTypes.add(SwConst.offerTypes.get(offer.offerType));
		}
		spnType = (WfSpinner)getView().findViewById(R.id.spn_fragment_offer_edit_offer_type);
		spnType.setupLayout("", new ArrayList<String>(SwConst.offerTypes.values()), 0, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				selectedType = (String)SwConst.offerTypes.keySet().toArray()[selectedPosition];
			}
		}, false);
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
			txtStartTime.setText(offer.startTime);
			txtEndTime.setText(offer.endTime);

			Date starDate = CCDateUtil.makeDate(offer.startDate);
			c.setTime(starDate);
			startYear = c.get(Calendar.YEAR);
			startMonthOfYear = c.get(Calendar.MONTH);
			startDay = c.get(Calendar.DAY_OF_MONTH);
			startHour = CCStringUtil.isEmpty(offer.startTime) ? 0 : Integer.parseInt(offer.startTime.split(":")[0]);
			startMinute = CCStringUtil.isEmpty(offer.startTime) ? 0 : Integer.parseInt(offer.startTime.split(":")[1]);

			Date endDate = CCDateUtil.makeDate(offer.endDate);
			c.setTime(endDate);
			endYear = c.get(Calendar.YEAR);
			endMonthOfYear = c.get(Calendar.MONTH);
			endDay = c.get(Calendar.DAY_OF_MONTH);
			endHour = CCStringUtil.isEmpty(offer.endTime) ? 0 : Integer.parseInt(offer.endTime.split(":")[0]);
			endMinute = CCStringUtil.isEmpty(offer.endTime) ? 0 : Integer.parseInt(offer.endTime.split(":")[1]);
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
			txtStartDate.setText(startYear + "/" + getDisplayNum(startMonthOfYear + 1) + "/" + getDisplayNum(startDay));
			txtEndDate.setText(endYear + "/" + getDisplayNum(endMonthOfYear + 1) + "/" + getDisplayNum(endDay));
			txtStartTime.setText(getDisplayNum(startHour) + ":" + getDisplayNum(startMinute));
			txtEndTime.setText(getDisplayNum(endHour) + ":" + getDisplayNum(endMinute));
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
		if(offer != null){
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
		}else{
			gotoOfferDetailFragment("1");
		}
	}

	private void gotoOfferDetailFragment(String key){
		WorkOfferDetailFragment fragment = new WorkOfferDetailFragment();
		WorkOffer offer = new WorkOffer();
		offer.key = key;
		fragment.setWorkOffer(offer);
		gotoFragment(fragment);
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
			jsonObject.put("key", offer.key);
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
		}
	}
}
