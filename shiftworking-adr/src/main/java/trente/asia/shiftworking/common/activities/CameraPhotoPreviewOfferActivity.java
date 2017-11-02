package nguyenhoangviet.vpcorp.shiftworking.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.CameraPhotoPreviewAccountFragment;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

/**
 * CameraPhotoPreviewOfferActivity
 *
 * @author TrungND
 */

public class CameraPhotoPreviewOfferActivity extends WelfareActivity{

	/**
	 * start preview for image from gallery
	 *
	 * @param fragment
	 */

	public static void starCameraFromGalleryPhotoPreviewActivity(Fragment fragment){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), CameraPhotoPreviewOfferActivity.class);
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
			Intent intent = new Intent(fragment.getActivity(), CameraPhotoPreviewOfferActivity.class);
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
