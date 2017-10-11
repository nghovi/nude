package trente.asia.shiftworking.services.requests.edit;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.dialog.SwTimePicker;
import trente.asia.shiftworking.common.interfaces.OnTimePickerListener;
import trente.asia.shiftworking.databinding.FragmentOvertimeEditBinding;
import trente.asia.welfare.adr.models.OvertimeRequestModel;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * Created by chi on 9/22/2017.
 */

public class OvertimeEditFragment extends AbstractEditFragment implements OnTimePickerListener{

	private OvertimeRequestModel		overtimeRequest;
	private ChiaseListDialog			spnType;
	private SwTimePicker				timePickerDialog;
	private FragmentOvertimeEditBinding	binding;
	private List<ApiObjectModel>		typeList;

	private boolean						timePickerStart;
	private int							selectedHour;
	private int							selectedMinute;
	private Map<String, String>			typeListMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overtime_edit, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	protected void setOnClickListener(){
		super.setOnClickListener();
		binding.lnrIdType.setOnClickListener(this);
		binding.lnrStartTime.setOnClickListener(this);
		binding.lnrEndTime.setOnClickListener(this);
	}

	@Override
	protected void initView(){
		super.initView();
		binding.txtStartTime.setText("00:00");
		binding.txtEndTime.setText("00:00");
	}

	@Override
	protected String getDetailApi(){
		return SwConst.API_OVERTIME_DETAIL;
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getDetailApi().equals(url)){
			overtimeRequest = CCJsonUtil.convertToModel(response.optString("overtime"), OvertimeRequestModel.class);
			typeList = CCJsonUtil.convertToModelList(response.optString("overtimeTypeList"), ApiObjectModel.class);
			typeListMap = WelfareFormatUtil.convertList2Map(typeList);
			if(!CCStringUtil.isEmpty(activeOfferId)){
				updateLayout(overtimeRequest);
			}
			buildDatePickerDialogs();
			initDialog(typeList);
		}else{
			super.successLoad(response, url);
		}
	}

	private void updateLayout(OvertimeRequestModel overtimeRequest){
		binding.txtOfferUser.setText(overtimeRequest.userName);
		binding.txtOfferType.setText(typeListMap.get(overtimeRequest.overtimeType));
		binding.txtStartDate.setText(overtimeRequest.startDateString);
		binding.txtStartTime.setText(overtimeRequest.startTimeString);
		binding.txtEndTime.setText(overtimeRequest.endTimeString);
	}

	private void initDialog(final List<ApiObjectModel> types){
		if(CCStringUtil.isEmpty(activeOfferId)){
			binding.txtOfferType.setText(types.get(0).value);
			binding.txtOfferType.setValue(types.get(0).key);
		}

		spnType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), WelfareFormatUtil.convertList2Map(types), binding.txtOfferType, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				if("E".equals(selectedKey)){
					binding.txtOfferType.setText(R.string.sw_overtime);
				}else{
					binding.txtOfferType.setText(R.string.sw_early);
				}
			}
		});
	}

	@Override
	protected void onClickBtnDone(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("userId", selectedUser.getKey());
			jsonObject.put("key", activeOfferId);
			jsonObject.put("startDateString", binding.txtStartDate.getText());
			jsonObject.put("startTimeString", binding.txtStartTime.getText());
			jsonObject.put("endTimeString", binding.txtEndTime.getText());
			jsonObject.put("overtimeType", binding.txtOfferType.getValue());
			jsonObject.put("note", binding.txtReason.getText());
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(getUpdateApi(), jsonObject, true);
	}

	@Override
	protected String getUpdateApi(){
		return SwConst.API_OVERTIME_UPDATE;
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
		case R.id.lnr_start_time:
			timePickerStart = true;
			selectedHour = Integer.parseInt(binding.txtStartTime.getText().toString().split(":")[0]);
			selectedMinute = Integer.parseInt(binding.txtStartTime.getText().toString().split(":")[1]);
			openTimePicker();
			break;
		case R.id.lnr_end_time:
			timePickerStart = false;
			selectedHour = Integer.parseInt(binding.txtEndTime.getText().toString().split(":")[0]);
			selectedMinute = Integer.parseInt(binding.txtEndTime.getText().toString().split(":")[1]);
			openTimePicker();
			break;
		case R.id.lnr_id_type:
			spnType.show();
			break;
		default:
			super.onClick(v);
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
	public void onTimePickerCompleted(int hour, int minute){
		if(timePickerStart){
			binding.txtStartTime.setText(getDisplayNum(hour) + ":" + getDisplayNum(minute));
		}else{
			binding.txtEndTime.setText(getDisplayNum(hour) + ":" + getDisplayNum(minute));
		}
	}
}
