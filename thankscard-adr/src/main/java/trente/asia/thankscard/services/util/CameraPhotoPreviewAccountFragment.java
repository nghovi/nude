package nguyenhoangviet.vpcorp.thankscard.services.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.utils.TCUtil;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.fragment.AbstractCameraPhotoPreviewFragment;

/**
 * CameraPhotoPreviewAccountFragment
 *
 * @author TrungND
 */
public class CameraPhotoPreviewAccountFragment extends AbstractCameraPhotoPreviewFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_camera_photo_preview, container, false);
		}
		return mRootView;
	}

	protected String makeAppFile(String fileName){
		String filePath = TCUtil.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}

	@Override
	protected void onPhotoTaken(String imagePath){

		super.onPhotoTaken(imagePath);

		Intent intent = activity.getIntent();
		intent.putExtra(WelfareConst.IMAGE_PATH_KEY, mOriginalPath);
		activity.setResult(Activity.RESULT_OK, intent);
		activity.finish();
	}

	@Override
	protected void gotoSignIn(){
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}
}
