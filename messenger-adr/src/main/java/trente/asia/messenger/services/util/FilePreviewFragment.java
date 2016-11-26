package trente.asia.messenger.services.util;

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
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import trente.asia.android.define.CAConst;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.commons.utils.MsUtils;
import trente.asia.messenger.services.user.MsgLoginFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.fragment.AbstractFilePreviewFragment;

/**
 * AbstractRecorderVideoFragment
 *
 * @author TrungND
 */
public class FilePreviewFragment extends AbstractFilePreviewFragment implements View.OnClickListener{

	private String activeBoardId;
	// private String mItemFileType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_file_preview, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		initHeader(R.drawable.wf_back_white, getString(R.string.msg_preview_title), null);

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

	@Override
	protected void onFileSelected(int resultOk, String filePath, String fileName){
		super.onFileSelected(resultOk, filePath, fileName);
		if(resultOk == Activity.RESULT_OK){
			TextView txtFileName = (TextView)getView().findViewById(R.id.txt_id_file_name);
			txtFileName.setText(fileName);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_id_ok:
			uploadFile();
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

	private void uploadFile(){
		Map<String, File> fileMap = new HashMap<>();
		File originalFile = new File(mOriginalPath);
		fileMap.put("messageFile", originalFile);
		// get thumbnail file
		if(WelfareConst.ITEM_FILE_TYPE_PHOTO.equals(mItemFileType)){
			// fileMap.put("thumbnailFile", originalFile);
		}else if(WelfareConst.ITEM_FILE_TYPE_MOVIE.equals(mItemFileType)){
			// get thumbnail image
			Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mOriginalPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
			// save thumbnail to file
			OutputStream outStream = null;
			try{
				String thumbnailPath = MsUtils.getFilesFolderPath() + "/" + "thumbnail.jpg";
				File thumbnailFile = new File(thumbnailPath);
				outStream = new FileOutputStream(thumbnailFile);
				bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				fileMap.put("thumbnailFile", thumbnailFile);
			}catch(IOException ex){
				Log.e(CAConst.ROOT, ex.toString());
			}
		}

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeBoardId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpload(WfUrlConst.WF_MSG_0004, jsonObject, fileMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		if(WfUrlConst.WF_MSG_0004.equals(url)){
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
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new MsgLoginFragment());
	}
}
