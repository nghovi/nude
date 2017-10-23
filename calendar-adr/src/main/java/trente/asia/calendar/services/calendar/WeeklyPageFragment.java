package trente.asia.calendar.services.calendar;

import static trente.asia.android.util.CsDateUtil.CS_DATE_TIME_1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseFragment;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.PageContainerFragment;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkRequest;
import trente.asia.calendar.services.calendar.view.DailyScheduleList;
import trente.asia.calendar.services.todo.TodoListFragment;
import trente.asia.calendar.services.todo.TodoListTodayFragment;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * WeeklyPageFragment
 *
 * @author VietNH
 */
public class WeeklyPageFragment extends SchedulesPageFragment implements ObservableScrollView.ScrollViewListener{

	public static final Integer			CELL_HEIGHT_PIXEL			= WelfareUtil.dpToPx(18);
	private static final int			MAX_BLOCK_NUM_TO_SHOW_TEXT	= 3;
	protected LinearLayout				lnrHeader;
	private RelativeLayout				rltExpandBar;
	private RelativeLayout				rltPart1;
	private RelativeLayout				rltPart2;
	private RelativeLayout				rltPart2LineContainer;
	private Map<Integer, List<Integer>>	columnTopMarginsMap;
	private int							maxTopMargin				= 0;
	private ScrollView					scrollViewPart1;
	public ObservableScrollView			scrMain;
	private boolean						shouldClick;
	private LinearLayout				lnrVerticalLineContainer;

	private float						mDownX;
	private float						mDownY;
	private final float					SCROLL_THRESHOLD			= 5;
	private boolean						firstTime					= true;
	private static final int			MARGIN_TOP_PX				= WelfareUtil.dpToPx(10);

