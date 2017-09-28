package trente.asia.shiftworking.services.offer.edit;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseEditText;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.dialog.SwTimePicker;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnTimePickerListener;
import trente.asia.shiftworking.common.interfaces.OnUserAdapterListener;
import trente.asia.shiftworking.common.models.SwUserModel;
import trente.asia.shiftworking.databinding.FragmentHolidayWorkingDetailBinding;
import trente.asia.shiftworking.databinding.FragmentHolidayWorkingEditBinding;
import trente.asia.shiftworking.databinding.FragmentOvertimeEditBinding;
import trente.asia.shiftworking.services.offer.list.OvertimeListFragment;
import trente.asia.shiftworking.services.offer.model.HolidayWorkingModel;
import trente.asia.shiftworking.services.offer.model.OvertimeModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * Created by chi on 9/22/2017.
 */

public class HolidayWorkingEditFragment extends AbstractSwFragment implements OnUserAdapterListener{

	private HolidayWorkingModel					offer;
	private DatePickerDialog					datePickerDialogStart;
	private ChiaseTextView						txtUserName;
	private ChiaseTextView						txtStartDate;
	private ChiaseEditText						txtReason;
	private String								activeOfferId;
	private FragmentHolidayWorkingEditBinding	binding;
	private String								execType;
	private UserModel							selectedUser;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	public void setExecType(String execType){
		this.execType = execType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_holiday_working_edit, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	private void setOnClickListener(){
		getView().findViewById(R.id.lnr_start_date).setOnClickListener(this);
		Boolean b = Boolean.valueOf(myself.adminFlag);
		if(b && activeOfferId == null){
			getView().findViewById(R.id.arrow_icon).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.lnr_user).setOnClickListener(this);
		}
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	protected void initData(){
		selectedUser = myself;
		loadWorkOfferForm();
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.sw_back_white, myself.userName, R.drawable.sw_action_save);
		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setOnClickListener(this);
		txtUserName = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_holiday_working_detail_offer_user);
		txtStartDate = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_holiday_working_detail_start_date);
		txtReason = (ChiaseEditText)getView().findViewById((R.id.txt_fragment_holiday_working_detail_reason));

		setOnClickListener();
	}

	private void loadWorkOfferForm(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeOfferId);
			jsonObject.put("execType", execType);
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(SwConst.API_HOLIDAY_WORKING_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_HOLIDAY_WORKING_DETAIL.equals(url)){
			offer = CCJsonUtil.convertToModel(response.optString("holidayWork"), HolidayWorkingModel.class);
			buildDatePickerDialogs(offer);
			if(!CCStringUtil.isEmpty(activeOfferId)){
				loadWorkOffer(offer);
			}else{
				txtUserName.setText(selectedUser.userName);
				txtStartDate.setText(CCFormatUtil.formatDate(Calendar.getInstance().getTime()));
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void loadWorkOffer(HolidayWorkingModel offer){
		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject(lnrContent, new JSONObject(gson.toJson(offer)));
			txtUserName.setText(offer.userName);
			txtStartDate.setText(offer.startDateString);
		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	private void buildDatePickerDialogs(HolidayWorkingModel offerModel){
		Calendar calendar = Calendar.getInstance();
		Date starDate = new Date();

		if(!CCStringUtil.isEmpty(activeOfferId)){
			starDate = CCDateUtil.makeDate(offerModel.startDateString);
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
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("userId", selectedUser.getKey());
			jsonObject.put("key", activeOfferId);
			jsonObject.put("startDateString", txtStartDate.getText());
			jsonObject.put("note", txtReason.getText());
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(SwConst.API_HOLIDAY_WORKING_UPDATE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(SwConst.API_HOLIDAY_WORKING_UPDATE.equals(url)){
			((ChiaseActivity)activity).isInitData = true;
			((WelfareActivity)activity).dataMap.put(SwConst.ACTION_OFFER_UPDATE, CCConst.YES);
			getFragmentManager().popBackStack();
		}else{
			super.successUpdate(response, url);
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
		case R.id.lnr_user:
			goToSelectUserEditFragment();
			break;
		default:
			break;
		}
	}

	private void goToSelectUserEditFragment(){
		SelectUserEditFragment fragment = new SelectUserEditFragment();
		fragment.setSelectedUser(selectedUser);
		fragment.setCallback(this);
		gotoFragment(fragment);
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

	@Override
	public void onSelectUser(UserModel user){
		selectedUser = user;
		txtUserName.setText(selectedUser.getUserName());
	}
}
