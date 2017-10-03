package trente.asia.shiftworking.services.requests.edit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnUserAdapterListener;
import trente.asia.shiftworking.databinding.FragmentVacationEditBinding;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.models.VacationRequestModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * Created by viet on 11/25/2016.
 */

public class VacationEditFragment extends AbstractEditFragment{

	private FragmentVacationEditBinding	binding;
	private String						amount;
	private List<ApiObjectModel>		vacationTypes;
	private DatePickerDialog			datePickerDialogEnd;
	private ChiaseListDialog			spnType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacation_edit, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		binding.txtEndDate.setText(CCFormatUtil.formatDate(new Date()));
		super.initView();
	}

	@Override
	protected void setOnClickListener(){
		super.setOnClickListener();
		binding.lnrEndDate.setOnClickListener(this);
		binding.lnrIdType.setOnClickListener(this);
	}

	@Override
	protected String getDetailApi(){
		return SwConst.API_VACATION_DETAIL;
	}

	@Override
	protected String getUpdateApi(){
		return SwConst.API_VACATION_UPDATE;
	}

	@Override
	protected void initData(){
		super.initData();
		if(CCStringUtil.isEmpty(activeOfferId)){
			loadTypeList();
		}
	}

	private void loadTypeList(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", selectedUser.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(SwConst.API_VACATION_TYPE, jsonObject, true);
	}

	private void getVacationAmount(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", binding.txtOfferType.getValue());
			jsonObject.put("targetUserId", selectedUser.key);
			jsonObject.put("searchStart", binding.txtStartDate.getText());
			jsonObject.put("searchEnd", binding.txtEndDate.getText());
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(SwConst.API_VACATION_AMOUNT, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_VACATION_TYPE.equals(url)){
			getVacationTypeList(response);
		}else if(SwConst.API_VACATION_AMOUNT.equals((url))){
			amount = response.optString("amount");
			float amoutUnit = Float.parseFloat(response.optString("vacationTypeAmount"));
			if(amoutUnit < 1){
				binding.lnrEndDate.setVisibility(View.GONE);
			}else{
				binding.lnrEndDate.setVisibility(View.VISIBLE);
			}
			if(Float.parseFloat(amount) >= 0){
				updateVacationAmount(response.optString("amountString"));
			}else{
				binding.txtStartDate.setText(binding.txtEndDate.getText());
				getVacationAmount();
			}

		}else if(SwConst.API_VACATION_DETAIL.equals(url)){
			if (!CCStringUtil.isEmpty(activeOfferId)) {
				updateLayout(response);
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void updateLayout(JSONObject response){
		VacationRequestModel vacationRequest = CCJsonUtil.convertToModel(response.optString("vacation"), VacationRequestModel.class);
		selectedUser = new UserModel(vacationRequest.userId, vacationRequest.userName);
		binding.txtOfferUser.setText(selectedUser.userName);
		binding.txtOfferType.setText(vacationRequest.vacationName);
		binding.txtOfferType.setValue(vacationRequest.vacationId);
		binding.txtStartDate.setText(vacationRequest.startDateString);
		binding.txtEndDate.setText(vacationRequest.endDateString);
		updateVacationAmount(vacationRequest.amountString);
		binding.switchSickAbsent.setChecked(vacationRequest.sickAbsent);
		binding.edtNote.setText(vacationRequest.note);
		amount = vacationRequest.amount;
		if(Float.parseFloat(amount) < 1){
			binding.lnrEndDate.setVisibility(View.GONE);
		}else{
			binding.lnrEndDate.setVisibility(View.VISIBLE);
		}
		loadTypeList();
	}

	private void updateVacationAmount(String amountString){
		binding.txtAmount.setText(amountString);
	}

	private void getVacationTypeList(JSONObject response){
		vacationTypes = new ArrayList<>();
		String map = response.optString("map");
		if(!CCStringUtil.isEmpty(map)){
			String[] types = map.split(",");
			for(String type : types){
				int equalIndex = type.indexOf("=");
				vacationTypes.add(new ApiObjectModel(type.substring(0, equalIndex), type.substring(equalIndex + 1)));
			}
		}
		initTypeListDialog(vacationTypes);
		buildDatePickerDialogs();
	}

	private void initTypeListDialog(List<ApiObjectModel> vacationTypes){
		if(CCStringUtil.isEmpty(activeOfferId) && !vacationTypes.isEmpty()){
			binding.txtOfferType.setText(vacationTypes.get(0).value);
			binding.txtOfferType.setValue(vacationTypes.get(0).key);
			OnOfferTypeChangedUpdateLayout();
		}

		spnType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), WelfareFormatUtil.convertList2Map(vacationTypes), binding.txtOfferType, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				OnOfferTypeChangedUpdateLayout();
			}
		});
	}

	private void OnOfferTypeChangedUpdateLayout(){
		getVacationAmount();
	}

	@Override
	protected void buildDatePickerDialogs(){
		super.buildDatePickerDialogs();
		Calendar calendar = Calendar.getInstance();
		Date endDate = CCDateUtil.makeDate(binding.txtEndDate.getText().toString());
		calendar.setTime(endDate);
		datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDate = year + "/" + getDisplayNum(month + 1) + "/" + getDisplayNum(dayOfMonth);
				binding.txtEndDate.setText(startDate);
				getVacationAmount();
				buildDatePickerDialogs();
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	protected void setStartDate(String startDate){
		super.setStartDate(startDate);
		binding.txtEndDate.setText(startDate);
		getVacationAmount();
		buildDatePickerDialogs();
	}

	@Override
	protected void onClickBtnDone(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeOfferId);
			jsonObject.put("userId", selectedUser.key);
			jsonObject.put("vacationId", binding.txtOfferType.getValue());
			jsonObject.put("startDateString", binding.txtStartDate.getText().toString());
			jsonObject.put("endDateString", binding.txtEndDate.getText().toString());
			jsonObject.put("note", binding.edtNote.getText().toString());
			jsonObject.put("amount", amount);
			jsonObject.put("sickAbsent", binding.switchSickAbsent.isChecked());
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(getUpdateApi(), jsonObject, true);
	}

	@Override
	public void onSelectUser(UserModel user){
		super.onSelectUser(user);
		loadTypeList();
	}

	@Override
	public void onClick(View v){
		super.onClick(v);
		switch(v.getId()){
		case R.id.lnr_end_date:
			datePickerDialogEnd.show();
			break;
		case R.id.lnr_id_type:
			spnType.show();
			break;
		default:
			break;
		}
	}
}
