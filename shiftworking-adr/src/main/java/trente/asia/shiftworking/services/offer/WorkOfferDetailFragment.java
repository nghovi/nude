package trente.asia.shiftworking.services.offer;

import static trente.asia.shiftworking.services.offer.WorkOfferFragment.buildOfferStatusMaster;
import static trente.asia.shiftworking.services.offer.WorkOfferFragment.buildOfferTypeMaster;

import java.util.ArrayList;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListViewNoScroll;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.activities.MainActivity;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOffer;
import trente.asia.shiftworking.services.offer.view.ApproveHistoryAdapter;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.DeptModel;

public class WorkOfferDetailFragment extends AbstractSwFragment{

	private WorkOffer			offer;
	private String				selectedWorkOfferStatusCode;
	Map<String, String>			targetUserModels	= new HashMap<String, String>();
	Map<String, List<Double>>	groupInfo;
	private int					approveGroupType;
	private ImageView			btnEdit;
	private Map<String, String>	offerTypesMaster;
	private Map<String, String>	offerStatusMaster;
	private String				offerPermission;
	private EditText			edtComment;

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
		buildHeaderWithBackBtn(myself.userName);
		// buildWorkOfferDetail();
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
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(WfUrlConst.WF_SW_OFFER_DETAIL, jsonObject, true);
	}

	private void buildWorkOfferDetail(){
		buildOfferInfo();
		buildWorkOfferApproveHistory();
		buildActionButtons();
	}

	private void buildOfferInfo(){
		String offerType = offerTypesMaster.get(offer.offerType);

		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_offer_type)).setText(offerType);

		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_start_date)).setText(offer.startDateString);

		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_end_date)).setText(offer.endDateString);

		if(CCStringUtil.isEmpty(offer.startTimeString)){
			getView().findViewById(R.id.lnr_start_time).setVisibility(View.INVISIBLE);
		}else{
			((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_start_time)).setText(offer.startTimeString);
		}

		if(CCStringUtil.isEmpty(offer.endTimeString)){
			getView().findViewById(R.id.lnr_end_time).setVisibility(View.INVISIBLE);
		}else{
			((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_end_time)).setText(offer.endTimeString);
		}

		TextView txtNote = (TextView)getView().findViewById(R.id.txt_fragment_offer_detail_note);
		txtNote.setText(offer.note);

		edtComment = (EditText)getView().findViewById(R.id.edt_fragment_offer_detail_comment);
	}

	private void buildActionButtons(){
		Button btnReject = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_reject);

		Button btnApprove = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_approve);

		if(WorkOffer.OFFER_STATUS_OFFER.equals(offer.approveResult) && WorkOffer.OFFER_PERMISSION_APPROVEABLE.equals(offerPermission)){
			btnReject.setVisibility(View.VISIBLE);
			btnApprove.setVisibility(View.VISIBLE);
			btnReject.setOnClickListener(this);
			btnApprove.setOnClickListener(this);
		}else{
			btnReject.setVisibility(View.GONE);
			btnApprove.setVisibility(View.GONE);
		}

		Button btnDelete = (Button)getView().findViewById(R.id.btn_fragment_offer_detail_delete);
		if(WorkOffer.OFFER_STATUS_OFFER.equals(offer.approveResult) && offer.userId.equals(myself.key)){
			btnDelete.setVisibility(View.VISIBLE);
			btnDelete.setOnClickListener(this);
		}else{
			btnDelete.setVisibility(View.GONE);
		}
	}

	private void buildWorkOfferApproveHistory(){
		ChiaseListViewNoScroll lstApproveHistory = (ChiaseListViewNoScroll)getView().findViewById(R.id.lst_fragment_offer_detail_approve_history);
		ApproveHistoryAdapter adapter = new ApproveHistoryAdapter(activity, offer.listHistories);
		lstApproveHistory.setAdapter(adapter);
	}

	//
	// private String getApproveComment(WorkOffer.Approve approve){
	// String result = "";
	// if(offer.listHistories != null){
	// for(int i = offer.listHistories.size() - 1; i >= 0; i--){
	// String flow = offer.listHistories.get(i).flow;
	// if(flow.equals(approve.flow)){
	// return offer.listHistories.get(i).note;
	// }
	// }
	// }
	// return result;
	// }

	protected void buildHeaderWithBackBtn(String title){
		super.initHeader(R.drawable.wf_back_white, title, R.drawable.ic_action_remove);
		btnEdit = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		if(WorkOffer.OFFER_PERMISSION_EDITABLE.equals(offerPermission)){
			btnEdit.setVisibility(View.VISIBLE);
			btnEdit.setOnClickListener(this);
		}else{
			btnEdit.setVisibility(View.INVISIBLE);
		}
	}

	private void onClickBtnDelete(){
		final WfDialog dlgConfirmDelete = new WfDialog(activity);
		dlgConfirmDelete.setDialogTitleButton(getString(R.string.fragment_offer_edit_confirm_delete_msg), getString(android.R.string.ok), getString(android.R.string.cancel), new View.OnClickListener() {

			@Override
			public void onClick(View v){
				sendDeleteOfferRequest();
				dlgConfirmDelete.dismiss();
			}
		}).show();
	}

	private void sendDeleteOfferRequest(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", offer.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_SW_OFFER_DELETE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_SW_OFFER_DELETE.equals(url)){
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
		}else{
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
		}
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		offer = CCJsonUtil.convertToModel(response.optString("offer"), WorkOffer.class);
		offerPermission = response.optString("permission");
		offerTypesMaster = buildOfferTypeMaster(activity, response);
		offerStatusMaster = buildOfferStatusMaster(activity, response);

		groupInfo = CCJsonUtil.convertToModel(response.optString("groupJoinMap"), Map.class);
		if(offer == null){
			((MainActivity)activity).isInitData = true;
			onClickBackBtn();
		}else{
			targetUserModels = CCJsonUtil.convertToModel(response.optString("targetUserModel"), Map.class);
			buildWorkOfferDetail();
		}
	}

	public void setOfferTypeStatusMaster(Map<String, String> offerTypesMaster, Map<String, String> offerStatusMaster){
		this.offerTypesMaster = offerTypesMaster;
		this.offerStatusMaster = offerStatusMaster;
	}

	private void gotoWorkOfferEditFragment(){
		WorkOfferEditFragment fragment = new WorkOfferEditFragment();
		fragment.setOffer(offer);
		fragment.setOfferTypeStatusMaster(offerTypesMaster, offerStatusMaster);
		List<DeptModel> deptModels = new ArrayList<>();
		// fragment.setFiltersAndDepts(filters, deptModels);
		gotoFragment(fragment);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			gotoWorkOfferEditFragment();
			break;
		case R.id.btn_fragment_offer_detail_delete:
			onClickBtnDelete();
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

	public void setWorkOffer(WorkOffer offer){
		this.offer = offer;
	}
}
