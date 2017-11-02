package nguyenhoangviet.vpcorp.dailyreport.services.report;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.android.view.ChiaseEditText;
import nguyenhoangviet.vpcorp.dailyreport.DRConst;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.activities.CameraPhotoPreviewActivity;
import nguyenhoangviet.vpcorp.dailyreport.activities.FilePreviewActivity;
import nguyenhoangviet.vpcorp.dailyreport.fragments.AbstractDRFragment;
import nguyenhoangviet.vpcorp.dailyreport.services.comment.menu.CommentMenuManager;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.DRMessageContentModel;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.ReportModel;
import nguyenhoangviet.vpcorp.dailyreport.utils.DRUtil;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuButtonsListener;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuManageListener;

/**
 * ReportCommentFragment
 * 
 * @author TrungND on 2/15/2016.
 */
public class ReportCommentFragment extends AbstractDRFragment implements View.OnClickListener{

	private View					mViewForMenuBehind;
	private CommentMenuManager		menuManager;
	private Button					mBtnSend;
	private Button					mBtnAttach;
	private ChiaseEditText			mEdtComment;

	private OnMenuManageListener	onMenuManagerListener	= new OnMenuManageListener() {

																@Override
																public void onMenuOpened(){
																}

																@Override
																public void onMenuClosed(){
																	mEdtComment.setEnabled(true);
																	mViewForMenuBehind.setVisibility(View.GONE);
																}
															};

	protected OnMenuButtonsListener	onMenuButtonsListener	= new OnMenuButtonsListener() {

																@Override
																public void onCameraClicked(){
																	CameraPhotoPreviewActivity.starCameraPhotoPreviewActivity(ReportCommentFragment.this, reportModel.key, true);
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onAudioClicked(){
																}

																@Override
																public void onFileClicked(){
																	FilePreviewActivity.startFilePreviewActivity(ReportCommentFragment.this, reportModel.key, mEdtComment.getText().toString());
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onVideoClicked(){
																}

																@Override
																public void onLocationClicked(){
																}

																@Override
																public void onGalleryClicked(){
																	CameraPhotoPreviewActivity.starCameraFromGalleryPhotoPreviewActivity(ReportCommentFragment.this, reportModel.key, true);
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onContactClicked(){
																}
															};
	private ReportModel				reportModel;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_report_comment;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.dr_report_comment_title), null);

		mViewForMenuBehind = getView().findViewById(R.id.viewForMenuBehind);
		mBtnAttach = (Button)getView().findViewById(R.id.btn_id_attach);
		mBtnSend = (Button)getView().findViewById(R.id.btn_id_send);
		mEdtComment = (ChiaseEditText)getView().findViewById(R.id.edt_id_comment);

		mBtnAttach.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);
		mViewForMenuBehind.setOnClickListener(this);

		menuManager = new CommentMenuManager();
		menuManager.setMenuLayout(activity, R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);

		mEdtComment.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				if(DRUtil.isNotEmpty(editable.toString())){
					mBtnAttach.setEnabled(false);
				}else{
					mBtnAttach.setEnabled(true);
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
		super.onActivityResult(requestCode, resultCode, returnedIntent);

		if(resultCode != Activity.RESULT_OK) return;
		switch(requestCode){
		case WelfareConst.RequestCode.PHOTO_CHOOSE:
			appendFile(returnedIntent);
			break;
		case WelfareConst.RequestCode.VIDEO_CHOOSE:
			appendFile(returnedIntent);
			break;
		case WelfareConst.RequestCode.PICK_FILE:
			appendFile(returnedIntent);
			break;
		default:
			break;
		}
	}

	private void appendFile(Intent returnedIntent){
		String detailMessage = returnedIntent.getExtras().getString("detail");
		DRMessageContentModel photoModel = CCJsonUtil.convertToModel(CCStringUtil.toString(detailMessage), DRMessageContentModel.class);
		((WelfareActivity)activity).isInitData = true;
		onClickBackBtn();
	}

	private void sendComment(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("parentKey", reportModel.key);
			jsonObject.put("commentContent", mEdtComment.getText().toString());
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(DRConst.API_REPORT_COMMENT, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(DRConst.API_REPORT_COMMENT.equals(url)){
			Toast.makeText(activity, "You've commented on this report", Toast.LENGTH_LONG).show();
			((WelfareActivity)activity).isInitData = true;
			onClickBackBtn();
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_id_attach:
			if(AndroidUtil.verifyStoragePermissions(activity)){
				onButtonMenuClicked();
			}
			break;
		case R.id.viewForMenuBehind:
			onButtonMenuOpenedClicked();
			break;
		case R.id.btn_id_send:
			sendComment();
		default:
			break;
		}
	}

	private void onButtonMenuClicked(){
		mEdtComment.setEnabled(false);
		menuManager.openMenu(mBtnAttach);
		mViewForMenuBehind.setVisibility(View.VISIBLE);
	}

	private void onButtonMenuOpenedClicked(){
		if(mViewForMenuBehind.getVisibility() == View.GONE){
			return;
		}
		menuManager.closeMenu();
	}

	public void setReportModel(ReportModel reportModel){
		this.reportModel = reportModel;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		mViewForMenuBehind = null;
		menuManager = null;
		mBtnSend = null;
		mBtnAttach = null;
		mEdtComment = null;
	}

}
