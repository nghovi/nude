package nguyenhoangviet.vpcorp.calendar.services.calendar.view;

import java.util.Date;

import android.support.v4.app.FragmentManager;

import nguyenhoangviet.vpcorp.android.util.CsDateUtil;
import nguyenhoangviet.vpcorp.calendar.services.calendar.SchedulesPageFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.WeeklyPageFragment;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class WeeklySchedulesPagerAdapter extends SchedulesPagerAdapter{

	public WeeklySchedulesPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	protected Date choseSelectedDate(int position){
		return CsDateUtil.addWeek(TODAY, position - initialPosition);
	}

	@Override
	protected SchedulesPageFragment getFragment(){
		return new WeeklyPageFragment();
	}

}
