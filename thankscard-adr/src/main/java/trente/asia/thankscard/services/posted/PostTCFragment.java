package trente.asia.thankscard.services.posted;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.databinding.FragmentPostTcBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.fragments.dialogs.PostConfirmDialog;
import trente.asia.thankscard.services.common.model.Template;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 7/12/2017.
 */

public class PostTCFragment extends AbstractTCFragment implements View.OnClickListener,SelectDeptFragment.OnSelectDeptListener,SelectUserFragment.OnSelectUserListener,SelectCardFragment.OnSelectCardListener{
	public final int MAX_LETTER = 75;

	private List<Template>			templates;
	private FragmentPostTcBinding	binding;
	private Template				template;
	private List<DeptModel>			departments;
	private DeptModel				department;
	private UserModel				member;
	private String					message;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_tc, container, false);
			mRootView = binding.getRoot();

		}
		return mRootView;
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_post_tc;
	}

	@Override
	public boolean hasBackBtn(){
		return true;
	}

	@Override
	public boolean hasSettingBtn(){
		return true;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public int getTitle(){
		return R.string.fragment_post_edit_title;
	}

	@Override
	public void buildBodyLayout(){
		template = new Template();
		template.templateId = prefAccUtil.get(TcConst.PREF_TEMPLATE_ID);
		template.templateUrl = prefAccUtil.get(TcConst.PREF_TEMPLATE_PATH);
		binding.rltSelectDept.setOnClickListener(this);
		binding.rltSelectUser.setOnClickListener(this);
		binding.lnrSelectCard.setOnClickListener(this);
		binding.btnSend.setOnClickListener(this);
		binding.edtMessage.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				message = editable.toString();
				binding.txtCount.setText(String.valueOf(MAX_LETTER - message.length()));
			}
		});
	}

	@Override
	protected void initData(){
		super.initData();
		requestAccountInfo();
		requestTemplate();
		buildTemplate();
	}

	private void requestTemplate(){
		JSONObject param = new JSONObject();
		requestLoad(TcConst.API_GET_TEMPLATE, param, true);
	}

	private void requestAccountInfo(){
		JSONObject param = new JSONObject();
		requestLoad(WfUrlConst.WF_ACC_INFO_DETAIL, param, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(TcConst.API_GET_TEMPLATE.equals(url)){
			requestTemplateSuccess(response);
		}else if(WfUrlConst.WF_ACC_INFO_DETAIL.equals(url)){
			departments = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
			department = new DeptModel(CCConst.NONE, getString(R.string.chiase_common_none));
			departments.add(0, department);
			for(DeptModel dept : departments){
				if(CCCollectionUtil.isEmpty(dept.members)){
					dept.members = new ArrayList<>();
				}
				member = new UserModel(CCConst.NONE, getString(R.string.chiase_common_none));
				dept.members.add(0, member);
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void requestTemplateSuccess(JSONObject response){
		templates = CCJsonUtil.convertToModelList(response.optString("templates"), Template.class);
	}

	private void buildTemplate(){
		WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + template.templateUrl, binding.imgCard, null);
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.rlt_select_dept:
			SelectDeptFragment fragment = new SelectDeptFragment();
			gotoFragment(fragment);
			fragment.setDepartments(departments, department);
			fragment.setCallback(this);
			break;
		case R.id.rlt_select_user:
			if(!WelfareConst.NONE.equals(department.key)){
				SelectUserFragment userFragment = new SelectUserFragment();
				userFragment.setCallback(this);
				userFragment.setDepartments(department.members, member);
				gotoFragment(userFragment);
			}
			break;
		case R.id.lnr_select_card:
			SelectCardFragment cardFragment = new SelectCardFragment();
			cardFragment.setCards(templates);
			cardFragment.setCallback(this);
			gotoFragment(cardFragment);
			break;
		case R.id.btn_send:
			checkNewCard();
			break;
		default:
			break;
		}
	}

	private void checkNewCard(){
		if(CCConst.NONE.equals(member.key)){
			showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message1), getString(android.R.string.ok), null);
		}else if(CCStringUtil.isEmpty(message) || hasTooManyLetters(message)){
			showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message2, String.valueOf(MAX_LETTER)), getString(android.R.string.ok), null);
		}else if(this.template == null){
			showAlertDialog(getString(R.string.fragment_post_edit_alert_dlg_title), getString(R.string.fragment_post_edit_alert_dlg_message3), getString(android.R.string.ok), null);
		}else{
			showConfirmDialog();
		}
	}

	private boolean hasTooManyLetters(String message) {
		return message.length() > MAX_LETTER;
	}

	private void showConfirmDialog(){
		final PostConfirmDialog dialog = new PostConfirmDialog();
		dialog.setReceiverName(member.userName);
		dialog.show(getFragmentManager(), null);
		dialog.setListeners(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dialog.dismiss();
				requestPostNewCard(dialog.isSecret());
			}
		}, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dialog.dismiss();
			}
		});
	}

	private void requestPostNewCard(boolean isSecret){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("postDate", CCFormatUtil.formatDate(new Date()));
			jsonObject.put("categoryId", 1);
			jsonObject.put("templateId", template.templateId);

			UserModel userModel = prefAccUtil.getUserPref();
			jsonObject.put("posterId", userModel.key);

			jsonObject.put("receiverId", member.key);
			jsonObject.put("message", binding.edtMessage.getText().toString());
			jsonObject.put("isSecret", isSecret);
		}catch(JSONException ex){
			ex.printStackTrace();
		}

		log(jsonObject.toString());
		requestUpdate(TcConst.API_POST_NEW_CARD, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url) {
		if (TcConst.API_POST_NEW_CARD.equals(url)) {
			requestPostNewCardSuccess(response);
		} else {
			super.successUpdate(response, url);
		}
	}

	private void requestPostNewCardSuccess(JSONObject response) {
		showAlertDialog(getString(R.string.fragment_posted_confirm_success_title), getString(R.string.fragment_posted_confirm_success_message), getString(android.R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				onClickOkButtonAfterShowingSuccessDialog();
			}
		});
	}

	private void onClickOkButtonAfterShowingSuccessDialog() {
		((WelfareActivity) activity).isInitData = true;
		onClickBackBtn();
	}

	@Override
	public void onSelectDeptDone(DeptModel deptModel){
		this.department = deptModel;
		binding.deptName.setText(department.deptName);
		member = department.members.get(0);
		binding.userName.setText(member.userName);
	}

	private void log(String msg){
		Log.e("PostTc", msg);
	}

	@Override
	public void onSelectUserDone(UserModel userModel){
		this.member = userModel;
		binding.userName.setText(member.userName);
	}

	@Override
	public void onSelectCardDone(Template card){
		this.template = card;
		buildTemplate();
	}
}
