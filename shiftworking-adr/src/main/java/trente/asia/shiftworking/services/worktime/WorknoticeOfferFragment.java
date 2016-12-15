package trente.asia.shiftworking.services.worktime;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import asia.chiase.core.model.KeyValueModel;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.AndroidUtil;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.activities.CameraPhotoPreviewAccountActivity;
import trente.asia.shiftworking.common.fragments.AbstractLocationFragment;
import trente.asia.shiftworking.services.worktime.model.ProjectModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.menu.OnMenuButtonsListener;
import trente.asia.welfare.adr.menu.OnMenuManageListener;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.services.user.menu.AccountMenuManager;
import trente.asia.welfare.adr.utils.WelfareUtil;

public class WorknoticeOfferFragment extends AbstractLocationFragment{

	private ImageView				imgPhoto;
	private TextView				txtProjectName;
	private LinearLayout			lnrLocation;
	private TextView				txtLocation;
	private EditText				edtReason;

	private ChiaseTextView			txtNoticeType;
	private ChiaseTextView			txtTargetDept;
	private LinearLayout			lnrNoticeType;
	private LinearLayout			lnrTargetDept;
	private Button					btnSend;

	private ProjectModel			activeProject;
	private List<DeptModel>			deptModels;

	private String					latitude;
	private String					longitude;
	private String					mOriginalPath;
	private Uri						mImageUri;

	private ChiaseListDialog		dlgNoticeType;
	private ChiaseListDialog		dlgTargetDept;

	private AccountMenuManager		menuManager;
	private View					mViewForMenuBehind;

	private OnMenuManageListener	onMenuManagerListener	= new OnMenuManageListener() {

																@Override
																public void onMenuOpened(){
																}

																@Override
																public void onMenuClosed(){
																	mViewForMenuBehind.setVisibility(View.GONE);
																}
															};

