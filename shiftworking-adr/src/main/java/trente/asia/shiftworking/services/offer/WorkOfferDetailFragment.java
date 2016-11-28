package trente.asia.shiftworking.services.offer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.activities.MainActivity;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOffer;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;

public class WorkOfferDetailFragment extends AbstractSwFragment{

	private WorkOffer offer;
	private String				selectedWorkOfferStatusCode;
	Map<String, String>			targetUserModels	= new HashMap<String, String>();
	Map<String, List<Double>>	groupInfo;
	private int					approveGroupType;

	public void setFromPush(boolean fromPush){
		isFromPush = fromPush;
	}

	private boolean	isFromPush	= false;

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
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_offer;
	}

	@Override
	protected void initData(){
		requestWorkOfferDetail();
	}

	private void requestWorkOfferDetail(){

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", offer.key);
			jsonObject.put("viewMode", offer.viewMode);
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(WfUrlConst.API_THANKSCARD_GET_CATEGORY, jsonObject, true);
	}

	private void buildWorkOfferDetail(){
		buildHeaderWithBackBtn(getString(R.string.fragment_offer_detail_title));
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_username)).setText(offer.targetUserName);
		String offerType = SwConst.offerTypes.get(offer.offerType);
		String subType = SwConst.subTypes.get(offer.offerType).get(offer.subType);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_offer_type)).setText(offerType + "/" + subType);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_status)).setText(SwConst.offerStatus.get(offer.status));

		// String startDate = offer.startDateString;
		// ((TextView) getView().findViewById(R.id.txt_fragment_offer_detail_start_date))
		// .setText(CSWUtil.isEmpty(offer.startTime) ? startDate : startDate + " ~ "
		// + offer.startTime);
		//
		// String endDate = offer.endDateString;
		// ((TextView) getView().findViewById(R.id.txt_fragment_offer_detail_end_date))
		// .setText(CSWUtil.isEmpty(offer.endTime) ? endDate : endDate + " ~ " + offer.endTime);

		TextView txtReason = (TextView)getView().findViewById(R.id.txt_fragment_offer_detail_reason);
		txtReason.setText(offer.content);

		TextView txtMemo = (TextView)getView().findViewById(R.id.txt_fragment_offer_detail_memo);
		txtMemo.setText(offer.note);

		buildApproveResult();

		buildApproveAction();
	}

	private void buildApproveResult(){
		TextView txtApprove1 = (TextView)getView().findViewById(R.id.txt_fragment_offer_detail_approve1);
		TextView txtApprove1Comment = (TextView)getView().findViewById(R.id.txt_fragment_offer_detail_approve1_comment);
		TextView txtApprove2 = (TextView)getView().findViewById(R.id.txt_fragment_offer_detail_approve2);
		TextView txtApprove2Comment = (TextView)getView().findViewById(R.id.txt_fragment_offer_detail_approve2_comment);

		buildApproveResultPart(txtApprove1, txtApprove1Comment, offer.approve1);
		buildApproveResultPart(txtApprove2, txtApprove2Comment, offer.approve2);
	}

	private void buildApproveResultPart(TextView txtResult, TextView txtComment, WorkOffer.Approve approve){
		// if (CSWUtil.isNotEmpty(approve.result)) {
		// txtResult.setVisibility(View.VISIBLE);
		// txtResult.setText(approve.result.equals(SwConst.APPROVE_STATUS_NA) ? SwConst.approveTypes
		// .get(SwConst.APPROVE_STATUS_NA) : getString(
		// R.string.fragment_offer_detail_approve, SwConst.approveTypes.get(approve.result),
		// approve.historyName));
		// String approveComment = getApproveComment(approve);
		// if (CSWUtil.isNotEmpty(approveComment)) {
		// txtComment.setVisibility(View.VISIBLE);
		// txtComment.setText("\"" + approveComment + "\"");
		// } else {
		// txtComment.setVisibility(View.GONE);
		// }
		// } else {
		// txtResult.setVisibility(View.GONE);
		// txtComment.setVisibility(View.GONE);
		// }
	}

	private String getApproveComment(WorkOffer.Approve approve){
		String result = "";
		if(offer.historyList != null){
			for(int i = offer.historyList.size() - 1; i >= 0; i--){
				String flow = offer.historyList.get(i).flow;
				if(flow.equals(approve.flow)){
					return offer.historyList.get(i).note;
				}
			}
		}
		return result;
	}

	private void onClickApproveButton(){
		// Calendar c = Calendar.getInstance();
		// String execType = "approve";
		// String flow =
		// approveGroupType == ApproveStatus.APPROVE1 ? offer.approve1.flow
		// : offer.approve2.flow;
		// JSONObject jsonObject =
		// getJsonBuilder()
		// .add("startTime", offer.startTime)
		// .add("endTime", offer.endTime)
		// .add("key", offer.key)
		// .add("keyHash", offer.keyHash)
		// .add("execType", execType)
		// .add("flow", flow)
		// .add("offerUserModelId", offer.offerUserModelId)
		// .add("offerType", offer.offerType)
		// .add("subType", offer.subType)
		// .add("status", selectedWorkOfferStatusCode)
		// .add("approveNote",
		// ((EditText) getView().findViewById(
		// R.id.edt_fragment_offer_detail_comment)).getText()
		// .toString()).add("offerDate", offer.offerDateString)
		// .add("startDate", offer.startDateString)
		// .add("endDate", offer.endDateString).add("content", offer.content)
		// .add("note", offer.note).getJsonObj();
		// requestUpdate(SwConst.API_WORK_OFFER_UPDATE, jsonObject, true);
	}

	protected void buildHeaderWithBackBtn(String title){
		// if (isFromPush) {
		// super.buildHeaderWithTitle(title);
		// } else {
		// super.buildHeaderWithBackBtn(title);
		// }
		// UserModel userMe = prefAccUtil.getUserModelPref();
		// int userType = getUserModelType(groupInfo, offer, userMe);
		// if ((offer.status == Offer.OFFER_STATUS_OFFERING && (isUserModelWorkOffer(userMe, offer)
		// || (userType == UserModel.GROUP && isUserModelWorkOffer(userMe, offer)) || userType == UserModel.ADMIN))
		// || (offer.status == Offer.OFFER_STATUS_APPROVING
		// && offer.approve1.result.equals(SwConst.APPROVE_STATUS_YET)
		// && (offer.approve2.result.equals(SwConst.APPROVE_STATUS_YET) || offer.approve2.result
		// .equals(SwConst.APPROVE_STATUS_NA)) && (isUserModelWorkOffer(userMe, offer)
		// || (userType == UserModel.GROUP && isUserModelWorkOffer(userMe, offer)) || userType == UserModel.ADMIN))
		//
		// || (offer.status == Offer.OFFER_STATUS_APPROVING
		// && offer.approve2.result.equals(SwConst.APPROVE_STATUS_YET)
		// && (offer.approve1.result.equals(SwConst.APPROVE_STATUS_YET) || offer.approve1.result
		// .equals(SwConst.APPROVE_STATUS_NA)) && (isUserModelWorkOffer(userMe, offer)
		// || (userType == UserModel.GROUP && isUserModelWorkOffer(userMe, offer)) || userType == UserModel.ADMIN))) {
		// ImageView btnEdit =
		// (ImageView) getView().findViewById(R.id.img_view_common_header_right_btn);
		// btnEdit.setImageResource(R.drawable.bb_action_edit);
		// btnEdit.setVisibility(View.VISIBLE);
		// btnEdit.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// gotoWorkOfferEdit();
		// }
		// });
		// }
	}

	public static int getUserModelType(Map<String, List<Double>> groupInfo, WorkOffer offer, UserModel userMe){
		// if (userMe.isBoss) {
		// return UserModel.ADMIN;
		// } else if (canApprove(groupInfo, offer, userMe)) {
		// return UserModel.GROUP;
		// }
		// return UserModel.NORMAL;
		return 0;
	}

	public static boolean isUserModelWorkOffer(UserModel userMe, WorkOffer offer){
		// return userMe.userId.equals(offer.offerUserModelId);
		return false;
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		// ((MainActivity) activity).onClickWorkOfferListFooter(Integer.parseInt(CSWUtil.getDateString(
		// offer.endDateString, SwConst.DISPLAY_DATE_FORMAT, SwConst.DISPLAY_MONTH_FORMAT)) - 1);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		// // TODO: 5/27/2016 api should return targetUserName ?
		String targetUserName = offer.targetUserName;
		offer = CCJsonUtil.convertToModel(response.optString("offer"), WorkOffer.class);
		groupInfo = CCJsonUtil.convertToModel(response.optString("groupJoinMap"), Map.class);
		if(offer == null){
			((MainActivity)activity).isInitData = true;
			onClickBackBtn();
		}else{
			offer.targetUserName = targetUserName;
			targetUserModels = CCJsonUtil.convertToModel(response.optString("targetUserModel"), Map.class);
			buildWorkOfferDetail();
		}
	}

	private void gotoWorkOfferEdit(){
		// WorkOfferEditFragment workWorkOfferEditFragment = new WorkOfferEditFragment();
		// workWorkOfferEditFragment.setWorkOffer(offer);
		// workWorkOfferEditFragment.setGroupInfo(groupInfo);
		// workWorkOfferEditFragment.setTargetUserModels(targetUserModels);
		// ((MainActivity) activity).addFragment(workWorkOfferEditFragment);
	}

	private void buildApproveAction(){
		// UserModel userMe = prefAccUtil.getUserModelPref();
		// int userType = getUserModelType(groupInfo, offer, userMe);
		// approveGroupType = getApproveGroupType(userMe, userType);
		// ApproveStatus approveStatus =
		// new ApproveStatus(offer.approve1.result, offer.approve2.result, userType,
		// approveGroupType);
		// final List<String> approveStatusValues = SwConst.approveStatusListMap.get(approveStatus);
		// if (CSWUtil.isNotEmpty(offer.approveNote))
		// ((TextView) getView().findViewById(R.id.edt_fragment_offer_detail_comment))
		// .setText(offer.approveNote);
		// if (approveStatusValues != null) {
		// buildApproveButton();
		//
		// ArrayAdapter<String> dataAdapter =
		// new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item,
		// approveStatusValues);
		// dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Spinner spinner =
		// (Spinner) getView().findViewById(R.id.spn_fragment_offer_detail_approve_type);
		// spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// selectedWorkOfferStatusCode =
		// (String) CSWUtil.getKeyFromValue(SwConst.approveTypes,
		// approveStatusValues.get(position));
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		//
		// }
		// });
		// spinner.setAdapter(dataAdapter);
		// int defaultPosition = 0;
		// String previousApproveValue =
		// approveGroupType == ApproveStatus.APPROVE1 ? SwConst.approveTypes
		// .get(offer.approve1.result)
		// : (approveGroupType == ApproveStatus.APPROVE2 ? SwConst.approveTypes
		// .get(offer.approve2.result) : null);
		// for (int i = 0; i < approveStatusValues.size(); i++) {
		// if (approveStatusValues.get(i).equals(previousApproveValue)) {
		// defaultPosition = i;
		// }
		// }
		// spinner.setSelection(defaultPosition);
		// } else {
		// getView().findViewById(R.id.lnr_fragment_offer_detail_approve).setVisibility(View.GONE);
		// if (CSWUtil.isEmpty(offer.approveNote))
		// getView().findViewById(R.id.edt_fragment_offer_detail_comment).setVisibility(
		// View.GONE);
		// }
	}

	private int getApproveGroupType(UserModel userMe, int userType){
		// if (offer.approve2.groupId != null) {
		// List<Double> memberIds = groupInfo.get(offer.approve2.groupId);
		// for (Double memberId : memberIds) {
		// if (memberId.intValue() == Integer.parseInt(userMe.userId)) {
		// return ApproveStatus.APPROVE2;
		// }
		// }
		// }
		//
		// if (offer.approve1.groupId != null) {
		// List<Double> memberIds = groupInfo.get(offer.approve1.groupId);
		// for (Double memberId : memberIds) {
		// if (memberId.intValue() == Integer.parseInt(userMe.userId)) {
		// return ApproveStatus.APPROVE1;
		// }
		// }
		// }

		// if (userType == UserModel.ADMIN) {
		// if ((offer.approve1.result.equals(SwConst.APPROVE_STATUS_YET)
		// || offer.approve1.result.equals(SwConst.APPROVE_STATUS_ACCEPT)
		// || offer.approve1.result.equals(SwConst.APPROVE_STATUS_STOP) || offer.approve1.result
		// .equals(SwConst.APPROVE_STATUS_NA))
		// && (offer.approve2.result.equals(SwConst.APPROVE_STATUS_YET)
		// || offer.approve2.result.equals(SwConst.APPROVE_STATUS_ACCEPT) || offer.approve2.result
		// .equals(SwConst.APPROVE_STATUS_STOP))) {
		// return ApproveStatus.APPROVE2;
		// } else {
		// return ApproveStatus.APPROVE1;
		// }
		// }

		return ApproveStatus.APPROVE0;
	}

	public static boolean canApprove(Map<String, List<Double>> groupInfo, WorkOffer offer, UserModel userMe){
		// if (userMe.userId.equals(offer.offerUserModelId)) {
		// return false;
		// }
		//
		// List<Double> memberIds = null;
		// if (CSWUtil.isNotEmpty(offer.approve1.groupId)) {
		// memberIds = groupInfo.get(offer.approve1.groupId);
		// }
		// if (memberIds != null && CSWUtil.isNotEmpty(offer.approve2.groupId)) {
		// List<Double> approve2GroupIds = groupInfo.get(offer.approve2.groupId);
		// if (approve2GroupIds != null) {
		// memberIds.addAll(approve2GroupIds);
		// }
		// } else if (CSWUtil.isNotEmpty(offer.approve2.groupId)) {
		// memberIds = groupInfo.get(offer.approve2.groupId);
		// }
		// if (memberIds != null) {
		// for (Double memberId : memberIds) {
		// if (memberId.intValue() == Integer.parseInt(userMe.userId)) {
		// return true;
		// }
		// }
		// }
		return false;
	}

	private void buildApproveButton(){
		Button btnAprrove = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_approve);
		btnAprrove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickApproveButton();
			}
		});
	}

	@Override
	public void onClick(View v){

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

	public void setWorkOffer(WorkOffer offer){
		this.offer = offer;
	}
}
