package trente.asia.shiftworking.services.requests.detail;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.activities.MainActivity;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.databinding.FragmentOvertimeDetailBinding;
import trente.asia.shiftworking.services.requests.edit.OvertimeEditFragment;
import trente.asia.welfare.adr.models.OvertimeRequestModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.ApproveHistory;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

public class OvertimeDetailFragment extends AbstractDetailFragment {

	private OvertimeRequestModel			overtimeRequest;
	private FragmentOvertimeDetailBinding	binding;
	public List<ApiObjectModel>				typeList;
	private Map<String, String>				typeListMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overtime_detail, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	protected String getDetailApi(){
		return SwConst.API_OVERTIME_DETAIL;
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_OVERTIME_DETAIL.equals(url)){
			overtimeRequest = CCJsonUtil.convertToModel(response.optString("overtime"), OvertimeRequestModel.class);

			typeList = CCJsonUtil.convertToModelList(response.optString("overtimeTypeList"), ApiObjectModel.class);
			typeListMap = WelfareFormatUtil.convertList2Map(typeList);
			offerPermission = response.optString("permission");
			setOtRequestModel(overtimeRequest);

			if(overtimeRequest == null){
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

	private void setOtRequestModel(OvertimeRequestModel otRequest){
		binding.txtOfferUser.setText(otRequest.userName);
		binding.txtOfferType.setText(typeListMap.get(this.overtimeRequest.overtimeType));
		binding.txtStartDate.setText(otRequest.startDateString);
		binding.txtStartTime.setText(otRequest.startTimeString + "-" + otRequest.endTimeString);
		binding.txtReason.setText(otRequest.note);
	}

	@Override
	protected String getOfferUserId(){
		return overtimeRequest.userId;
	}

	@Override
	protected List<ApproveHistory> getListHistories(){
		return overtimeRequest.listHistories;
	}

	@Override
	protected void gotoWorkOfferEditFragment(){
		OvertimeEditFragment fragment = new OvertimeEditFragment();
		fragment.setActiveOfferId(overtimeRequest.key);
		gotoFragment(fragment);
	}

	@Override
	protected String getOfferKey(){
		return overtimeRequest.key;
	}

	@Override
	protected String getApproveApi(){
		return SwConst.API_OVERTIME_APPROVE;
	}

	@Override
	protected String getDeleteApi(){
		return SwConst.API_OVERTIME_DELETE;
	}
}
