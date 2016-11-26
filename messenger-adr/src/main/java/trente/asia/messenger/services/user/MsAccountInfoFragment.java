package trente.asia.messenger.services.user;

import android.os.Bundle;

import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.activities.CameraPhotoPreviewAccountActivity;
import trente.asia.messenger.commons.utils.MsUtils;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.services.user.AccountInfoFragment;

/**
 * MsAccountInfoFragment
 *
 * @author TrungND
 */
public class MsAccountInfoFragment extends AccountInfoFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();
	}

	protected String makeAppFile(String fileName){
		String filePath = MsUtils.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}

	protected void eventCameraClicked(){
		CameraPhotoPreviewAccountActivity.starCameraPhotoPreviewActivity(MsAccountInfoFragment.this);
	}

	protected void eventGalleryClicked(){
		CameraPhotoPreviewAccountActivity.starCameraFromGalleryPhotoPreviewActivity(MsAccountInfoFragment.this);
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new MsgLoginFragment());
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_MS;
	}
}
