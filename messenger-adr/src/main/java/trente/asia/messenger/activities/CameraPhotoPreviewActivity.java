package nguyenhoangviet.vpcorp.messenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.services.util.CameraPhotoPreviewFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

public class CameraPhotoPreviewActivity extends WelfareActivity{

	/**
	 * start preview for image from gallery
	 * 
	 * @param fragment
	 */
	public static void starCameraFromGalleryPhotoPreviewActivity(Fragment fragment, int activeBoardId){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), CameraPhotoPreviewActivity.class);
			intent.putExtra(WelfareConst.Extras.TYPE_OF_PHOTO_INTENT, WelfareConst.PhotoIntents.GALLERY);
			intent.putExtra(WelfareConst.Extras.ACTIVE_BOARD_ID, activeBoardId + "");
			fragment.startActivityForResult(intent, WelfareConst.RequestCode.PHOTO_CHOOSE);
		}
	}

	/**
	 * start preview for image from camera
	 * 
	 * @param fragment
	 */
	public static void starCameraPhotoPreviewActivity(Fragment fragment, int activeBoardId){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), CameraPhotoPreviewActivity.class);
			intent.putExtra(WelfareConst.Extras.TYPE_OF_PHOTO_INTENT, WelfareConst.PhotoIntents.CAMERA);
			intent.putExtra(WelfareConst.Extras.ACTIVE_BOARD_ID, activeBoardId + "");
			fragment.startActivityForResult(intent, WelfareConst.RequestCode.PHOTO_CHOOSE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);

		addFragment(new CameraPhotoPreviewFragment());
	}
}
