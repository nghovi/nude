package trente.asia.calendar.commons.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.android.view.ChiaseTextViewCheckable;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * RepeatWeeklyDayLinearLayout
 *
 * @author TrungND
 */

public class RepeatWeeklyDayLinearLayout extends LinearLayout{

	private Context					mContext;
	private List<RepeatDayModel>	lstRepeatDay	= new ArrayList<>();
	private String					repeatData;
	private String					startDate;

	private ChiaseTextViewCheckable	txtDay1;
	private ChiaseTextViewCheckable	txtDay2;
	private ChiaseTextViewCheckable	txtDay3;
	private ChiaseTextViewCheckable	txtDay4;
	private ChiaseTextViewCheckable	txtDay5;
	private ChiaseTextViewCheckable	txtDay6;
	private ChiaseTextViewCheckable	txtDay7;

	private class RepeatDayModel{

		public DayModel					dayModel;
		public ChiaseTextViewCheckable	txtDay;

		public RepeatDayModel(){
		}

		public RepeatDayModel(DayModel dayModel, ChiaseTextViewCheckable txtDay){
			this.dayModel = dayModel;
			this.txtDay = txtDay;
			this.txtDay.setText(dayModel.day);
			this.txtDay.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					((ChiaseTextViewCheckable)view).toggle();
				}
			});
		}
	}

	public RepeatWeeklyDayLinearLayout(Context context){
		super(context);
		this.mContext = context;
	}

	public RepeatWeeklyDayLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public void initialization(){
		txtDay1 = (ChiaseTextViewCheckable)this.findViewById(R.id.txt_id_day1);
		txtDay2 = (ChiaseTextViewCheckable)this.findViewById(R.id.txt_id_day2);
		txtDay3 = (ChiaseTextViewCheckable)this.findViewById(R.id.txt_id_day3);
		txtDay4 = (ChiaseTextViewCheckable)this.findViewById(R.id.txt_id_day4);
		txtDay5 = (ChiaseTextViewCheckable)this.findViewById(R.id.txt_id_day5);
		txtDay6 = (ChiaseTextViewCheckable)this.findViewById(R.id.txt_id_day6);
		txtDay7 = (ChiaseTextViewCheckable)this.findViewById(R.id.txt_id_day7);

		PreferencesAccountUtil accountUtil = new PreferencesAccountUtil(mContext);
		List<DayModel> lstDay = CsDateUtil.getAllDay4Week(CCNumberUtil.toInteger(accountUtil.getSetting().CL_START_DAY_IN_WEEK));
		lstRepeatDay.add(new RepeatDayModel(lstDay.get(0), txtDay1));
		lstRepeatDay.add(new RepeatDayModel(lstDay.get(1), txtDay2));
		lstRepeatDay.add(new RepeatDayModel(lstDay.get(2), txtDay3));
		lstRepeatDay.add(new RepeatDayModel(lstDay.get(3), txtDay4));
		lstRepeatDay.add(new RepeatDayModel(lstDay.get(4), txtDay5));
		lstRepeatDay.add(new RepeatDayModel(lstDay.get(5), txtDay6));
		lstRepeatDay.add(new RepeatDayModel(lstDay.get(6), txtDay7));
	}

	public String getSelectedDays(){
		StringBuilder textBuilder = new StringBuilder();
		StringBuilder idBuilder = new StringBuilder();
		for(RepeatDayModel repeatDayModel : lstRepeatDay){
			if(repeatDayModel.txtDay.isChecked()){
				textBuilder.append(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, repeatDayModel.dayModel.date) + ", ");
				idBuilder.append(CCDateUtil.makeCalendar(repeatDayModel.dayModel.date).get(Calendar.DAY_OF_WEEK) + ",");
			}
		}

		if(textBuilder.length() > 0){
			repeatData = idBuilder.substring(0, idBuilder.length() - 1);
			return textBuilder.substring(0, textBuilder.length() - 2);
		}

		// set start date value
		Calendar startCalendar = CCDateUtil.makeCalendar(WelfareFormatUtil.makeDate(this.startDate));
		repeatData = String.valueOf(startCalendar.get(Calendar.DAY_OF_WEEK));
        initDefaultValue(this.startDate);
		return CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, WelfareFormatUtil.makeDate(this.startDate));
	}

	public void initDefaultValue(String startDate){
		this.startDate = startDate;
		Date activeDate = WelfareFormatUtil.makeDate(startDate);
		Calendar activeCalendar = CCDateUtil.makeCalendar(activeDate);
		for(RepeatDayModel repeatDayModel : lstRepeatDay){
			Calendar repeatCalendar = CCDateUtil.makeCalendar(repeatDayModel.dayModel.date);
			if(activeCalendar.get(Calendar.DAY_OF_WEEK) == repeatCalendar.get(Calendar.DAY_OF_WEEK)){
				repeatDayModel.txtDay.setChecked(true);
			}else{
				repeatDayModel.txtDay.setChecked(false);
			}
		}
	}

	public String getRepeatData(){
		return this.repeatData;
	}
}
