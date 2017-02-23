package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.services.calendar.CalendarListFragment;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.WeeklyPageFragment;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class SchedulesPagerAdapter extends FragmentPagerAdapter {

    private final int ACTIVE_PAGE = Integer.MAX_VALUE / 2;
    private final Date TODAY = Calendar.getInstance().getTime();
    private NavigationHeader navigationHeader;
    private PageSharingHolder pageSharingHolder;

    public SchedulesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Date activeDate = CsDateUtil.addWeek(TODAY, position - ACTIVE_PAGE);
        SchedulesPageFragment fragment = getFragment();
        fragment.setPageSharingHolder(this.pageSharingHolder);
        fragment.setSelectedDate(activeDate);
        return fragment;
    }

    protected SchedulesPageFragment getFragment() {
        return new SchedulesPageFragment();
    }

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public void setNavigationHeader(NavigationHeader navigationHeader) {
        this.navigationHeader = navigationHeader;
    }

    public void setPageSharingHolder(PageSharingHolder pageSharingHolder) {
        this.pageSharingHolder = pageSharingHolder;
    }
}
