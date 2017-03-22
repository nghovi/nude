package trente.asia.calendar.services.calendar;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class MonthlyFragment extends SchedulesPageContainerFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_monthly;
	}

	@Override
	protected void setActiveDate(int position){
		Date activeDate = CsDateUtil.addMonth(TODAY, position - INITIAL_POSITION);
		prefAccUtil.set(ClConst.PREF_ACTIVE_DATE, CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, activeDate));

		String title = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, activeDate);
		TextView txtHeaderTitle = (TextView)getView().findViewById(R.id.txt_id_header_title);
		txtHeaderTitle.setText(title);
	}

	@Override
	protected ClFragmentPagerAdapter initPagerAdapter(){
		return new MonthlyCalendarPagerAdapter(getChildFragmentManager());
	}
}
