package trente.asia.calendar.services.calendar.view;

import android.support.v4.app.FragmentManager;

import trente.asia.calendar.services.calendar.DailyPageFragment;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class DailySchedulesPagerAdapter extends SchedulesPagerAdapter {


    public DailySchedulesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    protected SchedulesPageFragment getFragment() {
        return new DailyPageFragment();
    }

}
