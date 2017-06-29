package trente.asia.calendar.commons.dialogs;

import android.content.Context;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;

/**
 * SwDialog
 *
 * @author TrungND
 */
public class ClDialog extends ChiaseDialog{

	private Context mContext;

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

	public ClDialog setDialogScheduleEditMode(){
		this.setContentView(R.layout.dialog_schedule_edit_mode);

		return this;
	}

	public ClDialog setDialogScheduleConfirmMode(){
		this.setContentView(R.layout.dialog_schedule_confirm_mode);

		return this;
	}

	public void updateScheduleEditModeTitle(String title){
		if(!CCStringUtil.isEmpty(title)){
			TextView txtTitle = (TextView)this.findViewById(R.id.txt_id_edit_mode_title);
			txtTitle.setText(title);
		}

	}
}
