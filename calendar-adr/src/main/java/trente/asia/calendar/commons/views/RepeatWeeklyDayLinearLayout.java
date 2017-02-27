package trente.asia.calendar.commons.views;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.android.view.ChiaseTextViewCheckable;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * RepeatWeeklyDayLinearLayout
 *
 * @author TrungND
 */

public class RepeatWeeklyDayLinearLayout extends LinearLayout{

	private Context					mContext;
	private List<DayModel>			lstDay;

	private ChiaseTextViewCheckable	txtDay1;
	private ChiaseTextViewCheckable	txtDay2;
	private ChiaseTextViewCheckable	txtDay3;
	private ChiaseTextViewCheckable	txtDay4;
	private ChiaseTextViewCheckable	txtDay5;
	private ChiaseTextViewCheckable	txtDay6;
	private ChiaseTextViewCheckable	txtDay7;

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
		lstDay = CsDateUtil.getAllDay4Week(CCNumberUtil.toInteger(accountUtil.getSetting().CL_START_DAY_IN_WEEK));
		txtDay1.setText(lstDay.get(0).day);
		txtDay2.setText(lstDay.get(1).day);
		txtDay3.setText(lstDay.get(2).day);
		txtDay4.setText(lstDay.get(3).day);
		txtDay5.setText(lstDay.get(4).day);
		txtDay6.setText(lstDay.get(5).day);
		txtDay7.setText(lstDay.get(6).day);

		txtDay1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				((ChiaseTextViewCheckable)view).toggle();
			}
		});
		txtDay2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				((ChiaseTextViewCheckable)view).toggle();
			}
		});
		txtDay3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				((ChiaseTextViewCheckable)view).toggle();
			}
		});
		txtDay4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				((ChiaseTextViewCheckable)view).toggle();
			}
		});
		txtDay5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				((ChiaseTextViewCheckable)view).toggle();
			}
		});
		txtDay6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				((ChiaseTextViewCheckable)view).toggle();
			}
		});
		txtDay7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				((ChiaseTextViewCheckable)view).toggle();
			}
		});
	}

	public String getSelectedDays(){
		StringBuilder builder = new StringBuilder();
		if(txtDay1.isChecked()){
			builder.append(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, lstDay.get(0).date) + ", ");
		}

		if(txtDay2.isChecked()){
			builder.append(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, lstDay.get(1).date) + ", ");
		}

		if(txtDay3.isChecked()){
			builder.append(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, lstDay.get(2).date) + ", ");
		}

		if(txtDay4.isChecked()){
			builder.append(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, lstDay.get(3).date) + ", ");
		}

		if(txtDay5.isChecked()){
			builder.append(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, lstDay.get(4).date) + ", ");
		}

		if(txtDay6.isChecked()){
			builder.append(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, lstDay.get(5).date) + ", ");
		}

		if(txtDay7.isChecked()){
			builder.append(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_13, lstDay.get(6).date) + ", ");
		}

		if(builder.length() > 0){
			return builder.substring(0, builder.length() - 2);
		}
		return builder.toString();
	}
}
