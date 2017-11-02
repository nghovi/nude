package nguyenhoangviet.vpcorp.calendar.services.calendar.view;

import android.support.v4.app.FragmentManager;

import nguyenhoangviet.vpcorp.calendar.commons.views.ClFragmentPagerAdapter;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public abstract class SchedulesPagerAdapter extends ClFragmentPagerAdapter{

	public SchedulesPagerAdapter(FragmentManager fm){
		super(fm);
	}
}
