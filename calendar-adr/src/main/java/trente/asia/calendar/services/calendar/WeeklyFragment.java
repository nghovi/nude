package nguyenhoangviet.vpcorp.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import nguyenhoangviet.vpcorp.android.util.CsDateUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.ClPageFragment;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.PageContainerFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.view.SchedulesPagerAdapter;
import nguyenhoangviet.vpcorp.calendar.services.calendar.view.WeeklySchedulesPagerAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyFragment extends PageContainerFragment{

	public View					goldenHourView;
	public ObservableScrollView	timeScroll;
	public RelativeLayout		rltTimeContainer;
	public ImageView			imgExpand;
	public LinearLayout			lnrTimeColumn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_week, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		lnrTimeColumn = (LinearLayout)getView().findViewById(R.id.lnr_time_column);
		imgExpand = (ImageView)getView().findViewById(R.id.ic_icon_expand);
		txtToday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickFooterItemWeekly();
			}
		});
		rltTimeContainer = (RelativeLayout)getView().findViewById(R.id.rlt_time_container);

		// Add horizontal time lines
		Calendar c = CCDateUtil.makeCalendarToday();
		List<String> times = new ArrayList<>();
		for(int i = 0; i < 24; i++){
			c.set(Calendar.HOUR_OF_DAY, i);
			String startTime = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, c.getTime());
			times.add(startTime);
		}

		// times.add("");

		Collections.sort(times);

		// // first row need to subtract some px
		// addStartTimeRow(times.get(0), TIME_COLUMN_WIDTH_PX - MARGIN_LEFT_RIGHT_DP / 2);

		for(int i = 0; i < times.size(); i++){
			int bottomMargin = 0;
			if(i == times.size() - 1){
				bottomMargin = WelfareUtil.dpToPx(TIME_COLUMN_WIDTH_PX) - WelfareUtil.dpToPx(MARGIN_TEXT_MIDDLE_DP) - 1;
			}
			addStartTimeRow(times.get(i), (i) * WelfareUtil.dpToPx(TIME_COLUMN_WIDTH_PX) - WelfareUtil.dpToPx(MARGIN_TEXT_MIDDLE_DP) - 1, bottomMargin);
		}

		timeScroll = (ObservableScrollView)getView().findViewById(R.id.src_time_container);

	}

	private void addStartTimeRow(String startTime, int marginTop, int bottomMargin){
		View cell = activity.getLayoutInflater().inflate(R.layout.item_daily_schedule, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		if("00:00".equals(startTime)){
			marginTop = 0;
		}
		params.setMargins(0, marginTop, 0, bottomMargin);
		cell.setLayoutParams(params);
		((TextView)cell.findViewById(R.id.txt_item_daily_schedule_start_time)).setText(startTime);
		cell.findViewById(R.id.time_divider).setVisibility(View.INVISIBLE);
		if(startTime.equals(SchedulesPageFragment.GOLDEN_HOUR_STR)){
			goldenHourView = cell;
		}
		rltTimeContainer.addView(cell);
	}

	@Override
	protected void onFragmentSelected(ClPageFragment fragment){
		WeeklyPageFragment weeklyPageFragment = (WeeklyPageFragment)fragment;
		imgExpand.setVisibility(View.INVISIBLE);
        weeklyPageFragment.updateTimeColumnPosition();
		weeklyPageFragment.scrMain.setScrollViewListener(weeklyPageFragment);
		timeScroll.setScrollViewListener(weeklyPageFragment);
		weeklyPageFragment.scrMain.scrollTo(timeScroll.x, timeScroll.y);
		weeklyPageFragment.setOnClickListenerForExpandIcon();
		scrollNeighbors(timeScroll.x, timeScroll.y, timeScroll.oldx, timeScroll.oldy);
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

	public void scrollNeighbors(int x, int y, int oldx, int oldy){
		scrollNeighbor(leftNeighborFragment, x, y, oldx, oldy);
		scrollNeighbor(rightNeiborFragment, x, y, oldx, oldy);
	}

	public void scrollNeighbor(ClPageFragment clPageFragment, int x, int y, int oldx, int oldy){
		if(clPageFragment != null){
			ObservableScrollView scrollView = ((WeeklyPageFragment)clPageFragment).scrMain;
			if(scrollView != null){
				scrollView.setCoordinate(x, y, oldx, oldy);
				scrollView.scrollTo(x, y);
			}
		}
	}
}
