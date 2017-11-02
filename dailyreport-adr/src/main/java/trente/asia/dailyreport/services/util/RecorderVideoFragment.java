package nguyenhoangviet.vpcorp.dailyreport.services.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import nguyenhoangviet.vpcorp.android.define.CsConst;
import nguyenhoangviet.vpcorp.dailyreport.BuildConfig;
import nguyenhoangviet.vpcorp.dailyreport.DRConst;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.services.user.DrLoginFragment;
import nguyenhoangviet.vpcorp.dailyreport.utils.DRUtil;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.fragment.AbstractRecorderVideoFragment;

/**
 * RecorderVideoFragment
 *
 * @author TrungND
 */
public class RecorderVideoFragment extends AbstractRecorderVideoFragment implements View.OnClickListener{

	private ImageView	mImgPreview;
	private File		mThumbnailFile;
	private String		activeReportId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_recorder_video, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		initHeader(R.drawable.wf_back_white, "Preview", null);

		mImgPreview = (ImageView)getView().findViewById(R.id.img_id_preview);
		activeReportId = activity.getIntent().getExtras().getString(WelfareConst.Extras.ACTIVE_REPORT_ID);
		LinearLayout lnrOk = (LinearLayout)getView().findViewById(R.id.lnr_id_ok);
		LinearLayout lnrCancel = (LinearLayout)getView().findViewById(R.id.lnr_id_cancel);
		lnrOk.setOnClickListener(this);
		lnrCancel.setOnClickListener(this);

		host = BuildConfig.HOST;
		super.initView();
	}

	protected String makeAppFile(String fileName){
		String filePath = DRUtil.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}

	protected void successRecord(Bitmap bmThumbnail){
		mImgPreview.setImageBitmap(bmThumbnail);
		// save thumbnail to file
		OutputStream outStream = null;
		try{
			String thumbnailPath = DRUtil.getFilesFolderPath() + "/" + "thumbnail.jpg";
			mThumbnailFile = new File(thumbnailPath);
			outStream = new FileOutputStream(mThumbnailFile);
			bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
		}catch(IOException ex){
			Log.e(CsConst.ROOT, ex.toString());
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_id_ok:
			uploadPhoto();
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

	private void uploadPhoto(){
		Map<String, File> fileMap = new HashMap<>();
		File originalFile = new File(mOriginalPath);
		fileMap.put("commentFile", originalFile);
		fileMap.put("thumbnailFile", mThumbnailFile);

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("parentKey", activeReportId);
			jsonObject.put("key", null);
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
	protected void onClickBackBtn(){
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}
}
