package trente.asia.dailyreport.services.report.view;

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

import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.report.MyReportFragment;
import trente.asia.dailyreport.services.report.model.ReportModel;

/**
 * Created by viet on 7/12/2016.
 */
public class DRCalendarView extends RelativeLayout{

	private Activity			activity;
	public GregorianCalendar	gregorianCalendar, gregorianCalendarCloned;	// view_dr_calendar
																			// instances.

	public CalendarAdapter		adapter;									// adapter instance
	// marker.

	public DRCalendarView(Context context){
		super(context);
	}

	public DRCalendarView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public DRCalendarView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public DRCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public interface OnReportModelSelectedListener{

		public void onReportModelSelected(ReportModel reportModel);
	}

	public void setOnReportModelSelectedListener(OnReportModelSelectedListener onReportModelSelectedListener){
		this.onReportModelSelectedListener = onReportModelSelectedListener;
	}

	private OnReportModelSelectedListener onReportModelSelectedListener;

	public void updateLayout(Activity activity, int year, int month, final List<ReportModel> reportModelList){
		this.activity = activity;
		Locale.setDefault(Locale.US);

		gregorianCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
		gregorianCalendar.set(GregorianCalendar.YEAR, year);
		gregorianCalendar.set(GregorianCalendar.MONTH, month - 1);
		gregorianCalendarCloned = (GregorianCalendar)gregorianCalendar.clone();

		adapter = new CalendarAdapter(this.activity, gregorianCalendar, reportModelList);

		GridView gridview = (GridView)findViewById(R.id.view_dr_calendar_gridview);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView parent, View v, int position, long id){
				Calendar selectedDate = CalendarAdapter.dayString.get(position);
				int gridvalue = selectedDate.get(Calendar.DAY_OF_MONTH);

				// navigate to next or previous gregorianCalendar on clicking offdays.
				if((gridvalue > 10) && (position < 8)){
					// setPreviousMonth();
					// refreshCalendar();
				}else if((gridvalue < 7) && (position > 28)){
					// setNextMonth();
					// refreshCalendar();
				}else{ // currentMonth
					ReportModel reportModel = MyReportFragment.getReportByDay(selectedDate, reportModelList);
					onReportModelSelectedListener.onReportModelSelected(reportModel);
				}
			}
		});
	}

}
