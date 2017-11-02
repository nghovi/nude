package nguyenhoangviet.vpcorp.shiftworking.services.requests.detail;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.activity.ChiaseActivity;
import nguyenhoangviet.vpcorp.android.view.ChiaseListViewNoScroll;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.WorkRequestFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.adapter.ApproveHistoryAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.dialog.WfDialog;
import nguyenhoangviet.vpcorp.welfare.adr.models.ApproveHistory;

public abstract class AbstractDetailFragment extends AbstractSwFragment{

	protected ImageView	imgEdit;
	protected String	offerPermission;
	protected EditText	edtComment;
	protected String	activeOfferId;
	protected String	execType;
	protected Button	btnDelete;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	public void setExecType(String execType){
		this.execType = execType;
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.sw_back_white, myself.userName, null);
		edtComment = (EditText)getView().findViewById(R.id.edt_comment);
		imgEdit = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		btnDelete = (Button) getView().findViewById(R.id.btn_delete);
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	protected void initData(){
		loadWorkOffer();
	}

	protected void loadWorkOffer(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeOfferId);
			jsonObject.put("execType", execType);
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(getDetailApi(), jsonObject, true);
	}

	protected abstract String getDetailApi();

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

	protected void buildWorkOfferDetail(){
		judgeEditPermission();
		judgeAprovePermission();
		judgeDeletePermission();
		buildWorkOfferApproveHistory();
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

	private void judgeDeletePermission(){
		if(SwConst.OFFER_CAN_EDIT_DELETE.equals(offerPermission) || SwConst.OFFER_ONLY_DELETE.equals(offerPermission)){
			btnDelete.setVisibility(View.VISIBLE);
			btnDelete.setOnClickListener(this);
		}else{
			btnDelete.setVisibility(View.GONE);
		}
	}

	private void judgeAprovePermission(){
		boolean permissionApprove = SwConst.OFFER_CAN_APPROVE.equals(offerPermission);
		LinearLayout lnrApproveArea = (LinearLayout)getView().findViewById(R.id.lnr_id_approve_area);
		if(permissionApprove){
			lnrApproveArea.setVisibility(View.VISIBLE);
			Button btnReject = (Button)getView().findViewById(R.id.btn_reject);
			Button btnApprove = (Button)getView().findViewById(R.id.btn_approve);
			btnReject.setOnClickListener(this);
			btnApprove.setOnClickListener(this);
		}else{
			lnrApproveArea.setVisibility(View.GONE);
		}
	}

	protected void buildWorkOfferApproveHistory(){
		if(getOfferUserId().equals(myself.key)){
			getView().findViewById(R.id.lnr_approve_history).setVisibility(View.VISIBLE);
			ChiaseListViewNoScroll lstApproveHistory = (ChiaseListViewNoScroll)getView().findViewById(R.id.lst_offer_status);
			ApproveHistoryAdapter adapter = new ApproveHistoryAdapter(activity, getListHistories());
			lstApproveHistory.setAdapter(adapter);
		}else{
			getView().findViewById(R.id.lnr_approve_history).setVisibility(View.GONE);
		}
	}

	protected abstract String getOfferUserId();

	protected abstract List<ApproveHistory> getListHistories();

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(getDeleteApi().equals(url)){
			getFragmentManager().popBackStack();
		}else{
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
			super.successUpdate(response, url);
		}
	}

	protected abstract void gotoWorkOfferEditFragment();

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			gotoWorkOfferEditFragment();
			break;
		case R.id.btn_approve:
			onClickBtnApprove();
			break;
		case R.id.btn_reject:
			onClickBtnReject();
			break;
		case R.id.btn_delete:
			onClickBtnDelete();
			break;
		default:
			break;
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
			jsonObject.put("key", activeOfferId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(getDeleteApi(), jsonObject, true);
	}

	protected void sendApproveResult(String approveResult){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", getOfferKey());
			jsonObject.put("approveComment", edtComment.getText().toString());
			jsonObject.put("approveResult", approveResult);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(getApproveApi(), jsonObject, true);
	}

	protected abstract String getOfferKey();

	protected abstract String getApproveApi();

	protected abstract String getDeleteApi();

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
			gotoFragment(new WorkRequestFragment());
		}else{
			super.onClickBackBtn();
		}
	}
}
