package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.calendar.services.calendar.view.WeeklyScheduleListAdapter;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends SchedulesPageFragment{

	protected ObservableListView		observableListView;
	protected WeeklyScheduleListAdapter	adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_weekly_page, container, false);
		}
		return mRootView;
	}

	protected void initCalendarView(){
		// no calendar view
	}

	@Override
	protected void initView(){
		super.initView();
	}
	//
	// @Override
	// protected List<CalendarDayModel> getDisplayedDayForList(){
	// List<CalendarDayModel> results = new ArrayList<>();
	// Date startOfWeek = dates.get(0);
	// Date endOfWeek = dates.get(dates.size() - 1);
	// for(CalendarDayModel calendarDayModel : calendarDayModels){
	// if(CCDateUtil.compareDate(startOfWeek, calendarDayModel.date, false) <= 0 && CCDateUtil.compareDate(calendarDayModel.date, endOfWeek, false) <=
	// 0){
	// results.add(calendarDayModel);
	// }
	// }
	// return results;
	// }

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
	public void onClick(View v){

	}
}
