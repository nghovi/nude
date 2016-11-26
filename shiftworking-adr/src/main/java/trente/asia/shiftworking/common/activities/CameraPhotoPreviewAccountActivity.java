package trente.asia.shiftworking.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.CameraPhotoPreviewAccountFragment;
import trente.asia.android.util.AndroidUtil;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * Created by PC-Trente on 9/30/2016.
 */

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
