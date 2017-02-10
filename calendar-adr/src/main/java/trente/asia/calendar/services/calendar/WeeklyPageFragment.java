package trente.asia.calendar.services.calendar;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
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
		List<CalendarDayModel> dummy = getDummyData();
		adapter = new CalendarDayListAdapter(activity, R.layout.item_calendar_day, dummy, new CalendarDayListAdapter.OnScheduleClickListener() {

			@Override
			public void onClick(ScheduleModel schedule){
				onClickSchedule(schedule);
			}
		});
		lstCalendarDay.setAdapter(adapter);
		calendarView = (CalendarView)getView().findViewById(R.id.calendar_view);
		calendarView.setOnCalendarDaySelectedListener(this);
		calendarView.updateLayout(activity, 2017, 2, dummy);
	}

	private void onClickSchedule(ScheduleModel schedule){
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

	public List<CalendarDayModel> getDummyData(){
		List<CalendarDayModel> dummyData = new ArrayList<>();

		ScheduleModel e1 = new ScheduleModel();
		e1.startDate = "2017/07/09";
		e1.endDate = "2017/07/09";
		e1.startTime = "10:00";
		e1.endTime = "12:00";
		e1.scheduleName = "Date Thuy";
		e1.key = "1";
		e1.url = "google.com.vn";
		e1.scheduleNote = "she refused";

		ScheduleModel e2 = new ScheduleModel();
		e2.startDate = "2017/07/09";
		e2.endDate = "2017/07/09";
		e2.startTime = "08:00";
		e2.key = "1";
		e2.endTime = "11:00";
		e2.scheduleName = "Go to MocChau with ...";
		e2.scheduleNote = "done";

		CalendarDayModel d1 = new CalendarDayModel();
		d1.date = "2017/06/02";
		d1.schedules = new ArrayList<>();
		d1.schedules.add(e1);
		d1.schedules.add(e2);

		CalendarDayModel d2 = new CalendarDayModel();
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
	public void onCalendarDaySelected(CalendarDayModel reportModel){
		// // TODO: 2/8/2017
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}
}
