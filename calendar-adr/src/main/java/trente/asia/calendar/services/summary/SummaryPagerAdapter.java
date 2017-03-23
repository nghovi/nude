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
	private int				mProgress;

	public SummaryPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public int getCount(){
		return mProgress;
	}

	@Override
	protected ClPageFragment getFragment(){
		return new SummaryPageFragment();
	}

	@Override
	protected Date choseSelectedDate(int position){
		return CsDateUtil.addMonth(TODAY, GRAPH_COLUMN_NUM * (position - initialPosition));
	}

	public int getMProgress(){
		return mProgress;
	}

	public void setMProgress(int mProgress){
		this.mProgress = mProgress;
	}
}
