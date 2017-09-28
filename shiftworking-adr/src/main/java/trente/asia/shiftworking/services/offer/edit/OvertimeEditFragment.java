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
import java.util.Map;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDataUtil;
import asia.chiase.core.util.CCDateUtil;
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
import trente.asia.shiftworking.databinding.FragmentOvertimeEditBinding;
import trente.asia.shiftworking.services.offer.list.OvertimeListFragment;
import trente.asia.shiftworking.services.offer.model.OvertimeModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by chi on 9/22/2017.
 */

public class OvertimeEditFragment extends AbstractSwFragment implements OnTimePickerListener,OnUserAdapterListener{

	private OvertimeModel				offer;
	private ChiaseListDialog			spnType;
	private DatePickerDialog			datePickerDialogStart;
	private SwTimePicker				timePickerDialog;
	private ChiaseTextView				txtUserName;
	private ChiaseTextView				txtStartTime;
	private ChiaseTextView				txtEndTime;
	private ChiaseTextView				txtStartDate;
	private ChiaseTextView				txtOfferType;
	private ChiaseEditText				txtReason;
	private String						activeOfferId;
	private FragmentOvertimeEditBinding	binding;
	private String						execType;
	private List<ApiObjectModel>		typeList;
	private String						m;
	private String						h;
	private boolean						timePickerStart;
	private UserModel					selectedUser;
	private int							selectedHour;
	private int							selectedMinute;
	private Map<String, String>			typeListMap;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	public void setExecType(String execType){
		this.execType = execType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overtime_edit, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	private void setOnClickListener(){
		getView().findViewById(R.id.lnr_id_type).setOnClickListener(this);
		getView().findViewById(R.id.lnr_start_date).setOnClickListener(this);
		getView().findViewById(R.id.lnr_start_time).setOnClickListener(this);
		getView().findViewById(R.id.lnr_end_time).setOnClickListener(this);
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

		txtUserName = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_offer_user);
		txtOfferType = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_offer_type);
		txtStartDate = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_start_date);
		txtStartTime = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_start_time);
		txtEndTime = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_overtime_detail_end_time);
		txtReason = (ChiaseEditText)getView().findViewById((R.id.txt_fragment_overtime_detail_reason));

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

		requestLoad(SwConst.API_OVERTIME_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_OVERTIME_DETAIL.equals(url)){
			offer = CCJsonUtil.convertToModel(response.optString("overtime"), OvertimeModel.class);
			typeList = CCJsonUtil.convertToModelList(response.optString("overtimeTypeList"), ApiObjectModel.class);
			typeListMap = WelfareFormatUtil.convertList2Map(typeList);
			buildDatePickerDialogs(offer);
			if(!CCStringUtil.isEmpty(activeOfferId)){
				loadWorkOffer(offer);
			}else{
				txtUserName.setText(selectedUser.userName);
			}
			initDialog(typeList);
		}else{
			super.successLoad(response, url);
		}
	}

	private void loadWorkOffer(OvertimeModel offer){
		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject(lnrContent, new JSONObject(gson.toJson(offer)));
			txtUserName.setText(offer.userName);
			txtOfferType.setText(typeListMap.get(offer.overtimeType));
			txtStartDate.setText(offer.startDateString);
			txtStartTime.setText(offer.startTimeString);
			txtEndTime.setText(CCStringUtil.toString(offer.endTimeString));

		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	private void initDialog(final List<ApiObjectModel> o){
		if(CCStringUtil.isEmpty(activeOfferId)){
			txtOfferType.setText(o.get(0).value);
			txtOfferType.setValue(o.get(0).key);
		}

		spnType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), WelfareFormatUtil.convertList2Map(o), txtOfferType, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				if("E".equals(selectedKey)){
					txtOfferType.setText("Early");
				}else{
					txtOfferType.setText("Overtime");
				}
			}
		});
	}

	private void buildDatePickerDialogs(OvertimeModel offerModel){
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
			jsonObject.put("startTimeString", txtStartTime.getText());
			jsonObject.put("endTimeString", txtEndTime.getText());
			jsonObject.put("overtimeType", txtOfferType.getValue());
			jsonObject.put("note", txtReason.getText());
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
		case R.id.lnr_user:
			goToSelectUserEditFragment();
			break;
		case R.id.lnr_start_date:
			datePickerDialogStart.show();
			break;
		case R.id.lnr_start_time:
			timePickerStart = true;
			selectedHour = Integer.parseInt(txtStartTime.getText().toString().split(":")[0]);
			selectedMinute = Integer.parseInt(txtStartTime.getText().toString().split(":")[1]);
			openTimePicker();
			break;
		case R.id.lnr_end_time:
			timePickerStart = false;
			selectedHour = Integer.parseInt(txtEndTime.getText().toString().split(":")[0]);
			selectedMinute = Integer.parseInt(txtEndTime.getText().toString().split(":")[1]);
			openTimePicker();
			break;
		case R.id.lnr_id_type:
			spnType.show();
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

	private void openTimePicker(){
		timePickerDialog = new SwTimePicker();
		timePickerDialog.setStartTime(timePickerStart);
		timePickerDialog.setCallback(this);
		timePickerDialog.setSelectedHour(selectedHour);
		timePickerDialog.setSelectedMinute(selectedMinute);
		FragmentManager fm = getFragmentManager();
		timePickerDialog.show(fm, "dialog");
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
	public void onTimePickerCompleted(int hour, int minute){
		if(hour < 10){
			h = "0" + hour;
		}else{
			h = String.valueOf(hour);
		}
		if(minute < 10){
			m = "0" + minute;
		}else{
			m = String.valueOf(minute);
		}
		if(timePickerStart){
			txtStartTime.setText(h + ":" + m);
		}else{
			txtEndTime.setText(h + ":" + m);
		}
	}

	@Override
	public void onSelectUser(UserModel user){
		selectedUser = user;
		txtUserName.setText(selectedUser.getUserName());
	}
}
