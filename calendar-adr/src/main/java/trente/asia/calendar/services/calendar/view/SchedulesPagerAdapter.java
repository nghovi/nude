package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public abstract class SchedulesPagerAdapter extends FragmentPagerAdapter{

	protected final Date							TODAY		= Calendar.getInstance().getTime();
	protected NavigationHeader						navigationHeader;
	protected PageSharingHolder						pageSharingHolder;
	protected int									initialPosition;

	protected Map<Integer, SchedulesPageFragment>	pagesMap	= new HashMap<>();
	protected OnChangeCalendarUserListener			listener;

	public SchedulesPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int position){
		SchedulesPageFragment fragment = pagesMap.get(position);
		if(fragment == null){
			fragment = getFragment();
			Date selectedDate = choseSelectedDate(position);
			fragment.setPageSharingHolder(this.pageSharingHolder);
			fragment.setSelectedDate(selectedDate);
			fragment.setPagePosition(position);
			fragment.setChangeCalendarUserListener(listener);
			pagesMap.put(position, fragment);
		}
		return fragment;
	}

	abstract Date choseSelectedDate(int position);

	abstract SchedulesPageFragment getFragment();

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
