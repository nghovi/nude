package trente.asia.calendar.commons.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsUtil;
import trente.asia.android.view.adapter.ChiaseSpinnerAdapter;
import trente.asia.android.view.model.ChiaseSpinnerModel;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClRepeatUtil;
import trente.asia.calendar.commons.views.RepeatWeeklyDayLinearLayout;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * ClScheduleRepeatDialog
 *
 * @author TrungND
 */
public class ClScheduleRepeatDialog extends CLOutboundDismissDialog{

	private final TextView				txtUntil;
	private Context						mContext;
	private TextView					txtRepeat;
	private Spinner						spnRepeatType;
	private Spinner						spnRepeatLimit;

	private LinearLayout				lnrWeeklyDay;
	private LinearLayout				lnrLimitUtil;
	private LinearLayout				lnrLimitAfter;
	private TextView					txtLimitUtil;

	private RepeatWeeklyDayLinearLayout	lnrRepeatWeeklyDay;
	// private Calendar calendarLimit = Calendar.getInstance();
	private DatePickerDialog			datePickerDialogLimit;
	private SwitchCompat				swtRepeat;

	private ScheduleModel				repeatModel	= new ScheduleModel();
	private String						startDateStr;
	private List<ChiaseSpinnerModel>	lstRepeatType;
	private List<ChiaseSpinnerModel>	lstRepeatLimit;
	private String						limitDateStr;
	private int							mYear;
	private int							mMonth;
	private int							mDay;

	public interface OnChangeRepeatTypeListener{

		public void onChange(boolean isRepeated);
	}

	public void setOnChangeRepeatTypeListener(OnChangeRepeatTypeListener onChangeRepeatTypeListener){
		this.onChangeRepeatTypeListener = onChangeRepeatTypeListener;
	}

	OnChangeRepeatTypeListener onChangeRepeatTypeListener;

	public ClScheduleRepeatDialog(Context context, TextView txtRepeat, TextView txtUntil){
		super(context);
		this.setContentView(R.layout.dialog_common_schedule_repeat);
		this.mContext = context;
		this.txtRepeat = txtRepeat;
		this.txtUntil = txtUntil;
		this.initialization();
	}

