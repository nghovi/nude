package trente.asia.dailyreport.services.kpi.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import trente.asia.dailyreport.services.kpi.ActualPlanAddFragment;

/**
 * Created by hviet on 6/28/17.
 */

public class CalendarPagerAdapter extends android.support.v4.app.FragmentPagerAdapter{

	private KpiCalendarView.OnDayClickedListener	onDayClickListener;
	private Map<Integer, CalendarFragment>			fragmentMap	= new HashMap<>();

	public CalendarPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int position){

		CalendarFragment calendarFragment = fragmentMap.get(position);
		if(calendarFragment == null){
			calendarFragment = new CalendarFragment();
			fragmentMap.put(position, calendarFragment);
		}
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, position - Integer.MAX_VALUE / 2);
		calendarFragment.setCalendar(c);
		calendarFragment.setOnDayClickListener(this.onDayClickListener);
		return calendarFragment;
	}

	@Override
	public int getCount(){
		return Integer.MAX_VALUE;
	}

	public void setOnDayClickListener(KpiCalendarView.OnDayClickedListener onDayClickListener){
		this.onDayClickListener = onDayClickListener;
	}
}
