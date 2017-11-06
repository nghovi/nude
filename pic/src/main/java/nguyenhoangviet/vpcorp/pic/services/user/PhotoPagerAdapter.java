package nguyenhoangviet.vpcorp.pic.services.user;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import nguyenhoangviet.vpcorp.android.util.CsDateUtil;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class PhotoPagerAdapter extends android.support.v4.app.FragmentPagerAdapter{

	protected Date choseSelectedDate(int position){
		return CsDateUtil.addDate(TODAY, position - initialPosition);
	}

	protected PhotoPageFragment getFragment(){
		return new PhotoPageFragment();
	}

	protected final Date						TODAY		= Calendar.getInstance().getTime();
	protected PageSharingHolder					pageSharingHolder;
	protected int								initialPosition;

	public Map<Integer, PhotoPageFragment>	pagesMap	= new HashMap<>();

	public PhotoPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int position){
		PhotoPageFragment fragment = pagesMap.get(position);
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

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return Integer.MAX_VALUE;
	}

	public void setPageSharingHolder(PageSharingHolder pageSharingHolder){
		this.pageSharingHolder = pageSharingHolder;
	}

	public void setInitialPosition(int initialPosition){
		this.initialPosition = initialPosition;
	}

}
