package trente.asia.calendar.commons.dialogs;

import android.content.Context;

import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;

/**
 * SwDialog
 *
 * @author TrungND
 */
public class ClDialog extends ChiaseDialog{

	private Context				mContext;

	public ClDialog(Context context){
		super(context);
		this.mContext = context;
	}

	public ClDialog(Context context, int theme){
		super(context, theme);
		this.mContext = context;
	}

	public ClDialog setDialogScheduleList(){
		this.setContentView(R.layout.dialog_schedule_list);

		return this;
	}
}
