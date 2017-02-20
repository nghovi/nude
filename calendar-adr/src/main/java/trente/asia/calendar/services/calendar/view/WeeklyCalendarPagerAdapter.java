package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.services.calendar.CalendarListFragment;
import trente.asia.calendar.services.calendar.WeeklyPageFragment;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * WeeklyCalendarPagerAdapter
 *
 * @author TrungND
 */
public class WeeklyCalendarPagerAdapter extends FragmentPagerAdapter{

	private final int	ACTIVE_PAGE	= Integer.MAX_VALUE / 2;
	// private final Date TODAY = CsDateUtil.makeMonthWithFirstDate();
	private final Date	TODAY		= Calendar.getInstance().getTime();

	public WeeklyCalendarPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int position){
		Date activeDate = CsDateUtil.addWeek(TODAY, position - ACTIVE_PAGE);
		// Calendar activeCalendar = CCDateUtil.makeCalendar(activeMonth);
		// activeCalendar.setFirstDayOfWeek(Calendar.THURSDAY);

		WeeklyPageFragment weeklyPageFragment = new WeeklyPageFragment();
		weeklyPageFragment.setSelectedDate(activeDate);
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
