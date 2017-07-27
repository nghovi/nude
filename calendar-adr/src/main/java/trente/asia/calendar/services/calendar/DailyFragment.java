package trente.asia.calendar.services.calendar;

import java.util.Date;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.PageContainerFragment;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;
import trente.asia.calendar.services.calendar.view.DailySchedulesPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * DailyFragment
 *
 * @author VietNH
 */
public class DailyFragment extends PageContainerFragment{

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_daily;
	}

	@Override
	protected Date getActiveDate(int position){
		return CsDateUtil.addDate(TODAY, position - INITIAL_POSITION);
	}

	@Override
	protected void setActiveDate(int position){
		Date activeDate = getActiveDate(position);
		String title = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, activeDate);
		updateHeader(title);
	}

	@Override
	protected ClFragmentPagerAdapter initPagerAdapter(){
		return new DailySchedulesPagerAdapter(getChildFragmentManager());
	}

}
