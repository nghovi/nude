package trente.asia.shiftworking.services.offer.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListViewNoScroll;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.activities.MainActivity;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.databinding.FragmentOvertimeDetailBinding;
import trente.asia.shiftworking.services.offer.adapter.VacationApproveHistoryAdapter;
import trente.asia.shiftworking.services.offer.list.OvertimeListFragment;
import trente.asia.shiftworking.services.offer.model.OvertimeModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

public class OvertimeDetailFragment extends AbstractSwFragment{

	private OvertimeModel offer;
	private Map<String, String>			targetUserModels	= new HashMap<String, String>();
	private Map<String, List<Double>>	groupInfo;
	private ImageView					imgEdit;
	private Map<String, String>			offerStatusMaster;
	private String						offerPermission;

	private EditText					edtComment;
	private String						activeOfferId;
	private String						execType;
	private FragmentOvertimeDetailBinding binding;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	public void setExecType(String execType){
		this.execType = execType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overtime_detail, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.sw_back_white, myself.userName, null);

		edtComment = (EditText)getView().findViewById(R.id.edt_fragment_offer_detail_comment);
		imgEdit = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	protected void initData(){
		loadWorkOffer();
	}

	private void loadWorkOffer(){

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
			setWorkOffer(offer);

			offerPermission = response.optString("permission");
			offerStatusMaster = buildOfferStatusMaster(response);

			groupInfo = CCJsonUtil.convertToModel(response.optString("groupJoinMap"), Map.class);
			if(offer == null){
				((MainActivity)activity).isInitData = true;
				onClickBackBtn();
			}else{
				targetUserModels = CCJsonUtil.convertToModel(response.optString("targetUserModel"), Map.class);
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

	private Map<String, String> buildOfferStatusMaster(JSONObject response){
		Map<String, String> offerStatusMaster = new LinkedHashMap<>();
		List<ApiObjectModel> lstStatus = CCJsonUtil.convertToModelList(response.optString("offerStatusList"), ApiObjectModel.class);
		lstStatus.add(new ApiObjectModel(CCConst.NONE, getString(R.string.chiase_common_all)));
		return WelfareFormatUtil.convertList2Map(lstStatus);
	}

	private void setWorkOffer(OvertimeModel offerModel){
		try{
			LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject(lnrContent, new JSONObject(gson.toJson(offerModel)));
		}catch(JSONException e){
			e.printStackTrace();
		}


		((TextView)getView().findViewById(R.id.txt_fragment_overtime_detail_offer_user)).setText(offerModel.userName);
        if ("E".equals(offerModel.overtimeType)) {
            ((TextView) getView().findViewById(R.id.txt_fragment_overtime_detail_offer_type)).setText(getResources().getString(R.string.fragment_overtime_detail_type_early));
        }else{
            ((TextView) getView().findViewById(R.id.txt_fragment_overtime_detail_offer_type)).setText(getResources().getString(R.string.fragment_overtime_detail_type_overtime));
        }
		((TextView)getView().findViewById(R.id.txt_fragment_overtime_detail_start_date)).setText(offerModel.startDateString);
		((TextView)getView().findViewById(R.id.txt_fragment_overtime_detail_start_time)).setText(offerModel.startTimeString + "-" + offerModel.endTimeString);
//		((TextView)getView().findViewById(R.id.txt_fragment_overtime_detail_end_date)).setText(offerModel.endDateString);
		((TextView)getView().findViewById(R.id.txt_fragment_overtime_detail_reason)).setText(offerModel.note);

	}

	private void buildWorkOfferDetail(){
		judgeEditPermission();
		judgeAprovePermission();
		buildWorkOfferApproveHistory();
		// buildOfferComment();
	}

	// private void buildOfferComment(){
	// if(OvertimeModel.OFFER_STATUS_OFFER.equals(offer.approveResult) && SwConst.OFFER_CAN_APPROVE.equals(offerPermission)){
	// getView().findViewById(R.id.lnr_fragment_offer_detail_comment).setVisibility(View.VISIBLE);
	// }else{
	// getView().findViewById(R.id.lnr_fragment_offer_detail_comment).setVisibility(View.GONE);
	// }
	// }

	private void judgeAprovePermission(){
		boolean permissionApprove = SwConst.OFFER_CAN_APPROVE.equals(offerPermission);
		LinearLayout lnrApproveArea = (LinearLayout)getView().findViewById(R.id.lnr_id_approve_area);
		if(permissionApprove){
			lnrApproveArea.setVisibility(View.VISIBLE);
			Button btnReject = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_reject);
			Button btnApprove = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_approve);
			btnReject.setOnClickListener(this);
			btnApprove.setOnClickListener(this);
		}else{
			lnrApproveArea.setVisibility(View.GONE);
		}
	}

	private void buildWorkOfferApproveHistory(){
		if(offer.userId.equals(myself.key)){
			getView().findViewById(R.id.lnr_fragment_overtime_detail_offer_status).setVisibility(View.VISIBLE);
			ChiaseListViewNoScroll lstApproveHistory = (ChiaseListViewNoScroll)getView().findViewById(R.id.lst_fragment_offer_detail_offer_status);
			VacationApproveHistoryAdapter adapter = new VacationApproveHistoryAdapter(activity, offer.listHistories);
			lstApproveHistory.setAdapter(adapter);
		}else{
			getView().findViewById(R.id.lnr_fragment_overtime_detail_offer_status).setVisibility(View.GONE);
		}
	}

	protected void judgeEditPermission(){
		if(SwConst.OFFER_CAN_EDIT_DELETE.equals(offerPermission) || SwConst.OFFER_ONLY_DELETE.equals(offerPermission) || SwConst.OFFER_CAN_ONLY_EDIT.equals(offerPermission)){
			imgEdit.setImageResource(R.drawable.sw_action_edit);
			imgEdit.setVisibility(View.VISIBLE);
			imgEdit.setOnClickListener(this);
		}else{
			imgEdit.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		((ChiaseActivity)activity).isInitData = true;
		onClickBackBtn();
	}

	private void gotoWorkOfferEditFragment(){
//		OvertimeEditFragment fragment = new OvertimeEditFragment();
//		fragment.setActiveOfferId(offer.key);
//		fragment.setExecType(execType);
//		gotoFragment(fragment);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			gotoWorkOfferEditFragment();
			break;
		case R.id.btn_fragment_offer_detail_approve:
			onClickBtnApprove();
			break;
		case R.id.btn_fragment_offer_detail_reject:
			onClickBtnReject();
			break;
		default:
			break;
		}
	}

	private void sendApproveResult(String approveResult){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", offer.key);
			jsonObject.put("approveComment", edtComment.getText().toString());
			jsonObject.put("approveResult", approveResult);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(SwConst.API_OVERTIME_APPROVE, jsonObject, true);
	}

	private void onClickBtnReject(){
		sendApproveResult("NG");
	}

	private void onClickBtnApprove(){
		sendApproveResult("OK");
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
}
