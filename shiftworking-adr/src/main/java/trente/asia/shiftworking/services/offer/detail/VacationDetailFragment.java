package trente.asia.shiftworking.services.offer.detail;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

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
import trente.asia.shiftworking.databinding.FragmentVacationDetailBinding;
import trente.asia.shiftworking.services.offer.adapter.VacationApproveHistoryAdapter;
import trente.asia.shiftworking.services.offer.edit.VacationEditFragment;
import trente.asia.shiftworking.services.offer.list.VacationListFragment;
import trente.asia.shiftworking.services.offer.model.VacationModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

public class VacationDetailFragment extends AbstractSwFragment{

	private VacationModel					offer;
	private ImageView						imgEdit;
	private Map<String, String>				offerStatusMaster;
	private String							offerPermission;

	private EditText						edtComment;
	private String							activeOfferId;
	private String							execType;
	private FragmentVacationDetailBinding	binding;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	public void setExecType(String execType){
		this.execType = execType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacation_detail, container, false);
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

		requestLoad(SwConst.API_VACATION_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_VACATION_DETAIL.equals(url)){
			offer = CCJsonUtil.convertToModel(response.optString("vacation"), VacationModel.class);
			setWorkOffer(offer);

			offerPermission = response.optString("permission");
			offerStatusMaster = buildOfferStatusMaster(response);

			if(offer == null){
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

	private Map<String, String> buildOfferStatusMaster(JSONObject response){
		List<ApiObjectModel> lstStatus = CCJsonUtil.convertToModelList(response.optString("offerStatusList"), ApiObjectModel.class);
		lstStatus.add(new ApiObjectModel(CCConst.NONE, getString(R.string.chiase_common_all)));
		return WelfareFormatUtil.convertList2Map(lstStatus);
	}

	private void setWorkOffer(VacationModel offerModel){
		try{
			LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject(lnrContent, new JSONObject(gson.toJson(offerModel)));
		}catch(JSONException e){
			e.printStackTrace();
		}

		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_offer_user)).setText(offerModel.userName);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_offer_type)).setText(offerModel.vacationName);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_start_date)).setText(offerModel.startDateString);
		((TextView)getView().findViewById(R.id.txt_fragment_offer_detail_note)).setText(offerModel.note);
		binding.txtSickAbsent.setText(offerModel.sickAbsent ? getString(R.string.sw_yes) : getString(R.string.sw_no));
        binding.txtAmount.setText(offerModel.amountString);
        if (Float.parseFloat(offerModel.amount) >= 1) {
            binding.txtEndDate.setText(offerModel.endDateString);
            binding.txtEndDate.setVisibility(View.VISIBLE);
        } else {
            binding.txtEndDate.setVisibility(View.GONE);
        }
	}

	private void buildWorkOfferDetail(){
		judgeEditPermission();
		judgeAprovePermission();
		judgeDeletePermission();
		buildWorkOfferApproveHistory();
	}

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
			ChiaseListViewNoScroll lstApproveHistory = (ChiaseListViewNoScroll)getView().findViewById(R.id.lst_fragment_offer_detail_offer_status);
			VacationApproveHistoryAdapter adapter = new VacationApproveHistoryAdapter(activity, offer.listHistories);
			lstApproveHistory.setAdapter(adapter);
		}else{
			getView().findViewById(R.id.lnr_fragment_offer_detail_approve_history).setVisibility(View.GONE);
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

	private void judgeDeletePermission() {
		if (SwConst.OFFER_CAN_EDIT_DELETE.equals(offerPermission) || SwConst.OFFER_ONLY_DELETE.equals(offerPermission)) {
			binding.btnDelete.setVisibility(View.VISIBLE);
			binding.btnDelete.setOnClickListener(this);
		} else {
			binding.btnDelete.setVisibility(View.GONE);
		}
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(SwConst.API_VACATION_DELETE.equals(url)){
			getFragmentManager().popBackStack();
		}else{
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
			super.successUpdate(response, url);
		}
	}

	private void gotoWorkOfferEditFragment(){
		VacationEditFragment fragment = new VacationEditFragment();
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
		requestUpdate(SwConst.API_VACATION_DELETE, jsonObject, true);
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
		requestUpdate(SwConst.API_VACATION_APPROVE, jsonObject, true);
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
			gotoFragment(new VacationListFragment());
		}else{
			super.onClickBackBtn();
		}
	}
}
