package trente.asia.dailyreport.services.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import trente.asia.android.util.AndroidUtil;
import trente.asia.dailyreport.BuildConfig;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.user.DrLoginFragment;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.fragment.AbstractCameraPhotoPreviewFragment;

/**
 * AbstractCameraPhotoPreviewFragment
 *
 * @author TrungND
 */
public class CameraPhotoPreviewFragment extends AbstractCameraPhotoPreviewFragment implements View.OnClickListener{

	private ImageView	mImgPreview;
	private String		activeReportId;
	// private String commentContent;
	boolean				uploadImageAfterCrop	= true;

	public void setUploadFlag(boolean isUpload){
		uploadImageAfterCrop = isUpload;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_camera_photo_preview, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		initHeader(R.drawable.wf_back_white, getString(R.string.dr_preview_title), null);

		mImgPreview = (ImageView)getView().findViewById(R.id.img_id_preview);
		LinearLayout lnrOk = (LinearLayout)getView().findViewById(R.id.lnr_id_ok);
		LinearLayout lnrCancel = (LinearLayout)getView().findViewById(R.id.lnr_id_cancel);
		lnrOk.setOnClickListener(this);
		lnrCancel.setOnClickListener(this);

		activeReportId = activity.getIntent().getExtras().getString(WelfareConst.Extras.ACTIVE_REPORT_ID);
		// commentContent = activity.getIntent().getExtras().getString(WelfareConst.Extras.COMMENT_CONTENT);
		host = BuildConfig.HOST;
		isCheckMaxSize = true;
		super.initView();
	}

	protected String makeAppFile(String fileName){
		String filePath = DRUtil.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}

	@Override
	protected void onPhotoTaken(String imagePath){

		super.onPhotoTaken(imagePath);

		Uri uriPreview = AndroidUtil.getUriFromFile(activity, new File(mOriginalPath));
		mImgPreview.setImageURI(uriPreview);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_id_ok:
			handlePhotoAfterCrop();
			break;
		case R.id.lnr_id_cancel:
			File appFolder = new File(DRUtil.getFilesFolderPath());
			deleteAppFolder(appFolder);
			activity.setResult(Activity.RESULT_CANCELED);
			activity.finish();
			break;
		default:
			break;
		}
	}

	private void handlePhotoAfterCrop(){
		if(uploadImageAfterCrop){
			uploadPhoto();
		}else{
			Intent intent = activity.getIntent();
			intent.putExtra("detail", mOriginalPath);
			activity.setResult(Activity.RESULT_OK, intent);
			activity.finish();
		}
	}

	private void uploadPhoto(){
		Map<String, File> fileMap = new HashMap<>();
		File originalFile = new File(mOriginalPath);
		fileMap.put("commentFile", originalFile);
		fileMap.put("thumbnailFile", originalFile);

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("parentKey", activeReportId);
			jsonObject.put("key", null);
			// jsonObject.put("commentContent", commentContent);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpload(DRConst.API_REPORT_COMMENT, jsonObject, fileMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		if(DRConst.API_REPORT_COMMENT.equals(url)){
			File appFolder = new File(DRUtil.getFilesFolderPath());
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
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new DrLoginFragment());
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		mImgPreview = null;
	}
}
