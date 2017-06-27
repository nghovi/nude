package trente.asia.dailyreport.services.kpi.view;

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
import trente.asia.dailyreport.services.kpi.ActualPlanAddFragment;
import trente.asia.dailyreport.services.kpi.model.ActualPlan;
import trente.asia.dailyreport.services.report.MyReportFragment;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.view.CalendarAdapter;

/**
 * Created by viet on 7/12/2016.
 */
public class KpiCalendarView extends RelativeLayout{

	private Activity			activity;
	public GregorianCalendar	gregorianCalendar, gregorianCalendarCloned;	// view_dr_calendar
																			// instances.

	public KpiCalendarAdapter	adapter;									// adapter instance

	// marker.

	public KpiCalendarView(Context context){
		super(context);
	}

	public KpiCalendarView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public KpiCalendarView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public KpiCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public interface OnDayClickedListener{

		public void onDayClicked(ActualPlan actualPlan);
	}

	public void setOnDayClickedListener(OnDayClickedListener onDayClickedListener){
		this.onDayClickedListener = onDayClickedListener;
	}

	private OnDayClickedListener onDayClickedListener;

	public void updateLayout(Activity activity, int year, int month, final List<ActualPlan> actualPlanList){
		this.activity = activity;
		Locale.setDefault(Locale.US);

		gregorianCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
		gregorianCalendar.set(GregorianCalendar.YEAR, year);
		gregorianCalendar.set(GregorianCalendar.MONTH, month);
		gregorianCalendarCloned = (GregorianCalendar)gregorianCalendar.clone();

		adapter = new KpiCalendarAdapter(this.activity, gregorianCalendar, actualPlanList);

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
					ActualPlan actualPlan = ActualPlanAddFragment.getActualPlanByDay(selectedDate, actualPlanList);
					onDayClickedListener.onDayClicked(actualPlan);
				}
			}
		});
	}

}
