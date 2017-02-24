package trente.asia.calendar.services.calendar.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;
import java.util.Date;

import trente.asia.calendar.services.calendar.SchedulesPageFragment;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class SchedulesPagerAdapter extends FragmentPagerAdapter {

    protected final Date TODAY = Calendar.getInstance().getTime();
    protected NavigationHeader navigationHeader;
    protected PageSharingHolder pageSharingHolder;
    protected int initialPosition;

    public SchedulesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Date selectedDate = choseSelectedDate(position);
        SchedulesPageFragment fragment = getFragment();
        fragment.setPageSharingHolder(this.pageSharingHolder);
        fragment.setSelectedDate(selectedDate);
        fragment.setPagePosition(position);
        return fragment;
    }

    protected Date choseSelectedDate(int position) {
        return TODAY;
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

    public void setInitialPosition(int initialPosition) {
        this.initialPosition = initialPosition;
    }
}
