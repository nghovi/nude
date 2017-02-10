package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;

/**
 * Created by viet on 7/12/2016.
 */
public class CalendarView extends RelativeLayout{

	private Activity	activity;
	public GregorianCalendar	gregorianCalendar, gregorianCalendarCloned; // view_dr_calendar
																			// instances.

	public CLDAdapter			adapter;									// adapter instance

	// marker.

	public CalendarView(Context context){
		super(context);
	}

	public CalendarView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public interface OnCalendarDaySelectedListener{

		public void onCalendarDaySelected(CalendarDayModel reportModel);
	}

	public void setOnCalendarDaySelectedListener(OnCalendarDaySelectedListener onCalendarDaySelectedListener){
		this.onCalendarDaySelectedListener = onCalendarDaySelectedListener;
	}

	private OnCalendarDaySelectedListener	onCalendarDaySelectedListener;

	public void showWeekOnly(String date){
		adapter.setWeekVisibleOnly(date, true);
	}

	public void showAll(String date){
		adapter.setWeekVisibleOnly(date, false);
	}

	public void updateLayout(Activity activity, int year, int month, final List<CalendarDayModel> reportModelList){
		this.activity = activity;
		Locale.setDefault(Locale.US);

		gregorianCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
		gregorianCalendar.set(GregorianCalendar.YEAR, year);
		gregorianCalendar.set(GregorianCalendar.MONTH, month - 1);
		gregorianCalendarCloned = (GregorianCalendar)gregorianCalendar.clone();

		adapter = new CLDAdapter(this.activity, gregorianCalendar, reportModelList);

		GridView gridview = (GridView)findViewById(R.id.view_dr_calendar_gridview);
		gridview.setAdapter(adapter);
//		gridview.set

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView parent, View v, int position, long id){
				Calendar selectedDate = adapter.dayString.get(position);
				int gridvalue = selectedDate.get(Calendar.DAY_OF_MONTH);

				// navigate to next or previous gregorianCalendar on clicking offdays.
				if((gridvalue > 10) && (position < 8)){
					// setPreviousMonth();
					// refreshCalendar();
				}else if((gridvalue < 7) && (position > 28)){
					// setNextMonth();
					// refreshCalendar();
				}else{ // currentMonth
				// CalendarDay reportModel = MyReportFragment.getReportByDay(selectedDate, reportModelList);
				// onCalendarDaySelectedListener.onCalendarDaySelected(reportModel);
				}
			}
		});
	}

}