	protected OnMenuButtonsListener	onMenuButtonsListener	= new OnMenuButtonsListener() {

																@Override
																public void onCameraClicked(){
																	eventCameraClicked();
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onAudioClicked(){
																}

																@Override
																public void onFileClicked(){
																}

																@Override
																public void onVideoClicked(){
																}

																@Override
																public void onLocationClicked(){
																}

																@Override
																public void onGalleryClicked(){
																	eventGalleryClicked();
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onContactClicked(){
																}
															};

	public void setActiveProject(ProjectModel activeProject){
		this.activeProject = activeProject;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_notice_offer, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_work_time;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.sw_back_white, myself.userName, null);

		imgPhoto = (ImageView)getView().findViewById(R.id.img_id_photo);
		lnrLocation = (LinearLayout)getView().findViewById(R.id.lnr_id_location);
		txtLocation = (TextView)getView().findViewById(R.id.txt_id_location);
		txtProjectName = (TextView)getView().findViewById(R.id.txt_id_project_name);
		txtProjectName.setText(activeProject.projectName);

		btnSend = (Button)getView().findViewById(R.id.btn_id_send);
		edtReason = (EditText)getView().findViewById(R.id.edt_id_reason);
		txtNoticeType = (ChiaseTextView)getView().findViewById(R.id.txt_id_notice_type);
		txtTargetDept = (ChiaseTextView)getView().findViewById(R.id.txt_id_target_dept);
		lnrNoticeType = (LinearLayout)getView().findViewById(R.id.lnr_id_notice_type);
		lnrTargetDept = (LinearLayout)getView().findViewById(R.id.lnr_id_target_dept);

		imgPhoto.setOnClickListener(this);
		lnrLocation.setOnClickListener(this);
		lnrNoticeType.setOnClickListener(this);
		lnrTargetDept.setOnClickListener(this);
		btnSend.setOnClickListener(this);

		menuManager = new AccountMenuManager();
		menuManager.setMenuLayout(activity, trente.asia.welfare.adr.R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);
		mViewForMenuBehind = getView().findViewById(R.id.viewForMenuBehind);
		mViewForMenuBehind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onButtonMenuOpenedClicked();
			}
		});
	}

	@Override
	protected void initData(){
		loadNoticeForm();
	}

	private void loadNoticeForm(){

		JSONObject jsonObject = new JSONObject();
		try{
			Date date = new Date();
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, date));
			jsonObject.put("userId", myself.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_NOTICE_0003, jsonObject, true);

	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_NOTICE_0003.equals(url)){

			// notice type
			List<KeyValueModel> pairs = CCJsonUtil.convertToModelList(response.optString("noticeTypes"), KeyValueModel.class);
			Map<String, String> noticeTypeMap = new LinkedHashMap<>();
			for(KeyValueModel pair : pairs){
				noticeTypeMap.put(pair.getKey(), pair.getValue());
			}
			txtNoticeType.setText(pairs.get(0).getValue());
			txtNoticeType.setValue(pairs.get(0).getKey());
			dlgNoticeType = new ChiaseListDialog(activity, getString(R.string.sw_work_item_notice_type), noticeTypeMap, txtNoticeType, null);

			// dept
			deptModels = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
			Map<String, String> deptMap = new LinkedHashMap<String, String>();
			for(DeptModel dept : deptModels){
				deptMap.put(CCStringUtil.toString(dept.key), dept.deptName);
			}
			txtTargetDept.setText(deptModels.get(0).deptName);
			txtTargetDept.setValue(CCStringUtil.toString(deptModels.get(0).key));

			dlgTargetDept = new ChiaseListDialog(activity, getString(R.string.sw_work_item_target_dept), deptMap, txtTargetDept, null);

		}else{
			super.successLoad(response, url);
		}
	}

	private void sendNotice(){
		JSONObject jsonObject = new JSONObject();
		Map<String, File> photoMap = new HashMap<>();
		if(mImageUri != null){
			photoMap.put("photo", new File(mImageUri.getPath()));
		}
		try{
			jsonObject.put("projectId", activeProject.key);
			jsonObject.put("deptId", txtTargetDept.value);
			jsonObject.put("noticeType", txtNoticeType.value);
			jsonObject.put("reason", CCStringUtil.toString(edtReason.getText()));
			jsonObject.put("gpsLatitude", latitude);
			jsonObject.put("gpsLongitude", longitude);
			jsonObject.put("location", CCStringUtil.toString(txtLocation.getText()));
			jsonObject.put("userId", myself.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpload(WfUrlConst.WF_NOTICE_0002, jsonObject, photoMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		if(WfUrlConst.WF_NOTICE_0002.equals(url)){
			getFragmentManager().popBackStack();
		}else{
			super.successUpload(response, url);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_photo:
			menuManager.openMenu(imgPhoto);
			mViewForMenuBehind.setVisibility(View.VISIBLE);
			break;

		case R.id.lnr_id_location:
			getLocation();
			break;

		case R.id.lnr_id_notice_type:
			dlgNoticeType.show();
			break;

		case R.id.lnr_id_target_dept:
			dlgTargetDept.show();
			break;

		case R.id.btn_id_send:
			sendNotice();
			break;

		default:
			break;
		}
	}

	@Override
	protected void successLocation(){
		Location lastKnownLocation = WelfareUtil.getLocation(activity);
		if(lastKnownLocation != null){
			latitude = CCStringUtil.toString(lastKnownLocation.getLatitude());
			longitude = CCStringUtil.toString(lastKnownLocation.getLongitude());
			String address = WelfareUtil.getAddress4Location(activity, lastKnownLocation);
			txtLocation.setText(address);
		}else{
			Toast.makeText(activity, getString(R.string.chiase_common_not_working_gps), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
		super.onActivityResult(requestCode, resultCode, returnedIntent);
		if(resultCode != Activity.RESULT_OK) return;
		switch(requestCode){
		case WelfareConst.RequestCode.PHOTO_CHOOSE:
			if(returnedIntent != null){
				String detailMessage = returnedIntent.getExtras().getString("detail");
				if(WelfareConst.WF_FILE_SIZE_NG.equals(detailMessage)){
					alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
					alertDialog.show();
				}else{
					String imagePath = returnedIntent.getExtras().getString(WelfareConst.IMAGE_PATH_KEY);
					Uri uri = AndroidUtil.getUriFromFileInternal(activity, new File(imagePath));
					cropImage(uri);
				}
			}

			break;
		case WelfareConst.RequestCode.PHOTO_CROP:
			try{
                imgPhoto.setImageURI(mImageUri);
			}catch(Exception ex){
				ex.printStackTrace();
			}

			break;

		default:
			break;
		}
	}

	private void cropImage(Uri imageUri){
		long date = System.currentTimeMillis();
		String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
		String filePath = makeAppFile(filename);
		File imageFile = new File(filePath);
		try{
			imageFile.createNewFile();
		}catch(IOException ex){
			ex.printStackTrace();
		}

		mImageUri = Uri.fromFile(imageFile);
		WelfareUtil.startCropActivity(this, imageUri, mImageUri);
	}

	private void onButtonMenuOpenedClicked(){
		if(mViewForMenuBehind.getVisibility() == View.GONE){
			return;
		}
		menuManager.closeMenu();
	}

	protected void eventCameraClicked(){
		CameraPhotoPreviewAccountActivity.starCameraPhotoPreviewActivity(WorknoticeOfferFragment.this);
	}

	protected void eventGalleryClicked(){
		CameraPhotoPreviewAccountActivity.starCameraFromGalleryPhotoPreviewActivity(WorknoticeOfferFragment.this);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		imgPhoto = null;
		lnrLocation = null;
		txtLocation = null;
		txtNoticeType = null;
		txtTargetDept = null;
		edtReason = null;

		btnSend = null;
		activeProject = null;
		dlgNoticeType = null;
		dlgTargetDept = null;
	}
}
