package trente.asia.calendar.services.setting;

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
import android.support.v4.app.Fragment;
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
import trente.asia.android.util.AndroidUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.activites.CameraPhotoPreviewAccountActivity;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.user.ClLoginFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.menu.OnMenuButtonsListener;
import trente.asia.welfare.adr.menu.OnMenuManageListener;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.services.user.menu.AccountMenuManager;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClAccInforFragment extends AbstractClFragment{

	public static final String		IS_CHECKED				= "1";
	public static final String		UN_CHECKED				= "0";

	private View					mViewForMenuBehind;
	private Uri						mImageUri;

	private ImageView				imgAvatar;
	protected Button				btnUpdate;
	private TextView				txtUserName;

	private EditText				edtOldPassword;
	private EditText				edtNewPassword;
	private EditText				edtConfirmPassword;

	protected SwitchCompat			swtChangePassWord;
	private boolean					isChangePass;

	protected SwitchCompat			swtNotification;
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
			mRootView = inflater.inflate(R.layout.fragment_clacc_infor, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_setting;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.wf_back_white, getString(R.string.wf_account_info_title), null);

		imgAvatar = (ImageView)getView().findViewById(trente.asia.welfare.adr.R.id.img_fragment_setting_avatar);

		edtOldPassword = (EditText)getView().findViewById(trente.asia.welfare.adr.R.id.txt_old_password);
		edtNewPassword = (EditText)getView().findViewById(trente.asia.welfare.adr.R.id.txt_new_password);
		edtConfirmPassword = (EditText)getView().findViewById(trente.asia.welfare.adr.R.id.txt_comfirm_password);
		txtUserName = (TextView)getView().findViewById(trente.asia.welfare.adr.R.id.txt_id_username);

		setupBtnUpdate();

		imgAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onButtonMenuClicked();
			}
		});

		mViewForMenuBehind = getView().findViewById(trente.asia.welfare.adr.R.id.viewForMenuBehind);
		mViewForMenuBehind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onButtonMenuOpenedClicked();
			}
		});

		menuManager = new AccountMenuManager();
		menuManager.setMenuLayout(activity, trente.asia.welfare.adr.R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);

		swtChangePassWord = (SwitchCompat)getView().findViewById(trente.asia.welfare.adr.R.id.swt_change_password);
		swtChangePassWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
				if(isChecked){
					getView().findViewById(trente.asia.welfare.adr.R.id.lnr_change_password).setVisibility(View.VISIBLE);
					isChangePass = true;
				}else{
					getView().findViewById(trente.asia.welfare.adr.R.id.lnr_change_password).setVisibility(View.GONE);
					isChangePass = false;
				}
			}
		});

		swtNotification = (SwitchCompat)getView().findViewById(trente.asia.welfare.adr.R.id.swt_push_notification);
	}

	protected void setupBtnUpdate(){
		btnUpdate = (Button)getView().findViewById(trente.asia.welfare.adr.R.id.btn_id_update);
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
			Toast.makeText(activity, "Change Avatar Successful !", Toast.LENGTH_LONG).show();
			File appFolder = new File(ClUtil.getFilesFolderPath());
			deleteAppFolder(appFolder);
		}else{
			super.successUpload(response, url);
		}
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_INFO_UPDATEINFO.equals(url)){
			// super.onBackListener();
			Toast.makeText(activity, "Change Password Successful !", Toast.LENGTH_LONG).show();
		}else if(WfUrlConst.WF_ACC_INFO_SETTING.equals(url)){
			// super.onBackListener();
			Toast.makeText(activity, "Change Push Notification Successful !", Toast.LENGTH_LONG).show();
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
				File imageFile = new File(mImageUri.getPath());
				SettingModel settingModel = prefAccUtil.getSetting();
				if(CCNumberUtil.toLong(settingModel.WF_MAX_FILE_SIZE).compareTo(imageFile.length()) < 0){
					alertDialog.setMessage("File is big");
					alertDialog.show();
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
		WelfareUtil.startCropActivity(this, imageUri, mImageUri);
	}

	// ////////////////////////////////////////////////////////////

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		host = BuildConfig.HOST;
	}

	protected String makeAppFile(String fileName){
		String filePath = ClUtil.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}

	protected void eventCameraClicked(){
		CameraPhotoPreviewAccountActivity.starCameraPhotoPreviewActivity(ClAccInforFragment.this);
	}

	protected void eventGalleryClicked(){
		CameraPhotoPreviewAccountActivity.starCameraFromGalleryPhotoPreviewActivity(ClAccInforFragment.this);
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new ClLoginFragment());
	}

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

	@Override
	public void onClick(View view){

	}
}
