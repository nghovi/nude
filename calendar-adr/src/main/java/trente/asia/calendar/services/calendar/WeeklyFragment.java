package trente.asia.calendar.services.calendar;

import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.view.SchedulesPagerAdapter;
import trente.asia.calendar.services.calendar.view.WeeklySchedulesPagerAdapter;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyFragment extends PageContainerFragment{

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_weekly;
	}

	@Override
	protected SchedulesPagerAdapter initPagerAdapter(){
		return new WeeklySchedulesPagerAdapter(getChildFragmentManager(), activity);
	}

}
