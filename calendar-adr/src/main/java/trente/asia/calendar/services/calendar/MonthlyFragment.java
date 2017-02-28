package trente.asia.calendar.services.calendar;

import java.util.Date;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarPagerAdapter;
import trente.asia.calendar.services.calendar.view.SchedulesPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class MonthlyFragment extends PageContainerFragment{

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_monthly;
	}

	@Override
	protected void setActiveDate(int position){
		Date activeMonth = CsDateUtil.addMonth(TODAY, position - INITIAL_POSITION);
		prefAccUtil.set(ClConst.PREF_ACTIVE_DATE, CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, activeMonth));
	}

	@Override
	SchedulesPagerAdapter initPagerAdapter(){
		return new MonthlyCalendarPagerAdapter(getChildFragmentManager(), changeCalendarUserListener);
	}
}
