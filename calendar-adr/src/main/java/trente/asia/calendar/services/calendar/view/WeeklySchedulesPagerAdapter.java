package trente.asia.calendar.services.calendar.view;

import android.support.v4.app.FragmentManager;

import java.util.Date;

import trente.asia.android.util.CsDateUtil;
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
    protected Date choseSelectedDate(int position) {
        return CsDateUtil.addWeek(TODAY, position - initialPosition);
    }

    @Override
    protected SchedulesPageFragment getFragment() {
        return new WeeklyPageFragment();
    }

}
