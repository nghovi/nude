package trente.asia.dailyreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import trente.asia.android.util.AndroidUtil;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.util.CameraPhotoPreviewAccountFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;

public class CameraPhotoPreviewAccountActivity extends WelfareActivity{

	/**
	 * start preview for image from gallery
	 * 
	 * @param fragment
	 */

	public static void starCameraFromGalleryPhotoPreviewActivity(Fragment fragment){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), CameraPhotoPreviewAccountActivity.class);
			intent.putExtra(WelfareConst.Extras.TYPE_OF_PHOTO_INTENT, WelfareConst.PhotoIntents.GALLERY);
			fragment.startActivityForResult(intent, WelfareConst.RequestCode.PHOTO_CHOOSE);
		}
	}

	/**
	 * start preview for image from camera
	 * 
	 * @param fragment
	 */
	public static void starCameraPhotoPreviewActivity(Fragment fragment){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), CameraPhotoPreviewAccountActivity.class);
			intent.putExtra(WelfareConst.Extras.TYPE_OF_PHOTO_INTENT, WelfareConst.PhotoIntents.CAMERA);
			fragment.startActivityForResult(intent, WelfareConst.RequestCode.PHOTO_CHOOSE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		addFragment(new CameraPhotoPreviewAccountFragment());
	}
}
