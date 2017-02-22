package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.services.calendar.DailyPageFragment;

/**
 * WeeklyCalendarPagerAdapter
 *
 * @author TrungND
 */
public class DailyCalendarPagerAdapter extends FragmentPagerAdapter{

	private final int	ACTIVE_PAGE	= Integer.MAX_VALUE / 2;
	// private final Date TODAY = CsDateUtil.getFirstDateOfCurrentMonth();
	private final Date	TODAY		= Calendar.getInstance().getTime();
	private NavigationHeader navigationHeader;

	public DailyCalendarPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int position){
		Date activeDate = CsDateUtil.addDate(TODAY, position - ACTIVE_PAGE);
		// Calendar activeCalendar = CCDateUtil.makeCalendar(activeMonth);
		// activeCalendar.setFirstDayOfWeek(Calendar.THURSDAY);

		DailyPageFragment dailyPageFragment = new DailyPageFragment();
		dailyPageFragment.setNavigationHeader(navigationHeader);
		dailyPageFragment.setSelectedDate(activeDate);
		return dailyPageFragment;
	}

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return Integer.MAX_VALUE;
	}

	public void setNavigationHeader(NavigationHeader navigationHeader) {
		this.navigationHeader = navigationHeader;
	}
}
