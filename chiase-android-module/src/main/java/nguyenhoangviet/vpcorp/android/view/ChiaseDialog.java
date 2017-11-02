package nguyenhoangviet.vpcorp.android.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

/**
 * Created by TrungND on 20/04/2015.
 */
public class ChiaseDialog extends Dialog{

	public ChiaseDialog(Context context){
		super(context);
	}

	public ChiaseDialog(Context context, int theme){
		super(context, theme);
	}

	public void setContentView(int layoutResID){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(layoutResID);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}
}
