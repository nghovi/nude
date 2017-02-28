package trente.asia.calendar.services.calendar.view;

import java.util.Date;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.services.calendar.MonthlyPageFragment;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;

/**
 * BoardPagerAdapter
 *
 * @author TrungND
 */
public class MonthlyCalendarPagerAdapter extends SchedulesPagerAdapter{

	public MonthlyCalendarPagerAdapter(FragmentManager fm, OnChangeCalendarUserListener listener){
		super(fm);
		this.listener = listener;
	}

	@Override
	Date choseSelectedDate(int position){
		return CsDateUtil.addMonth(TODAY, position - initialPosition);
	}

	@Override
	SchedulesPageFragment getFragment(){
		return new MonthlyPageFragment();
	}
}
