package trente.asia.shiftworking.services.requests.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.List;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.activities.MainActivity;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.databinding.FragmentHolidayWorkingDetailBinding;
import trente.asia.shiftworking.services.requests.WorkRequestFragment;
import trente.asia.shiftworking.services.requests.edit.HolidayWorkingEditFragment;
import trente.asia.welfare.adr.models.ApproveHistory;
import trente.asia.shiftworking.services.requests.model.HolidayWorkingModel;
import trente.asia.welfare.adr.activity.WelfareActivity;

public class HolidayWorkingDetailFragment extends AbstractDetailFragment {

	private HolidayWorkingModel holidayWorkingRequest;
	private FragmentHolidayWorkingDetailBinding	binding;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	public void setExecType(String execType){
		this.execType = execType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_holiday_working_detail, container, false);
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
		if(SwConst.API_HOLIDAY_WORKING_DETAIL.equals(url)){
			holidayWorkingRequest = CCJsonUtil.convertToModel(response.optString("holidayWork"), HolidayWorkingModel.class);
			setWorkOffer(holidayWorkingRequest);
			offerPermission = response.optString("permission");

			if(holidayWorkingRequest == null){
				((MainActivity)activity).isInitData = true;
				onClickBackBtn();
			}else{
				buildWorkOfferDetail();
			}
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onResume(){
		super.onResume();

		if(!((WelfareActivity)activity).dataMap.isEmpty()){
			String isDelete = CCStringUtil.toString(((WelfareActivity)activity).dataMap.get(SwConst.ACTION_OFFER_DELETE));
			String isUpdate = CCStringUtil.toString(((WelfareActivity)activity).dataMap.get(SwConst.ACTION_OFFER_UPDATE));
			if(CCConst.YES.equals(isDelete) || CCConst.YES.equals(isUpdate)){
				((WelfareActivity)activity).dataMap.clear();
				((WelfareActivity)activity).isInitData = true;
				if(CCConst.YES.equals(isDelete)){
					getFragmentManager().popBackStack();
				}
			}
		}
	}

	private void setWorkOffer(HolidayWorkingModel offerModel){
		binding.txtOfferUser.setText(offerModel.userName);
		binding.txtStartDate.setText(offerModel.startDateString);
		binding.txtReason.setText(offerModel.note);
	}

	@Override
	protected String getOfferUserId(){
		return holidayWorkingRequest.userId;
	}

	@Override
	protected List<ApproveHistory> getListHistories(){
		return holidayWorkingRequest.listHistories;
	}

	protected void gotoWorkOfferEditFragment(){
		HolidayWorkingEditFragment fragment = new HolidayWorkingEditFragment();
		fragment.setActiveOfferId(holidayWorkingRequest.key);
		gotoFragment(fragment);
	}

	@Override
	protected String getOfferKey(){
		return holidayWorkingRequest.key;
	}

	@Override
	protected String getApproveApi(){
		return SwConst.API_HOLIDAY_WORKING_APPROVE;
	}

	@Override
	protected String getDeleteApi(){
		return SwConst.API_HOLIDAY_WORKING_DELETE;
	}

	@Override
	protected void onClickBackBtn(){
		if(isClickNotification){
			emptyBackStack();
			gotoFragment(new WorkRequestFragment());
		}else{
			super.onClickBackBtn();
		}
	}
}
