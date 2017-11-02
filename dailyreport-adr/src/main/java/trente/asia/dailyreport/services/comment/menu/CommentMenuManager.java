package nguyenhoangviet.vpcorp.dailyreport.services.comment.menu;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.welfare.adr.R;
import nguyenhoangviet.vpcorp.welfare.adr.animators.SupportAnimator;
import nguyenhoangviet.vpcorp.welfare.adr.animators.ViewAnimationUtils;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuButtonsListener;
import nguyenhoangviet.vpcorp.welfare.adr.menu.OnMenuManageListener;
import nguyenhoangviet.vpcorp.welfare.adr.utils.AnimUtils;

/**
 * CommentMenuManager
 *
 * @author TrungND
 */
public class CommentMenuManager{

	private LinearLayout			lnrMenuMain;
	private SupportAnimator			menuAnimator;
	private LinearLayout			camera;
	private LinearLayout			gallery;
	private LinearLayout			file;
	private LinearLayout			video;

	private OnMenuManageListener	listener;
	private OnMenuButtonsListener	buttonsListener;
	private boolean					isShowFileOption	= true;
	private Activity				mActivity;

	public void setMenuLayout(Activity activity, int menuLayoutId, OnMenuManageListener listener, final OnMenuButtonsListener buttonsListener){
		this.mActivity = activity;

		lnrMenuMain = (LinearLayout)activity.findViewById(menuLayoutId);
		// location = (LinearLayout)activity.findViewById(R.id.lnr_id_location);
		camera = (LinearLayout)activity.findViewById(R.id.lnr_id_camera);
		gallery = (LinearLayout)activity.findViewById(R.id.lnr_id_gallery);
		// audio = (LinearLayout) activity.findViewById(R.id.audio);
		video = (LinearLayout)activity.findViewById(R.id.lnr_id_video);
		// contact = (LinearLayout) activity.findViewById(R.id.contact);

		this.listener = listener;
		this.buttonsListener = buttonsListener;

		// location.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onGraphIconClicked(View v){
		// buttonsListener.onLocationClicked();
		// }
		// });

		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				buttonsListener.onCameraClicked();
			}
		});

		gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				buttonsListener.onGalleryClicked();
			}
		});

		file = (LinearLayout)activity.findViewById(R.id.lnr_id_file);
		file.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(AndroidUtil.verifyStoragePermissions(mActivity)){
					buttonsListener.onFileClicked();
				}
			}
		});

		video.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(AndroidUtil.verifyStoragePermissions(mActivity)){
					buttonsListener.onVideoClicked();
				}
			}
		});
	}

	public void openMenu(View btnSend){

		((View)lnrMenuMain.getParent().getParent()).setVisibility(View.VISIBLE);

		// get the center for the clipping circle
		int cx = btnSend.getLeft();
		int cy = lnrMenuMain.getBottom();

		// get the final radius for the clipping circle
		int finalRadius = Math.max(lnrMenuMain.getWidth(), lnrMenuMain.getHeight());

		menuAnimator = ViewAnimationUtils.createCircularReveal(lnrMenuMain, cx, cy, 0, finalRadius + 300);
		menuAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		menuAnimator.setDuration(WelfareConst.MENU_LAYOUT_ANIMATION_DURATION);
		menuAnimator.addListener(new SupportAnimator.AnimatorListener() {

			@Override
			public void onAnimationStart(){
			}

			@Override
			public void onAnimationEnd(){
				listener.onMenuOpened();
			}

			@Override
			public void onAnimationCancel(){
			}

			@Override
			public void onAnimationRepeat(){
			}
		});
		menuAnimator.start();

		handleButtonsOnOpen();

	}

	protected void handleButtonsOnOpen(){

		int start = 150;
		int offset = 50;

		// singleButtonAnimationOn(audio, start + 0 * offset);
		// singleButtonAnimationOn(contact, start + 1 * offset);
		// singleButtonAnimationOn(location, start + 2 * offset);
		// singleButtonAnimationOn(video, start + 3 * offset);
		// singleButtonAnimationOn(gallery, start + 4 * offset);
		// singleButtonAnimationOn(file, start + 5 * offset);
		// singleButtonAnimationOn(camera, start + 6 * offset);

	}

	protected void singleButtonAnimationOn(final View view, int offset){
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run(){
				view.setVisibility(View.VISIBLE);
				AnimUtils.scale(view, 0.6f, 1, WelfareConst.MENU_BUTTON_ANIMATION_DURATION, null);
			}
		}, offset);
	}

	public void closeMenu(){

		if(menuAnimator == null){
			return;
		}

		menuAnimator = menuAnimator.reverse();
		if(menuAnimator != null){
			menuAnimator.addListener(new SupportAnimator.AnimatorListener() {

				@Override
				public void onAnimationStart(){
				}

				@Override
				public void onAnimationEnd(){
					listener.onMenuClosed();
					((View)lnrMenuMain.getParent().getParent()).setVisibility(View.GONE);
				}

				@Override
				public void onAnimationCancel(){
				}

				@Override
				public void onAnimationRepeat(){
				}
			});

			menuAnimator.start();
		}

		handleButtonsOnClose();

	}

	protected void handleButtonsOnClose(){

		// singleButtonAnimationOff(audio, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
		// singleButtonAnimationOff(location,
		// Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
		// singleButtonAnimationOff(video, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
		// singleButtonAnimationOff(gallery,
		// Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
		// singleButtonAnimationOff(contact,
		// Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
		// singleButtonAnimationOff(camera, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);
		// singleButtonAnimationOff(file, Const.AnimationDuration.MENU_LAYOUT_ANIMATION_DURATION);

	}

	protected void singleButtonAnimationOff(final View view, int offset){
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run(){
				view.setVisibility(View.INVISIBLE);
			}
		}, offset);
	}

	public void hideFileOption(){
		isShowFileOption = false;
	}
}
