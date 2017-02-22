package trente.asia.calendar.services.calendar.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.services.calendar.MonthlyPageFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;

/**
 * BoardPagerAdapter
 *
 * @author TrungND
 */
public class MonthlyCalendarPagerAdapter extends FragmentPagerAdapter{

	private final int							ACTIVE_PAGE	= Integer.MAX_VALUE / 2;
	private final Date							TODAY		= CsDateUtil.getFirstDateOfCurrentMonth();
	private Map<Integer, MonthlyPageFragment>	monthlyMap	= new HashMap<>();

	private OnChangeCalendarUserListener		listener;

	public MonthlyCalendarPagerAdapter(FragmentManager fm, OnChangeCalendarUserListener listener){
		super(fm);
        this.listener = listener;
	}

	@Override
	public Fragment getItem(int position){
		MonthlyPageFragment monthlyPageFragment = this.monthlyMap.get(position);
		if(monthlyPageFragment == null){
			Date activeMonth = CsDateUtil.addMonth(TODAY, position - ACTIVE_PAGE);
			monthlyPageFragment = new MonthlyPageFragment();
			monthlyPageFragment.setActiveMonth(activeMonth);
            monthlyPageFragment.setChangeCalendarUserListener(listener);
			this.monthlyMap.put(position, monthlyPageFragment);
		}

		return monthlyPageFragment;
	}

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return Integer.MAX_VALUE;
	}
}
