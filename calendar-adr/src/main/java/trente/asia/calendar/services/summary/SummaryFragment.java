package trente.asia.calendar.services.summary;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.PageContainerFragment;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;

/**
 * SummaryFragment
 *
 * @author VietNH
 */
public class SummaryFragment extends PageContainerFragment{

	@Override
	protected boolean isShowUserList(){
		return false;
	}

	@Override
	protected ClFragmentPagerAdapter initPagerAdapter(){
		return new SummaryPagerAdapter(getChildFragmentManager());
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_summary;
	}
}
