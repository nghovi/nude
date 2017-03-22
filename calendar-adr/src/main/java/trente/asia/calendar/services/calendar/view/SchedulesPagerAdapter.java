package trente.asia.calendar.services.calendar.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public abstract class SchedulesPagerAdapter extends ClFragmentPagerAdapter{

	protected OnChangeCalendarUserListener	listener;

	public SchedulesPagerAdapter(FragmentManager fm){
		super(fm);
	}

	public SchedulesPagerAdapter(FragmentManager fm, OnChangeCalendarUserListener listener){
		super(fm);
		this.listener = listener;
	}

	@Override
	public Fragment getItem(int position){
		SchedulesPageFragment clPageFragment = (SchedulesPageFragment)super.getItem(position);
		clPageFragment.setChangeCalendarUserListener(listener);
		return clPageFragment;
	}
}
