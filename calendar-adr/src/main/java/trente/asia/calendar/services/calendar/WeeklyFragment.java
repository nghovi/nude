package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarDay;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;

import static trente.asia.calendar.services.calendar.model.CalendarDay.*;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyFragment extends AbstractClFragment{

	private ListView				lstCalendarDay;
	private CalendarDayListAdapter	adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_weekly, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, "Weekly", null);
		lstCalendarDay = (ListView)getView().findViewById(R.id.lst_calendar_day);
		adapter = new CalendarDayListAdapter(activity, R.layout.item_calendar_day, getDummyData());
		lstCalendarDay.setAdapter(adapter);
	}

	@Override
	protected void initData(){
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_weekly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public List<CalendarDay> getDummyData(){
		List<CalendarDay> dummyData = new ArrayList<>();

		CalendarDay.Event e1 = new CalendarDay.Event();
		e1.startTime = "10:00";
		e1.endTime = "12:00";

		CalendarDay.Event e2 = new CalendarDay.Event();
		e2.startTime = "08:00";
		e2.endTime = "11:00";

		CalendarDay d1 = new CalendarDay();
		d1.date = "2017/06/02";
		d1.events = new ArrayList<>();
		d1.events.add(e1);
		d1.events.add(e2);

		CalendarDay d2 = new CalendarDay();
		d2.date = "2017/08/02";
		d2.events = new ArrayList<>();
		d2.events.add(e1);
		d2.events.add(e2);

		dummyData.add(d1);
		dummyData.add(d2);
		return dummyData;
	}
}
