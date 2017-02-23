package trente.asia.calendar.services.calendar;

import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.view.DailySchedulesPagerAdapter;
import trente.asia.calendar.services.calendar.view.SchedulesPagerAdapter;

/**
 * DailyFragment
 *
 * @author VietNH
 */
public class DailyFragment extends PageContainerFragment {

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_daily;
    }

    @Override
    protected SchedulesPagerAdapter initPagerAdapter() {
        return new DailySchedulesPagerAdapter(getChildFragmentManager());
    }


}
