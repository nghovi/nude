package trente.asia.calendar.commons.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.android.view.adapter.ChiaseSpinnerAdapter;
import trente.asia.android.view.model.ChiaseSpinnerModel;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * ClScheduleRepeatDialog
 *
 * @author TrungND
 */
public class ClScheduleRepeatDialog extends ChiaseDialog{

	private Context			mContext;
	private Spinner			spnRepeatType;
	private Spinner			spnRepeatLimit;

	private LinearLayout	lnrWeeklyDay;
	private LinearLayout	lnrLimitUtil;
	private LinearLayout	lnrLimitAfter;
	private TextView		txtLimitUtil;

	private Calendar		calendarLimit	= Calendar.getInstance();

	public ClScheduleRepeatDialog(Context context){
		super(context);
		this.setContentView(R.layout.dialog_common_schedule_repeat);
		this.mContext = context;

		this.initialization();
	}

	private void initialization(){
		spnRepeatType = (Spinner)this.findViewById(R.id.spn_id_repeat_type);
		spnRepeatLimit = (Spinner)this.findViewById(R.id.spn_id_repeat_limit);
		lnrWeeklyDay = (LinearLayout)this.findViewById(R.id.lnr_id_weekly_day);

		lnrLimitUtil = (LinearLayout)this.findViewById(R.id.lnr_id_repeat_until);
		lnrLimitAfter = (LinearLayout)this.findViewById(R.id.lnr_id_repeat_after);
		txtLimitUtil = (TextView)this.findViewById(R.id.txt_id_repeat_limit_util);

		calendarLimit.add(Calendar.MONTH, 1);

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
		spnRepeatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				ChiaseSpinnerModel model = (ChiaseSpinnerModel)parent.getItemAtPosition(position);
				onChangeRepeatType(model);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent){

			}
		});

		ChiaseSpinnerAdapter adapterRepeatLimit = new ChiaseSpinnerAdapter(mContext, lstRepeatLimit);
		spnRepeatLimit.setAdapter(adapterRepeatLimit);
		spnRepeatLimit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				ChiaseSpinnerModel model = (ChiaseSpinnerModel)parent.getItemAtPosition(position);
				onChangeRepeatLimit(model);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent){

			}
		});
	}

	private void onChangeRepeatType(ChiaseSpinnerModel model){
		if(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY.equals(model.key)){
			lnrWeeklyDay.setVisibility(View.VISIBLE);
		}else{
			lnrWeeklyDay.setVisibility(View.GONE);
		}
	}

	private void onChangeRepeatLimit(ChiaseSpinnerModel model){
		if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(model.key)){
			lnrLimitUtil.setVisibility(View.GONE);
			lnrLimitAfter.setVisibility(View.GONE);
		}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(model.key)){
			lnrLimitUtil.setVisibility(View.VISIBLE);
			lnrLimitAfter.setVisibility(View.GONE);
            txtLimitUtil.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, calendarLimit.getTime()));
		}else{
			lnrLimitUtil.setVisibility(View.GONE);
			lnrLimitAfter.setVisibility(View.VISIBLE);
		}
	}
}
