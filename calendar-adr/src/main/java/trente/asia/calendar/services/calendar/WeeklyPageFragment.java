package trente.asia.calendar.services.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.view.CalendarDayView;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.calendar.services.calendar.view.WeeklyScheduleListAdapter;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends SchedulesPageListViewFragment implements ObservableScrollViewCallbacks,CalendarView.OnCalendarDaySelectedListener,CalendarDayView.OnDayClickListener{

	protected ObservableListView		observableListView;
	protected WeeklyScheduleListAdapter	adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_weekly_page, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		observableListView = (ObservableListView)getView().findViewById(R.id.scr_calendar_day);
		observableListView.setScrollViewCallbacks(this);
		observableListView.setDivider(null);
	}

	protected void onLoadSchedulesSuccess(JSONObject response){
		super.onLoadSchedulesSuccess(response);
	}

	protected void updateObservableScrollableView(){
		List<CalendarDayModel> displayedModels = getDisplayedDayForList();
		adapter = new WeeklyScheduleListAdapter(activity, R.layout.item_calendar_day, displayedModels, lstHoliday, this);
		observableListView.setAdapter(adapter);
	}

	@Override
	public void updateList(String dayStr){
		int selectedPosition = adapter.findPosition4Code(dayStr);
		observableListView.setSelection(selectedPosition);
	}

	@Override
	protected List<Date> getAllDate(){
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		List<Date> dates = CsDateUtil.getAllDate4Week(CCDateUtil.makeCalendar(selectedDate), firstDay);
		return dates;
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging){

	}

	@Override
	public void onDownMotionEvent(){

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState){
		if(scrollState == ScrollState.UP){
			for(WeeklyCalendarHeaderRowView rowView : lstHeaderRow){
				if(rowView.index != 0){
					rowView.setVisibility(View.GONE);
				}
			}
		}else if(scrollState == ScrollState.DOWN){
			for(WeeklyCalendarHeaderRowView rowView : lstHeaderRow){
				rowView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected CalendarDayView getCalendayDayView(){
		return new WeeklyCalendarDayView(getContext());
	}

	@Override
	public void onCalendarDaySelected(CalendarDayModel reportModel){

	}

	@Override
	public void onClick(View v){

	}
}
