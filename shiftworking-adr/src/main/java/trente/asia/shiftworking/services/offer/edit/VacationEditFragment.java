package trente.asia.shiftworking.services.offer.edit;

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
import trente.asia.shiftworking.services.offer.model.VacationModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * Created by viet on 11/25/2016.
 */

public class VacationEditFragment extends AbstractSwFragment implements OnUserAdapterListener{

	private ChiaseListDialog			spnType;
	private DatePickerDialog			datePickerDialogStart;
	private DatePickerDialog			datePickerDialogEnd;
	private ChiaseTextView				txtAmount;

	private ChiaseTextView				txtStartDate;
	private ChiaseTextView				txtEndDate;

	private ChiaseTextView				txtOfferType;
	private String						activeOfferId;
	private FragmentVacationEditBinding	binding;

	private ChiaseTextView				txtUserName;
	private UserModel					selectedUser;

	private List<ApiObjectModel>		vacationTypes;
	private String						amount;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacation_edit, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.sw_back_white, myself.userName, R.drawable.sw_action_save);
		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setOnClickListener(this);

		txtUserName = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_detail_offer_user);
		txtOfferType = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_offer_type);
		txtStartDate = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_start_date);
		txtEndDate = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_edit_end_date);
		txtAmount = (ChiaseTextView)getView().findViewById(R.id.txt_amount);

		txtStartDate.setText(CCFormatUtil.formatDate(new Date()));
		txtEndDate.setText(CCFormatUtil.formatDate(new Date()));
		setOnClickListener();
	}

	private void setOnClickListener(){
		if (Boolean.parseBoolean(myself.adminFlag) && CCStringUtil.isEmpty(activeOfferId)) {
			binding.lnrUser.setOnClickListener(this);
		} else {
			binding.userArrow.setVisibility(View.INVISIBLE);
		}
		binding.lnrStartDate.setOnClickListener(this);
		binding.lnrEndDate.setOnClickListener(this);
		binding.lnrIdType.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		if (CCStringUtil.isEmpty(activeOfferId)) {
			selectedUser = myself;
			txtUserName.setText(selectedUser.userName);
		} else {
			loadVacationDetail();
		}
		loadTypeList();
	}

	private void loadVacationDetail() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("key", activeOfferId);
			jsonObject.put("execType", SwConst.SW_OFFER_EXEC_TYPE_VIEW);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestLoad(SwConst.API_VACATION_DETAIL, jsonObject, true);
	}

	private void loadTypeList(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", "557");
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(SwConst.API_VACATION_TYPE, jsonObject, true);
	}

	private void getVacationAmount(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", txtOfferType.getValue());
			jsonObject.put("targetUserId", selectedUser.key);
			jsonObject.put("searchStart", txtStartDate.getText());
			jsonObject.put("searchEnd", txtEndDate.getText());
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
			updateVacationAmount(amount);
		}else if (SwConst.API_VACATION_DETAIL.equals(url)) {
			updateLayout(response);
		} else {
			super.successLoad(response, url);
		}
	}

	private void updateLayout(JSONObject response) {
		VacationModel vacation = CCJsonUtil.convertToModel(response.optString("vacation"), VacationModel.class);
		if (vacation == null) {
			return;
		}
		selectedUser = new UserModel(vacation.userId, vacation.userName);
		txtUserName.setText(selectedUser.userName);
		txtOfferType.setText(vacation.vacationName);
		txtOfferType.setValue(vacation.vacationId);
		txtStartDate.setText(vacation.startDateString);
		txtEndDate.setText(vacation.endDateString);
		updateVacationAmount(vacation.amount);
		binding.switchSickAbsent.setChecked(vacation.sickAbsent);
		binding.edtNote.setText(vacation.note);
	}

	private void getVacationTypeList(JSONObject response){
		vacationTypes = new ArrayList<>();
		vacationTypes.add(new ApiObjectModel("3", "birthday"));
		vacationTypes.add(new ApiObjectModel("16", "winter"));
		vacationTypes.add(new ApiObjectModel("4", "trainning"));
		// String[] types = response.optString("map").split(",");
		// for (String type : types) {
		// int equalIndex = type.indexOf("=");
		// vacationTypes.add(new ApiObjectModel(type.substring(0, equalIndex), type.substring(equalIndex + 1)));
		// }
		initTypeListDialog(vacationTypes);
		buildDatePickerDialogs();
	}

	private void updateVacationAmount(String amount){
		String textAmount;
		float number = Float.parseFloat(amount);
		if(number > 1){
			textAmount = getString(R.string.sw_day, "1") + " x " + (int)number;
			binding.lnrEndDate.setVisibility(View.VISIBLE);
		}else if(number == 1){
			textAmount = getString(R.string.sw_day, "1");
			binding.lnrEndDate.setVisibility(View.VISIBLE);
		}else{
			textAmount = getString(R.string.sw_day, amount);
			binding.lnrEndDate.setVisibility(View.GONE);
		}
		txtAmount.setText(textAmount);
	}

	private void initTypeListDialog(List<ApiObjectModel> vacationTypes){
		if(CCStringUtil.isEmpty(activeOfferId)){
			txtOfferType.setText(vacationTypes.get(0).value);
			txtOfferType.setValue(vacationTypes.get(0).key);
			OnOfferTypeChangedUpdateLayout();
		}

		spnType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), WelfareFormatUtil.convertList2Map(vacationTypes), txtOfferType, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				OnOfferTypeChangedUpdateLayout();
			}
		});
	}

	private void OnOfferTypeChangedUpdateLayout(){
		getVacationAmount();
	}

	private void buildDatePickerDialogs(){
		Calendar calendar = Calendar.getInstance();
		Date starDate = CCDateUtil.makeDate(txtStartDate.getText().toString());
		Date endDate = CCDateUtil.makeDate(txtEndDate.getText().toString());

		calendar.setTime(starDate);
		datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDate = year + "/" + getDisplayNum(month + 1) + "/" + getDisplayNum(dayOfMonth);
				txtStartDate.setText(startDate);
				txtEndDate.setText(startDate);
				getVacationAmount();
				buildDatePickerDialogs();
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		calendar.setTime(endDate);
		datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDate = year + "/" + getDisplayNum(month + 1) + "/" + getDisplayNum(dayOfMonth);
				txtEndDate.setText(startDate);
				getVacationAmount();
				buildDatePickerDialogs();
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeOfferId);
			jsonObject.put("userId", selectedUser.key);
			jsonObject.put("vacationId", txtOfferType.getValue());
			jsonObject.put("startDateString", txtStartDate.getText().toString());
			jsonObject.put("endDateString", txtEndDate.getText().toString());
			jsonObject.put("note", binding.edtNote.getText().toString());
			jsonObject.put("amount", amount);
			jsonObject.put("sickAbsent", binding.switchSickAbsent.isChecked());
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(SwConst.API_VACATION_UPDATE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(SwConst.API_VACATION_UPDATE.equals(url)){
			((ChiaseActivity)activity).isInitData = true;
			((WelfareActivity)activity).dataMap.put(SwConst.ACTION_OFFER_UPDATE, CCConst.YES);
			getFragmentManager().popBackStack();
		}else if(SwConst.API_OFFER_DELETE.equals(url)){
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
		requestUpdate(SwConst.API_VACATION_DELETE, jsonObject, true);
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
		case R.id.lnr_id_type:
			spnType.show();
			break;
		case R.id.btn_fragment_offer_detail_delete:
			onClickBtnDelete();
			break;
		case R.id.lnr_user:
			SelectUserEditFragment fragment = new SelectUserEditFragment();
			fragment.setSelectedUser(selectedUser);
			fragment.setCallback(this);
			gotoFragment(fragment);
			break;
		default:
			break;
		}
	}

	@Override
	public void onSelectUser(UserModel user){
		selectedUser = user;
		txtUserName.setText(selectedUser.userName);
	}
}
