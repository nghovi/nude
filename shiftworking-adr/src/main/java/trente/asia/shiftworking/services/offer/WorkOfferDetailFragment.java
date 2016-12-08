package trente.asia.shiftworking.services.offer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListViewNoScroll;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.activities.MainActivity;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOfferModel;
import trente.asia.shiftworking.services.offer.view.ApproveHistoryAdapter;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

public class WorkOfferDetailFragment extends AbstractSwFragment{

	private WorkOfferModel		offer;
	Map<String, String>			targetUserModels	= new HashMap<String, String>();
	Map<String, List<Double>>	groupInfo;
	private ImageView			imgEdit;
	private Map<String, String>	offerStatusMaster;
	private String				offerPermission;

	private EditText			edtComment;
	private String				activeOfferId;

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
		super.initHeader(R.drawable.wf_back_white, myself.userName, R.drawable.sw_action_edit);

		edtComment = (EditText)getView().findViewById(R.id.edt_fragment_offer_detail_comment);
		imgEdit = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_offer;
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

		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_offer_type)).setText(offerModel.approveResult);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_start_date)).setText(offerModel.startDateString);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_end_date)).setText(offerModel.endDateString);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_note)).setText(offerModel.note);

		if(WorkOfferModel.OFFER_TYPE_HOLIDAY_WORKING.equals(offer.offerType) || WorkOfferModel.OFFER_TYPE_OVERTIME.equals(offerModel.offerType)){
			((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_start_time)).setText(offerModel.startTimeString);
		}else{
			getView().findViewById(R.id.lnr_start_time).setVisibility(View.GONE);
		}

		if(WorkOfferModel.OFFER_TYPE_HOLIDAY_WORKING.equals(offer.offerType) || WorkOfferModel.OFFER_TYPE_OVERTIME.equals(offerModel.offerType)){
			((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_end_time)).setText(offerModel.endTimeString);
		}else{
			getView().findViewById(R.id.lnr_end_time).setVisibility(View.GONE);
		}
	}

	private void buildWorkOfferDetail(){
		judgeEditPermission();
		buildWorkOfferApproveHistory();
		buildOfferComment();
		buildActionButtons();
	}

	private void buildOfferComment(){
		if(WorkOfferModel.OFFER_STATUS_OFFER.equals(offer.approveResult) && WorkOfferModel.OFFER_PERMISSION_APPROVEABLE.equals(offerPermission)){
			getView().findViewById(R.id.lnr_fragment_offer_detail_comment).setVisibility(View.VISIBLE);
		}else{
			getView().findViewById(R.id.lnr_fragment_offer_detail_comment).setVisibility(View.GONE);
		}
	}

	private void buildActionButtons(){
		Button btnReject = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_reject);
		Button btnApprove = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_approve);

		boolean isApprove1CanApprove = WorkOfferModel.OFFER_PERMISSION_APPROVEABLE.equals(offerPermission) && (myself.key.equals(offer.approveUser1) && WorkOfferModel.APPROVE_STATUS_YET.equals(offer.approveResult1));
		boolean isApprove2CanApprove = WorkOfferModel.OFFER_PERMISSION_APPROVEABLE.equals(offerPermission) && myself.key.equals(offer.approveUser2) && WorkOfferModel.APPROVE_STATUS_OK.equals(offer.approveResult1) && WorkOfferModel.APPROVE_STATUS_YET.equals(offer.approveResult2);

		if(isApprove1CanApprove || isApprove2CanApprove){
			btnReject.setVisibility(View.VISIBLE);
			btnApprove.setVisibility(View.VISIBLE);
			btnReject.setOnClickListener(this);
			btnApprove.setOnClickListener(this);
		}else{
			btnReject.setVisibility(View.GONE);
			btnApprove.setVisibility(View.GONE);
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
		if(WorkOfferModel.OFFER_PERMISSION_EDITABLE.equals(offerPermission)){
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

	public static class ApproveStatus{

		public static final int	APPROVE0	= 0;
		public static final int	APPROVE1	= 1;
		public static final int	APPROVE2	= 2;

		public String			status1;			// approve 1 result;
		public String			status2;			// approve 2 result;
		public int				userType;
		public int				approveGroupType;

		public ApproveStatus(String status1, String status2, int userType, int approveGroupType){
			this.status1 = status1;
			this.status2 = status2;
			this.userType = userType;
			this.approveGroupType = approveGroupType;
		}

		@Override
		public int hashCode(){
			String result = "1";
			result += this.status1 + this.status2 + this.userType + this.approveGroupType;
			return Integer.parseInt(result);
		}

		@Override
		public boolean equals(final Object obj){
			if(this == obj) return true;
			if(obj == null) return false;
			if(getClass() != obj.getClass()) return false;
			final ApproveStatus other = (ApproveStatus)obj;
			return this.status1.equals(other.status1) && this.status2.equals(other.status2) && this.userType == other.userType && this.approveGroupType == other.approveGroupType;
		}
	}
}
