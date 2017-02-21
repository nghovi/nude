package trente.asia.calendar.commons.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Spinner;

import trente.asia.android.view.ChiaseDialog;
import trente.asia.android.view.adapter.ChiaseSpinnerAdapter;
import trente.asia.android.view.model.ChiaseSpinnerModel;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;

/**
 * ClScheduleRepeatDialog
 *
 * @author TrungND
 */
public class ClScheduleRepeatDialog extends ChiaseDialog{

	private Context	mContext;
	private Spinner	spnRepeatType;
	private Spinner	spnRepeatLimit;

	public ClScheduleRepeatDialog(Context context){
		super(context);
		this.setContentView(R.layout.dialog_common_schedule_repeat);
		this.mContext = context;

		this.initialization();
	}

	private void initialization(){
		spnRepeatType = (Spinner)this.findViewById(R.id.spn_id_repeat_type);
		spnRepeatLimit = (Spinner)this.findViewById(R.id.spn_id_repeat_limit);

		List<ChiaseSpinnerModel> lstRepeatType = new ArrayList<>();
		lstRepeatType.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY, mContext.getString(R.string.cl_schedule_repeat_type_weekly)));
		lstRepeatType.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_TYPE_MONTHLY, mContext.getString(R.string.cl_schedule_repeat_type_monthly)));
		lstRepeatType.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_TYPE_YEARLY, mContext.getString(R.string.cl_schedule_repeat_type_yearly)));

		List<ChiaseSpinnerModel> lstRepeatLimit = new ArrayList<>();
		lstRepeatLimit.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER, mContext.getString(R.string.cl_schedule_repeat_limit_forever)));
		lstRepeatLimit.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL, mContext.getString(R.string.cl_schedule_repeat_limit_until)));
		lstRepeatLimit.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER, mContext.getString(R.string.cl_schedule_repeat_limit_after)));

		ChiaseSpinnerAdapter adapterRepeatType = new ChiaseSpinnerAdapter(mContext, lstRepeatType);
		spnRepeatType.setAdapter(adapterRepeatType);

		ChiaseSpinnerAdapter adapterRepeatLimit = new ChiaseSpinnerAdapter(mContext, lstRepeatLimit);
		spnRepeatLimit.setAdapter(adapterRepeatLimit);
	}
}
