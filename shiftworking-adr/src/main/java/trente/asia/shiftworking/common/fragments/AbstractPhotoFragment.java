package nguyenhoangviet.vpcorp.shiftworking.common.fragments;

import android.view.View;

import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.activities.CameraPhotoPreviewAccountActivity;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuButtonsListener;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuManageListener;
import nguyenhoangviet.vpcorp.welfare.adr.services.user.menu.AccountMenuManager;

public abstract class AbstractPhotoFragment extends AbstractSwFragment{

	protected AccountMenuManager	menuManager;
	protected View					mViewForMenuBehind;
	protected String				mOriginalPath;

	protected OnMenuManageListener	onMenuManagerListener	= new OnMenuManageListener() {

																@Override
																public void onMenuOpened(){
																}

																@Override
																public void onMenuClosed(){
																	mViewForMenuBehind.setVisibility(View.GONE);
																}
															};

	protected OnMenuButtonsListener	onMenuButtonsListener	= new OnMenuButtonsListener() {

																@Override
																public void onCameraClicked(){
																	eventCameraClicked();
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onAudioClicked(){
																}

																@Override
																public void onFileClicked(){
																}

																@Override
																public void onVideoClicked(){
																}

																@Override
																public void onLocationClicked(){
																}

																@Override
																public void onGalleryClicked(){
																	eventGalleryClicked();
																	onButtonMenuOpenedClicked();
																}

																@Override
																public void onContactClicked(){
																}
															};

	@Override
	public void initView(){
		super.initView();

		menuManager = new AccountMenuManager();
		menuManager.setMenuLayout(activity, nguyenhoangviet.vpcorp.welfare.adr.R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);
		mViewForMenuBehind = getView().findViewById(R.id.viewForMenuBehind);
		mViewForMenuBehind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onButtonMenuOpenedClicked();
			}
		});
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	protected void eventCameraClicked(){
		CameraPhotoPreviewAccountActivity.starCameraPhotoPreviewActivity(this);
	}

	protected void eventGalleryClicked(){
		CameraPhotoPreviewAccountActivity.starCameraFromGalleryPhotoPreviewActivity(this);
	}

	protected void onButtonMenuOpenedClicked(){
		if(mViewForMenuBehind.getVisibility() == View.GONE){
			return;
		}
		menuManager.closeMenu();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}
}
