package nguyenhoangviet.vpcorp.android.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import nguyenhoangviet.vpcorp.android.R;

/**
 * LoadingDialog
 */
public class ChiaseLoadingDialog extends Dialog{

	private AnimationDrawable	mFrameAnimation;

	public Boolean				continueShowing	= false;

	public ChiaseLoadingDialog(Context context){
		super(context);

		View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.setContentView(view);
		this.setCancelable(false);

		// this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		// this.setContentView(R.layout.custom_progress_dialog);
		// this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// this.setCancelable(false);
		// ImageView img = (ImageView)this.findViewById(R.id.img_id_loading);
		// mFrameAnimation = (AnimationDrawable)img.getBackground();
		// this.setOnShowListener(new OnShowListener() {
		//
		// @Override
		// public void onShow(DialogInterface dialog){
		// mFrameAnimation.start();
		// }
		// });
	}

	public ChiaseLoadingDialog(Context context, int theme){
		super(context, theme);
	}

	@Override
	public void show(){
		super.show();
	}

	@Override
	public void dismiss(){
		if(mFrameAnimation != null && mFrameAnimation.isRunning()){
			mFrameAnimation.stop();
		}
		super.dismiss();
	}
}
