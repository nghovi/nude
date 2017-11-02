package nguyenhoangviet.vpcorp.messenger.services.user;

import android.os.Bundle;

import nguyenhoangviet.vpcorp.messenger.BuildConfig;
import nguyenhoangviet.vpcorp.messenger.activities.CameraPhotoPreviewAccountActivity;
import nguyenhoangviet.vpcorp.messenger.commons.utils.MsUtils;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.services.user.AccountInfoFragment;

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
