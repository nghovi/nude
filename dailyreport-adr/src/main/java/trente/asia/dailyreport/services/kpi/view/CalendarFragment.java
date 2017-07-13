package trente.asia.dailyreport.services.kpi.view;

import java.util.Calendar;

import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;

/**
 * Created by hviet on 6/28/17.
 */

public class CalendarFragment extends AbstractDRFragment{

	private KpiCalendarView							kpiCalendarView;
	private KpiCalendarView.OnDayClickedListener	onDayClickListener;
	private Calendar								calendar;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_calendar;
	}

	@Override
	public void buildBodyLayout(){
		kpiCalendarView = (KpiCalendarView)getView().findViewById(R.id.my_report_fragment_calendar_view);
		kpiCalendarView.setOnDayClickedListener(this.onDayClickListener);
		kpiCalendarView.setCalendar(this.calendar);
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	public void setOnDayClickListener(KpiCalendarView.OnDayClickedListener onDayClickListener){
		this.onDayClickListener = onDayClickListener;
	}

	public KpiCalendarView getKpiCalendarView(){
		return kpiCalendarView;
	}

	public void setCalendar(Calendar calendar){
		this.calendar = calendar;
	}

	public Calendar getCalendar(){
		return this.calendar;
	}

}
