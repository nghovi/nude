package trente.asia.welfare.adr.fragment;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.util.AndroidUtil;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * AbstractCameraPhotoPreviewFragment
 *
 * @author TrungND
 */
public abstract class AbstractCameraPhotoPreviewFragment extends WelfareFragment{

	protected String	mOriginalPath;
	private boolean		mIsOverJellyBean;
	protected boolean	isCheckMaxSize	= false;

	@Override
	public void initView(){
		super.initView();

		mIsOverJellyBean = AndroidUtil.isBuildOver(18);
		if(activity.getIntent().getExtras().getInt(WelfareConst.Extras.TYPE_OF_PHOTO_INTENT) == WelfareConst.PhotoIntents.GALLERY){
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
		}else{
			try{
				startCamera();
			}catch(Exception ex){
				ex.printStackTrace();
				Toast.makeText(activity, "Init camera error", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void startCamera(){

		// Check if camera exists
		if(!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) && !activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){

			Toast.makeText(activity, "Init camera error", Toast.LENGTH_LONG).show();
		}else{
			try{
				long date = System.currentTimeMillis();
				String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
				mOriginalPath = makeAppFile(filename);

				File file = new File(mOriginalPath);
				Uri outputFileUri = AndroidUtil.getUriFromFile(activity, file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(intent, WelfareConst.RequestCode.CAMERA);

			}catch(Exception ex){
				ex.printStackTrace();
				Toast.makeText(activity, "Init camera error", Toast.LENGTH_LONG).show();
			}
		}
	}

	abstract protected String makeAppFile(String fileName);

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
			File file = new File(mOriginalPath);
			// Toast.makeText(activity, "File path: " + mOriginalPath, Toast.LENGTH_LONG).show();
			boolean exists = file.exists();
			if(exists){
				onPhotoTaken(mOriginalPath);
			}else{
				Toast.makeText(activity, "Camera is error", Toast.LENGTH_LONG).show();
			}
			break;
		case WelfareConst.RequestCode.GALLERY:
			Uri uri = null;
			if(returnedIntent != null){
				uri = returnedIntent.getData();

				long date = System.currentTimeMillis();
				String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
				String desPath = makeAppFile(filename);

				mOriginalPath = WelfareUtil.getImagePath(activity, uri, mIsOverJellyBean, desPath);
				File originalFile = new File(mOriginalPath);
				if(isCheckMaxSize){
                    SettingModel settingModel = prefAccUtil.getSetting();
					Log.e("Photo Preview", "MAX_SIZE = " + settingModel.WF_MAX_FILE_SIZE);
					if(settingModel.WF_MAX_FILE_SIZE != null &&
							CCNumberUtil.toLong(settingModel.WF_MAX_FILE_SIZE).compareTo(originalFile.length()) < 0){
						Intent intent = activity.getIntent();
						intent.putExtra("detail", WelfareConst.WF_FILE_SIZE_NG);
						activity.setResult(Activity.RESULT_OK, intent);
						activity.finish();
					}else{
						onPhotoTaken(mOriginalPath);
					}
				}else{
					onPhotoTaken(mOriginalPath);
				}
			}else{
				Toast.makeText(activity, "Gallery is error", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}

	protected void onPhotoTaken(String imagePath){

		long date = System.currentTimeMillis();
		String filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;

		String desFilePath = makeAppFile(filename);
		File resizeFile = AndroidUtil.resizeFile(activity, new File(imagePath), desFilePath);
		mOriginalPath = resizeFile.getPath();

		// Uri uriPreview = Uri.fromFile(resizeFile);
		// mImgPreview.setImageURI(uriPreview);
	}

	@Override
	protected void onClickBackBtn(){
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
