package trente.asia.calendar.services.calendar.view;

import android.support.v4.app.FragmentManager;

import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.WeeklyPageFragment;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class WeeklySchedulesPagerAdapter extends SchedulesPagerAdapter {


    public WeeklySchedulesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    protected SchedulesPageFragment getFragment() {
        return new WeeklyPageFragment();
    }

}
