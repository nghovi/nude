package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.MediaRouteButton;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * DailyPageFragment
 *
 * @author Vietnh
 */
public class DailyPageFragment extends SchedulesPageFragment{

	private LinearLayout						lnrScheduleAllDays;
	private TextView							txtTodoInfo;
	private LinearLayout						lnrListSchedules;
	private Map<String, List<ScheduleModel>>	startTimeSchedulesMap;
	private ImageView							imgBirthdayIcon;

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
		imgBirthdayIcon = (ImageView)getView().findViewById(R.id.img_birthday_daily_page);
		lnrScheduleAllDays = (LinearLayout)getView().findViewById(R.id.lnr_schedule_all_day_container);
		lnrListSchedules = (LinearLayout)getView().findViewById(R.id.lnr_fragment_daily_page_schedules_time);
		lnrListSchedules.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onDailyScheduleClickListener(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate));
			}
		});
		Calendar c = CCDateUtil.makeCalendarToday();
		startTimeSchedulesMap = new HashMap<>();
		for(int i = 0; i < 24; i++){
			String startTime = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, c.getTime());
			startTimeSchedulesMap.put(startTime, new ArrayList<ScheduleModel>());
			c.add(Calendar.HOUR, 1);
		}

	}

	@Override
	protected void initCalendarHeader(){

	}

	@Override
	protected void initDayViews(){

	}

	@Override
	protected void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){
		if(!CCCollectionUtil.isEmpty(todos)){
			txtTodoInfo.setVisibility(View.VISIBLE);
			txtTodoInfo.setText(getString(R.string.two_things_todo, String.valueOf(todos.size())));
		}

		if(!CCCollectionUtil.isEmpty(lstBirthdayUser)){
			imgBirthdayIcon.setVisibility(View.VISIBLE);
		}

		lnrScheduleAllDays.removeAllViews();
		List<ScheduleModel> allDaySchedules = new ArrayList<>();
		for(String key : startTimeSchedulesMap.keySet()){
			startTimeSchedulesMap.put(key, new ArrayList<ScheduleModel>());
		}

		for(ScheduleModel scheduleModel : schedules){
			if(!scheduleModel.isAllDay){
				String keyMap = scheduleModel.startTime.split(":")[0] + ":00";
				startTimeSchedulesMap.get(keyMap).add(scheduleModel);
			}
		}

		for(ScheduleModel scheduleModel : schedules){
			if(scheduleModel.isAllDay){
				allDaySchedules.add(scheduleModel);
				TextView textView = WeeklyPageFragment.makeTextView(activity, scheduleModel.scheduleName, 0, 0, LinearLayout.LayoutParams.MATCH_PARENT, WeeklyPageFragment.getColor(scheduleModel), 0);
				lnrScheduleAllDays.addView(textView);
			}else{
				List<ScheduleModel> scheduleModels = startTimeSchedulesMap.get(scheduleModel.startTime);
				if(CCCollectionUtil.isEmpty(scheduleModels)){
					scheduleModels = new ArrayList<>();
					scheduleModels.add(scheduleModel);
					startTimeSchedulesMap.put(scheduleModel.startTime, scheduleModels);
				}else{
					scheduleModels.add(scheduleModel);
				}
			}
		}

		lnrListSchedules.removeAllViews();

		List<String> keys = new ArrayList<>(startTimeSchedulesMap.keySet());
		Collections.sort(keys);

		for(String key : keys){
			List<ScheduleModel> scheduleModels = startTimeSchedulesMap.get(key);
			addStartTimeRow(key, scheduleModels);
		}
	}

	private void addStartTimeRow(String startTime, List<ScheduleModel> scheduleModels){
		View cell = inflater.inflate(R.layout.item_daily_schedule, null);
		((TextView)cell.findViewById(R.id.txt_item_daily_schedule_start_time)).setText(startTime);
		LinearLayout lnrSchedules = (LinearLayout)cell.findViewById(R.id.lnr_schedule_list_item_daily);
		for(ScheduleModel scheduleModel : scheduleModels){
			TextView textView = new TextView(activity);
			textView.setText(scheduleModel.scheduleName);
			lnrSchedules.addView(textView);
		}
		lnrListSchedules.addView(cell);
	}

	@Override
	protected List<Date> getAllDate(){
		List<Date> result = new ArrayList<>();
		result.add(selectedDate);
		return result;
	}

	@Override
	public void onClick(View v){

	}
}
