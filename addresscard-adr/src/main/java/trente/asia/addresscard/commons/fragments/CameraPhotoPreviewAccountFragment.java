package trente.asia.addresscard.commons.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.commons.defines.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.services.user.ACLoginFragment;
import trente.asia.android.util.CsUtil;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.fragment.AbstractCameraPhotoPreviewFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraPhotoPreviewAccountFragment extends AbstractCameraPhotoPreviewFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_camera_photo_preview, container, false);
		}
		return mRootView;
	}

    @Override
    public void initView(){
        super.initView();
        isCheckMaxSize = true;
    }

    @Override
	protected String makeAppFile(String fileName){
		return CsUtil.makeAppFile(ACConst.APP_FOLDER, fileName);
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
		gotoFragment(new ACLoginFragment());
	}

}
