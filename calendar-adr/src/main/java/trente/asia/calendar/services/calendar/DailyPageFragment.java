package trente.asia.calendar.services.calendar;

import static trente.asia.calendar.services.calendar.WeeklyPageFragment.getWorkRequestComparator;
import static trente.asia.calendar.services.calendar.WeeklyPageFragment.makeTextView2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
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
import android.widget.RelativeLayout;
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
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * DailyPageFragment
 *
 * @author Vietnh
 */
public class DailyPageFragment extends SchedulesPageFragment implements ObservableScrollView.ScrollViewListener{

	private LinearLayout						lnrScheduleAllDays;
	private TextView							txtTodoInfo;
	private RelativeLayout						rltListSchedules;
	private Map<String, List<ScheduleModel>>	startTimeSchedulesMap;
	private ImageView							imgBirthdayIcon;
	private int									moreNumber;
	private boolean								firstTime	= true;
	private int									numRow;
	private List<EventRect>						mEventRects	= new ArrayList<>();
	private DailyFragment						parent;
	private ImageView							imgExpand;
	// private int screenW;

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
		imgExpand = (ImageView)getView().findViewById(R.id.ic_icon_expand);
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
		rltListSchedules = (RelativeLayout)getView().findViewById(R.id.lnr_fragment_daily_page_schedules_time);
		rltListSchedules.setOnClickListener(this);
		scrSchedules.setScrollViewListener(this);
		parent = (DailyFragment)getParentFragment();

