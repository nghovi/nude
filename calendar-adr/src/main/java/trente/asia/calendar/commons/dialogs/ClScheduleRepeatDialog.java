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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.android.view.adapter.ChiaseSpinnerAdapter;
import trente.asia.android.view.model.ChiaseSpinnerModel;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.model.ScheduleRepeatModel;
import trente.asia.calendar.commons.utils.ClRepeatUtil;
import trente.asia.calendar.commons.views.RepeatWeeklyDayLinearLayout;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * ClScheduleRepeatDialog
 *
 * @author TrungND
 */
public class ClScheduleRepeatDialog extends CLOutboundDismissDialog{

	private Context						mContext;
	private TextView					txtRepeat;
	private Spinner						spnRepeatType;
	private Spinner						spnRepeatLimit;

	private LinearLayout				lnrWeeklyDay;
	private LinearLayout				lnrLimitUtil;
	private LinearLayout				lnrLimitAfter;
	private TextView					txtLimitUtil;

	private RepeatWeeklyDayLinearLayout	lnrRepeatWeeklyDay;
	private Calendar					calendarLimit	= Calendar.getInstance();
	private DatePickerDialog			datePickerDialogLimit;
	private SwitchCompat				swtRepeat;
	private EditText					edtLimitTimes;

	private ScheduleRepeatModel			repeatModel		= new ScheduleRepeatModel();
	private String						startDate;
	private List<ChiaseSpinnerModel>	lstRepeatType;
	private List<ChiaseSpinnerModel>	lstRepeatLimit;

	public ClScheduleRepeatDialog(Context context, TextView txtRepeat){
		super(context);
		this.setContentView(R.layout.dialog_common_schedule_repeat);
		this.mContext = context;
		this.txtRepeat = txtRepeat;

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
		edtLimitTimes = (EditText)this.findViewById(R.id.edt_id_limit_times);

		lnrRepeatWeeklyDay = (RepeatWeeklyDayLinearLayout)this.findViewById(R.id.lnr_id_repeat_weekly_day);
		lnrRepeatWeeklyDay.initialization();

		// calendarLimit.add(Calendar.MONTH, 1);
		datePickerDialogLimit = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String dateString = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtLimitUtil.setText(dateString);
			}
		}, calendarLimit.get(Calendar.YEAR), calendarLimit.get(Calendar.MONTH), calendarLimit.get(Calendar.DAY_OF_MONTH));

		lstRepeatType = new ArrayList<>();
		lstRepeatType.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY, mContext.getString(R.string.cl_schedule_repeat_type_weekly)));
		lstRepeatType.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_TYPE_MONTHLY, mContext.getString(R.string.cl_schedule_repeat_type_monthly)));
		lstRepeatType.add(new ChiaseSpinnerModel(ClConst.SCHEDULE_REPEAT_TYPE_YEARLY, mContext.getString(R.string.cl_schedule_repeat_type_yearly)));

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
				ClScheduleRepeatDialog.this.dismiss();
				txtRepeat.setText(getRepeatValue());
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
			// calendarLimit = Calendar.getInstance();
			txtLimitUtil.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendarLimit.getTime()));
		}else{
			lnrLimitUtil.setVisibility(View.GONE);
			lnrLimitAfter.setVisibility(View.VISIBLE);
		}
	}

	public void initDefaultValue(){
		if(ClRepeatUtil.isRepeat(repeatModel.repeatType) && ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY.equals(repeatModel.repeatType)){
			lnrRepeatWeeklyDay.initDefaultValue(repeatModel.repeatData);
		}else{
			if(!CCStringUtil.isEmpty(startDate)){
				Calendar startCalendar = CCDateUtil.makeCalendar(WelfareFormatUtil.makeDate(startDate));
				String repeatData = String.valueOf(startCalendar.get(Calendar.DAY_OF_WEEK));
                lnrRepeatWeeklyDay.setStartDate(startDate);
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
		}else if(ClConst.SCHEDULE_REPEAT_TYPE_YEARLY.equals(repeatType.key)){
		}

		ChiaseSpinnerModel repeatLimit = (ChiaseSpinnerModel)spnRepeatLimit.getSelectedItem();
		repeatModel.repeatLimitType = repeatLimit.key;
		if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(repeatLimit.key)){
		}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(repeatLimit.key)){
			repeatModel.repeatEnd = txtLimitUtil.getText().toString();
		}else{
			String timesValue = edtLimitTimes.getText().toString();
			if(CCStringUtil.isEmpty(timesValue)){
				repeatModel.repeatInterval = "1";
			}else{
				repeatModel.repeatInterval = timesValue;
			}
		}

		return ClRepeatUtil.getRepeatDescription(repeatModel, mContext);
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
		// this.initDefaultValue();
	}

	public void setRepeatModel(ScheduleRepeatModel repeatModel){
		this.repeatModel = repeatModel;
		this.spnRepeatType.setSelection(CsUtil.findPosition4Spinner(lstRepeatType, repeatModel.repeatType));
		this.initDefaultValue();

		this.spnRepeatLimit.setSelection(CsUtil.findPosition4Spinner(lstRepeatLimit, repeatModel.repeatLimitType));
		if(ClConst.SCHEDULE_REPEAT_LIMIT_FOREVER.equals(repeatModel.repeatLimitType)){
		}else if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(repeatModel.repeatLimitType)){
			Date repeatEndDate = WelfareUtil.makeDate(repeatModel.repeatEnd);
			calendarLimit = CCDateUtil.makeCalendar(repeatEndDate);
			datePickerDialogLimit.getDatePicker().updateDate(calendarLimit.get(Calendar.YEAR), calendarLimit.get(Calendar.MONTH), calendarLimit.get(Calendar.DAY_OF_MONTH));
			// txtLimitUtil.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, repeatEndDate));
		}else{
			edtLimitTimes.setText(repeatModel.repeatInterval);
		}
	}

	public ScheduleRepeatModel getRepeatModel(){
		return repeatModel;
	}
}
