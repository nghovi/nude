package trente.asia.calendar.commons.views;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.android.view.ChiaseTextViewCheckable;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * RepeatWeeklyDayLinearLayout
 *
 * @author TrungND
 */

public class RepeatWeeklyDayLinearLayout extends LinearLayout{

	private Context mContext;

	public RepeatWeeklyDayLinearLayout(Context context){
		super(context);
		this.mContext = context;
	}

	public RepeatWeeklyDayLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public void initialization(){
		TextView txtDay1 = (TextView)this.findViewById(R.id.txt_id_day1);
		TextView txtDay2 = (TextView)this.findViewById(R.id.txt_id_day2);
		TextView txtDay3 = (TextView)this.findViewById(R.id.txt_id_day3);
		TextView txtDay4 = (TextView)this.findViewById(R.id.txt_id_day4);
		TextView txtDay5 = (TextView)this.findViewById(R.id.txt_id_day5);
		TextView txtDay6 = (TextView)this.findViewById(R.id.txt_id_day6);
		TextView txtDay7 = (TextView)this.findViewById(R.id.txt_id_day7);

        PreferencesAccountUtil accountUtil = new PreferencesAccountUtil(mContext);
		List<DayModel> lstDay = CsDateUtil.getAllDay4Week(CCNumberUtil.toInteger(accountUtil.getSetting().CL_START_DAY_IN_WEEK));
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
}
