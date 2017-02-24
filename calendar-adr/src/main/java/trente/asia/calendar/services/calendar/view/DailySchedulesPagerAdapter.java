package trente.asia.calendar.services.calendar.view;

import android.support.v4.app.FragmentManager;

import java.util.Date;

import trente.asia.android.util.CsDateUtil;
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
    protected Date choseSelectedDate(int position) {
        return CsDateUtil.addMonth(TODAY, position - initialPosition);
    }

    @Override
    protected SchedulesPageFragment getFragment() {
        return new DailyPageFragment();
    }

}
