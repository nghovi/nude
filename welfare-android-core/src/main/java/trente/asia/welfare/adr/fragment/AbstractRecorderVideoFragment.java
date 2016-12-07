package trente.asia.welfare.adr.fragment;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.util.AndroidUtil;
import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.SettingModel;

/**
 * AbstractRecorderVideoFragment
 *
 * @author TrungND
 */
public abstract class AbstractRecorderVideoFragment extends WelfareFragment{

	protected String mOriginalPath;

	@Override
	public void initView(){
		super.initView();

		openCamera();
	}

	abstract protected String makeAppFile(String fileName);

	/**
	 * open camera to record video
	 */
	private void openCamera(){

		if(!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) && !activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
			Toast.makeText(activity, "Init camera error", Toast.LENGTH_LONG).show();
		}else{

			try{
				Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, WelfareConst.Video.MAX_RECORDING_VIDEO_TIME);

				long date = System.currentTimeMillis();
				String filename = WelfareConst.FilesName.VIDEO_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.VIDEO_TEMP_FILE_EXT;
				mOriginalPath = makeAppFile(filename);
				File video = new File(mOriginalPath);
				Uri uriSavedVideo = AndroidUtil.getUriFromFile(activity, video);

				// String filePath = video.getPath();

				// but on older devices don't work with EXTRA_OUTPUT
				if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedVideo);
				}

				startActivityForResult(cameraIntent, WelfareConst.RequestCode.CAMERA);
			}catch(Exception ex){
				ex.printStackTrace();
				Toast.makeText(activity, "Init camera error", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
		super.onActivityResult(requestCode, resultCode, returnedIntent);

		if(resultCode == Activity.RESULT_CANCELED){
			activity.setResult(Activity.RESULT_CANCELED);
			activity.finish();
			return;
		}

		if(resultCode != Activity.RESULT_OK) return;
		switch(requestCode){
		case WelfareConst.RequestCode.CAMERA:
			Uri selectedVideoUri = returnedIntent.getData();
			String abc = returnedIntent.getData().getPath();

			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
				mOriginalPath = AndroidUtil.getVideoPath(activity, selectedVideoUri);
			}else{
				if(selectedVideoUri.getScheme().equals("content") || selectedVideoUri.getScheme().equals("file")){
					mOriginalPath = AndroidUtil.getVideoPath(activity, selectedVideoUri);
				}else{
					mOriginalPath = selectedVideoUri.toString();
				}
			}

			File originalFile = new File(mOriginalPath);
            SettingModel settingModel = prefAccUtil.getSetting();
			if(CCNumberUtil.toLong(settingModel.WF_MAX_FILE_SIZE).compareTo(originalFile.length()) < 0){
				Intent intent = activity.getIntent();
				intent.putExtra("detail", WelfareConst.WF_FILE_SIZE_NG);
				activity.setResult(Activity.RESULT_OK, intent);
				activity.finish();
			}else{
				// get thumbnail image
				Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mOriginalPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
				if(bmThumbnail == null){
					bmThumbnail = AndroidUtil.createBitmapFromResouce(getResources(), R.drawable.thumbnail_failure);
				}
				successRecord(bmThumbnail);
			}
			break;
		default:
			break;
		}
	}

	abstract protected void successRecord(Bitmap bmThumbnail);

	@Override
	protected void onClickBackBtn(){
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}
}
