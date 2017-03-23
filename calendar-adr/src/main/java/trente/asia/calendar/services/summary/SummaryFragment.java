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
	protected boolean isSwipeRightToLeftOnly(){
		return true;
	}

	@Override
	protected void setWizardProgress(int progress){
		if(progress > ((SummaryPagerAdapter)mPagerAdapter).getMProgress()){
			((SummaryPagerAdapter)mPagerAdapter).setMProgress(progress);
			mPagerAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_summary;
	}
}
