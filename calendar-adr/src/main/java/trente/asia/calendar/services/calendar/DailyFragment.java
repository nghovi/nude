package trente.asia.calendar.services.calendar;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;
import trente.asia.calendar.services.calendar.view.DailySchedulesPagerAdapter;

/**
 * DailyFragment
 *
 * @author VietNH
 */
public class DailyFragment extends SchedulesPageContainerFragment{

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_daily;
	}

	@Override
	protected ClFragmentPagerAdapter initPagerAdapter(){
		return new DailySchedulesPagerAdapter(getChildFragmentManager());
	}

}