	private WeeklyFragment				parent;
	private int							height						= WelfareUtil.dpToPx(MARGIN_LEFT_RIGHT_PX) + MARGIN_TOP_PX + WelfareUtil.dpToPx(1);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_weekly_page, container, false);
		}
		return mRootView;
	}

	public void expand(final View v){
		v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		final int targetHeight = v.getMeasuredHeight();
		height = targetHeight + WelfareUtil.dpToPx(MARGIN_LEFT_RIGHT_PX) + MARGIN_TOP_PX;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t){
				if(interpolatedTime == 1){
					v.getLayoutParams().height = targetHeight;
					v.requestLayout();
					updateTimeColumnPosition();
				}else{
					v.getLayoutParams().height = (int)(targetHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds(){
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
		// scrollToFavouritePost();
	}

	public void collapse(final View v, final int targetHeight){
		final int initialHeight = v.getMeasuredHeight();
		height = targetHeight + WelfareUtil.dpToPx(TIME_COLUMN_WIDTH_PX) - WelfareUtil.dpToPx(MARGIN_LEFT_RIGHT_PX);
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
		// scrollToFavouritePost();
		updateTimeColumnPosition();
	}

	@Override
	protected void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){
		// Collections.sort(lstWorkRequest, getWorkRequestComparator());
		super.updateSchedules(schedules, categories);
		// sortSchedules(schedules, dates.get(0), dates.get(dates.size() - 1), true);
		// schedules = multiplyWithUsers(schedules);

		List<ScheduleModel> normalSchedules = new ArrayList<>();
		List<ScheduleModel> allDaySchedules = new ArrayList<>();

		for(ScheduleModel scheduleModel : schedules){
			if(!scheduleModel.isAllDay){
				normalSchedules.add(scheduleModel);
			}else{
				allDaySchedules.add(scheduleModel);
			}
		}

		buildPart1(allDaySchedules);
		buildPart2(normalSchedules);
	}

	// public static void sortSchedules(List<ScheduleModel> schedules, Date dateStart, Date dateEnd, boolean checkAllDayTime){
	//// final Comparator<ScheduleModel> normalComparator = MonthlyPageFragment.getScheduleComparator(checkAllDayTime);
	// if(!checkAllDayTime){
	// Collections.sort(schedules, normalComparator);
	// }else{
	// final Comparator<ScheduleModel> periodComparator = MonthlyCalendarRowView.getPeriodScheduleComparator(dateStart, dateEnd);
	// Collections.sort(schedules, new Comparator<ScheduleModel>() {
	//
	// @Override
	// public int compare(ScheduleModel o1, ScheduleModel o2){
	// if(o1.isPeriod && !o2.isPeriod){
	// return -1;
	// }else if(!o1.isPeriod && o2.isPeriod){
	// return 1;
	// }else if(o1.isPeriod && o2.isPeriod){
	// return periodComparator.compare(o1, o2);
	// }
	// return normalComparator.compare(o1, o2);
	// }
	// });
	// }
	// }

	private void buildPart1(List<ScheduleModel> schedules){
		rltPart1.removeAllViews();
		float screenW = lnrHeader.getMeasuredWidth();
		Calendar cStartWeek = CCDateUtil.makeCalendarWithDateOnly(dates.get(0));
		columnTopMarginsMap = new HashMap<>();
		int oldMaxTopMargin = maxTopMargin;
		maxTopMargin = 0;
		int topMargin = 0;
		int itemNum = 0;

		// todo
		for(Todo todo : todos){
			final Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(todo.limitDate);
			int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - cStartWeek.get(Calendar.DAY_OF_YEAR);

			if(!columnTopMarginsMap.keySet().contains(dayDistance)){
				int leftMargin = (int)(screenW * dayDistance / 7);
				topMargin = getNextTopMargin(dayDistance, dayDistance);
				TextView textView = makeTextView(activity, getString(R.string.cl_footer_todo), leftMargin, topMargin, (int)(screenW / 7), Color.GRAY, 0, Gravity.LEFT);
				textView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						gotoTodoListTodayFragment(c2);
					}
				});
				rltPart1.addView(textView);
				itemNum++;
			}
		}

		Map<String, Boolean> birthdayIconMap = new HashMap<>();
		// birthday
		for(ScheduleModel scheduleModel : schedules){
			if("BI".equals(scheduleModel.eventType)) {
				String keyDate = WelfareUtil.getDateString(scheduleModel.startDate);
				if (!birthdayIconMap.containsKey(keyDate)) {
					Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(scheduleModel.startDate);
					int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - cStartWeek.get(Calendar.DAY_OF_YEAR);
					int leftMargin = (int) (screenW * (0 + dayDistance) / 7 + (screenW / 7));
					topMargin = getNextTopMargin(dayDistance, dayDistance);

					ImageView imageViewBirthday = new ImageView(activity);
					imageViewBirthday.setImageResource(R.drawable.cl_icon_birthday);
					RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(CELL_HEIGHT_PIXEL, CELL_HEIGHT_PIXEL);
					rlp.setMargins(leftMargin, topMargin, 0, 0);
					imageViewBirthday.setLayoutParams(rlp);
					rltPart1.addView(imageViewBirthday);
					birthdayIconMap.put(keyDate, true);
					itemNum++;
				}
			}
		}

		// holiday
		for(HolidayModel holidayModel : lstHoliday){
			Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(holidayModel.startDate);
			int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - cStartWeek.get(Calendar.DAY_OF_YEAR);
			int leftMargin = (int)(screenW * (0 + dayDistance) / 7);
			topMargin = getNextTopMargin(dayDistance, dayDistance);
			TextView textView = makeTextView(activity, holidayModel.holidayName, leftMargin, topMargin, (int)(screenW / 7), Color.WHITE, Color.RED, Gravity.LEFT);
			rltPart1.addView(textView);
			itemNum++;
		}

		// work requests
		for(WorkRequest workRequest : lstWorkRequest){
			Calendar cStart = CCDateUtil.makeCalendarWithDateOnly(workRequest.startDate);
			Calendar cEnd = CCDateUtil.makeCalendarWithDateOnly(workRequest.endDate);

			int dayDistance = Math.max(0, cStart.get(Calendar.DAY_OF_YEAR) - cStartWeek.get(Calendar.DAY_OF_YEAR));
			int dayDistanceEnd = Math.min(7, cEnd.get(Calendar.DAY_OF_YEAR) - cStartWeek.get(Calendar.DAY_OF_YEAR));
			int cellNumber = dayDistanceEnd - dayDistance + 1;

			int leftMargin = (int)(screenW * (0 + dayDistance) / 7);
			topMargin = getNextTopMargin(dayDistance, dayDistance + cellNumber - 1);

			TextView textView = makeTextView(activity, workRequest.offerTypeName, leftMargin, topMargin, (int)(screenW * cellNumber / 7), Color.parseColor(workRequest.userColor), 0, Gravity.LEFT);
			rltPart1.addView(textView);
			itemNum++;
		}

		// all day schedules
		for(ScheduleModel schedule : schedules){
			Calendar cStart = CCDateUtil.makeCalendarWithDateOnly(schedule.startDate);
			Calendar cEnd = CCDateUtil.makeCalendarWithDateOnly(schedule.endDate);

			int dayDistance = Math.max(0, cStart.get(Calendar.DAY_OF_YEAR) - cStartWeek.get(Calendar.DAY_OF_YEAR));
			int dayDistanceEnd = Math.min(7, cEnd.get(Calendar.DAY_OF_YEAR) - cStartWeek.get(Calendar.DAY_OF_YEAR));
			int cellNumber = dayDistanceEnd - dayDistance + 1;
			int maxWidth = (int)(screenW * cellNumber / 7);
			int leftMargin = (int)(screenW * (0 + dayDistance) / 7);

			topMargin = getNextTopMargin(dayDistance, dayDistance + cellNumber - 1);
			TextView textView = makeTextView(activity, schedule.scheduleName, leftMargin, topMargin, maxWidth, getColor(schedule), 0, Gravity.LEFT);
			if(!"BI".equals(schedule.eventType)) {
				rltPart1.addView(textView);
			}
			itemNum++;
		}
		int rowNum = maxTopMargin / CELL_HEIGHT_PIXEL;
		if(rowNum == 0){
			rowNum = Math.min(1, itemNum);
		}else{
			rowNum = rowNum + 1;
		}
		int moreNumber = rowNum - MAX_ROW;

		if(moreNumber <= 0){
			int initialHeight = rowNum * WeeklyPageFragment.CELL_HEIGHT_PIXEL;
			rltPart1.getLayoutParams().height = initialHeight;
			height = initialHeight + WelfareUtil.dpToPx(MARGIN_LEFT_RIGHT_PX) + MARGIN_TOP_PX;
			rltPart1.requestLayout();
			rltExpandBar.setVisibility(View.GONE);
			if(isActivePage()){
				parent.imgExpand.setVisibility(View.INVISIBLE);
			}
		}else{
			if(isActivePage()){
				parent.imgExpand.setVisibility(View.VISIBLE);
			}
			int maxTopMarginAllowed = (MAX_ROW - 1) * CELL_HEIGHT_PIXEL;
			while(rltExpandBar.getChildAt(1) != null){
				rltExpandBar.removeViewAt(1);
			}
			for(int key : columnTopMarginsMap.keySet()){
				List<Integer> usedTopMargins = columnTopMarginsMap.get(key);
				int more = 0;
				if(!CCStringUtil.isEmpty(usedTopMargins)){
					for(int margin : usedTopMargins){
						more = margin >= maxTopMarginAllowed ? more + 1 : more;
					}
				}
				if(more > 0){
					TextView textView = new TextView(activity);
					textView.setMaxLines(1);
					textView.setEllipsize(TextUtils.TruncateAt.END);
					textView.setTextSize(13);
					textView.setMaxWidth((int)(screenW / 7 - 2));
					textView.setGravity(Gravity.LEFT);
					// textView.setBackgroundColor(Color.WHITE);
					// textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.wf_background_gray_border_white));
					textView.setText("+" + more);
					RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams((int)(screenW / 7 - 2), RelativeLayout.LayoutParams.WRAP_CONTENT);
					rlp.setMargins((int)(key * screenW / 7), 0, 0, 0);
					textView.setLayoutParams(rlp);
					rltExpandBar.addView(textView);
				}
			}

			if(firstTime){
				showCollapse();
			}else if(oldMaxTopMargin != maxTopMargin){
				if(isExpanded){
					rltExpandBar.setVisibility(View.VISIBLE);
					if(isActivePage()){
						parent.imgExpand.setVisibility(View.VISIBLE);
					}
					for(int i = 1; i < rltExpandBar.getChildCount(); i++){
						rltExpandBar.getChildAt(i).setVisibility(View.GONE);
					}
					height = rowNum * WeeklyPageFragment.CELL_HEIGHT_PIXEL + WelfareUtil.dpToPx(TIME_COLUMN_WIDTH_PX) / 2 + WelfareUtil.dpToPx(MARGIN_TEXT_MIDDLE_PX) - 1;
					rltPart1.getLayoutParams().height = height;
					rltPart1.requestLayout();
				}else{
					showCollapse();
				}
			}else{
				for(int i = 1; i < rltExpandBar.getChildCount(); i++){
					if(isExpanded){
						rltExpandBar.getChildAt(i).setVisibility(View.GONE);
					}else{
						rltExpandBar.getChildAt(i).setVisibility(View.VISIBLE);
					}
				}
			}
		}

		setOnTouchListener(scrollViewPart1, (int)(screenW / 7));
		firstTime = false;
		if(isActivePage()){
			updateTimeColumnPosition();
		}
	}

	public void updateTimeColumnPosition(){
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)parent.lnrTimeColumn.getLayoutParams();
		lp.setMargins(0, height, 0, 0);
		parent.lnrTimeColumn.setLayoutParams(lp);
		parent.lnrTimeColumn.requestLayout();
	}

	private void showCollapse(){
		rltExpandBar.setVisibility(View.VISIBLE);
		if(isActivePage()){
			parent.imgExpand.setVisibility(View.VISIBLE);
		}
		for(int i = 1; i < rltExpandBar.getChildCount(); i++){
			rltExpandBar.getChildAt(i).setVisibility(View.VISIBLE);
		}
		int initialHeight = (MAX_ROW - 1) * WeeklyPageFragment.CELL_HEIGHT_PIXEL;
		rltPart1.getLayoutParams().height = initialHeight;
		height = initialHeight + WelfareUtil.dpToPx(TIME_COLUMN_WIDTH_PX) - WelfareUtil.dpToPx(MARGIN_LEFT_RIGHT_PX);
		rltPart1.requestLayout();
	}

	private void gotoTodoListTodayFragment(Calendar c2){
		TodoListTodayFragment todoListTodayFragment = new TodoListTodayFragment();
		todoListTodayFragment.setSelectedDate(c2.getTime());
		((ChiaseFragment)getParentFragment()).gotoFragment(todoListTodayFragment);
	}

	private int getNextTopMargin(int keyStart, int keyEnd){
		int result = 0;
		boolean sastisfied = false;
		while(!sastisfied){
			for(int i = keyStart; i <= keyEnd; i++){
				List<Integer> usedMarginTops = columnTopMarginsMap.get(i);
				if(CCCollectionUtil.isEmpty(usedMarginTops)){
					sastisfied = true;
				}else{
					if(!usedMarginTops.contains(result)){
						sastisfied = true;
					}else{
						sastisfied = false;
						break;
					}
				}
			}

			result = sastisfied ? result : result + CELL_HEIGHT_PIXEL;
		}

		for(int i = keyStart; i <= keyEnd; i++){
			List<Integer> usedMarginTops = columnTopMarginsMap.get(i);
			if(CCCollectionUtil.isEmpty(usedMarginTops)){
				usedMarginTops = new ArrayList<>();
				usedMarginTops.add(result);
				columnTopMarginsMap.put(i, usedMarginTops);
			}else{
				usedMarginTops.add(result);
			}
		}

		maxTopMargin = Math.max(maxTopMargin, result);

		return result;
	}

	public static TextView makeTextView(Context activity, String text, int leftMargin, int topMargin, int maxWidth, int bgColor, int textColor, int gravity){
		TextView textView = new TextView(activity);
		textView.setMaxLines(1);
		// textView.setEllipsize(TextUtils.TruncateAt.END);
		textView.setTextSize(13);

		textView.setMaxWidth(maxWidth);
		textView.setGravity(gravity);

		if(bgColor != 0){
			// textView.setBackgroundColor(bgColor);
			textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.wf_background_round_border_white));
			GradientDrawable bgShape = (GradientDrawable)textView.getBackground();
			bgShape.setColor(bgColor);
		}
		if(textColor != 0){
			textView.setTextColor(textColor);
		}
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(maxWidth, CELL_HEIGHT_PIXEL);
		rlp.setMargins(leftMargin, topMargin, 0, 0);
		textView.setLayoutParams(rlp);
		textView.setText(text);
		return textView;
	}

	public static TextView makeBlock(Context activity, String text, int leftMargin, int topMargin, int maxWidth, int height, int bgColor, int textColor, int gravity){
		TextView textView = new TextView(activity);
		// textView.setMaxLines(1);
		// textView.setEllipsize(TextUtils.TruncateAt.END);
		textView.setTextSize(13);

		textView.setMaxWidth(maxWidth);
		textView.setGravity(gravity);

		if(bgColor != 0){
			// textView.setBackgroundColor(bgColor);
			textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.wf_background_round_border_transparent));
			GradientDrawable bgShape = (GradientDrawable)textView.getBackground();
			bgShape.setColor(bgColor);
		}
		if(textColor != 0){
			textView.setTextColor(textColor);
		}
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(maxWidth, height);
		rlp.setMargins(leftMargin, topMargin, 0, 0);
		textView.setLayoutParams(rlp);
		textView.setText(text);
		return textView;
	}

	private void buildPart2(List<ScheduleModel> schedules){
		rltPart2.removeAllViews();
		float screenW = lnrHeader.getMeasuredWidth();

		Map<Date, List<ScheduleModel>> daySchedulesMap = DailyScheduleList.buildDaySchedulesMap(dates, schedules);

		Calendar c1 = CCDateUtil.makeCalendarWithDateOnly(dates.get(0));

		for(Date date : daySchedulesMap.keySet()){
			Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(date);
			int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
			int leftMargin = (int)(screenW * dayDistance / 7) + 1;
			List<ScheduleModel> daySchedules = daySchedulesMap.get(date);
			DailyPageFragment.buildBlocks(activity, leftMargin, screenW / 7, daySchedules, rltPart2, MAX_BLOCK_NUM_TO_SHOW_TEXT);
		}

		setOnTouchListener(scrSchedules, (int)(screenW / 7));
		if(isActivePage() && parent.timeScroll.y == 0){
			scrollToFavouritePost();
		}
	}

	private void setOnTouchListener(View scrollview, final int cellWidth){
		scrollview.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event){

				switch(event.getAction() & MotionEvent.ACTION_MASK){
				case MotionEvent.ACTION_DOWN:
					mDownX = event.getX();
					mDownY = event.getY();
					shouldClick = true;
					break;
				case MotionEvent.ACTION_MOVE:
					if(shouldClick && (Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)){
						shouldClick = false;
					}
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					if(shouldClick && ((PageContainerFragment)getParentFragment()).getScrollingState() != ViewPager.SCROLL_STATE_DRAGGING){
						float x = event.getAxisValue(MotionEvent.AXIS_X);
						float y = cellWidth;
						int column = (int)(x / y);
						if(column >= 0 && column < dates.size()){
							onDailyScheduleClickListener(dates.get(column));
						}
						return true;
					}
					break;
				default:
					break;
				}

				return false;
			}
		});
	}

	private void scrollToFavouritePost(){
		scrSchedules.post(new Runnable() {

			@Override
			public void run(){
				TodoListFragment.scrollToView(parent.timeScroll, parent.goldenHourView, 2);
			}
		});
	}

	public static int getColor(ScheduleModel scheduleModel){
		return Color.parseColor(scheduleModel.getScheduleColor());
	}

	protected void initCalendarView(){
		initCalendarHeader();
		initDayViews();
	}

	@Override
	protected void initCalendarHeader(){
		lnrHeader = (LinearLayout)getView().findViewById(R.id.lnr_weekly_header);
		lnrHeader.removeAllViews();
		// addCell("", "", 0, today);
		Calendar c = Calendar.getInstance();
		for(Date date : dates){
			c.setTime(date);
			addCell(String.valueOf(c.get(Calendar.DAY_OF_MONTH)), CCStringUtil.toUpperCase(CCFormatUtil.formatDateCustom(CS_DATE_TIME_1, date)), c.get(Calendar.DAY_OF_WEEK), date);
		}
	}

	private void addCell(String number, String day, int dayOfWeek, Date date){
		LinearLayout lnrDay = new LinearLayout(activity);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.weight = 1;
		lnrDay.setGravity(Gravity.CENTER);
		lnrDay.setLayoutParams(layoutParams);
		lnrDay.setOrientation(LinearLayout.VERTICAL);

		TextView txtDayNumber = new TextView(activity);
		txtDayNumber.setGravity(Gravity.CENTER);
		txtDayNumber.setText(number);

		TextView txtTitleItem = new TextView(activity);
		txtTitleItem.setGravity(Gravity.CENTER);
		txtTitleItem.setText(day);

		if(CCDateUtil.compareDate(date, today, false) == 0){
			txtTitleItem.setTypeface(null, Typeface.BOLD);
			txtDayNumber.setTypeface(null, Typeface.BOLD);
		}

		if(Calendar.SUNDAY == dayOfWeek){
			txtTitleItem.setTextColor(Color.RED);
			// txtDayNumber.setTextColor(Color.RED);
		}else if(Calendar.SATURDAY == dayOfWeek){
			txtTitleItem.setTextColor(Color.BLUE);
			// txtDayNumber.setTextColor(Color.BLUE);
		}else{
			txtTitleItem.setTextColor(getNormalDayColor());
			// txtDayNumber.setTextColor(getNormalDayColor());
		}

		lnrDay.addView(txtDayNumber);
		lnrDay.addView(txtTitleItem);

		lnrHeader.addView(lnrDay);
	}

	@Override
	protected void initDayViews(){
		lnrVerticalLineContainer = (LinearLayout)getView().findViewById(R.id.lnr_vertical_line_container);
		// lnrVerticalLineContainer.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v){
		// Toast.makeText(activity, "hoooo", Toast.LENGTH_LONG).show();
		// }
		// });

		for(int i = 0; i < 7; i++){
			View verticalBar = new View(activity);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
			verticalBar.setLayoutParams(layoutParams);
			verticalBar.setBackgroundColor(Color.BLACK);

			LinearLayout lnrDay = new LinearLayout(activity);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			lnrDay.setGravity(Gravity.RIGHT);
			lnrDay.setLayoutParams(lp);
			lnrDay.addView(verticalBar);
			lnrVerticalLineContainer.addView(lnrDay);
		}

	}

	@Override
	protected String getExecType(){
		return "W";
	}

	@Override
	protected void initView(){
		super.initView();
		parent = (WeeklyFragment)getParentFragment();
		scrMain = (ObservableScrollView)getView().findViewById(R.id.scr_schedules);
		if(isActivePage()){
			parent.timeScroll.setScrollViewListener(this);
			scrMain.setScrollViewListener(this);
			setOnClickListenerForExpandIcon();
		}
		scrollViewPart1 = (ScrollView)getView().findViewById(R.id.scroll_part1);
		rltPart1 = (RelativeLayout)getView().findViewById(R.id.rlt_part1);
		rltPart2 = (RelativeLayout)getView().findViewById(R.id.rlt_part2);
		rltPart2LineContainer = (RelativeLayout)getView().findViewById(R.id.rlt_part2_line_container);
		rltExpandBar = (RelativeLayout)getView().findViewById(R.id.rlt_expand);

		// imgExpand = (ImageView)getView().findViewById(R.id.ic_icon_expand);

		// Add horizontal time lines
		Calendar c = CCDateUtil.makeCalendarToday();
		List<String> times = new ArrayList<>();
		for(int i = 0; i < 24; i++){
			String startTime = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, c.getTime());
			times.add(startTime);
			c.add(Calendar.HOUR, 1);
		}

		Collections.sort(times);

		for(int i = 0; i < times.size(); i++){
			addSHorizontalLine(times.get(i), WelfareUtil.dpToPx(60 * (i + 1)));
		}

		//// TODO: 10/13/17 why next page line is not good
		scrMain.post(new Runnable() {

			@Override
			public void run(){
				scrMain.scrollTo(parent.timeScroll.x, parent.timeScroll.y);
				scrMain.setCoordinate(parent.timeScroll.x, parent.timeScroll.y, parent.timeScroll.oldx, parent.timeScroll.oldy);
			}
		});
	}

	private void addSHorizontalLine(String startTime, int i){
		View cell = new View(activity);
		cell.setBackgroundColor(Color.GRAY);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WelfareUtil.dpToPx(1));
		params.setMargins(0, i, 0, 0);
		cell.setLayoutParams(params);
		rltPart2LineContainer.addView(cell);
	}

	public void setOnClickListenerForExpandIcon(){
		parent.imgExpand.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(isExpanded){
					collapse(rltPart1, (MAX_ROW - 1) * WeeklyPageFragment.CELL_HEIGHT_PIXEL);
					for(int i = 0; i < rltExpandBar.getChildCount(); i++){
						rltExpandBar.getChildAt(i).setVisibility(View.VISIBLE);
					}
					isExpanded = false;
					parent.imgExpand.setImageResource(R.drawable.down);
				}else{
					parent.imgExpand.setImageResource(R.drawable.up);
					expand(rltPart1);
					for(int i = 0; i < rltExpandBar.getChildCount(); i++){
						rltExpandBar.getChildAt(i).setVisibility(View.GONE);
					}
					isExpanded = true;
				}
			}
		});
	}

	@Override
	protected List<Date> getAllDate(){
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		List<Date> dates = CsDateUtil.getAllDatesOfWeek(CCDateUtil.makeCalendar(selectedDate), firstDay);
		return dates;
	}

	@Override
	public void onClick(View v){

	}

	// public static Comparator<? super WorkRequest> getWorkRequestComparator(){
	// return new Comparator<WorkRequest>() {
	//
	// @Override
	// public int compare(WorkRequest o1, WorkRequest o2){
	// return o1.offerTypeName.compareTo(o2.offerTypeName);
	// }
	// };
	// }

	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy){
		if(scrollView == scrMain){
			parent.timeScroll.scrollTo(x, y);
			parent.timeScroll.setCoordinate(x, y, oldx, oldy);
		}else if(scrollView == parent.timeScroll){
			scrMain.scrollTo(x, y);
			scrMain.setCoordinate(x, y, oldx, oldy);
		}
		parent.scrollNeighbors(x, y, oldx, oldy);
	}
}
