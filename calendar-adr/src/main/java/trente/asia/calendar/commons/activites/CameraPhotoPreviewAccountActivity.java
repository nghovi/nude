package nguyenhoangviet.vpcorp.calendar.commons.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.CameraPhotoPreviewAccountFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

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
