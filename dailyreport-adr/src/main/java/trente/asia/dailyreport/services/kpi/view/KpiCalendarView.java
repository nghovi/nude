package trente.asia.dailyreport.services.kpi.view;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.kpi.model.ActionPlan;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * Created by viet on 7/12/2016.
 */
public class KpiCalendarView extends RelativeLayout{

	public GregorianCalendar	gregorianCalendar, gregorianCalendarCloned;	// view_dr_calendar
																			// instances.

	private KpiCalendarAdapter	adapter;									// adapter instance
	private GridView			gridview;

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

	public void setCalendar(Calendar calendar){
		Locale.setDefault(Locale.US);
		gregorianCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
		gregorianCalendar.set(GregorianCalendar.YEAR, calendar.get(Calendar.YEAR));
		gregorianCalendar.set(GregorianCalendar.MONTH, calendar.get(Calendar.MONTH));
		gregorianCalendarCloned = (GregorianCalendar)gregorianCalendar.clone();

		adapter = new KpiCalendarAdapter(getContext(), gregorianCalendar);
		gridview = (GridView)findViewById(R.id.view_dr_calendar_gridview);
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
					if(position != adapter.getSelectedPosition()){
						adapter.updateSelectedCell(position);
						onDayClickedListener.onDayClicked(selectedDate);
					}
				}
			}
		});
		gridview.setAdapter(adapter);
		((TextView)findViewById(R.id.fragment_txt_calendar_header_date)).setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, calendar.getTime()));
	}

	public interface OnDayClickedListener{

		public void onDayClicked(Calendar selectedDate);
	}

	public void setOnDayClickedListener(OnDayClickedListener onDayClickedListener){
		this.onDayClickedListener = onDayClickedListener;
	}

	private OnDayClickedListener onDayClickedListener;

	public void updateLayoutWithData(Map<String, String> statusMap){
		adapter.updateLayout(statusMap);
	}

}
