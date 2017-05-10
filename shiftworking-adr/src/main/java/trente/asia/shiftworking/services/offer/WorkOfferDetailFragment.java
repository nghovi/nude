package trente.asia.shiftworking.services.offer;

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
import trente.asia.shiftworking.services.offer.model.WorkOfferModel;
import trente.asia.shiftworking.services.offer.view.ApproveHistoryAdapter;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

public class WorkOfferDetailFragment extends AbstractSwFragment{

	private WorkOfferModel				offer;
	private Map<String, String>			targetUserModels	= new HashMap<String, String>();
	private Map<String, List<Double>>	groupInfo;
	private ImageView					imgEdit;
	private Map<String, String>			offerStatusMaster;
	private String						offerPermission;

	private EditText					edtComment;
	private String						activeOfferId;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_offer_detail, container, false);
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
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(WfUrlConst.WF_SW_OFFER_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_SW_OFFER_DETAIL.equals(url)){
			offer = CCJsonUtil.convertToModel(response.optString("offer"), WorkOfferModel.class);
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

	private void setWorkOffer(WorkOfferModel offerModel){
		try{
			LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject(lnrContent, new JSONObject(gson.toJson(offerModel)));
		}catch(JSONException e){
			e.printStackTrace();
		}

		if(!offerModel.userId.equals(myself.key)){
			getView().findViewById(R.id.lnr_frament_offer_detail_status).setVisibility(View.GONE);
		}else{
			getView().findViewById(R.id.lnr_frament_offer_detail_status).setVisibility(View.VISIBLE);
			((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_offer_status)).setText(offerModel.offerStatusName);
		}

		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_offer_type)).setText(offerModel.offerTypeName);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_start_date)).setText(offerModel.startDateString);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_end_date)).setText(offerModel.endDateString);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_note)).setText(offerModel.note);

		if(WorkOfferModel.OFFER_TYPE_HOLIDAY_WORKING.equals(offer.offerType) || WorkOfferModel.OFFER_TYPE_OVERTIME.equals(offerModel.offerType) || WorkOfferModel.OFFER_TYPE_SHORT_TIME.equals(offerModel.offerType)){
			((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_start_time)).setText(offerModel.startTimeString);
		}else{
			getView().findViewById(R.id.lnr_start_time).setVisibility(View.GONE);
		}

		if(WorkOfferModel.OFFER_TYPE_HOLIDAY_WORKING.equals(offer.offerType) || WorkOfferModel.OFFER_TYPE_OVERTIME.equals(offerModel.offerType) || WorkOfferModel.OFFER_TYPE_SHORT_TIME.equals(offerModel.offerType)){
			((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_end_time)).setText(offerModel.endTimeString);
		}else{
			getView().findViewById(R.id.lnr_end_time).setVisibility(View.GONE);
		}
	}

	private void buildWorkOfferDetail(){
		judgeEditPermission();
		judgeAprovePermission();
		buildWorkOfferApproveHistory();
		// buildOfferComment();
	}

	// private void buildOfferComment(){
	// if(WorkOfferModel.OFFER_STATUS_OFFER.equals(offer.approveResult) && SwConst.OFFER_CAN_APPROVE.equals(offerPermission)){
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
			getView().findViewById(R.id.lnr_fragment_offer_detail_approve_history).setVisibility(View.VISIBLE);
			ChiaseListViewNoScroll lstApproveHistory = (ChiaseListViewNoScroll)getView().findViewById(R.id.lst_fragment_offer_detail_approve_history);
			ApproveHistoryAdapter adapter = new ApproveHistoryAdapter(activity, offer.listHistories);
			lstApproveHistory.setAdapter(adapter);
		}else{
			getView().findViewById(R.id.lnr_fragment_offer_detail_approve_history).setVisibility(View.GONE);
		}
	}

	protected void judgeEditPermission(){
		if(SwConst.OFFER_CAN_EDIT_DELETE.equals(offerPermission) || SwConst.OFFER_CAN_ONLY_EDIT.equals(offerPermission)){
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
		WorkOfferEditFragment fragment = new WorkOfferEditFragment();
		fragment.setActiveOfferId(offer.key);
		gotoFragment(fragment);
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
		requestUpdate(WfUrlConst.WF_SW_OFFER_APPROVE, jsonObject, true);
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
			gotoFragment(new WorkOfferListFragment());
		}else{
			super.onClickBackBtn();
		}
	}
}
