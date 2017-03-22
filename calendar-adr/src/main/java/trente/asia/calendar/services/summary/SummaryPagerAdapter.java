package trente.asia.calendar.services.summary;

import java.util.Date;

import android.support.v4.app.FragmentManager;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;
import trente.asia.calendar.commons.views.NavigationHeader;
import trente.asia.calendar.commons.views.PageSharingHolder;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class SummaryPagerAdapter extends ClFragmentPagerAdapter {

    protected NavigationHeader navigationHeader;
    protected PageSharingHolder pageSharingHolder;
    protected int initialPosition;


    public SummaryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    protected ClPageFragment getFragment() {
        return new SummaryPageFragment();
    }

    @Override
    protected Date choseSelectedDate(int position) {
        //// TODO: 3/22/2017  
        return CsDateUtil.addMonth(TODAY, position - initialPosition);
    }

}
