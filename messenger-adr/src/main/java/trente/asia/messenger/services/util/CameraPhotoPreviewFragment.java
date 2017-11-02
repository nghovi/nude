package nguyenhoangviet.vpcorp.messenger.services.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.messenger.BuildConfig;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.commons.defines.MsConst;
import nguyenhoangviet.vpcorp.messenger.commons.utils.MsUtils;
import nguyenhoangviet.vpcorp.messenger.services.user.MsgLoginFragment;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.fragment.AbstractCameraPhotoPreviewFragment;

/**
 * AbstractCameraPhotoPreviewFragment
 *
 * @author TrungND
 */
public class CameraPhotoPreviewFragment extends AbstractCameraPhotoPreviewFragment implements View.OnClickListener{

	private ImageView		mImgPreview;
	private String			activeBoardId;
	private RelativeLayout	mRltPreview;
	private LinearLayout	mLnrFile;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_camera_photo_preview, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		initHeader(R.drawable.wf_back_white, getString(R.string.msg_preview_title), null);

		mImgPreview = (ImageView)getView().findViewById(R.id.img_id_preview);
		mRltPreview = (RelativeLayout)getView().findViewById(R.id.rlt_id_preview);
		mLnrFile = (LinearLayout)getView().findViewById(R.id.lnr_id_file);

		LinearLayout lnrOk = (LinearLayout)getView().findViewById(R.id.lnr_id_ok);
		LinearLayout lnrCancel = (LinearLayout)getView().findViewById(R.id.lnr_id_cancel);
		lnrOk.setOnClickListener(this);
		lnrCancel.setOnClickListener(this);

		activeBoardId = activity.getIntent().getExtras().getString(WelfareConst.Extras.ACTIVE_BOARD_ID);
		host = BuildConfig.HOST;
		isCheckMaxSize = true;
		super.initView();
	}

	protected String makeAppFile(String fileName){
		String filePath = MsUtils.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}

	@Override
	protected void onPhotoTaken(String imagePath){

		super.onPhotoTaken(imagePath);

		File originalFile = new File(mOriginalPath);
		if(originalFile != null && originalFile.exists()){
			if(originalFile.length() > WelfareConst.MAX_FILE_SIZE_2MB){
				mRltPreview.setVisibility(View.GONE);
				mLnrFile.setVisibility(View.VISIBLE);

				TextView txtFileName = (TextView)getView().findViewById(R.id.txt_id_file_name);
				txtFileName.setText(originalFile.getName());
			}else{
				mRltPreview.setVisibility(View.VISIBLE);
				mLnrFile.setVisibility(View.GONE);
				Uri uriPreview = AndroidUtil.getUriFromFile(activity, new File(mOriginalPath));
				mImgPreview.setImageURI(uriPreview);
			}
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_id_ok:
			uploadPhoto();
			break;
		case R.id.lnr_id_cancel:
			File appFolder = new File(MsUtils.getFilesFolderPath());
			deleteAppFolder(appFolder);
			activity.setResult(Activity.RESULT_CANCELED);
			activity.finish();
			break;
		default:
			break;
		}
	}

	private void uploadPhoto(){
		Map<String, File> fileMap = new HashMap<>();
		File originalFile = new File(mOriginalPath);
		Log.e("CameraFragment", "mOriginalPath = " + mOriginalPath);
		fileMap.put("messageFile", originalFile);
//		 fileMap.put("thumbnailFile", originalFile);

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeBoardId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		Log.e("CameraFragment", "activeBoardId = " + activeBoardId);
		requestUpload(MsConst.API_MESSAGE_UPDATE, jsonObject, fileMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		Log.e("CameraFragment", "successUpload");
		if(MsConst.API_MESSAGE_UPDATE.equals(url)){
			File appFolder = new File(MsUtils.getFilesFolderPath());
			deleteAppFolder(appFolder);
			Intent intent = activity.getIntent();
			intent.putExtra("detail", response.optString("detail"));
			activity.setResult(Activity.RESULT_OK, intent);
			activity.finish();
		}else{
			super.successUpload(response, url);
		}
	}

	@Override
	protected String getServiceCd() {
		return WelfareConst.SERVICE_CD_MS;
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		Log.e("CameraFragment", "gotoSignIn");
		gotoFragment(new MsgLoginFragment());
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		mImgPreview = null;
		mRltPreview = null;
		mLnrFile = null;
	}
}
