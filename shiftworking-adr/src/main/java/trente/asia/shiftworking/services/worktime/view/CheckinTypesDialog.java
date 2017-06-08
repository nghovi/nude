package trente.asia.shiftworking.services.worktime.view;

import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.services.worktime.model.WorkingTimeModel;

/**
 * Created by hviet on 2017/06/07.
 */

public class CheckinTypesDialog extends ChiaseListDialog{

	public CheckinTypesDialog(Context context, String title, Map<String, String> map, ChiaseTextView txtItem, OnItemClicked itemClickListener){
		super(context, title, map, txtItem, itemClickListener);
	}

	public CheckinTypesDialog(Context context, String title, Map<String, String> map, ChiaseTextView txtItem, OnItemClicked itemClickListener, WorkingTimeModel model){
		super(context, title, map, txtItem, itemClickListener);
		TextView txtTime = (TextView)findViewById(R.id.txt_dialog_checkin_types_time);
		txtTime.setText(model.timeLog);
		TextView txtType = (TextView)findViewById(R.id.txt_dialog_checkin_types_type);
		txtType.setText(model.workingTypeName);
	}

	@Override
	public void setContentView(int layoutResID){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.dialog_checkin_types);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}
}
