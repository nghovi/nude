package trente.asia.shiftworking.services.requests.edit;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseEditText;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.databinding.FragmentHolidayWorkingEditBinding;
import trente.asia.shiftworking.services.requests.model.HolidayWorkingModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by chi on 9/22/2017.
 */

public class HolidayWorkingEditFragment extends AbstractEditFragment{

	private HolidayWorkingModel					offer;
	private FragmentHolidayWorkingEditBinding	binding;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_holiday_working_edit, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	protected String getDetailApi(){
		return SwConst.API_HOLIDAY_WORKING_DETAIL;
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getDetailApi().equals(url)){
			HolidayWorkingModel offer = CCJsonUtil.convertToModel(response.optString("holidayWork"), HolidayWorkingModel.class);
			if(!CCStringUtil.isEmpty(activeOfferId)){
				updateLayout(offer);
			}
			buildDatePickerDialogs();
		}else{
			super.successLoad(response, url);
		}
	}

	private void updateLayout(HolidayWorkingModel offer){
		binding.txtOfferUser.setText(offer.userName);
		binding.txtStartDate.setText(offer.startDateString);
	}

	@Override
	protected void onClickBtnDone(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("userId", selectedUser.getKey());
			jsonObject.put("key", activeOfferId);
			jsonObject.put("startDateString", binding.txtStartDate.getText());
			jsonObject.put("note", binding.txtReason.getText());
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(getUpdateApi(), jsonObject, true);
	}

	@Override
	protected String getUpdateApi(){
		return SwConst.API_HOLIDAY_WORKING_UPDATE;
	}
}
