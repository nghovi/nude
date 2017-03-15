package trente.asia.calendar.services.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.view.DailyScheduleList;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * DailyPageFragment
 *
 * @author Vietnh
 */
public class DailyPageFragment extends SchedulesPageListViewFragment implements DailyScheduleClickListener,ObservableScrollViewCallbacks{

	private ClDialog				dialogScheduleList;
	private ObservableScrollView	observableScrollView;

	private DailyScheduleList		dailyScheduleList;
	private boolean					canScroll	= false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_daily_page, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		observableScrollView = (ObservableScrollView)getView().findViewById(R.id.scr_calendar_day);
		observableScrollView.setScrollViewCallbacks(this);
		dailyScheduleList = (DailyScheduleList)getView().findViewById(R.id.lnr_view_daily_schedules);
		dailyScheduleList.init(getLayoutInflater(null), this);
		initDialog();
	}

	private void initDialog(){
		dialogScheduleList = new ClDialog(activity);
		dialogScheduleList.setDialogScheduleList();
	}

	@Override
	protected List<Date> getAllDate(){
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		Date firstDateOfMonth = CCDateUtil.makeDateWithFirstday(selectedDate);
		List<Date> dates = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(firstDateOfMonth), firstDay);
		return dates;
	}

	@Override
	protected void updateObservableScrollableView(){
        canScroll = false;
		dailyScheduleList.updateFor(selectedDate, lstSchedule, lstHoliday, lstWorkOffer, lstBirthdayUser);
	}

	@Override
	public void updateList(String dayStr){
		selectedDate = CCDateUtil.makeDateCustom(dayStr, WelfareConst.WL_DATE_TIME_7);
		updateObservableScrollableView();
	}

	@Override
	public void onDailyScheduleClickListener(String day){
		dialogScheduleList.show();
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging){
        if(firstScroll){
            canScroll = true;
        }
	}

	@Override
	public void onDownMotionEvent(){

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState){
		if(canScroll){
			if(scrollState == ScrollState.UP){
				lnrCalendarContainer.setVisibility(View.GONE);
			}else if(scrollState == ScrollState.DOWN){
				lnrCalendarContainer.setVisibility(View.VISIBLE);
			}
		}
	}

	protected String getUpperTitle(){
		return CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, selectedDate);
	}

	@Override
	public void onClick(View v){

	}
}
