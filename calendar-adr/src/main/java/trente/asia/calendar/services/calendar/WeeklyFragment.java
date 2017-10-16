package trente.asia.calendar.services.calendar;

import android.view.View;

import java.util.Date;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.PageContainerFragment;
import trente.asia.calendar.services.calendar.view.SchedulesPagerAdapter;
import trente.asia.calendar.services.calendar.view.WeeklySchedulesPagerAdapter;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyFragment extends PageContainerFragment{

	@Override
	protected void initView(){
		super.initView();
		txtToday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickFooterItemWeekly();
			}
		});
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_weekly;
	}

	@Override
	protected SchedulesPagerAdapter initPagerAdapter(){
		return new WeeklySchedulesPagerAdapter(getChildFragmentManager());
	}

	@Override
	protected Date getActiveDate(int position){
		return CsDateUtil.addWeek(TODAY, position - INITIAL_POSITION);
	}
}
