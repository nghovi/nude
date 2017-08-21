package asia.trente.officeletter.services.setting;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.activities.CameraPhotoPreviewAccountActivity;
import asia.trente.officeletter.commons.fragment.AbstractOLFragment;
import asia.trente.officeletter.commons.utils.OLUtils;
import trente.asia.android.util.AndroidUtil;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/15/2016.
 */
public class AccountInfoFragment extends AbstractOLFragment implements AvatarSelectionDialog.OnSelectHandlerListener{

	private ImageView				imgAvatar;
	private AvatarSelectionDialog	dialog;

	private SwitchCompat			swtPass;
	private SwitchCompat			swtPush;
	private EditText				edtpassword;
	private EditText				edtnewPassword1;
	private EditText				edtnewPassword2;

	private String					mImagePath;
	private Uri						mImageUri;
	private boolean					mIsOverJellyBean;



	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_accinfo;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_setting;
	}

	@Override
	protected void initView() {
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.wf_account_info_title), null);
		buildBodyLayout();
	}

	public void buildBodyLayout(){
		buildAccountInfo();
		buildSwitch();
		mIsOverJellyBean = AndroidUtil.isBuildOver(18);
		if (getFooterItemId() == 0) {
			getView().findViewById(R.id.footer).setVisibility(View.GONE);
		}
	}

	private void buildAccountInfo(){
		imgAvatar = (ImageView)getView().findViewById(R.id.img_fragment_setting_avatar);
		imgAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				showDialogAvatarHandler();
			}
		});
		TextView txtUserName = (TextView)getView().findViewById(R.id.txt_id_username);
		txtUserName.setText(myself.userName);
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
			if(CCConst.NONE.equals(settingModel.WF_PUSH_SETTING)){
				swtPush.setChecked(false);
			}else{
				swtPush.setChecked(true);
			}
			swtPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
					saveNoticeFlag(isChecked);
				}
			});
		}else{
			super.successLoad(response, url);
		}
	}

	private void showDialogAvatarHandler(){
		if(dialog == null){
			dialog = new AvatarSelectionDialog();
			dialog.setHandlerSelectedListener(this);
		}
		dialog.show(getFragmentManager(), null);
	}

	private void takePicture(){
		CameraPhotoPreviewAccountActivity.starCameraPhotoPreviewActivity(this);
	}

	private void selectPicture(){
		if(mIsOverJellyBean){
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent, WelfareConst.RequestCode.GALLERY);
		}else{
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			this.startActivityForResult(intent, WelfareConst.RequestCode.GALLERY);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK) return;

		switch(requestCode){
		case WelfareConst.RequestCode.PHOTO_CHOOSE:
			if(data != null){
				String detailMessage = data.getExtras().getString("detail");
				if(WelfareConst.WF_FILE_SIZE_NG.equals(detailMessage)){
					alertDialog.setMessage(getString(trente.asia.welfare.adr.R.string.wf_invalid_photo_over));
					alertDialog.show();
				}else{
					String imagePath = data.getExtras().getString(WelfareConst.IMAGE_PATH_KEY);
					Uri uri = AndroidUtil.getUriFromFileInternal(activity, new File(imagePath));
					cropImage(uri);
				}
			}
			break;
		case WelfareConst.RequestCode.GALLERY:
			Uri uri = null;
			if(data != null){
				uri = data.getData();
				long date = System.currentTimeMillis();
				String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
				String desPath = OLUtils.getFilesFolderPath() + filename;

				mImagePath = WelfareUtil.getImagePath(activity, uri, mIsOverJellyBean, desPath);
				Uri uri1 = AndroidUtil.getUriFromFileInternal(activity, new File(mImagePath));
				cropImage(uri1);
			}else{
				Toast.makeText(activity, "Gallery is error", Toast.LENGTH_LONG).show();
			}
			break;
		case WelfareConst.RequestCode.PHOTO_CROP:
			File imageFile = new File(mImageUri.getPath());
            SettingModel settingModel = prefAccUtil.getSetting();
			if(CCNumberUtil.toLong(settingModel.WF_MAX_FILE_SIZE).compareTo(imageFile.length()) < 0){
				alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
				alertDialog.show();
			}else{
				imgAvatar.setImageURI(mImageUri);
				saveAvatarImageToServer(false);
			}

			break;
		default:
			break;
		}
	}

	private void log(String msg) {
		Log.e("TcAccount", msg);
	}

	private void cropImage(Uri imageUri){
		long date = System.currentTimeMillis();
		String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
		String filePath = OLUtils.getFilesFolderPath() + "/" + filename;
		File imageFile = new File(filePath);
		try {
			imageFile.createNewFile();
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		mImageUri = Uri.fromFile(imageFile);
		WelfareUtil.startCropActivity(this, imageUri, mImageUri);
	}

	private void saveAvatarImageToServer(boolean isDeleted){
		Map<String, File> fileMap = new HashMap<>();
		fileMap.put("avatar", new File(mImageUri.getPath()));

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", myself.key);
		}catch(JSONException ex){
			ex.printStackTrace();
		}

		requestUpload(WfUrlConst.WF_ACC_INFO_UPDATE_AVATAR, jsonObject, fileMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_INFO_UPDATE_AVATAR.equals(url)){
			Toast.makeText(activity, getString(R.string.chiase_common_success_change_avatar), Toast.LENGTH_LONG).show();
			File appFolder = new File(OLUtils.getFilesFolderPath());
			deleteAppFolder(appFolder);
		}else{
			super.successUpload(response, url);
		}
	}

	private void buildSwitch(){
		swtPass = (SwitchCompat)getView().findViewById(R.id.swt_change_password);
		swtPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
				if(isChecked){
					getView().findViewById(R.id.lnr_change_password).setVisibility(View.VISIBLE);
				}else{
					getView().findViewById(R.id.lnr_change_password).setVisibility(View.GONE);
				}
			}
		});

		swtPush = (SwitchCompat)getView().findViewById(R.id.swt_push_notification);

		Button btnSetting = (Button)getView().findViewById(R.id.btn_id_update);
		btnSetting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				saveSetting();
			}
		});
		edtpassword = (EditText)getView().findViewById(R.id.txt_old_password);
		edtnewPassword1 = (EditText)getView().findViewById(R.id.txt_new_password);
		edtnewPassword2 = (EditText)getView().findViewById(R.id.txt_comfirm_password);

		// addTextWatcher(btnSetting, Arrays.asList((View)edtpassword, edtnewPassword1, edtnewPassword2));
	}

	private void saveSetting(){

		JSONObject param = new JSONObject();

		try{
			param.put("key", myself.key);
			param.put("oldPassword", edtpassword.getText().toString());
			param.put("newPassword", edtnewPassword1.getText().toString());
			param.put("confirmPassword", edtnewPassword2.getText().toString());

		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_ACC_INFO_UPDATEINFO, param, true);
	}

	private void saveNoticeFlag(boolean isChecked){

		JSONObject param = new JSONObject();

		try{
			param.put("key", myself.key);
			param.put("changePush", isChecked ? "1" : "0");
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_ACC_INFO_SETTING, param, true);

	}

	@Override
	protected void successUpdate(JSONObject response, String url){

		switch(CCStringUtil.checkNull(url)){
		case WfUrlConst.WF_ACC_INFO_UPDATEINFO:
			Toast.makeText(getContext(), getString(R.string.chiase_common_success_change_password), Toast.LENGTH_SHORT).show();
			swtPass.setChecked(false);
			break;
		case WfUrlConst.WF_ACC_INFO_SETTING:
			Toast.makeText(getContext(), getString(R.string.chiase_common_success_change_push_setting), Toast.LENGTH_SHORT).show();
			break;
		default:
			super.successUpdate(response, url);
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		imgAvatar = null;
	}

	@Override
	public void onAvatarHandlerSelected(int handlerId){
		switch(handlerId){
		case AvatarSelectionDialog.USE_CAMERA:
			if(AndroidUtil.verifyStoragePermissions(activity)){
				takePicture();
			}
			break;
		case AvatarSelectionDialog.USE_GALLERY:
			if(AndroidUtil.verifyStoragePermissions(activity)){
				selectPicture();
			}
			break;
		default:
			break;
		}
	}
}
