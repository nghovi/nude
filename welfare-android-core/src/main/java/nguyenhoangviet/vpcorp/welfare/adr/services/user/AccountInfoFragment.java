package nguyenhoangviet.vpcorp.welfare.adr.services.user;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.welfare.adr.R;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareFragment;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuButtonsListener;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuManageListener;
import nguyenhoangviet.vpcorp.welfare.adr.models.SettingModel;
import nguyenhoangviet.vpcorp.welfare.adr.services.user.menu.AccountMenuManager;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/15/2016.
 */
public abstract class AccountInfoFragment extends WelfareFragment{

	public static final String		IS_CHECKED				= "1";
	public static final String		UN_CHECKED				= "0";

	private View					mViewForMenuBehind;
	private Uri						mImageUri;

	private ImageView				imgAvatar;
	protected Button				btnUpdate;

	private EditText				edtOldPassword;
	private EditText				edtNewPassword;
	private EditText				edtConfirmPassword;

	protected SwitchCompat			swtChangePassWord;
	private boolean					isChangePass;

	protected SwitchCompat			swtNotification;
	private boolean					isNotification;

	private AccountMenuManager		menuManager;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_account_info, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.wf_account_info_title), null);

		imgAvatar = (ImageView)getView().findViewById(R.id.img_fragment_setting_avatar);

		edtOldPassword = (EditText)getView().findViewById(R.id.txt_old_password);
		edtNewPassword = (EditText)getView().findViewById(R.id.txt_new_password);
		edtConfirmPassword = (EditText)getView().findViewById(R.id.txt_comfirm_password);

		setupBtnUpdate();

		imgAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onButtonMenuClicked();
			}
		});

		mViewForMenuBehind = getView().findViewById(R.id.viewForMenuBehind);
		mViewForMenuBehind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onButtonMenuOpenedClicked();
			}
		});

		menuManager = new AccountMenuManager();
		menuManager.setMenuLayout(activity, R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);

		swtChangePassWord = (SwitchCompat)getView().findViewById(R.id.swt_change_password);
		swtChangePassWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
				if(isChecked){
					getView().findViewById(R.id.lnr_change_password).setVisibility(View.VISIBLE);
					isChangePass = true;
				}else{
					getView().findViewById(R.id.lnr_change_password).setVisibility(View.GONE);
					isChangePass = false;
				}
			}
		});

		swtNotification = (SwitchCompat)getView().findViewById(R.id.swt_push_notification);
	}

	protected void setupBtnUpdate(){
		btnUpdate = (Button)getView().findViewById(R.id.btn_id_update);
		btnUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(isChangePass){
					updateAccountInfo();
				}
			}
		});
		// addTextWatcher(btnUpdate, Arrays.asList((View)edtOldPassword, edtNewPassword, edtConfirmPassword));
	}

	protected void onButtonMenuClicked(){
		menuManager.openMenu(imgAvatar);
		mViewForMenuBehind.setVisibility(View.VISIBLE);
	}

	private void onButtonMenuOpenedClicked(){
		if(mViewForMenuBehind.getVisibility() == View.GONE){
			return;
		}
		menuManager.closeMenu();
	}

	@Override
	public void initData(){
		this.loadAccountDetail();
	}

	private void loadAccountDetail(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(WfUrlConst.WF_ACC_INFO_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_INFO_DETAIL.equals(url)){
			if(!CCStringUtil.isEmpty(myself.avatarPath)){
				WfPicassoHelper.loadImage(activity, host + myself.avatarPath, imgAvatar, null);
			}

			SettingModel settingModel = CCJsonUtil.convertToModel(response.optString("setting"), SettingModel.class);
			if(IS_CHECKED.equals(settingModel.WF_PUSH_SETTING)){
				swtNotification.setChecked(true);
			}else{
				swtNotification.setChecked(false);
			}
			swtNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked){
					updateNotification(isChecked);
				}
			});

			TextView txtUserName = (TextView)getView().findViewById(R.id.txt_id_username);
			txtUserName.setText(myself.userName);
		}else{
			super.successLoad(response, url);
		}
	}

	private void updateAccountInfo(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", myself.key);
			jsonObject.put("oldPassword", edtOldPassword.getText().toString());
			jsonObject.put("newPassword", edtNewPassword.getText().toString());
			jsonObject.put("confirmPassword", edtConfirmPassword.getText().toString());
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_ACC_INFO_UPDATEINFO, jsonObject, true);
	}

	private void updateNotification(boolean isChecked){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", myself.key);
			jsonObject.put("changePush", isChecked ? IS_CHECKED : UN_CHECKED);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_ACC_INFO_SETTING, jsonObject, true);
	}

	private void updateAccountAvatar(){
		JSONObject jsonObject = new JSONObject();
		Map<String, File> fileMap = new HashMap<>();
		if(mImageUri != null){
			fileMap.put("avatar", new File(mImageUri.getPath()));
		}
		try{
			jsonObject.put("key", myself.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpload(WfUrlConst.WF_ACC_INFO_UPDATE_AVATAR, jsonObject, fileMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_INFO_UPDATE_AVATAR.equals(url)){
			// super.onBackListener();
			Toast.makeText(activity, getString(R.string.chiase_common_success_change_avatar), Toast.LENGTH_LONG).show();
		}else{
			super.successUpload(response, url);
		}
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_INFO_UPDATEINFO.equals(url)){
			// super.onBackListener();
			Toast.makeText(activity, getString(R.string.chiase_common_success_change_password), Toast.LENGTH_LONG).show();
		}else if(WfUrlConst.WF_ACC_INFO_SETTING.equals(url)){
			// super.onBackListener();
			Toast.makeText(activity, getString(R.string.chiase_common_success_change_push_setting), Toast.LENGTH_LONG).show();
		}else{
			super.successUpload(response, url);
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
//					alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
//					alertDialog.show();
				}else{
					String imagePath = returnedIntent.getExtras().getString(WelfareConst.IMAGE_PATH_KEY);
					Uri uri = AndroidUtil.getUriFromFileInternal(activity, new File(imagePath));
					// Uri uri = AndroidUtil.getUriFromFile(activity, new File(imagePath));
					cropImage(uri);
				}
			}

			break;

		case WelfareConst.RequestCode.PHOTO_CROP:
			try{
				File imageFile = new File(mImageUri.getPath());
				SettingModel settingModel = prefAccUtil.getSetting();
				if(CCNumberUtil.toLong(settingModel.WF_MAX_FILE_SIZE).compareTo(imageFile.length()) < 0){
//					alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
//					alertDialog.show();
				}else{
					imgAvatar.setImageURI(mImageUri);
					updateAccountAvatar();
				}
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
		// ToDo Trung: imageUri is null
		WelfareUtil.startCropActivity(this, imageUri, mImageUri);
	}

	abstract protected String makeAppFile(String fileName);

	abstract protected void eventCameraClicked();

	abstract protected void eventGalleryClicked();

	@Override
	public void onDestroy(){
		super.onDestroy();
		mViewForMenuBehind = null;
		mImageUri = null;

		imgAvatar = null;
		btnUpdate = null;

		edtOldPassword = null;
		edtNewPassword = null;
		edtConfirmPassword = null;

		swtChangePassWord = null;
		swtNotification = null;

		menuManager = null;
	}
}
