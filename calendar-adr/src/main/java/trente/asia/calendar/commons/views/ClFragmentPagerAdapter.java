package trente.asia.calendar.commons.views;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import trente.asia.calendar.commons.fragments.ClPageFragment;

/**
 * ClFragmentPagerAdapter
 *
 * @author Vietnh
 */
public abstract class ClFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter{

	protected final Date					TODAY		= Calendar.getInstance().getTime();
	protected NavigationHeader				navigationHeader;
	protected PageSharingHolder				pageSharingHolder;
	protected int							initialPosition;

	protected Map<Integer, ClPageFragment>	pagesMap	= new HashMap<>();

	public ClFragmentPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int position){
		ClPageFragment fragment = pagesMap.get(position);
		if(fragment == null){
			fragment = getFragment();
			Date selectedDate = choseSelectedDate(position);
			fragment.setSelectedDate(selectedDate);
			fragment.setPagePosition(position);
			fragment.setPageSharingHolder(pageSharingHolder);
			pagesMap.put(position, fragment);
		}
		return fragment;
	}

	protected abstract Date choseSelectedDate(int position);

	protected abstract ClPageFragment getFragment();

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return Integer.MAX_VALUE;
	}

	public void setNavigationHeader(NavigationHeader navigationHeader){
		this.navigationHeader = navigationHeader;
	}

	public void setPageSharingHolder(PageSharingHolder pageSharingHolder){
		this.pageSharingHolder = pageSharingHolder;
	}

	public void setInitialPosition(int initialPosition){
		this.initialPosition = initialPosition;
	}
}