	private void initialization(){
		spnRepeatType = (Spinner)this.findViewById(R.id.spn_id_repeat_type);
		spnRepeatLimit = (Spinner)this.findViewById(R.id.spn_id_repeat_limit);
		lnrWeeklyDay = (LinearLayout)this.findViewById(R.id.lnr_id_weekly_day);

		lnrLimitUtil = (LinearLayout)this.findViewById(R.id.lnr_id_repeat_until);
		lnrLimitAfter = (LinearLayout)this.findViewById(R.id.lnr_id_repeat_after);
		txtLimitUtil = (TextView)this.findViewById(R.id.txt_id_repeat_limit_util);
		swtRepeat = (SwitchCompat)this.findViewById(R.id.swt_id_repeat);

		lnrRepeatWeeklyDay = (RepeatWeeklyDayLinearLayout)this.findViewById(R.id.lnr_id_repeat_weekly_day);
		lnrRepeatWeeklyDay.initialization();
		// calendarLimit.add(Calendar.MONTH, 1);

		lstRepeatType = new ArrayList<>();
		lstRepeatType.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY, mContext.getString(R.string.cl_schedule_repeat_type_weekly)));
		lstRepeatType.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_TYPE_MONTHLY, mContext.getString(R.string.cl_schedule_repeat_type_monthly)));

		lstRepeatLimit = new ArrayList<>();
		lstRepeatLimit.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER, mContext.getString(R.string.cl_schedule_repeat_limit_forever)));
		lstRepeatLimit.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL, mContext.getString(R.string.cl_schedule_repeat_limit_until)));
		// lstRepeatLimit.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER,
		// mContext.getString(R.string.cl_schedule_repeat_limit_after)));

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

		txtLimitUtil.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				datePickerDialogLimit.show();
			}
		});
		this.findViewById(R.id.lnr_id_done).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				boolean isRepeat = swtRepeat.isChecked();

				if(onChangeRepeatTypeListener != null){
					onChangeRepeatTypeListener.onChange(isRepeat);
				}
				txtRepeat.setText(getRepeatValue());
				if(isRepeat){
					if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(repeatModel.repeatLimitType) || CCStringUtil.isEmpty(repeatModel.repeatLimitType)){
						txtUntil.setText(mContext.getString(R.string.cl_schedule_repeat_limit_forever));
					}else{
						txtUntil.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_SHORT, repeatModel.repeatEnd));
					}
				}
				ClScheduleRepeatDialog.this.dismiss();

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
			Calendar cal = Calendar.getInstance();
			if(!CCStringUtil.isEmpty(startDateStr)){
				Date start = CCDateUtil.makeDateCustom(startDateStr, WelfareConst.WF_DATE_TIME_SHORT);
				cal.setTime(start);
			}
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
			if(repeatModel.repeatEnd != null){
				limitDateStr = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_SHORT, repeatModel.repeatEnd);
			}else{
				limitDateStr = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_SHORT, cal.getTime());
			}
			txtLimitUtil.setText(limitDateStr);
		}else{
			lnrLimitUtil.setVisibility(View.GONE);
			lnrLimitAfter.setVisibility(View.VISIBLE);
		}
		if(limitDateStr != null){
			mYear = Integer.parseInt(limitDateStr.split("/")[0]);
			mMonth = Integer.parseInt(limitDateStr.split("/")[1]) - 1;
			mDay = Integer.parseInt(limitDateStr.split("/")[2]);

			if(datePickerDialogLimit == null){
				datePickerDialogLimit = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
						String dateString = year + "/" + (month + 1) + "/" + (dayOfMonth);
						txtLimitUtil.setText(dateString);
					}
				}, mYear, mMonth, mDay);
			}else{
				datePickerDialogLimit.updateDate(mYear, mMonth, mDay);
			}
		}
	}

	public void initDefaultValue(){
		if(ClRepeatUtil.isRepeat(repeatModel.repeatType) && ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY.equals(repeatModel.repeatType)){
			lnrRepeatWeeklyDay.initDefaultValue(repeatModel.repeatData);
		}else{
			if(!CCStringUtil.isEmpty(startDateStr)){
				Calendar startCalendar = CCDateUtil.makeCalendar(WelfareFormatUtil.makeDate(startDateStr));
				String repeatData = String.valueOf(startCalendar.get(Calendar.DAY_OF_WEEK));
				lnrRepeatWeeklyDay.setStartDate(startDateStr);
				lnrRepeatWeeklyDay.initDefaultValue(repeatData);
			}
		}
	}

	public String getRepeatValue(){
		if(!swtRepeat.isChecked()){
			repeatModel.repeatType = ClConst.SCHEDULE_REPEAT_TYPE_NONE;
			return ClRepeatUtil.getRepeatDescription(repeatModel, mContext);
		}

		ChiaseSpinnerModel repeatType = (ChiaseSpinnerModel)spnRepeatType.getSelectedItem();
		repeatModel.repeatType = repeatType.key;
		if(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY.equals(repeatType.key)){
			String selectedDays = lnrRepeatWeeklyDay.getSelectedDays();
			if(CCStringUtil.isEmpty(selectedDays)){
			}else{
				repeatModel.repeatData = lnrRepeatWeeklyDay.getRepeatData();
			}
		}else if(ClConst.SCHEDULE_REPEAT_TYPE_MONTHLY.equals(repeatType.key)){
		}

		ChiaseSpinnerModel repeatLimit = (ChiaseSpinnerModel)spnRepeatLimit.getSelectedItem();
		repeatModel.repeatLimitType = repeatLimit.key;
		if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(repeatLimit.key)){
		}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(repeatLimit.key)){
			repeatModel.repeatEnd = CCDateUtil.makeDateCustom(txtLimitUtil.getText().toString(), WelfareConst.WF_DATE_TIME_SHORT);
		}

		return ClRepeatUtil.getRepeatDescription(repeatModel, mContext);
	}

	public void setStartDateStr(String startDateStr){
		this.startDateStr = startDateStr;
	}

	public void setRepeatModel(ScheduleModel repeatModel){
		this.repeatModel = repeatModel;
		this.spnRepeatType.setSelection(CsUtil.findPosition4Spinner(lstRepeatType, repeatModel.repeatType));
		this.initDefaultValue();

		this.spnRepeatLimit.setSelection(CsUtil.findPosition4Spinner(lstRepeatLimit, repeatModel.repeatLimitType));
		if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(repeatModel.repeatLimitType)){
		}
		// else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(repeatModel.repeatLimitType)){
		// limitDateStr = CCFormatUtil.formatDateCustom("yyyy/MM/dd",repeatModel.repeatEnd);
		// mYear = Integer.parseInt(limitDateStr.split("/")[0]);
		// mMonth = Integer.parseInt(limitDateStr.split("/")[1]) - 1;
		// mDay = Integer.parseInt(limitDateStr.split("/")[2]);
		// calendarLimit = CCDateUtil.makeCalendar(repeatModel.repeatEnd);
		// datePickerDialogLimit.getDatePicker().updateDate(mYear, mMonth, mDay);
		// }
	}

	public ScheduleModel getRepeatModel(){
		return repeatModel;
	}
}
