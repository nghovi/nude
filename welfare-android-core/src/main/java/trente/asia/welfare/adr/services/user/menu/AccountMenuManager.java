package trente.asia.welfare.adr.services.user.menu;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.animators.SupportAnimator;
import trente.asia.welfare.adr.animators.ViewAnimationUtils;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.menu.OnMenuButtonsListener;
import trente.asia.welfare.adr.menu.OnMenuManageListener;
import trente.asia.welfare.adr.utils.AnimUtils;

/**
 * AccountMenuManager
 *
 * @author TrungND
 */
public class AccountMenuManager{

	private LinearLayout			lnrMenuMain;
	private SupportAnimator			menuAnimator;
	private LinearLayout			camera;
	private LinearLayout			gallery;

	private OnMenuManageListener	listener;
	private OnMenuButtonsListener	buttonsListener;
	private boolean					isShowFileOption	= true;

	public void setMenuLayout(Activity activity, int menuLayoutId, OnMenuManageListener listener, final OnMenuButtonsListener buttonsListener){

		lnrMenuMain = (LinearLayout)activity.findViewById(menuLayoutId);
		camera = (LinearLayout)activity.findViewById(R.id.lnr_id_camera);
		gallery = (LinearLayout)activity.findViewById(R.id.lnr_id_gallery);

		this.listener = listener;
		this.buttonsListener = buttonsListener;

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
}
