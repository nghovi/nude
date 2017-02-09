package trente.asia.calendar.services.calendar;

import static trente.asia.calendar.services.calendar.model.CalendarDay.Schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarDay;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends WelfareFragment implements ObservableScrollViewCallbacks,CalendarView.OnCalendarDaySelectedListener{

	private ObservableListView		lstCalendarDay;
	private CalendarDayListAdapter	adapter;
	private CalendarView			calendarView;
	private Date					selectedDate;

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
		lstCalendarDay = (ObservableListView)getView().findViewById(R.id.lst_calendar_day);
		lstCalendarDay.setScrollViewCallbacks(this);
		List<CalendarDay> dummy = getDummyData();
		adapter = new CalendarDayListAdapter(activity, R.layout.item_calendar_day, dummy, new CalendarDayListAdapter.OnScheduleClickListener() {

			@Override
			public void onClick(Schedule schedule){
				onClickSchedule(schedule);
			}
		});
		lstCalendarDay.setAdapter(adapter);
		calendarView = (CalendarView)getView().findViewById(R.id.calendar_view);
		calendarView.setOnCalendarDaySelectedListener(this);
		calendarView.updateLayout(activity, 2017, 2, dummy);
	}

	private void onClickSchedule(Schedule schedule){
		ScheduleDetailFragment fragment = new ScheduleDetailFragment();
		fragment.setSchedule(schedule);

		android.support.v4.app.FragmentManager manager = getParentFragment().getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(trente.asia.android.R.id.ipt_id_body, fragment);
		transaction.addToBackStack(null);
		transaction.commit();

		// gotoFragment(fragment);
	}

	@Override
	protected void initData(){
	}

	// @Override
	// public int getFooterItemId(){
	// return 0;
	// }

	// @Override
	// public void onClick(View v){
	// switch(v.getId()){
	// default:
	// break;
	// }
	// }

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public List<CalendarDay> getDummyData(){
		List<CalendarDay> dummyData = new ArrayList<>();

		Schedule e1 = new Schedule();
		e1.startTime = "10:00";
		e1.endTime = "12:00";
		e1.name="Date Thuy";
		e1.url="google.com.vn";
		e1.note = "she refused";

		Schedule e2 = new Schedule();
		e2.startTime = "08:00";
		e2.endTime = "11:00";
		e2.name = "Go to MocChau with ...";
		e2.note = "done";

		CalendarDay d1 = new CalendarDay();
		d1.date = "2017/06/02";
		d1.schedules = new ArrayList<>();
		d1.schedules.add(e1);
		d1.schedules.add(e2);

		CalendarDay d2 = new CalendarDay();
		d2.date = "2017/08/02";
		d2.schedules = new ArrayList<>();
		d2.schedules.add(e1);
		d2.schedules.add(e2);

		dummyData.add(d1);
		dummyData.add(d2);
		dummyData.add(d2);
		dummyData.add(d2);
		dummyData.add(d2);
		dummyData.add(d2);
		dummyData.add(d2);

		return dummyData;
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging){

	}

	@Override
	public void onDownMotionEvent(){

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState){
		CalendarView calendarView = (CalendarView)getView().findViewById(R.id.calendar_view);
		if(scrollState == ScrollState.UP){
			calendarView.showWeekOnly(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, selectedDate));// // TODO: 2/8/2017
		}else if(scrollState == ScrollState.DOWN){
			calendarView.showAll(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, selectedDate));// // TODO: 2/8/2017
		}
	}

	@Override
	public void onCalendarDaySelected(CalendarDay reportModel){
		// // TODO: 2/8/2017
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}
}
