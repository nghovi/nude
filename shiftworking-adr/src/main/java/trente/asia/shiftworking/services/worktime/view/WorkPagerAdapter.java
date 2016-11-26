package trente.asia.shiftworking.services.worktime.view;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import trente.asia.shiftworking.services.shiftworking.view.CommonMonthView;
import trente.asia.shiftworking.services.worktime.WorkerFragment;

/**
 * WorkPagerAdapter
 *
 * @author TrungND
 */
public class WorkPagerAdapter extends FragmentStatePagerAdapter{

	private List<Fragment> lstFragment;
    private CommonMonthView monthView;

	public WorkPagerAdapter(FragmentManager fragmentManager, List<Fragment> lstFragment){
		super(fragmentManager);
		this.lstFragment = lstFragment;
        this.monthView = monthView;
	}

	@Override
	public Fragment getItem(int position){
		Fragment fragment = this.lstFragment.get(position);
		return fragment;
	}

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return lstFragment.size();
	}
}