		// Add horizontal time lines
		Calendar c = CCDateUtil.makeCalendarToday();
		List<String> times = new ArrayList<>();
		for(int i = 1; i < 24; i++){
			c.set(Calendar.HOUR_OF_DAY, i);
			String startTime = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, c.getTime());
			times.add(startTime);
		}

		Collections.sort(times);

		for(int i = 0; i < times.size(); i++){
			int bottomMargin = 0;
			if(i == times.size() - 1){
				bottomMargin = TIME_WIDTH_PX - MARGIN_TEXT_MIDDLE_PX - 1;
			}
			addStartTimeRow(times.get(i), (i + 1) * DailyPageFragment.TIME_WIDTH_PX - MARGIN_TEXT_MIDDLE_PX, bottomMargin);
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
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy){
		parent.scrollNeighbors(x, y, oldx, oldy);
	}

	public class RECT{

		public int		startX;
		public int		endX;
		public int		startY;
		public int		endY;
		public String	key;
	}

	@Override
	protected void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){

		float screenW = rltListSchedules.getMeasuredWidth() - TIME_WIDTH_PX - MARGIN_LEFT_RIGHT_PX;

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

		List<ScheduleModel> normalSchedules = new ArrayList<>();
		List<ScheduleModel> allDaySchedules = new ArrayList<>();

		for(ScheduleModel scheduleModel : schedules){
			if(!scheduleModel.isAllDay){
				normalSchedules.add(scheduleModel);
			}else{
				allDaySchedules.add(scheduleModel);
			}
		}

		buildBlocks(activity, TIME_WIDTH_PX - MARGIN_LEFT_RIGHT_PX, screenW, normalSchedules, rltListSchedules);

		for(ScheduleModel scheduleModel : allDaySchedules){
			TextView textView = WeeklyPageFragment.makeTextView(activity, scheduleModel.scheduleName, 0, 0, LinearLayout.LayoutParams.MATCH_PARENT, WeeklyPageFragment.getColor(scheduleModel), 0, Gravity.CENTER);
			lnrScheduleAllDays.addView(textView);
		}

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

		firstTime = false;
		scrollToFavouritePost();

	}

	public static void buildBlocks(Context context, int leftPx, float containerWidth, List<ScheduleModel> scheduleModels, RelativeLayout container){
		List<EventRect> eventRects = new ArrayList<>();
		for(ScheduleModel scheduleModel : scheduleModels){
			EventRect eventRect = new EventRect(scheduleModel);
			eventRects.add(eventRect);
		}

		eventRects = computePositionOfEvents(eventRects);

		for(EventRect eventRect : eventRects){
			int leftMargin = leftPx + (int)(containerWidth * eventRect.left);
			int topMargin = WelfareUtil.dpToPx((int)eventRect.top);
			int width = (int)(eventRect.width * containerWidth);
			int height = WelfareUtil.dpToPx((int)eventRect.bottom - (int)eventRect.top);
			TextView txtView = makeTextView2(context, eventRect.schedule.scheduleName, leftMargin, topMargin, width, height, Color.parseColor(eventRect.schedule.scheduleColor), 0, Gravity.CENTER);
			container.addView(txtView);
		}
	}

	/**
	 * A class to hold reference to the events and their visual representation. An EventRect is
	 * actually the rectangle that is drawn on the calendar for a given schedule. There may be more
	 * than one rectangle for a single schedule (an schedule that expands more than one day). In that
	 * case two instances of the EventRect will be used for a single schedule. The given schedule will be
	 * stored in "originalEvent". But the schedule that corresponds to rectangle the rectangle
	 * instance will be stored in "schedule".
	 */
	public static final class EventRect{

		public ScheduleModel	schedule;
		public float			left;
		public float			width;
		public float			top;
		public float			bottom;

		/**
		 * Create a new instance of schedule rect. An EventRect is actually the rectangle that is drawn
		 * on the calendar for a given schedule. There may be more than one rectangle for a single
		 * schedule (an schedule that expands more than one day). In that case two instances of the
		 * EventRect will be used for a single schedule. The given schedule will be stored in
		 * "originalEvent". But the schedule that corresponds to rectangle the rectangle instance will
		 * be stored in "schedule".
		 * 
		 * @param schedule Represents the schedule which this instance of rectangle represents.
		 */
		public EventRect(ScheduleModel schedule){
			this.schedule = schedule;
		}
	}

	/**
	 * Calculates the left and right positions of each events. This comes handy specially if events
	 * are overlapping.
	 * 
	 * @param eventRects The events along with their wrapper class.
	 */
	public static List<EventRect> computePositionOfEvents(List<EventRect> eventRects){
		// Make "collision groups" for all events that collide with others.
		List<EventRect> result = new ArrayList<>();
		List<List<EventRect>> collisionGroups = new ArrayList<List<EventRect>>();
		for(EventRect eventRect : eventRects){
			boolean isPlaced = false;

			outerLoop:for(List<EventRect> collisionGroup : collisionGroups){
				for(EventRect groupEvent : collisionGroup){
					if(isEventsCollide(groupEvent.schedule, eventRect.schedule)){
						collisionGroup.add(eventRect);
						isPlaced = true;
						break outerLoop;
					}
				}
			}

			if(!isPlaced){
				List<EventRect> newGroup = new ArrayList<EventRect>();
				newGroup.add(eventRect);
				collisionGroups.add(newGroup);
			}
		}

		for(List<EventRect> collisionGroup : collisionGroups){
			expandEventsToMaxWidth(collisionGroup, result);
		}

		return result;
	}

	/**
	 * Expands all the events to maximum possible width. The events will try to occupy maximum
	 * space available horizontally.
	 *
	 * @param collisionGroup The group of events which overlap with each other.
	 * @param result
	 */
	public static void expandEventsToMaxWidth(List<EventRect> collisionGroup, List<EventRect> result){
		// Expand the events to maximum possible width.
		List<List<EventRect>> columns = new ArrayList<List<EventRect>>();
		columns.add(new ArrayList<EventRect>());
		for(EventRect eventRect : collisionGroup){
			boolean isPlaced = false;
			for(List<EventRect> column : columns){
				if(column.size() == 0){
					column.add(eventRect);
					isPlaced = true;
				}else if(!isEventsCollide(eventRect.schedule, column.get(column.size() - 1).schedule)){
					column.add(eventRect);
					isPlaced = true;
					break;
				}
			}
			if(!isPlaced){
				List<EventRect> newColumn = new ArrayList<EventRect>();
				newColumn.add(eventRect);
				columns.add(newColumn);
			}
		}

		// Calculate left and right position for all the events.
		// Get the maxRowCount by looking in all columns.
		int maxRowCount = 0;
		for(List<EventRect> column : columns){
			maxRowCount = Math.max(maxRowCount, column.size());
		}
		for(int i = 0; i < maxRowCount; i++){
			// Set the left and right values of the schedule.
			float j = 0;
			for(List<EventRect> column : columns){
				if(column.size() >= i + 1){
					EventRect eventRect = column.get(i);
					eventRect.width = 1f / columns.size();
					eventRect.left = j / columns.size();
					eventRect.top = CCDateUtil.convertTime2Min(eventRect.schedule.startTime);
					eventRect.bottom = CCDateUtil.convertTime2Min(eventRect.schedule.endTime);
					result.add(eventRect);
				}
				j++;
			}
		}
	}

	/**
	 * Checks if two events overlap.
	 * 
	 * @param event1 The first schedule.
	 * @param event2 The second schedule.
	 * @return true if the events overlap.
	 */
	public static boolean isEventsCollide(ScheduleModel event1, ScheduleModel event2){

		long start1 = event1.getStartTimeMilis();
		long end1 = event1.getEndTimeMilis();
		long start2 = event2.getStartTimeMilis();
		long end2 = event2.getEndTimeMilis();
		return !((start1 >= end2) || (end1 <= start2));
	}

	private List<RECT> getRemainRects(List<ScheduleModel> arranged, List<RECT> rects){
		List<RECT> result = new ArrayList<>();
		for(RECT rect : rects){
			boolean isPlaced = false;
			for(ScheduleModel arran : arranged){
				if(arran.key.equals(rect.key)){
					isPlaced = true;
					break;
				}
			}
			if(!isPlaced){
				result.add(rect);
			}
		}
		return result;
	}

	private int getLeftMargin(RECT meRect, List<ScheduleModel> arranged, Map<String, RECT> schedulePositions, int blockSize){
		int leftMargin = 0;
		for(ScheduleModel scheduleModel : arranged){
			RECT rect = schedulePositions.get(scheduleModel.key);
			if(meRect.startY >= rect.endY || meRect.endY <= rect.startY){
				continue;
			}else{
				if(leftMargin + blockSize > rect.startX){
					leftMargin = rect.endX;
				}
			}
		}
		return leftMargin;
	}

	private int countBlocksContains(RECT me, List<RECT> rects){
		int count = 0;
		List<RECT> collide = getCollisionRects(me, rects);
		for(RECT child : collide){
			int childConflict = countBlocksContains(child, collide);
			if(childConflict + 1 > count){
				count = childConflict + 1;
			}
		}

		return count;
	}

	List<RECT> getCollisionRects(RECT me, List<RECT> others){
		List<RECT> result = new ArrayList<>();
		for(RECT rect : others){
			if(!rect.key.equals(me.key) && (me.endY > rect.startY || me.startY < rect.endY)){
				result.add(rect);
			}
		}
		return result;
	}

	private List<ScheduleModel> getConflicts(Map<String, RECT> scheduleRects, List<ScheduleModel> normalSchedules, RECT meRect){
		List<ScheduleModel> result = new ArrayList<>();
		for(ScheduleModel scheduleModel : normalSchedules){
			RECT otherRect = scheduleRects.get(scheduleModel.key);
			if(meRect.endY < otherRect.startY || meRect.startY < otherRect.endY){
				result.add(scheduleModel);
			}
		}
		return result;
	}

	private void showCollapse(){
		lnrScheduleAllDays.getLayoutParams().height = Math.max(0, (MAX_ROW - numRow - 1) * WeeklyPageFragment.CELL_HEIGHT_PIXEL);
		lnrScheduleAllDays.requestLayout();
		txtMore.setVisibility(View.VISIBLE);
		imgExpand.setVisibility(View.VISIBLE);
	}

	private void scrollToFavouritePost(){
		if(scrSchedules.y == 0){
			scrSchedules.post(new Runnable() {

				@Override
				public void run(){
					TodoListFragment.scrollToView(scrSchedules, thisHourView, 2);
				}
			});
		}
	}

	private void addStartTimeRow(String startTime, int marginTop, int bottomMargin){
		View cell = inflater.inflate(R.layout.item_daily_schedule, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, marginTop, 0, bottomMargin);
		cell.setLayoutParams(params);
		((TextView)cell.findViewById(R.id.txt_item_daily_schedule_start_time)).setText(startTime);
		if(startTime.equals(GOLDEN_HOUR_STR)){
			thisHourView = cell;
		}
		rltListSchedules.addView(cell);
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
