package nguyenhoangviet.vpcorp.dailyreport.view;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

/**
 * Created by viet on 7/8/2016.
 */
public class DRCalendarHeader extends LinearLayout{

	private Date		selectedDate;

	public final static int	STEP_TYPE_WEEK	= 1;
	public final static int	STEP_TYPE_MONTH	= 2;
	public final static int	STEP_TYPE_YEAR	= 3;

	public void setStepType(int stepType){
		this.stepType = stepType;
	}

	private int	stepType	= 1;

	public DRCalendarHeader(Context context){
		super(context);
	}

	public DRCalendarHeader(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public DRCalendarHeader(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public DRCalendarHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public int getSelectedMonth(){
		return CCDateUtil.makeCalendar(selectedDate).get(Calendar.MONTH);
	}

	public int getSelectedYear(){
		return CCDateUtil.makeCalendar(selectedDate).get(Calendar.YEAR);
	}

	public Date getSelectedDate(){
		return selectedDate;
	}

	public String getSelectedYearMonthStr(){
		return CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, selectedDate);
	}

	private int						minYear		= -1;
	private int						minMonth	= -1;
	private int						maxYear		= -1;
	private int						maxMonth	= -1;
	private OnViewChangeListener	onViewChangeListener;
	private OnTimeChangeListener	onTimeChangeListener;
	private ImageView				btnNext;
	private ImageView				btnBack;
	private ImageView				btnList;
	private ImageView				btnCalendar;
	private Button btnNow;

	public interface OnViewChangeListener{

		public void viewAsList();

		public void viewAsCalendar();
	}

	public interface OnTimeChangeListener{

		public void onTimeChange(Date newSelectedDate);
	}

	public void setOnViewChangeListener(OnViewChangeListener onViewChangeListener){
		this.onViewChangeListener = onViewChangeListener;
	}

	public void buildLayout(int minYear, int minMonth, int maxYear, int maxMonth, Date selectedDate,

	final OnViewChangeListener onViewChangeListener, OnTimeChangeListener onTimeChangeListener, String btnNowText){
		this.maxMonth = maxMonth;
		this.maxYear = maxYear;
		this.minMonth = minMonth;
		this.minYear = minYear;
		this.selectedDate = selectedDate;
		this.onViewChangeListener = onViewChangeListener;
		this.onTimeChangeListener = onTimeChangeListener;
		btnNext = (ImageView)findViewById(R.id.btn_group_header_next);
		btnBack = (ImageView)findViewById(R.id.btn_group_header_back);
		btnList = (ImageView)findViewById(R.id.btn_view_list);
		btnCalendar = (ImageView)findViewById(R.id.btn_view_calendar);
		btnCalendar.setEnabled(false);

		btnNow = (Button)findViewById(R.id.img_calendar_header_this_month);
		btnNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view){
				Calendar c = Calendar.getInstance();
				onDateSelected(c.getTime());
			}
		});
        btnNow.setText(btnNowText);

		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				increaseYearMonth(true);
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				increaseYearMonth(false);
			}
		});

		btnCalendar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view){
				btnCalendar.setEnabled(false);
				btnList.setEnabled(true);
				onViewChangeListener.viewAsCalendar();
			}
		});

		btnList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view){
				btnCalendar.setEnabled(true);
				btnList.setEnabled(false);
				onViewChangeListener.viewAsList();
			}
		});
	}

	private void onDateSelected(Date selectedDate){
		this.selectedDate = selectedDate;
		updateDirectingButtonStatusByDate();
		onTimeChangeListener.onTimeChange(selectedDate);
	}

	private void increaseYearMonth(boolean isNext){
		int stepSize = 1;
		if(!isNext){
			stepSize = -1;
		}
		Calendar c = CCDateUtil.makeCalendar(selectedDate);
		switch(stepType){
		case STEP_TYPE_MONTH:
			c.add(Calendar.MONTH, stepSize);
			break;
		case STEP_TYPE_WEEK:
			c.add(Calendar.DATE, 7 * stepSize);
			break;
		case STEP_TYPE_YEAR:
			c.add(Calendar.YEAR, stepSize);
			break;
		};
		selectedDate = c.getTime();
		updateDirectingButtonStatusByDate();
		onTimeChangeListener.onTimeChange(c.getTime());
	}

	private void updateDirectingButtonStatusByDate(){
		Calendar c = CCDateUtil.makeCalendar(selectedDate);
		int selectedYear = c.get(Calendar.YEAR);
		int selectedMonth = c.get(Calendar.MONTH);
		if(selectedYear == maxYear && selectedMonth == maxMonth){
			btnNext.setEnabled(false);
		}else{
			btnNext.setEnabled(true);
		}
		if(selectedYear == minYear && selectedMonth == minMonth){
			btnBack.setEnabled(false);
		}else{
			btnBack.setEnabled(true);
		}
	}

}
