package trente.asia.calendar.services.calendar.view;

import java.util.Date;

import android.support.v4.app.FragmentManager;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.services.calendar.DailyPageFragment;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class DailySchedulesPagerAdapter extends SchedulesPagerAdapter{

	public DailySchedulesPagerAdapter(FragmentManager fm, OnChangeCalendarUserListener listener){
		super(fm, listener);
	}

	@Override
	protected Date choseSelectedDate(int position){
		return CsDateUtil.addMonth(TODAY, position - initialPosition);
	}

	@Override
	protected SchedulesPageFragment getFragment(){
		return new DailyPageFragment();
	}

}
