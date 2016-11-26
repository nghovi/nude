package trente.asia.dailyreport.view;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import trente.asia.dailyreport.R;
import trente.asia.dailyreport.dialogs.MonthYearPickerDialog;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 7/8/2016.
 */
public class DRCalendarHeader extends LinearLayout{

	public static final int			VIEW_AS_CALENDAR	= 0;
	public static final int			VIEW_AS_LIST		= 1;
	private TextView				txtDate;
	private MonthYearPickerDialog	monthYearPickerDialog;

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
		return selectedMonth;
	}

	public int getSelectedYear(){
		return selectedYear;
	}

	public Date getSelectedTime(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, selectedYear);
		c.set(Calendar.MONTH, selectedMonth - 1);
		return c.getTime();
	}

	public String getSelectedYearMonthStr(){
		return WelfareUtil.getYearMonthStr(selectedYear, selectedMonth);
	}

	private int						selectedMonth	= -1;
	private int						selectedYear	= -1;
	private int						minYear			= -1;
	private int						minMonth		= -1;
	private int						maxYear			= -1;
	private int						maxMonth		= -1;
	private OnViewChangeListener	onViewChangeListener;
	private OnTimeChangeListener	onTimeChangeListener;
	private ImageView				btnNext;
	private ImageView				btnBack;
	private ImageView				btnList;
	private ImageView				btnCalendar;
	private ImageView				imgCalendar;
	private ImageView				imgList;
	private Button					btnThisMonth;
	private Fragment				fragment;

	// public DRCalendarHeader(Context context) {
	// super(context);
	// }

	public interface OnViewChangeListener{

		public void viewAsList();

		public void viewAsCalendar();
	}

	public interface OnTimeChangeListener{

		public void onTimeChange(int newYear, int newMonth);
	}

	public void setOnViewChangeListener(OnViewChangeListener onViewChangeListener){
		this.onViewChangeListener = onViewChangeListener;
	}

	public void buildLayout(Fragment fragment, int minYear, int minMonth, int maxYear, int maxMonth, int selectedYear, int selectedMonth,

					final OnViewChangeListener onViewChangeListener, OnTimeChangeListener onTimeChangeListener){
		this.maxMonth = maxMonth;
		this.maxYear = maxYear;
		this.minMonth = minMonth;
		this.minYear = minYear;
		this.selectedYear = selectedYear;
		this.selectedMonth = selectedMonth;
		this.onViewChangeListener = onViewChangeListener;
		this.onTimeChangeListener = onTimeChangeListener;
		btnNext = (ImageView)findViewById(R.id.btn_calendar_header_next);
		btnBack = (ImageView)findViewById(R.id.btn_calendar_header_back);
		btnList = (ImageView)findViewById(R.id.btn_view_list);
		btnCalendar = (ImageView)findViewById(R.id.btn_view_calendar);
		btnCalendar.setEnabled(false);
		// txtDate = (TextView) findViewById(R.id.txt_calendar_header_date);
		btnThisMonth = (Button)findViewById(R.id.img_calendar_header_this_month);
		this.fragment = fragment;

		// txtDate.setText(getSelectedYearMonthStr());

		btnThisMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view){
				// showMonthPickerDialog();
				Calendar c = Calendar.getInstance();
				onYearMonthSelected(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
			}
		});

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

	protected void showMonthPickerDialog(){

		if(monthYearPickerDialog == null){
			monthYearPickerDialog = new MonthYearPickerDialog();
		}
		monthYearPickerDialog.setMinYear(minYear);
		monthYearPickerDialog.setMinMonth(minMonth);
		monthYearPickerDialog.setMaxMonth(maxMonth);
		monthYearPickerDialog.setMaxYear(maxYear);
		monthYearPickerDialog.setmSelectedTime(selectedYear, selectedMonth);
		monthYearPickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2){
				onYearMonthSelected(i, i1);
			}
		});
		if(monthYearPickerDialog.isAdded() == false) monthYearPickerDialog.show(fragment.getChildFragmentManager(), "MonthYearPickerDialog");
	}

	private void onYearMonthSelected(int year, int month){
		selectedYear = year;
		selectedMonth = month;
		// txtDate.setText(getSelectedYearMonthStr());
		updateDirectingButtonStatusByDate();
		onTimeChangeListener.onTimeChange(selectedYear, selectedMonth);
	}

	private void increaseYearMonth(boolean isNext){
		if(isNext){
			selectedMonth++;
			if(selectedMonth == 13){
				selectedMonth = 1;
				selectedYear++;
			}
		}else{
			selectedMonth--;
			if(selectedMonth == 0){
				selectedMonth = 12;
				selectedYear--;
			}
		}
		// txtDate.setText(getSelectedYearMonthStr());
		updateDirectingButtonStatusByDate();
		onTimeChangeListener.onTimeChange(selectedYear, selectedMonth);
	}

	private void updateDirectingButtonStatusByDate(){
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
