package trente.asia.calendar.services.summary;

import java.util.Date;

import android.support.v4.app.FragmentManager;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class SummaryPagerAdapter extends ClFragmentPagerAdapter{

	public static final int	GRAPH_COLUMN_NUM	= 4;

	public SummaryPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	protected ClPageFragment getFragment(){
		return new SummaryPageFragment();
	}

	@Override
	protected Date choseSelectedDate(int position){
		return CsDateUtil.addMonth(TODAY, GRAPH_COLUMN_NUM * (position - initialPosition));
	}

}
