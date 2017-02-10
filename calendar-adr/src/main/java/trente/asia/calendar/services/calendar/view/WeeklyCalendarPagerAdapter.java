package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import asia.chiase.core.util.CCDateUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.services.calendar.MonthlyPageFragment;
import trente.asia.calendar.services.calendar.WeeklyPageFragment;

/**
 * WeeklyCalendarPagerAdapter
 *
 * @author TrungND
 */
public class WeeklyCalendarPagerAdapter extends FragmentPagerAdapter{

	private final int	ACTIVE_PAGE	= Integer.MAX_VALUE / 2;
	private final Date	TODAY		= CsDateUtil.makeMonthWithFirstDate();

	public WeeklyCalendarPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int position){
		Date activeMonth = CsDateUtil.addMonth(TODAY, position - ACTIVE_PAGE);
		Calendar activeCalendar = CCDateUtil.makeCalendar(activeMonth);
		activeCalendar.setFirstDayOfWeek(Calendar.THURSDAY);

        WeeklyPageFragment weeklyPageFragment = new WeeklyPageFragment();
        weeklyPageFragment.setSelectedDate(activeMonth);
		return weeklyPageFragment;
	}

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return Integer.MAX_VALUE;
	}
}
