package trente.asia.shiftworking.services.requests.detail;

import java.util.List;

import org.json.JSONObject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.activities.MainActivity;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.databinding.FragmentVacationDetailBinding;
import trente.asia.shiftworking.services.requests.edit.VacationEditFragment;
import trente.asia.welfare.adr.models.ApproveHistory;
import trente.asia.welfare.adr.models.VacationRequestModel;

public class VacationDetailFragment extends AbstractDetailFragment {

	private VacationRequestModel			vacationRequest;
	private FragmentVacationDetailBinding	binding;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacation_detail, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	protected String getDetailApi(){
		return SwConst.API_VACATION_DETAIL;
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_VACATION_DETAIL.equals(url)){
			vacationRequest = CCJsonUtil.convertToModel(response.optString("vacation"), VacationRequestModel.class);
			setWorkOffer(vacationRequest);

			offerPermission = response.optString("permission");

			if(vacationRequest == null){
				((MainActivity)activity).isInitData = true;
				onClickBackBtn();
			}else{
				buildWorkOfferDetail();
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void setWorkOffer(VacationRequestModel vacationRequest){
		binding.txtOfferUser.setText(vacationRequest.userName);
		binding.txtOfferType.setText(vacationRequest.vacationName);
		binding.txtStartDate.setText(vacationRequest.startDateString);
		binding.txtReason.setText(vacationRequest.note);
		binding.txtSickAbsent.setText(vacationRequest.sickAbsent ? getString(R.string.sw_yes) : getString(R.string.sw_no));
		binding.txtAmount.setText(vacationRequest.amountString);
		if(Float.parseFloat(vacationRequest.amount) >= 1){
			binding.txtEndDate.setText(vacationRequest.endDateString);
			binding.lnrEndDate.setVisibility(View.VISIBLE);
		}else{
			binding.lnrEndDate.setVisibility(View.GONE);
		}
	}

	@Override
	protected String getOfferUserId(){
		return vacationRequest.userId;
	}

	@Override
	protected List<ApproveHistory> getListHistories(){
		return vacationRequest.listHistories;
	}

	@Override
	protected void gotoWorkOfferEditFragment(){
		VacationEditFragment fragment = new VacationEditFragment();
		fragment.setActiveOfferId(vacationRequest.key);
		gotoFragment(fragment);
	}

	@Override
	protected String getOfferKey(){
		return vacationRequest.key;
	}

	@Override
	protected String getApproveApi(){
		return SwConst.API_VACATION_APPROVE;
	}

	@Override
	protected String getDeleteApi(){
		return SwConst.API_VACATION_DELETE;
	}
}
