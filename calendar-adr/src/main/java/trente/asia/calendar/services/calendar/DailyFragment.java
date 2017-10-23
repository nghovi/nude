package trente.asia.calendar.services.calendar;

import java.text.SimpleDateFormat;
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
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.commons.fragments.PageContainerFragment;
import trente.asia.calendar.services.calendar.view.DailySchedulesPagerAdapter;
import trente.asia.calendar.services.calendar.view.SchedulesPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * DailyFragment
 *
 * @author VietNH
 */
public class DailyFragment extends PageContainerFragment{

	public View					goldenHourView;
	public ObservableScrollView	timeScroll;
	public RelativeLayout		rltTimeContainer;
	public ImageView			imgExpand;
	public LinearLayout			lnrTimeColumn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_day, container, false);
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
				onClickFooterItemDaily();
			}
		});
		rltTimeContainer = (RelativeLayout)getView().findViewById(R.id.rlt_time_container);

		// Add horizontal time lines
		Calendar c = CCDateUtil.makeCalendarToday();
		List<String> times = new ArrayList<>();
		for(int i = 1; i < 24; i++){
			c.set(Calendar.HOUR_OF_DAY, i);
			String startTime = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, c.getTime());
			times.add(startTime);
		}

		// times.add("");

		Collections.sort(times);

		// // first row need to subtract some px
		// addStartTimeRow(times.get(0), TIME_COLUMN_WIDTH_PX - MARGIN_LEFT_RIGHT_PX / 2);

		for(int i = 0; i < times.size(); i++){
			int bottomMargin = 0;
			if(i == times.size() - 1){
				bottomMargin = WelfareUtil.dpToPx(TIME_COLUMN_WIDTH_PX) - WelfareUtil.dpToPx(MARGIN_TEXT_MIDDLE_PX) - 1;
			}
			addStartTimeRow(times.get(i), (i + 1) * WelfareUtil.dpToPx(TIME_COLUMN_WIDTH_PX) - WelfareUtil.dpToPx(MARGIN_TEXT_MIDDLE_PX) - 1, bottomMargin);
		}

		timeScroll = (ObservableScrollView)getView().findViewById(R.id.src_time_container);
	}

	private void addStartTimeRow(String startTime, int marginTop, int bottomMargin){
		View cell = activity.getLayoutInflater().inflate(R.layout.item_daily_schedule, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
		DailyPageFragment dailyPageFragment = (DailyPageFragment)fragment;
		imgExpand.setVisibility(View.INVISIBLE);
		dailyPageFragment.updateTimeColumnPosition();
		dailyPageFragment.scrMain.setScrollViewListener(dailyPageFragment);
		timeScroll.setScrollViewListener(dailyPageFragment);
		dailyPageFragment.scrMain.scrollTo(timeScroll.x, timeScroll.y);
		dailyPageFragment.setOnClickListenerForExpandIcon();
		scrollNeighbors(timeScroll.x, timeScroll.y, timeScroll.oldx, timeScroll.oldy);
	}

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
		String title = CCFormatUtil.formatDateCustom("yyyy/M/d",activeDate);
		updateHeader(title);
	}

	@Override
	protected SchedulesPagerAdapter initPagerAdapter(){
		return new DailySchedulesPagerAdapter(getChildFragmentManager());
	}

	public void scrollNeighbors(int x, int y, int oldx, int oldy){
		scrollNeighbor(leftNeighborFragment, x, y, oldx, oldy);
		scrollNeighbor(rightNeiborFragment, x, y, oldx, oldy);
	}

	public void scrollNeighbor(ClPageFragment clPageFragment, int x, int y, int oldx, int oldy){
		if(clPageFragment != null){
			ObservableScrollView scrollView = ((DailyPageFragment)clPageFragment).scrSchedules;
			if(scrollView != null){
				scrollView.setCoordinate(x, y, oldx, oldy);
				scrollView.scrollTo(x, y);
			}
		}
	}
}
