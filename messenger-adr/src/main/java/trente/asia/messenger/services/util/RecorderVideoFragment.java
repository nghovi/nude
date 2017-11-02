package nguyenhoangviet.vpcorp.messenger.services.util;

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
import nguyenhoangviet.vpcorp.messenger.BuildConfig;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.commons.defines.MsConst;
import nguyenhoangviet.vpcorp.messenger.commons.utils.MsUtils;
import nguyenhoangviet.vpcorp.messenger.services.user.MsgLoginFragment;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.fragment.AbstractRecorderVideoFragment;

/**
 * AbstractRecorderVideoFragment
 *
 * @author TrungND
 */
public class RecorderVideoFragment extends AbstractRecorderVideoFragment implements View.OnClickListener{

	private ImageView	mImgPreview;
	private File		mThumbnailFile;
	private String		activeBoardId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_recorder_video, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		initHeader(R.drawable.wf_back_white, getString(R.string.msg_preview_title), null);

		mImgPreview = (ImageView)getView().findViewById(R.id.img_id_preview);
		activeBoardId = activity.getIntent().getExtras().getString(WelfareConst.Extras.ACTIVE_BOARD_ID);
		LinearLayout lnrOk = (LinearLayout)getView().findViewById(R.id.lnr_id_ok);
		LinearLayout lnrCancel = (LinearLayout)getView().findViewById(R.id.lnr_id_cancel);
		lnrOk.setOnClickListener(this);
		lnrCancel.setOnClickListener(this);

		activeBoardId = activity.getIntent().getExtras().getString(WelfareConst.Extras.ACTIVE_BOARD_ID);
		host = BuildConfig.HOST;
		super.initView();
	}

	protected String makeAppFile(String fileName){
		String filePath = MsUtils.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}

	protected void successRecord(Bitmap bmThumbnail){
		mImgPreview.setImageBitmap(bmThumbnail);
		// save thumbnail to file
		OutputStream outStream = null;
		try{
			String thumbnailPath = MsUtils.getFilesFolderPath() + "/" + "thumbnail.jpg";
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
		fileMap.put("messageFile", originalFile);
		fileMap.put("thumbnailFile", mThumbnailFile);

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeBoardId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpload(MsConst.API_MESSAGE_UPDATE, jsonObject, fileMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
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
	protected void onClickBackBtn(){
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new MsgLoginFragment());
	}

	@Override
	protected String getServiceCd() {
		return WelfareConst.SERVICE_CD_MS;
	}
}
