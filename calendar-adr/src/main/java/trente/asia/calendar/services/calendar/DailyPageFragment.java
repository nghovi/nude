package trente.asia.calendar.services.calendar;

import java.util.Date;
import java.util.List;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.view.DailyScheduleList;
import trente.asia.welfare.adr.activity.WelfareActivity;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_daily_page, container, false);
		}else{
			boolean isUpdate = CCConst.YES.equals(((WelfareActivity)activity).dataMap.get(ClConst.ACTION_SCHEDULE_UPDATE));
			boolean isDelete = CCConst.YES.equals(((WelfareActivity)activity).dataMap.get(ClConst.ACTION_SCHEDULE_DELETE));
			if(isUpdate || isDelete){
				((WelfareActivity)activity).dataMap.clear();
				((ChiaseActivity)activity).isInitData = true;
			}
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
		Date firstDateOfMonth = CCDateUtil.makeDateWithFirstday(selectedDate);
		List<Date> dates = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(firstDateOfMonth), CCNumberUtil.toInteger(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK));
		return dates;
	}

	@Override
	protected void updateObservableScrollableView(){
		dailyScheduleList.updateFor(selectedDate, lstSchedule, null, null, null);
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

	}

	@Override
	public void onDownMotionEvent(){

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState){
		if(scrollState == ScrollState.UP){
			lnrCalendarContainer.setVisibility(View.GONE);
		}else if(scrollState == ScrollState.DOWN){
			lnrCalendarContainer.setVisibility(View.VISIBLE);
		}
	}

	protected String getUpperTitle(){
		return CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, selectedDate);
	}

	@Override
	public void onClick(View v){

	}
}
