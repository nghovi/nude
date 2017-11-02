package nguyenhoangviet.vpcorp.dailyreport.services.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.services.user.DrLoginFragment;
import nguyenhoangviet.vpcorp.dailyreport.utils.DRUtil;
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
		String filePath = DRUtil.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}

	@Override
	protected void onPhotoTaken(String imagePath){

		super.onPhotoTaken(imagePath);

		// Uri uriPreview = Uri.fromFile(new File(mOriginalPath));
		// mImgPreview.setImageURI(uriPreview);
		Intent intent = activity.getIntent();
		intent.putExtra(WelfareConst.IMAGE_PATH_KEY, mOriginalPath);
		activity.setResult(Activity.RESULT_OK, intent);
		activity.finish();
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new DrLoginFragment());
	}
}
