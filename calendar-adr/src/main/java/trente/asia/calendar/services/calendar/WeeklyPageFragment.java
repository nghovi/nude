package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.welfare.adr.activity.WelfareFragment;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends WelfareFragment implements ObservableScrollViewCallbacks,CalendarView.OnCalendarDaySelectedListener{

	private ObservableListView	lstCalendarDay;
	private Date				selectedDate;

	private LinearLayout		lnrContentHeader;

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

		lnrContentHeader = (LinearLayout)getView().findViewById(R.id.lnr_id_content_header);
		lstCalendarDay = (ObservableListView)getView().findViewById(R.id.lst_calendar_day);
		lstCalendarDay.setScrollViewCallbacks(this);

		initContentHeader();
	}

	private void initContentHeader(){
		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// add calendar title
		View titleView = mInflater.inflate(R.layout.monthly_calendar_title, null);
		LinearLayout lnrRowTitle = (LinearLayout)titleView.findViewById(R.id.lnr_id_row_title);
		for(DayModel dayModel : CsDateUtil.getAllDay4Week(Calendar.THURSDAY)){
			View titleItem = mInflater.inflate(R.layout.monthly_calendar_title_item, null);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			titleItem.setLayoutParams(layoutParams);
			TextView txtTitleItem = (TextView)titleItem.findViewById(R.id.txt_id_row_content);
			if(Calendar.SUNDAY == dayModel.dayOfWeek){
				txtTitleItem.setTextColor(Color.RED);
			}else if(Calendar.SATURDAY == dayModel.dayOfWeek){
				txtTitleItem.setTextColor(Color.BLUE);
			}
			txtTitleItem.setText(dayModel.day);
			lnrRowTitle.addView(titleItem);
		}
		lnrContentHeader.addView(titleView);

		List<Date> lstDate = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(selectedDate));
		View rowView = null;
		LinearLayout lnrRowContent = null;
		for(int index = 0; index < lstDate.size(); index++){
			Date itemDate = lstDate.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = mInflater.inflate(R.layout.monthly_calendar_row, null);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				lnrContentHeader.addView(rowView);
			}

			WeeklyCalendarDayView dayView = new WeeklyCalendarDayView(activity);
			dayView.initialization(itemDate);

			lnrRowContent.addView(dayView);
		}
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
		List<CalendarDayModel> dummy = getDummyData();
		CalendarDayListAdapter adapter = new CalendarDayListAdapter(activity, R.layout.item_calendar_day, dummy, new CalendarDayListAdapter.OnScheduleClickListener() {

			@Override
			public void onClick(ScheduleModel schedule){
				onClickSchedule(schedule);
			}
		});
		lstCalendarDay.setAdapter(adapter);
	}

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
		// CalendarView calendarView = (CalendarView)getView().findViewById(R.id.calendar_view);
		// if(scrollState == ScrollState.UP){
		// calendarView.showWeekOnly(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, selectedDate));// // TODO: 2/8/2017
		// }else if(scrollState == ScrollState.DOWN){
		// calendarView.showAll(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, selectedDate));// // TODO: 2/8/2017
		// }
	}

	@Override
	public void onCalendarDaySelected(CalendarDayModel reportModel){
		// // TODO: 2/8/2017
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}
}
