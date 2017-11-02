package nguyenhoangviet.vpcorp.calendar.services.calendar.view;

import java.util.Date;

import android.support.v4.app.FragmentManager;

import asia.chiase.core.util.CCDateUtil;
import nguyenhoangviet.vpcorp.android.util.CsDateUtil;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.ClPageFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.MonthlyPageFragment;

/**
 * MonthlyCalendarPagerAdapter
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
