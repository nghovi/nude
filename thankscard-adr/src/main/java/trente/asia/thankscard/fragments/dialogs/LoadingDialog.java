package trente.asia.thankscard.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import trente.asia.thankscard.R;

/**
 * Created by viet on 3/28/2016.
 */
public class LoadingDialog extends Dialog{

	private AnimationDrawable	mFrameAnimation;
	private int					count	= 0;

	public LoadingDialog(Context context){

		super(context);

		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		this.setContentView(R.layout.dialog_loading);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.dimAmount = 0.7f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
		this.getWindow().setAttributes(lp);
		this.setCancelable(false);

		ImageView img = (ImageView)this.findViewById(R.id.img_id_loading);

	}

	public LoadingDialog(Context context, int theme){
		super(context, theme);
	}

	@Override
	public void show(){
		count++;
		if(isShowing() == false){
			super.show();
		}
	}

	@Override
	public void dismiss(){
		count--;
		if(count <= 0 && isShowing() == true){
			if(mFrameAnimation != null && mFrameAnimation.isRunning()){
				mFrameAnimation.stop();
			}
			super.dismiss();
		}
	}
}
