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
import trente.asia.calendar.commons.fragments.PageContainerFragment;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class MonthlyFragment extends PageContainerFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		txtToday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickFooterItemMonthly();
			}
		});
	}

	@Override
	protected Date getActiveDate(int position){
		return CsDateUtil.addMonth(TODAY, position - INITIAL_POSITION);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_monthly;
	}

	@Override
	protected ClFragmentPagerAdapter initPagerAdapter(){
		return new MonthlyCalendarPagerAdapter(getChildFragmentManager());
	}
}
