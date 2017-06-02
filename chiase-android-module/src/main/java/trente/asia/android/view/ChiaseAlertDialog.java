package trente.asia.android.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.R;

/**
 * ChiaseAlertDialog
 *
 * @author TrungND
 */
public class ChiaseAlertDialog extends Dialog{

	public ChiaseAlertDialog(Context context){
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_common_alert);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		// this.setContentView(R.layout.dialog_common_alert);
		this.setCanceledOnTouchOutside(false);

		LinearLayout lnrPositive = (LinearLayout)this.findViewById(R.id.lnr_id_positive);
		lnrPositive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				dismiss();
			}
		});
	}

	public void setMessage(String message){
		if(!CCStringUtil.isEmpty(message)){
			TextView txtMessage = (TextView)this.findViewById(R.id.txt_id_msg);
			txtMessage.setText(message);
		}
	}
}
