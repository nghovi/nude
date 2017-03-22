package trente.asia.calendar.services.calendar.view;

import java.util.Date;

import android.support.v4.app.FragmentManager;

import asia.chiase.core.util.CCDateUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.services.calendar.MonthlyPageFragment;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;

/**
 * BoardPagerAdapter
 *
 * @author TrungND
 */
public class MonthlyCalendarPagerAdapter extends SchedulesPagerAdapter{

	public MonthlyCalendarPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	protected Date choseSelectedDate(int position){
		Date date = new Date();
		Date firstDay = CCDateUtil.makeDateWithFirstday(date);
		return CsDateUtil.addMonth(firstDay, position - initialPosition);
	}

	@Override
	protected ClPageFragment getFragment(){
		return new MonthlyPageFragment();
	}
}
