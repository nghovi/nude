package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.DailyScheduleList;
import trente.asia.welfare.adr.define.WelfareConst;

import static trente.asia.calendar.services.calendar.SchedulesPageListViewFragment.REFRESH_API_TIME_MS;

/**
 * DailyPageFragment
 *
 * @author Vietnh
 */
public class DailyPageFragment extends SchedulesPageFragment{

	private LinearLayout	lnrScheduleAllDays;
	private TextView		txtTodoInfo;
	private LinearLayout	lnrListSchedules;

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
		txtTodoInfo = (TextView)getView().findViewById(R.id.txt_todo_things);
		lnrScheduleAllDays = (LinearLayout)getView().findViewById(R.id.lnr_schedule_all_day_container);
		lnrListSchedules = (LinearLayout)getView().findViewById(R.id.lnr_fragment_daily_page_schedules_time);
	}

	protected void initCalendarView(){
		// no calendar view
	}

	@Override
	protected void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){
		if(!CCCollectionUtil.isEmpty(todos)){
			txtTodoInfo.setText(getString(R.string.two_things_todo, String.valueOf(todos.size())));
		}

		lnrScheduleAllDays.removeAllViews();
		Map<String, List<ScheduleModel>> scheduleModelMap = new HashMap<>();
		List<ScheduleModel> allDaySchedules = new ArrayList<>();
		for(ScheduleModel scheduleModel : schedules){
			if(scheduleModel.isAllDay){
				allDaySchedules.add(scheduleModel);
				TextView textView = new TextView(activity);
				textView.setText(scheduleModel.scheduleName);
				lnrScheduleAllDays.addView(textView);
			}else{
				List<ScheduleModel> scheduleModels = scheduleModelMap.get(scheduleModel.startTime);
				if(CCCollectionUtil.isEmpty(scheduleModels)){
					scheduleModels = new ArrayList<>();
					scheduleModels.add(scheduleModel);
					scheduleModelMap.put(scheduleModel.startTime, scheduleModels);
				}else{
					scheduleModels.add(scheduleModel);
				}
			}
		}

		lnrListSchedules.removeAllViews();
		for(String startTime : scheduleModelMap.keySet()){
			List<ScheduleModel> scheduleModels = scheduleModelMap.get(startTime);
			addStartTimeRow(startTime, scheduleModels);
		}
	}

	private void addStartTimeRow(String startTime, List<ScheduleModel> scheduleModels){
		View cell = inflater.inflate(R.layout.item_daily_schedule, null);
		((TextView)cell.findViewById(R.id.txt_item_daily_schedule_start_time)).setText(startTime);
		LinearLayout lnrSchedules = (LinearLayout)cell.findViewById(R.id.lnr_schedule_all_day_container);
		for(ScheduleModel scheduleModel : scheduleModels){
			TextView textView = new TextView(activity);
			textView.setText(scheduleModel.scheduleName);
			lnrSchedules.addView(textView);
		}
		lnrListSchedules.addView(cell);
	}

	@Override
	protected List<Date> getAllDate(){
		return getAllDateForMonth(prefAccUtil, selectedDate);
	}

	@Override
	public void onClick(View v){

	}
}
