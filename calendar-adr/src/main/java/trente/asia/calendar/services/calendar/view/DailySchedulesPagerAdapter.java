package nguyenhoangviet.vpcorp.calendar.services.calendar.view;

import java.util.Date;

import android.support.v4.app.FragmentManager;

import nguyenhoangviet.vpcorp.android.util.CsDateUtil;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.ClPageFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.DailyPageFragment;

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
		return CsDateUtil.addDate(TODAY, position - initialPosition);
	}

	@Override
	protected ClPageFragment getFragment(){
		return new DailyPageFragment();
	}

}
