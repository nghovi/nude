package trente.asia.calendar.services.calendar.view;

import java.util.Date;

import android.support.v4.app.FragmentManager;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.services.calendar.DailyPageFragment;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class DailySchedulesPagerAdapter extends SchedulesPagerAdapter{

	public DailySchedulesPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	protected Date choseSelectedDate(int position){
		return CsDateUtil.addMonth(TODAY, position - initialPosition);
	}

	@Override
	protected ClPageFragment getFragment(){
		return new DailyPageFragment();
	}

}
