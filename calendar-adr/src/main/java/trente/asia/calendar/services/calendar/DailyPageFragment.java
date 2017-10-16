package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.activity.ChiaseFragment;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkRequest;
import trente.asia.calendar.services.todo.TodoListFragment;
import trente.asia.calendar.services.todo.TodoListTodayFragment;
import trente.asia.welfare.adr.define.WelfareConst;

import static trente.asia.calendar.services.calendar.WeeklyPageFragment.getWorkRequestComparator;

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
	private int									moreNumber;
	private boolean								firstTime	= true;
	private int									numRow;

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
		txtTodoInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				gotoTodoListTodayFragment(selectedDate);
			}
		});
		imgBirthdayIcon = (ImageView)getView().findViewById(R.id.img_birthday_daily_page);
		lnrScheduleAllDays = (LinearLayout)getView().findViewById(R.id.lnr_schedule_all_day_container);
		lnrScheduleAllDays.setOnClickListener(this);
		lnrListSchedules = (LinearLayout)getView().findViewById(R.id.lnr_fragment_daily_page_schedules_time);
		lnrListSchedules.setOnClickListener(this);
		Calendar c = CCDateUtil.makeCalendarToday();
		startTimeSchedulesMap = new HashMap<>();
		for(int i = 0; i < 24; i++){
			String startTime = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, c.getTime());
			startTimeSchedulesMap.put(startTime, new ArrayList<ScheduleModel>());
			c.add(Calendar.HOUR, 1);
		}

		imgExpand.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(isExpanded){
					collapse(lnrScheduleAllDays, Math.max(0, (MAX_ROW - numRow - 1) * WeeklyPageFragment.CELL_HEIGHT_PIXEL));
					txtMore.setVisibility(View.VISIBLE);
					isExpanded = false;
					imgExpand.setImageResource(R.drawable.down);
				}else{
					imgExpand.setImageResource(R.drawable.up);
					expand(lnrScheduleAllDays);
					txtMore.setVisibility(View.GONE);
					isExpanded = true;
				}
			}
		});

	}

	private void gotoTodoListTodayFragment(Date selectedDate){
		TodoListTodayFragment todoListTodayFragment = new TodoListTodayFragment();
		todoListTodayFragment.setSelectedDate(selectedDate);
		((ChiaseFragment)getParentFragment()).gotoFragment(todoListTodayFragment);
	}

	public void expand(final View v){
		v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		final int targetHeight = Math.max(0, ((LinearLayout)v).getChildCount() * WeeklyPageFragment.CELL_HEIGHT_PIXEL);
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t){
				v.getLayoutParams().height = interpolatedTime == 1 ? targetHeight : (int)(targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds(){
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
		scrollToFavouritePost();
	}

	public void collapse(final View v, final int targetHeight){
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t){
				if(interpolatedTime == 1){
					v.getLayoutParams().height = targetHeight;
					v.requestLayout();
				}else{
					v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds(){
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
		scrollToFavouritePost();
	}

	@Override
	protected void initCalendarHeader(){

	}

	@Override
	protected void initDayViews(){

	}

	@Override
	protected String getExecType(){
		return "D";
	}

	@Override
	protected void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){

		WeeklyPageFragment.sortSchedules(schedules, dates.get(0), dates.get(dates.size() - 1), false);
		numRow = 0;
		int oldMoreNumber = moreNumber;
		super.updateSchedules(schedules, categories);
		schedules = multiplyWithUsers(schedules);
		if(!CCCollectionUtil.isEmpty(todos)){
			txtTodoInfo.setVisibility(View.VISIBLE);
			txtTodoInfo.setText(getString(R.string.two_things_todo, String.valueOf(todos.size())));
			numRow += 1;
		}else{
			txtTodoInfo.setVisibility(View.GONE);
		}

		if(!CCCollectionUtil.isEmpty(lstBirthdayUser)){
			imgBirthdayIcon.setVisibility(View.VISIBLE);
			numRow += 1;
		}else{
			imgBirthdayIcon.setVisibility(View.GONE);
		}

		lnrScheduleAllDays.removeAllViews();

		// holiday
		for(HolidayModel holidayModel : lstHoliday){
			TextView textView = WeeklyPageFragment.makeTextView(activity, holidayModel.holidayName, 0, 0, LinearLayout.LayoutParams.MATCH_PARENT, Color.WHITE, Color.RED, Gravity.CENTER);
			lnrScheduleAllDays.addView(textView);
		}

		// request
		Collections.sort(lstWorkRequest, getWorkRequestComparator());
		for(WorkRequest workRequest : lstWorkRequest){
			TextView textView = WeeklyPageFragment.makeTextView(activity, workRequest.offerTypeName, 0, 0, LinearLayout.LayoutParams.MATCH_PARENT, Color.parseColor(workRequest.userColor), 0, Gravity.CENTER);
			lnrScheduleAllDays.addView(textView);
		}

		List<ScheduleModel> allDaySchedules = new ArrayList<>();
		for(String key : startTimeSchedulesMap.keySet()){
			startTimeSchedulesMap.put(key, new ArrayList<ScheduleModel>());
		}

		for(ScheduleModel scheduleModel : schedules){
			if(!scheduleModel.isAllDay){
				String keyMap = scheduleModel.startTime.split(":")[0] + ":00";
				startTimeSchedulesMap.get(keyMap).add(scheduleModel);
			}else{
				allDaySchedules.add(scheduleModel);
				TextView textView = WeeklyPageFragment.makeTextView(activity, scheduleModel.scheduleName, 0, 0, LinearLayout.LayoutParams.MATCH_PARENT, WeeklyPageFragment.getColor(scheduleModel), 0, Gravity.CENTER);
				lnrScheduleAllDays.addView(textView);
			}
		}

		lnrListSchedules.removeAllViews();

		int childCount = lnrScheduleAllDays.getChildCount();
		moreNumber = (childCount + numRow) - MAX_ROW;

		if(moreNumber <= 0){
			txtMore.setVisibility(View.GONE);
			imgExpand.setVisibility(View.GONE);
			lnrScheduleAllDays.getLayoutParams().height = Math.max(0, childCount * WeeklyPageFragment.CELL_HEIGHT_PIXEL);
			lnrScheduleAllDays.requestLayout();
		}else{
			txtMore.setText("+" + (moreNumber + 1));
			if(firstTime){
				showCollapse();
			}else if(oldMoreNumber != moreNumber){
				if(isExpanded){
					txtMore.setVisibility(View.GONE);
					lnrScheduleAllDays.getLayoutParams().height = Math.max(0, childCount * WeeklyPageFragment.CELL_HEIGHT_PIXEL);
					lnrScheduleAllDays.requestLayout();
					imgExpand.setVisibility(View.VISIBLE);
				}else{
					showCollapse();
				}
			}
		}

		List<String> keys = new ArrayList<>(startTimeSchedulesMap.keySet());
		Collections.sort(keys);

		for(String key : keys){
			List<ScheduleModel> scheduleModels = startTimeSchedulesMap.get(key);
			addStartTimeRow(key, scheduleModels);
		}

		firstTime = false;
		scrollToFavouritePost();

	}

	private void showCollapse(){
		lnrScheduleAllDays.getLayoutParams().height = Math.max(0, (MAX_ROW - numRow - 1) * WeeklyPageFragment.CELL_HEIGHT_PIXEL);
		lnrScheduleAllDays.requestLayout();
		txtMore.setVisibility(View.VISIBLE);
		imgExpand.setVisibility(View.VISIBLE);
	}

	private void scrollToFavouritePost(){
		scrollView.post(new Runnable() {

			@Override
			public void run(){
				TodoListFragment.scrollToView(scrollView, thisHourView, 2);
			}
		});
	}

	private void addStartTimeRow(String startTime, List<ScheduleModel> scheduleModels){
		View cell = inflater.inflate(R.layout.item_daily_schedule, null);
		((TextView)cell.findViewById(R.id.txt_item_daily_schedule_start_time)).setText(startTime);
		LinearLayout lnrSchedules = (LinearLayout)cell.findViewById(R.id.lnr_schedule_list_item_daily);
		for(ScheduleModel scheduleModel : scheduleModels){
			TextView textView = new TextView(activity);
			textView.setText(scheduleModel.scheduleName);
			int color = WeeklyPageFragment.getColor(scheduleModel);
			if("#FFFFFF".equals(scheduleModel.getScheduleColor())){
				color = Color.parseColor("#000000");
			}
			textView.setTextColor(color);
			textView.setGravity(Gravity.CENTER_VERTICAL);
			lnrSchedules.addView(textView);
		}
		if(startTime.equals(currentHour)){
			thisHourView = cell;
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
		switch(v.getId()){
		case R.id.lnr_fragment_daily_page_schedules_time:
		case R.id.lnr_schedule_all_day_container:
			onDailyScheduleClickListener(selectedDate);
			break;
		default:
			break;
		}
	}
}
